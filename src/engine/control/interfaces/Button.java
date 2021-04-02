package engine.control.interfaces;

import engine.Mathp;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.render.Render2D;

public class Button extends Element{
    int timeWork = 0;
    int responseRound = 0;
    String soundAct = null;
    //������, ��������� �� �������
    public Button(double x, double y, double sx, double sy, String t){
        locx = x;
        locy = y;
        sizex = sx;
        sizey = sy;
        setTexture(t);
    }

    public Button(double x, double y, double sx, double sy, String t,String activateSound){
        locx = x;
        locy = y;
        sizex = sx;
        sizey = sy;
        setTexture(t);
        soundAct = activateSound;
    }
    @Override
    public void draw() {
        double part = getStatus() == 4 ? 1 : getStatus();
        Render2D.angleCutDraw("button"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx,locy,sizex, sizey,0,0,0,part/MAX,(1 - (1+part)/MAX));

    }

    @Override
    public void work() {
        if(getStatus() == 0)
            return;
        if(Mathp.inRectangle(locx-sizex/2,locy-sizey/2,locx+sizex/2,locy+sizey/2, InputMain.getCursorX(),InputMain.getCursorY())){
            if(InputMain.getKeyState(InputMain.LMB) > -1){
                status = 3;
                if(responseRound == 0){
                    timeWork = -1;
                    responseRound++;
                } else if(responseRound == 1){
                    timeWork--;
                    if(timeWork == 0){
                        responseRound++;
                        timeWork=-1;
                    } else if(timeWork == -2){
                        timeWork = 50;
                    }
                } else {
                    timeWork--;
                    if(timeWork == 0){
                        timeWork=-1;
                    } else if(timeWork == -2){
                        timeWork = 10;
                    }
                }
            } else {
                if(getStatus() == 3){
                    status = 4;
                    responseRound=0;

                } else {
                    status = 2;
                    responseRound=0;

                }
            }
        } else {
            status = 1;
            responseRound=0;
        }
        if(isReleased() && soundAct != null){
            AudioManager.playSoundInterface(soundAct);
        }
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

    public double getX() {
        return locx;
    }

    public double getY() {
        return locy;
    }
}
