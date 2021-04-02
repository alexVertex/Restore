package engine.control.interfaces;

import engine.Mathp;
import engine.control.InputMain;
import engine.render.Render2D;

public class Scroll extends Element{

    int timeWork = 0;
    int responseRound = 0;

    int statusTop = 1;
    int statusBot = 1;

    public Scroll(double x, double y, double sx, double sy, String t){
        locx = x;
        locy = y;
        sizex = sx;
        sizey = sy;
        setTexture(t);

    }
    int position = 0;
    int maxPosition = 10;
    public void setMaxPosition(int maxPos){
        maxPosition = maxPos;
        if(position > maxPosition){
            position = maxPosition;
        }
    }
    public  int getPosition(){
        return position;
    }
    @Override
    public void draw() {
        double part = getStatus() == 4 ? 1 : getStatus();
        double positionOtn = (double)position/(double)maxPosition;
        double positionPer =  sizey-2*sizex*1.5;
        if(maxPosition>1)
        Render2D.angleCutDraw("scrollCenter"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx,locy-sizey/2+1.5*sizex+positionOtn*positionPer,sizex, sizex,0,0,0.5,part/MAX,(1 - (1+part)/MAX));

        part = statusTop == 4 ? 1 : statusTop;
        Render2D.angleCutDraw("scrollTop"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx,locy-sizey/2+sizex/2,sizex, sizex,-90,0.5,0,part/MAX,(1 - (1+part)/MAX));

        part = statusBot == 4 ? 1 : statusBot;
        Render2D.angleCutDraw("scrollBot"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx,locy+sizey/2-sizex/2,sizex, sizex,90,0.5,0,part/MAX,(1 - (1+part)/MAX));

    }

    @Override
    public void work() {
        if (getStatus() == 0)
            return;
        if (Mathp.inRectangle((locx) - sizex / 2, locy - sizey / 2 + sizex / 2 - sizex / 2, locx + sizex / 2, locy - sizey / 2 + sizex / 2 + sizex / 2, InputMain.getCursorX(), InputMain.getCursorY())) {
            if (InputMain.getKeyState(InputMain.LMB) > -1) {
                statusTop = 3;
            } else {
                if (statusTop == 3) {
                    statusTop = 4;
                } else {
                    statusTop = 2;
                }
            }
        } else {
            statusTop = 1;
        }

        if (Mathp.inRectangle((locx) - sizex / 2, locy + sizey / 2 - sizex / 2 - sizex / 2, locx + sizex / 2, locy + sizey / 2 - sizex / 2 + sizex / 2, InputMain.getCursorX(), InputMain.getCursorY())) {
            if (InputMain.getKeyState(InputMain.LMB) > -1) {
                statusBot = 3;
            } else {
                if (statusBot == 3) {
                    statusBot = 4;
                } else {
                    statusBot = 2;
                }
            }
        } else {
            statusBot = 1;
        }
        if (statusBot == 3) {
            if (workArrow()) {
                position++;
                if (position > maxPosition)
                    position = maxPosition;
            }
        } else if (statusTop == 3) {
            if (workArrow()) {
                position--;
                if (position < 0)
                    position = 0;
            }
        } else {
            responseRound = 0;
        }
        if(InputMain.isKeyPressed(InputMain.LMB)) {
            if (Mathp.inRectangle((locx) - sizex / 2, locy - sizey / 2 + sizex, locx + sizex / 2, locy + sizey / 2 - sizex, InputMain.getCursorX(), InputMain.getCursorY())) {
                locked = true;

            }
        }
        if(locked){
            if(!InputMain.isKeyPressed(InputMain.LMB)){
                locked = false;
            }
            double mousePose = (InputMain.getCursorY() - (locy - sizey / 2 + sizex)) / (sizey - 2 * sizex);
            position = (int)(mousePose*((maxPosition+1)));
            if (position < 0)
                position = 0;
            if (position > maxPosition)
                position = maxPosition;
        }
    }
    boolean locked = false;
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
    public void activate(){
        status = 1;
    }
    public void deactivate(){
        status = 0;
    }
    public boolean isResponsed(){
        return timeWork == -1 && status == 3;
    }

    public void setToLastPosition() {
        position = maxPosition;
    }
}
