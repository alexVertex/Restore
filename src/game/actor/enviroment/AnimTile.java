package game.actor.enviroment;

import engine.render.Camera;
import engine.render.Render2D;
import game.actor.game.Tile;
import game.actor.actorsHyper.EnviromentDataBase;

import java.util.HashMap;

public class AnimTile extends Tile {

    public AnimTile(String texture, int partX, int partY,String ID) {
        super(texture, partX, partY);
        setVariable("curAnimState",0.0);
        setVariable("locX",partX);
        setVariable("locY",partY);
        if (ID != null) {
            HashMap<String, Object> itemVariables = EnviromentDataBase.getData(ID);
            for (String el : itemVariables.keySet()) {
                setVariable(el, itemVariables.get(el));
            }
        }
    }

    String getTexture(){
        return (String) getVariable("texture");
    }

    @Override
    public int logic() {
        double curFrame = (Double) getVariable("curAnimState");
        double totalFrames = (Integer) getVariable("frames");
        curFrame += (Double) getVariable("animSpeed");
        if(curFrame >= totalFrames){
            curFrame -= totalFrames;
        }
        setVariable("curAnimState",curFrame);
        return 0;
    }

    public double[] getTileLoc(){
        return new double[]{(int)getVariable("locX"),(int)getVariable("locY")};
    }

    @Override
    public void draw() {
        double[] location = getTileLoc();
        double curFrame = Math.floor((Double) getVariable("curAnimState"));
        String texture = "animTiles/"+getTexture();
        double locX = location[0] + Camera.locX;
        double locY = location[1] + Camera.locY;
        double cutLeft = curFrame/(Integer)getVariable("frames");
        double cutRight = 1-(curFrame+1)/(Integer)getVariable("frames");
        double cutUp = 0;
        double cutDown = 0;
        Render2D.angleCutDraw("animTile",texture,locX,locY,SIZE,SIZE,0,cutLeft,cutRight,cutUp,cutDown);
    }

    @Override
    public void draw(double x, double y, double s) {
        double curFrame = Math.floor((Double) getVariable("curAnimState"));
        String texture = "animTiles/"+getTexture();
        double cutLeft = curFrame/(Integer)getVariable("frames");
        double cutRight = 1-(curFrame+1)/(Integer)getVariable("frames");
        double cutUp = 0;
        double cutDown = 0;
        Render2D.angleCutDraw("animTile"+x+""+y+""+s,texture, x, y,s,s,0,cutLeft,cutRight,cutUp,cutDown);
    }

    @Override
    public boolean isUndercursor(int curX, int curY){
        double[] location = getTileLoc();
        return Math.abs(curX - Camera.locX - location[0]) <= SIZE / 2 && Math.abs(curY - Camera.locY - location[1]) <= SIZE / 2;
    }
}
