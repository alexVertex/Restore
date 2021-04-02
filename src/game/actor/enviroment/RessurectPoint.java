package game.actor.enviroment;

import engine.Actor;
import engine.render.Camera;
import engine.render.Render2D;

public class RessurectPoint extends Actor {

    public RessurectPoint(int left, int top, int right, int bot, int resurectX, int resurectY,String mapTo, String transitionTo, String ID, String name,String mapToFileName){
        setVariable("left", left);
        setVariable("top", top);
        setVariable("right", right);
        setVariable("bot", bot);
        setVariable("locX", resurectX);
        setVariable("locY", resurectY);

        setVariable("mapTo", mapTo);
        setVariable("mapToFileName", mapToFileName);

        setVariable("transitionTo", transitionTo);
        setVariable("ID", ID);
        setVariable("name", name);

    }

    @Override
    public int logic() {
        return 0;
    }

    @Override
    public void draw() {
        double left = getVariableInteger("left");
        double top = getVariableInteger("top");
        double right = getVariableInteger("right");
        double bot = getVariableInteger("bot");
        double x = (left+right)/2 + Camera.locX;
        double y = (top+bot)/2 + Camera.locY;
        double sx = (right-left);
        double sy = (bot-top);
        Render2D.angleColorDraw("qwert","interface/white",x,y,sx,sy,0,1,0,0,0.25);
        double resX = getVariableInteger("locX") + Camera.locX;
        double resY = getVariableInteger("locY") + Camera.locY;
        Render2D.angleColorDraw("qwert1","interface/white",resX,resY,32,32,0,1,0,1,0.25);
    }

    @Override
    public void draw(double x, double y, double s) {

    }

    @Override
    public void drawInfo(boolean b) {

    }

    @Override
    public boolean isUndercursor(int cursorX, int cursorY) {
        return false;
    }

    public String saveData() {
        return getVariable("left")+":"+getVariable("top")+":"+getVariable("right")+":"+getVariable("bot")+":"+getVariable("locX")+":"+getVariable("locY")+
                ":" + getVariable("ID") + ":" + getVariable("name") + ":" + getVariable("mapTo") + ":" + getVariable("transitionTo")+ ":" + getVariable("mapToFileName");
    }
}
