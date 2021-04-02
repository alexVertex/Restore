package game.actor.magic;

import engine.Actor;
import engine.Mathp;
import engine.render.Camera;
import engine.render.Render2D;
import game.StartGame;
import game.actor.player.Player;
import game.actor.player.PathFinding;
import game.actor.player.PlayerVoice;
import screens.ingame.GameScreen;

public class MagicProjectile extends Actor {
    private static String[] farweaponClassesSkills = {"magicFire","magicWater","magicAir","magicEarth","magicMind","magicWizardry","magicSacred"};

    static int translate(String in){
        switch (in){
            case "magicFire": return 0;
            case "magicWater": return 1;
            case "magicAir": return 2;
            case "magicEarth": return 3;
            case "magicMind": return 4;
            case "magicWizardry": return 5;
            case "magicSacred": return 6;
        }
        return 0;
    }
    public MagicProjectile(String texture,String textureExplode, double startX, double startY, int sizeX, int sizeY,double speed, int frames,int framesExplode, Player target, int radius, Player lastHit, int effect, int power, int time, String school,String explodeSound, String projectileSound, String spellTexture, int spellCutX,int spellCutY){
        setVariable("projectileSound",projectileSound);
        setVariable("explodeSound",explodeSound);
        setVariable("spellClass",translate(school));
        setVariable("texture",texture);
        setVariable("locX",startX);
        setVariable("locY",startY);
        setVariable("sizeX",sizeX);
        setVariable("sizeY",sizeY);
        setVariable("target",target);
        setVariable("frames",frames);
        setVariable("type", 0);
        setVariable("partX",0);
        setVariable("partY",0);
        setVariable("speed",speed);
        setVariable("textureExplode",textureExplode);
        setVariable("framesExplode",framesExplode);
        setVariable("radius",radius);
        setVariable("animProgress",0.0);
        setVariable("state",0);
        setVariable("lastHit",lastHit);
        setVariable("effect",effect);
        setVariable("power",power);
        setVariable("time",time);
        setVariable("timeWorking",time);
        setVariable("spellTexture",spellTexture);
        setVariable("spellCutX",spellCutX);
        setVariable("spellCutY",spellCutY);
    }
    public MagicProjectile(String texture, double startX, double startY, int size, Player target, Player lastHit,int power, String school,String explodeSound, String projectileSound, String spellTexture, int spellCutX,int spellCutY){
        setVariable("projectileSound",projectileSound);
        setVariable("explodeSound",explodeSound);
        setVariable("spellClass",translate(school));
        setVariable("texture",texture);
        setVariable("locX",startX);
        setVariable("locY",startY);
        setVariable("sizeX",size);
        setVariable("sizeY",size);
        setVariable("target",target);
        setVariable("frames",1);
        setVariable("type", 0);
        setVariable("partX",0);
        setVariable("partY",0);
        setVariable("speed",7.0);
        setVariable("textureExplode",texture);
        setVariable("framesExplode",1);
        setVariable("radius",16);
        setVariable("animProgress",0.0);
        setVariable("state",0);
        setVariable("lastHit",lastHit);
        setVariable("effect",2);
        setVariable("power",power);
        setVariable("time",1);
        setVariable("timeWorking",1);
        setVariable("spellTexture",spellTexture);
        setVariable("spellCutX",spellCutX);
        setVariable("spellCutY",spellCutY);
    }
    @Override
    public int logic() {
        PlayerVoice.workVoiceInGame(this);
        double animProgress = (double)getVariable("animProgress");
        animProgress +=0.01;
        if(animProgress > 1){
            animProgress = 0;
        }
        setVariable("animProgress",animProgress);
        double frame = animProgress * getVariableInteger("frames");
        setVariable("partX",(int)frame);

        double[] targetLocs = ((Player)getVariable("target")).getTileLoc();
        double[] currentLocs = getTileLoc();
        double rast = Mathp.rast(targetLocs[0],targetLocs[1],currentLocs[0],currentLocs[1]);
        double angle = Math.atan2(targetLocs[1]-currentLocs[1],targetLocs[0]-currentLocs[0]);
        int time = getVariableInteger("timeWorking");

        double speed = (double)getVariable("speed");

        setVariable("angle",angle);

        if(getVariable("state").equals(0)) {
            if(rast < speed || speed <= 0){
                setVariable("locX",  targetLocs[0]);
                setVariable("locY",  targetLocs[1]);
                setVariable("texture", getVariable("textureExplode"));
                setVariable("frames", getVariable("framesExplode"));
                setVariable("sizeX", (int)(getVariable("radius"))*2);
                setVariable("sizeY",(int)(getVariable("radius"))*2);
                setVariable("state", 1);
                setVariable("partX", 0);
                setVariable("animProgress", 0.0);
                if(time > 0) {
                    work(false);
                    PlayerVoice.setVoiceIngame(this, "spells/explodes/"+getVariableString("explodeSound"), true);

                }
            } else {
                if(!getVariableString("projectileSound").equals("null"))
                    PlayerVoice.setVoiceIngame(this, "spells/projectiles/"+getVariableString("projectileSound"), false);

                double[] oldLocks = getTileLoc();
                currentLocs[0] += Math.cos(angle) * speed;
                currentLocs[1] += Math.sin(angle) * speed;
                double[] collidedCoords = hitObstakle(oldLocks[0],oldLocks[1],currentLocs[0],currentLocs[1]);
                if(collidedCoords != null){
                    setVariable("locX",  collidedCoords[0]);
                    setVariable("locY",  collidedCoords[1]);
                    setVariable("tarX", (int) collidedCoords[0]);
                    setVariable("tarY", (int) collidedCoords[1]);
                    setVariable("target",new Player(collidedCoords[0],collidedCoords[1]));

                    setVariable("texture", getVariable("textureExplode"));
                    setVariable("frames", getVariable("framesExplode"));
                    setVariable("sizeX", (int)(getVariable("radius"))*2);
                    setVariable("sizeY",(int)(getVariable("radius"))*2);
                    setVariable("state", 1);
                    setVariable("partX", 0);
                    setVariable("animProgress", 0.0);
                    if(time > 0) {
                        work(false);
                        PlayerVoice.setVoiceIngame(this, "spells/explodes/"+getVariableString("explodeSound"), true);
                    }
                }
                setVariable("locX",currentLocs[0]);
                setVariable("locY",currentLocs[1]);
            }
        } else if(getVariable("state").equals(1)){

            setVariable("locX",  targetLocs[0]);
            setVariable("locY",  targetLocs[1]);
        }

        if(getVariable("state").equals(1)){
            if(time < 0){
                work(true);
                time++;
                setVariable("timeWorking",time);
                PlayerVoice.setVoiceIngame(this, "spells/projectiles/"+getVariableString("projectileSound"), false);

            } else {
                if ((int) frame == getVariableInteger("frames") - 1) {
                    GameScreen.addDeleteRequest(this,StartGame.game.projectiles);
                }
            }
        }
        return 0;
    }
    private double[] hitObstakle(double startX, double startY, double finishX, double finishY){
        return PathFinding.getCollisionPoint(startX,startY,finishX,finishY,false);
    }
    private void work(boolean pereodic){
        double[] currentLocs = getTileLoc();
        Player target = (Player) getVariable("target");
        if(target.getVariable("ghost") == null){
            MagicBase.addEffect(target, (Player) getVariable("lastHit"), getVariableInteger("effect"), getVariableInteger("power"), getVariableInteger("time"), pereodic, getVariableInteger("spellClass"), getVariableString("spellTexture"), getVariableInteger("spellCutX"), getVariableInteger("spellCutY"));
        } else {
            for (Player el : StartGame.game.getNpcList()) {
                double[] enemyLoc = el.getTileLoc();
                double rast = Mathp.rast(currentLocs[0], currentLocs[1], enemyLoc[0], enemyLoc[1]);
                boolean obstackle = PathFinding.getCollisionPoint(target.getTileLoc()[0],target.getTileLoc()[1],el.getTileLoc()[0],el.getTileLoc()[1],false) != null;
                if (rast < getVariableInteger("radius") && !obstackle) {
                    MagicBase.addEffect(el, (Player) getVariable("lastHit"), getVariableInteger("effect"), getVariableInteger("power"), getVariableInteger("time"), pereodic, getVariableInteger("spellClass"), getVariableString("spellTexture"), getVariableInteger("spellCutX"), getVariableInteger("spellCutY"));
                }
            }
            for (Player el : StartGame.game.controlGroup) {
                double[] enemyLoc = el.getTileLoc();
                double rast = Mathp.rast(currentLocs[0], currentLocs[1], enemyLoc[0], enemyLoc[1]);
                boolean obstackle = PathFinding.getCollisionPoint(target.getTileLoc()[0],target.getTileLoc()[1],el.getTileLoc()[0],el.getTileLoc()[1],false) != null;

                if (rast < getVariableInteger("radius")&& !obstackle) {
                    MagicBase.addEffect(el, (Player) getVariable("lastHit"), getVariableInteger("effect"), getVariableInteger("power"), getVariableInteger("time"), pereodic, getVariableInteger("spellClass"), getVariableString("spellTexture"), getVariableInteger("spellCutX"), getVariableInteger("spellCutY"));
                }
            }
        }
    }
    public String getTexture(){
        return  "spells/projectiles/"+getVariable("texture");
    }
    public double[] getTextureLocation(){
        return new double[]{(int)getVariable("partX"),(int)getVariable("partY")};
    }
    public double[] getTileLoc(){
        return new double[]{(double)getVariable("locX"),(double)getVariable("locY")};
    }
    private double[] getTileSize(){
        return new double[]{(int)getVariable("sizeX"),(int)getVariable("sizeY")};
    }

    @Override
    public void draw() {
        double[] location = getTileLoc();
        double[] size = getTileSize();
        double[] part = getTextureLocation();
        String texture = getTexture();
        double locX = location[0] + Camera.locX;
        double locY = location[1] + Camera.locY;
        double cutLeft = part[0]/getVariableInteger("frames");
        double cutRight = 1-(part[0]+1)/getVariableInteger("frames");
        double cutUp = 0;
        double cutDown = 0;
        Render2D.angleCutDraw("projectile",texture,locX,locY,size[0],size[1],Math.toDegrees((double)getVariable("angle")),cutLeft,cutRight,cutUp,cutDown);
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
