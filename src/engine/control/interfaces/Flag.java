package engine.control.interfaces;

import engine.Mathp;
import engine.control.InputMain;
import engine.render.Render2D;

public class Flag extends Element{
    //������� ����
    public Flag(double x, double y, double sx, double sy, String t,int ischeked){
        locx = x;
        locy = y;
        sizex = sx;
        sizey = sy;
        setTexture(t);
        setSignal(ischeked);
    }
    @Override
    public void draw() {
        double part = getStatus() == 4 ? 1 : getStatus();
        Render2D.angleCutDraw("flag"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx,locy,sizex, sizey,0, getSignal() /2.0,(1 - (1+ getSignal())/2.0),part/MAX,(1 - (1+part)/MAX));
    }

    @Override
    public void work() {
        if(getStatus() == 0)
            return;
        if(Mathp.inRectangle(locx-sizex/2,locy-sizey/2,locx+sizex/2,locy+sizey/2, InputMain.getCursorX(),InputMain.getCursorY())){
            if(InputMain.getKeyState(InputMain.LMB) > -1){
                status = 3;
            } else {
                if(getStatus() == 3){
                    status = 4;
                    setSignal(1- getSignal());

                } else {
                    status = 2;
                }
            }
        } else {
            status = 1;
        }
    }
}
