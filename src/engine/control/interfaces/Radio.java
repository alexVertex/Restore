package engine.control.interfaces;


import engine.Mathp;
import engine.control.InputMain;
import engine.render.Render2D;

public class Radio extends Element{
    public Radio(double[] x, double[] y, double sx, double sy, String t,int choosen){
        Xes = x;
        Yes = y;
        statuses = new double[Yes.length];
        for (int i = 0; i < statuses.length; i++){
            statuses[i] = 1;
        }
        sizex = sx;
        sizey = sy;
        setTexture(t);
        setSignal(choosen);
    }
    double Xes[];
    double Yes[];
    double statuses[];

    @Override
    public void draw() {
        for(int i = 0; i < Xes.length;i++){
            double part = getStatus() == 4 ? 1 : statuses[i];
            double signal = i == getSignal() ? 1 : 0;
            Render2D.angleCutDraw("radio"+i+""+Xes[i]+""+Yes[i]+""+sizex+""+sizey,getTexture(),Xes[i],Yes[i],sizex, sizey,0,signal/2.0,(1 - (1+signal)/2.0),part/MAX,(1 - (1+part)/MAX));
        }

    }

    @Override
    public void work() {
        for(int i = 0; i < statuses.length; i++) {
            if (statuses[i] == 0)
                continue;
            if (Mathp.inRectangle(Xes[i] - sizex / 2, Yes[i] - sizey / 2, Xes[i] + sizex / 2, Yes[i] + sizey / 2, InputMain.getCursorX(), InputMain.getCursorY())) {
                if (InputMain.getKeyState(-1) > -1) {
                    statuses[i] = 3;
                } else {
                    if (statuses[i] == 3) {
                        statuses[i] = 4;
                    } else {
                        statuses[i] = 2;
                    }
                }
            } else {
                statuses[i] = 1;
            }
            if (statuses[i] == 4) {
                setSignal(i);
                statuses[i] = 1;
            }
        }
    }
}
