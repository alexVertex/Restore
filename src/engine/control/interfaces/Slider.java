package engine.control.interfaces;

import engine.Mathp;
import engine.control.InputMain;
import engine.render.Render2D;

public class Slider extends Element{

    public Slider(double x, double y, double sx, double sy, String t,double start){
        locx = x;
        locy = y;
        sizex = sx;
        sizey = sy;
        setTexture(t);
        setSignal(start);
        ARROW_SPEED = 1.0/sx;
    }
    public void setArrowSpeed(double speed) {
        ARROW_SPEED =speed;
    }
    int statusLeft = 1;
    int statusRight = 1;

    @Override
    public void draw() {
        double part = getStatus() == 4 ? 1 : getStatus();
        Render2D.angleCutDraw("sliderCenter"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx-(sizex-sizey*2)*0.5*(1- getSignal()),locy,(sizex-sizey*2)* getSignal(), sizey,0,0,0.125,part/MAX,(1 - (1+part)/MAX));

        part = statusRight == 4 ? 1 : statusRight;
        Render2D.angleCutDraw("sliderRight"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx+sizex/2-sizey/2,locy,sizey, sizey,0,0.875,0.0,part/MAX,(1 - (1+part)/MAX));

        part = statusLeft == 4 ? 1 : statusLeft;
        Render2D.angleCutDraw("sliderLeft"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx-sizex/2+sizey/2,locy,sizey, sizey,180,0.875,0.0,part/MAX,(1 - (1+part)/MAX));

    }
    @Override
    public boolean isReleased(){
        return status == PRESSED || statusRight == PRESSED || statusLeft == PRESSED;
    }


    @Override
    public void work() {
        if(getStatus() == 0)
            return;
        if(Mathp.inRectangle(locx-(sizex-sizey*2)/2,locy-sizey/2,locx+(sizex-sizey*2)/2,locy+sizey/2, InputMain.getCursorX(),InputMain.getCursorY())){
            if(InputMain.getKeyState(InputMain.LMB) > -1){
                status = 3;
            } else {
                if(getStatus() == 3){
                    status = 4;
                } else {
                    status = 2;
                }
            }
        } else {
            if(status == 3){
                if(InputMain.getKeyState(InputMain.LMB) == -2){
                    status = 1;
                }
            } else {
                status = 1;
            }
        }

        if(Mathp.inRectangle((locx+sizex/2-sizey/2)-sizey/2,locy-sizey/2,(locx+sizex/2-sizey/2)+sizey/2,locy+sizey/2, InputMain.getCursorX(),InputMain.getCursorY())){
            if(InputMain.getKeyState(InputMain.LMB) > -1){
                statusRight = 3;
            } else {
                if(statusRight == 3){
                    statusRight = 4;
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
                } else {
                    statusLeft = 2;
                }
            }
        } else {
            statusLeft = 1;
        }
        if(statusLeft == 3){
            setSignal(getSignal() - ARROW_SPEED);
            if(getSignal() <0) setSignal(0);
        }
        if(statusRight == 3){
            setSignal(getSignal() + ARROW_SPEED);
            if(getSignal() >1) setSignal(1);
        }
        if(status == 3){
            setSignal((InputMain.getCursorX()-(locx-(sizex-sizey*2)*0.5))/(sizex-sizey*2));
            if(getSignal() >1) setSignal(1);
            else if(getSignal() <0) setSignal(0);
        }
    }
    private double ARROW_SPEED = 0.001;
}
