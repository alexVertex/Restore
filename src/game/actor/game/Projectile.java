package game.actor.game;

import engine.Actor;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.actor.actorsHyper.ItemDataBase;
import org.newdawn.slick.Color;

public class Projectile extends Actor {
    public Projectile(String texture, double locX, double locY, double size, double frames,int frameAttack,double damage,String damageType){
        setVariable("texture","projectiles/"+texture);
        setVariable("locX",locX);
        setVariable("locY",locY);
        setVariable("size",size);
        setVariable("frames",frames);
        setVariable("curFrame",0.0);
        setVariable("animSpeed",0.1);
        setVariable("attacked",0);
        setVariable("frameAttack",frameAttack);
        setVariable("damage",damage);
        setVariable("damageType",damageType);
    }

    @Override
    public int logic() {
        double curFrame = (Double) getVariable("curFrame");
        double totalFrames = (Double) getVariable("frames");
        curFrame += (Double) getVariable("animSpeed");
        if(curFrame >= totalFrames){
            curFrame -= totalFrames;
        }
        setVariable("curFrame",curFrame);
        return 0;
    }

    public boolean setFrame(double attaclProgress){
        double totalFrames = (Double) getVariable("frames");
        double curFrame = totalFrames*attaclProgress;
        setVariable("curFrame",curFrame);
        if(getVariable("attacked").equals(0) && getVariable("frameAttack").equals((int)curFrame)){
            setVariable("attacked",1);
            return true;
        }
        return false;
    }

    String getTexture(){
        return (String) getVariable("texture");
    }
    private double[] getTileLoc(){
        return new double[]{(double)getVariable("locX"),(double)getVariable("locY")};
    }

    @Override
    public void draw() {
        double[] location = getTileLoc();
        double curFrame = Math.floor((Double) getVariable("curFrame"));
        String texture = getTexture();
        double locX = location[0] + Camera.locX;
        double locY = location[1] + Camera.locY;
        double cutLeft = curFrame/(Double) getVariable("frames");
        double cutRight = 1-(curFrame+1)/(Double)getVariable("frames");
        double cutUp = 0;
        double cutDown = 0;
        Render2D.angleCutDraw("projectile",texture,locX,locY,(Double) getVariable("size"),(Double) getVariable("size"),0,cutLeft,cutRight,cutUp,cutDown);
    }

    @Override
    public void draw(double x, double y, double s) {

    }

    public String[][] info() {
        String[][] info = null;
        if (this.getVariable("type").equals(ItemDataBase.TYPE_WEAPON)) {
            info = new String[][]{
                    {"ID:", getVariable("ID") + ""},
            };
        }
        return info;
    }

    @Override
    public void drawInfo(boolean onlyName){
        String[][] info = info();
        double sizeX = 212;
        double sizeY = info.length*20+10;
        if(onlyName) sizeY=30;
        float locX = InputMain.getCursorX()+28;
        float locY = InputMain.getCursorY()+24;
        if(locY + sizeY > Render2D.getWindowHeight()){
            locY -= sizeY+24;
        }
        Render2D.angleColorDraw("infoBack","interface/white",locX+sizeX/2,locY+sizeY/2,sizeX,sizeY,0,0,0,0,1);
        Render2D.angleDraw("infoBorder1","interface/borderBack",locX+sizeX/2,locY,sizeX,4,0);
        Render2D.angleDraw("infoBorder2","interface/borderBack",locX+sizeX/2,locY+sizeY,sizeX,4,0);
        Render2D.angleDraw("infoBorder3","interface/borderBack",locX,locY+sizeY/2,sizeY,4,90);
        Render2D.angleDraw("infoBorder4","interface/borderBack",locX+sizeX,locY+sizeY/2,sizeY,4,90);
        for(int i =0; i < info.length;i++){
            Text.drawString(info[i][0],locX+4, locY+8+i*20,Text.CAMBRIA_14, Color.white);
            Text.drawStringRigth(info[i][1],locX+4+200, locY+8+i*20,Text.CAMBRIA_14, Color.white);
            if(onlyName) break;
        }
    }

    public boolean isUndercursor(int curX, int curY){
        return false;
    }
}
