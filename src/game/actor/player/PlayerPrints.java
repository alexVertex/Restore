package game.actor.player;

import engine.Actor;
import engine.Mathp;
import engine.render.Camera;
import engine.render.Render2D;
import game.StartGame;
import screens.ingame.GameScreen;

public class PlayerPrints extends Actor {

    String[] textures = {"prints/redBlood","prints/blueBlood","prints/greenBlood"};
    public PlayerPrints(double X, double Y, int texture){
        setVariable("locX",X);
        setVariable("locY",Y);
        setVariable("radius",0.0);
        setVariable("angle", Mathp.random()*360.0);
        setVariable("texture",textures[texture]);
    }

    String[] textures1 = {"prints/steps"};
    public PlayerPrints(double X, double Y, int texture,double angle){
        setVariable("locX",X);
        setVariable("locY",Y);
        setVariable("radius",42.0);
        setVariable("timeWait",30.0);

        setVariable("angle", angle);
        setVariable("texture",textures1[texture]);
    }
    @Override
    public int logic() {
        if(getVariableDouble("radius") < 40){
            setVariable("radius",getVariableDouble("radius")+0.4);
        }
        if(getVariableDouble("radius") == 42.0){
            setVariable("timeWait",getVariableDouble("timeWait")-0.01);
        }
        return 0;
    }

    @Override
    public void draw() {
        if(getVariableDouble("radius") == 42.0) {
            double alpha = getVariableDouble("timeWait");
            if(alpha > 1) alpha = 1;
            if(alpha < 0) GameScreen.addDeleteRequest(this, StartGame.game.steps);
            Render2D.angleColorDraw(getVariableString("texture"),getVariableDouble("locX")+ Camera.locX,getVariableDouble("locY")+ Camera.locY,getVariableDouble("radius"),getVariableDouble("radius"),getVariableDouble("angle"),1,1,1,alpha);

        } else {
            Render2D.angleDraw(getVariableString("texture"),getVariableDouble("locX")+ Camera.locX,getVariableDouble("locY")+ Camera.locY,getVariableDouble("radius"),getVariableDouble("radius"),getVariableDouble("angle"));
        }
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
}
