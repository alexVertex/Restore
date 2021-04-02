package engine.control.interfaces;

import engine.Mathp;
import engine.control.InputMain;
import engine.render.Render2D;
import engine.render.Text;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import java.util.HashMap;

public class Droplist extends Element{

    int timeWork = 0;
    int responseRound = 0;

    private double statusRight=1;
    private double statusLeft=1;
    public void setValue(String value){
        for(int i = 0; i < values.length;i++){
            if(value.equals(values[i])){
                cursorPos = i;
                return;
            }
        }
    }
    public void setPos(int value){
        cursorPos = value;
    }
    public void setVals(String[] vals){
        values = vals;cursorPos = 0;
        text = vals[cursorPos];
    }
    public String getText() {
        return values[cursorPos];
    }
    public int getVal() {
        return cursorPos;
    }
    private int cursorPos;
    private int maximum = -1,minimum = -1;

    private String text;
    private int font;
    private boolean focused = false;
    private char hide = ' ';
    private double offsetX;
    String[] values;
    public Droplist(double x, double y, double sx, double sy, String t,int initText,String[] vals, int initFont,double ofX){
        locx = x;
        locy = y;
        sizex = sx;
        sizey = sy;
        setTexture(t);
        if(vals.length >0)
        text = vals[initText];
        font = initFont;offsetX = ofX;
        status = 1;
        values = vals;
        cursorPos = 0;
    }

    @Override
    public void draw() {
        float plusy = 8;

        double part = status == 4 ? 1 : status;
        Render2D.angleCutDraw("droplistCenter"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx,locy+plusy,(sizex-sizey*2), sizey,0,0.0,0.125,part/MAX,(1 - (1+part)/MAX));

        part = statusRight == 4 ? 1 : statusRight;
        Render2D.angleCutDraw("droplistRight"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx+sizex/2-sizey/2,locy+plusy,sizey, sizey,0,0.875,0.0,part/MAX,(1 - (1+part)/MAX));

        part = statusLeft == 4 ? 1 : statusLeft;
        Render2D.angleCutDraw("droplistLeft"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx-sizex/2+sizey/2,locy+plusy,sizey, sizey,180,0.875,0.0,part/MAX,(1 - (1+part)/MAX));

        Text.drawStringCenter(values[cursorPos],(float) (locx),(float)locy,font, Color.white);
    }
    @Override
    public void work() {
        if(getStatus() == 0)
            return;
        status = 1;
        if(Mathp.inRectangle((locx+sizex/2-sizey/2)-sizey/2,locy-sizey/2,(locx+sizex/2-sizey/2)+sizey/2,locy+sizey/2, InputMain.getCursorX(),InputMain.getCursorY())){
            if(InputMain.getKeyState(InputMain.LMB) > -1){
                statusRight = 3;
            } else {
                if(statusRight == 3){
                    statusRight = 4;
                    status = RELEASED;
                } else {
                    statusRight = 2;
                }
            }
        } else {
            statusRight = 1;
        }

        if(Mathp.inRectangle((locx-sizex/2+sizey/2)-sizey/2,locy-sizey/2,(locx-sizex/2+sizey/2)+sizey/2,locy+sizey/2, InputMain.getCursorX(),InputMain.getCursorY())){
            if(InputMain.getKeyState(InputMain.LMB) > -1){
                statusLeft = 3;
            } else {
                if(statusLeft == 3){
                    statusLeft = 4;
                    status = RELEASED;

                } else {
                    statusLeft = 2;
                }
            }
        } else {
            statusLeft = 1;
        }
        if (statusRight == 3) {
            if (workArrow()) {
                cursorPos++;
                if (cursorPos > values.length-1)
                    cursorPos = 0;
            }
        } else if (statusLeft == 3) {
            if (workArrow()) {
                cursorPos--;
                if (cursorPos < 0)
                    cursorPos = values.length-1;
            }
        } else {
            responseRound = 0;
        }

    }
    boolean workArrow(){
        if(responseRound == 0){
            timeWork = -1;
            responseRound++;
            return true;


        } else if(responseRound == 1){
            timeWork--;
            if(timeWork == 0){
                responseRound++;
                timeWork=-1;
                return true;
            } else if(timeWork == -2){
                timeWork = 50;
            }
        } else {
            timeWork--;
            if(timeWork == 0){
                timeWork=-1;
                return true;
            } else if(timeWork == -2){
                timeWork = 10;
            }
        }
        return false;
    }

    private static int oldPos = 0;
    public boolean isChanged() {
        return statusLeft == 3 || statusRight == 3;
    }
}
