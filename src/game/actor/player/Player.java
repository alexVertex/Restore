package game.actor.player;

import engine.Actor;
import engine.Mathp;
import engine.Start;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import engine.script.ExecuteScripts;
import game.StartGame;
import game.actor.actorsHyper.MercDataBase;
import game.actor.actorsHyper.NPCDataBase;
import game.actor.enviroment.Item;
import game.actor.game.Projectile;
import game.actor.magic.MagicBase;
import game.actor.magic.MagicEffect;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import screens.ingame.GameOverScreen;
import screens.ingame.MainMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static game.actor.game.Tile.SIZE;
import static game.actor.player.PlayerMovesSet.*;

public class Player extends Actor {
    public static final double DAMAGE_WEAPON_SKILL_MULT = 0.1;
    public static final double BARE_FIST_DAMAGE = 9.0;
    public static final double BARE_FIST_ATTACKSPEED = 0.1;
    public static final double BARE_FIST_DISTANT = 32.0;
    public static final Object BARE_FIST_DAMAGE_TYPE = 2;


    public Player(String ID){


    }
    public Player(double locX, double locY){
        setVariable("locX",locX);
        setVariable("locY",locY);
        setVariable("dead",0);
        setVariable("good",0);
        setVariable("ghost",0);

    }

    public Player(String ID, boolean merc,double locX, double locY){
        if (ID != null) {
            HashMap<String, Object> itemVariables = MercDataBase.getData(ID);
            for (String el : itemVariables.keySet()) {
                setVariable(el, itemVariables.get(el));
            }
        }
        setVariable("merc",merc);
        setVariable("locX",locX);
        setVariable("locY",locY);
        setVariable("tarX",locX);
        setVariable("tarY",locY);
        setVariable("partX",1);
        setVariable("partY",0);
        setVariable("dead",0);
        setVariable("alarmed",0);
        setVariable("attackProgress",0.0);
        setVariable("timeBeforeRecovery",0);
        setVariable("moveAnimState",0.0);
        setVariable("castingAnim",0);
        setVariable("battleMode",2);
        setVariable("damageArms",0.0);
        setVariable("damageLegs",0.0);
        List<MagicEffect> mes = new ArrayList<>();
        setVariable("effects",mes);
        setVariable("speed",1.1);
        setVariable("speedRun",2.6);
        setVariable("speedWalk",1.1);
        setVariable("speedCrouch",0.6);
        setVariable("voiceTimeWait",0);
        RolePlay.setParams(this);
        setVariable("moveTargetType",MOVE_TARGET_NON);
        setVariable("moveTarget",null);
        setVariable("attackProgressAdd",0.0);
        setVariable("projectile",null);
        setVariable("targetOn",-1);
        setVariable("speedGear",1);
        setVariable("exp",0);
        setVariable("damage",9.0);
        setVariable("damageType","armorStrike");
        setVariable("distant",32.0);
        setVariable("good",0);
        setVariable("stamina",getVariable("maxStamina"));
        setVariable("health",getVariable("maxHealth"));
        countEquipLoad();

    }

    public Player(int underPlayerControl,String portret, int portretX, int portretY, String ingameTexture,double locX,double locY,double maxHealth, double health, double maxStamina, double stamina,double maxLoad, double load,double attackSpeed){
        setVariable("ID","mainHero");

        setVariable("portret",portret);
        setVariable("portretX",portretX);
        setVariable("portretY",portretY);
        setVariable("playable",underPlayerControl);
        setVariable("ingameTexture",ingameTexture);
        setVariable("locX",locX);
        setVariable("locY",locY);
        setVariable("partX",1);
        setVariable("partY",0);
        setVariable("maxHealth",maxHealth);
        setVariable("health",health);
        setVariable("maxStamina",maxStamina);
        setVariable("stamina",stamina);
        setVariable("onCamera",0);
        setVariable("tarX",locX);
        setVariable("tarY",locY);
        setVariable("speed",1.1);
        setVariable("speedRun",2.6);
        setVariable("speedWalk",1.1);
        setVariable("speedCrouch",0.6);
        setVariable("timeBeforeRecovery",0);
        setVariable("moveAnimState",0.0);
        setVariable("weaponCur",null);
        setVariable("shieldCur",null);
        setVariable("weaponSpare",null);
        setVariable("shieldSpare",null);
        setVariable("armorHead",null);
        setVariable("armorTorso",null);
        setVariable("armorArms",null);
        setVariable("armorLegs",null);
        setVariable("spells1",null);
        setVariable("spells2",null);
        setVariable("spells3",null);
        setVariable("spells4",null);
        setVariable("spells5",null);
        setVariable("spells6",null);
        setVariable("spells7",null);
        setVariable("spells8",null);
        setVariable("uses1",null);
        setVariable("uses2",null);
        setVariable("uses3",null);
        setVariable("uses4",null);
        setVariable("uses5",null);
        setVariable("uses6",null);
        setVariable("uses7",null);
        setVariable("uses8",null);
        setVariable("magicFire",0);
        setVariable("magicWater",0);
        setVariable("magicAir",0);
        setVariable("magicEarth",0);
        setVariable("magicMind",0);
        setVariable("magicWizardry",0);
        setVariable("magicSacred",0);
        setVariable("equipLoad",load);
        setVariable("maxEquipLoad",maxLoad);
        setVariable("moveTargetType",MOVE_TARGET_NON);
        setVariable("moveTarget",null);
        setVariable("attackSpeed",attackSpeed);
        setVariable("attackProgress",0.0);
        setVariable("attackProgressAdd",0.0);
        setVariable("projectile",null);
        setVariable("targetOn",-1);
        setVariable("damageArms",0.0);
        setVariable("damageLegs",0.0);
        setVariable("dead",0);
        setVariable("exp",350);setVariable("totalXP",350);
        setVariable("speedGear",1);
        setVariable("castingAnim",0);
        setVariable("battleMode",0);
        setVariable("skillHealth",0);setVariable("skillStamina",0);setVariable("skillRestoration",0);setVariable("skillEquipLoad",0);setVariable("skillAttackSpeed",0);setVariable("skillLockPick",0);
        setVariable("skillDaggers",0);setVariable("skillBlades",0);setVariable("skillAxes",0);setVariable("skillBlunts",0);setVariable("skillHandToHand",0);setVariable("skillSpears",0);setVariable("skillBows",0);setVariable("skillCrossbows",0);
        setVariable("skillFireMagic",0);setVariable("skillWaterMagic",0);setVariable("skillEarthMagic",0);setVariable("skillAirMagic",0);setVariable("skillMentalMagic",0);setVariable("skillWizardry",0);setVariable("skillSacredMagic",0);
        setVariable("attributeStrength",25);setVariable("attributeAgility",25);setVariable("attributeIntellect",25);
        RolePlay.setParams(this);
        setVariable("stamina",getVariable("maxStamina"));
        setVariable("health",getVariable("maxHealth"));

        List<MagicEffect> mes = new ArrayList<>();
        setVariable("effects",mes);
        setVariable("vision",10);
        setVariable("damage",9.0);
        setVariable("damageType","armorStrike");
        setVariable("distant",32.0);
        setVariable("armorPierce",0);
        setVariable("armorSlash",0);
        setVariable("armorStrike",0);
        setVariable("armorFire",0);
        setVariable("armorFreeze",0);
        setVariable("armorElectro",0);
        setVariable("armorAcid",0);
        setVariable("armorMental",0);
        setVariable("armorWizardry",0);
        setVariable("armorSacred",0);
        setVariable("good",0);
        setVariable("bloodTexture",0);
        setVariable("voiceTimeWait",0);
        countEquipLoad();
    }
    private String[] itemsNames = {"weaponCur", "shieldCur", "weaponSpare", "shieldSpare", "armorHead", "armorArms", "armorTorso", "armorLegs", "uses1", "uses2", "uses3", "uses4", "uses5", "uses6", "uses7", "uses8"};
    private String[] spellNames = {"spells1",
            "spells2",
            "spells3",
            "spells4",
            "spells5",
            "spells6",
            "spells7",
            "spells8",
    };

    public void countEquipLoad(){
        int weight = 0;
        for(String el : itemsNames){
            if(getVariable(el) != null && !getVariable(el).equals("null")){
                weight += ((Item)getVariable(el)).getVariableInteger("weight");
            } else {
                setVariable(el,null);
            }
        }
        for(String el : spellNames){
            if(getVariable(el) != null && !getVariable(el).equals("null")){
                weight += 1;
            } else {
                setVariable(el,null);
            }
        }
        setVariable("equipLoad",(double)weight);
    }

    public boolean playedHalfStep = false;

    public double overload(){
        if(this instanceof NPC) {
            return 0;

        } else {
            return (double) getVariable("equipLoad") / (double) getVariable("maxEquipLoad");

        }
    }

    double damageArmsPercent(){
        return (double)getVariable("damageArms")/(double)getVariable("maxHealth")*2;
    }
    double damageLegsPercent(){
        return (double)getVariable("damageLegs")/(double)getVariable("maxHealth")*2;
    }

    public void death(Player lastHit) {
        if(getVariable("dead").equals(1)) return;
        PlayerPrints playerPrints = new PlayerPrints(getVariableDouble("locX"),getVariableDouble("locY"),getVariableInteger("bloodTexture"));
        StartGame.game.bloods.add(playerPrints);
        setVariable("dead",1);
        PlayerVoice.setVoiceIngame(this,1,true);
        int deadXP = (int)getVariable("exp");
        if(StartGame.game.controlGroup.contains(lastHit)){
            for(Player el : StartGame.game.controlGroup){
                RolePlay.gainXP(el,deadXP);
            }
        }
        if(this.getClass() == Player.class) {
            if (StartGame.game.allPlayersIsDead()) {
                if (!(Start.getScreen() instanceof GameOverScreen) && !(Start.getScreen() instanceof MainMenu))
                    Start.setScreen(new GameOverScreen());
            }
        }
    }

    @Override
    public int logic() {
        PlayerVoice.workVoiceInGame(this);
        if(getVariable("dead").equals(1)){

        } else {
            if (!getVariable("attackProgress").equals(0.0)) {
                if(getVariable("castingAnim").equals(0)){
                    PlayerAttacks.attackProgress(this);
                } else {
                    PlayerCastMagic.castProgress(this);
                }
            } else {
                if (getVariable("moveTargetType").equals(MOVE_TARGET_ENEMY)) {//is player attacks someone
                    if (((Actor) getVariable("moveTargetAttack")).getVariable("dead").equals(1)) {//is target dead
                        PlayerMovesSet.setTarget(this,getTileLoc()[0], getTileLoc()[1]);//stop
                        setVariable("moveTargetType", MOVE_TARGET_NON);
                        setVariable("moveTargetAttack", null);

                    } else {
                        Player target = (Player) getVariable("moveTargetAttack");
                        if(!target.isVisible()){
                            stop(this);
                            setVariable("moveTargetType", MOVE_TARGET_NON);
                            setVariable("moveTargetAttack", null);
                            PlayerVoice.setVoiceInterface(this,PlayerVoice.VOICE_LOSS_TARGET);
                            return 0;
                        }
                        if(target.getVariable("good").equals(0)) {
                            PlayerAttacks.setTargetEnemy(this, target);//attack
                        } else  {
                            PlayerAttacks.setTargetFriend(this,target);
                        }
                    }
                }
                if (getVariable("moveTargetType").equals(MOVE_TARGET_CAST)) {//is player attacks someone
                    if (((Actor) getVariable("moveTarget")).getVariable("dead").equals(1)) {//is target dead
                        PlayerMovesSet.setTarget(this,getTileLoc()[0], getTileLoc()[1]);//stop
                        setVariable("moveTargetType", MOVE_TARGET_NON);
                        setVariable("moveTarget", null);

                    } else {
                        PlayerCastMagic.setTargetEnemy(this,(Player) getVariable("moveTarget"));//attack
                    }
                }
                RolePlay.recovery(this);
                List<Point> path = (List<Point>) getVariable("path");
                if(path != null) {
                    if(path.size() > 0) {
                        double[] loc = {(double) getVariable("locX"), (double) getVariable("locY")};
                        double[] tar = {(double) path.get(0).getX(), (double) path.get(0).getY()};
                        double speed = (double) getVariable("speed");
                        double rast = Mathp.rast(loc[0], loc[1], tar[0], tar[1]);
                        if (rast > 0) {
                            speed = PlayerMovesWorks.getSpeed(this, speed);
                        }
                        PlayerMovesWorks.move(this, tar, loc, rast, speed, path);
                    }
                }
                if (!(this instanceof NPC)) {
                    if(path == null || path.size() == 0) {
                        AutoControl.autoPilot(this);
                    }
                }
            }
            List<MagicEffect> effects = ((List<MagicEffect>)getVariable("effects"));
            for(int i = 0; i < effects.size();){
                int remove = effects.get(i).work(this);
                if(remove == 1){
                    effects.remove(i);
                } else {
                    i++;
                }
            }
        }
        if(getVariable("script")!=null){
            ExecuteScripts.execute(getVariable("script")+"",this);
        }
        return 0;
    }


    private double[] getTextureLocation(){
        return new double[]{(int)getVariable("partX"),(int)getVariable("partY")};
    }
    public double[] getTileLoc(){
        return new double[]{(double)getVariable("locX"),(double)getVariable("locY")};
    }

    private static final double FRAMES_IN_LINE = 4;
    @Override
    public void draw() {
        //if(!isVisible()) return;
        double[] location = getTileLoc();
        double locX = location[0] + Camera.locX;
        double locY = location[1] + Camera.locY;
        draw(locX,locY,SIZE);
    }

    public boolean isVisible(){
        if(this instanceof NPC) {
            for (Player el : StartGame.game.controlGroup) {
                double rast = Mathp.rast(el.getTileLoc()[0], el.getTileLoc()[1], getTileLoc()[0], getTileLoc()[1]);
                if (rast < 32 * el.getVariableInteger("vision")*StartGame.game.region.getVisionMult()) {
                    if (PathFinding.getCollisionPoint(el.getTileLoc()[0], el.getTileLoc()[1], getTileLoc()[0], getTileLoc()[1], false) == null)
                        return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void draw(double x, double y, double s) {
        double[] part = getTextureLocation();
        double cutLeft = part[0]/FRAMES_IN_LINE;
        double cutRight = 1-(part[0]+1)/FRAMES_IN_LINE;
        double cutUp = part[1]/FRAMES_IN_LINE;
        double cutDown = 1-(part[1]+1)/FRAMES_IN_LINE;
        Render2D.angleCutDraw("player",(String) getVariable("ingameTexture"),x,y,s,s,0,cutLeft,cutRight,cutUp,cutDown);

    }

    public boolean isUndercursor(int curX, int curY){
        if(!isVisible()) return false;
        double[] location = getTileLoc();
        return Math.abs(curX - Camera.locX - location[0]) < SIZE / 2 && Math.abs(curY - Camera.locY - location[1]) < SIZE / 2;
    }

    public boolean isUndercursorAll(int curX, int curY){
        double[] location = getTileLoc();
        return Math.abs(curX - Camera.locX - location[0]) < SIZE / 2 && Math.abs(curY - Camera.locY - location[1]) < SIZE / 2;
    }


    public void setOnCamera(){
        int targetCameraX = (int) (-getVariableTrunked("locX")+ Render2D.getWindowWidth()/2);
        int targetCameraY = (int) (-getVariableTrunked("locY")+ Render2D.getWindowHeight()/2);

        Camera.setLocation(targetCameraX,targetCameraY);
    }

    public void drawProjectile() {
        if(!isVisible()) return;
        Projectile melee = (Projectile) getVariable("projectile");
        boolean good;
        Item weapon = (Item)getVariable("weaponCur");
        if(weapon == null){
            good = true;
        } else {
            good = !weapon.getVariable("filter").equals(4);
        }
        if(melee != null && !getVariable("dead").equals(1) && good)
            melee.draw();
    }

    public void castingAnim() {
        if(!isVisible()) return;
        if (getVariable("castingAnim").equals(1)) {
            double locX = getTileLoc()[0] + Camera.locX;
            double locY = getTileLoc()[1] + Camera.locY;
            String texture = "spells/casts/"+((Actor)getVariable("castingSpell")).getVariable("castAnim")+"";
            int part = (int)((double)getVariable("attackProgress")/0.025) % 8;
            double part1 = part;
            double cutLeft = part1/8.0;
            double cutRight = 1-(part1+1)/8.0;
            Render2D.angleCutDraw(texture,locX,locY,64,64,0,cutLeft,cutRight,0,0);
        }
    }

    int isWalking() {
        double[] loc = {(double)getVariable("locX"),(double)getVariable("locY")};
        double[] tar = {(double)getVariable("tarX"),(double)getVariable("tarY")};
        double rast = Mathp.rast(loc[0],loc[1],tar[0],tar[1]);
        if (rast > 0) {
            return getVariableInteger("speedGear");
        }
        return -1;
    }

    @Override
    public void toDo(String command,String[] args){
        switch (command){
            case "setTargetNon":
                PlayerMovesSet.setTarget(this,Integer.parseInt(args[0]),Integer.parseInt(args[1]));
                break;
        }
    }

    public boolean isAttackingSomebody(){
        return getVariableInteger("moveTargetType") == MOVE_TARGET_ENEMY;
    }

    public String[][] info() {
        String[][] info = null;
            info = new String[][]{
                    {"ID:", getVariable("ID") + ""},
            };
        return info;
    }
    public boolean addEffect(int effectCode, double effectPower, int effectTime,int school,String texture, int cutX, int cutY){
        List<MagicEffect> mes =  ((List<MagicEffect>) getVariable("effects"));
        MagicEffect oldEffect = null;
        for(MagicEffect el : mes){
            if(el.getVariable("code").equals(effectCode) && !el.getVariable("time").equals(-1)){
                double oldEffectPower = el.getVariableInteger("time") * el.getVariableDouble("effect");
                double newEffectPower = effectPower*effectTime;
                if(newEffectPower > oldEffectPower){
                    oldEffect = el;
                    break;
                } else {
                    return false;
                }
            }
        }
        if(oldEffect != null){
            mes.remove(oldEffect);
            MagicBase.removeEffect(this,oldEffect.getVariableInteger("code"),oldEffect.getVariableDouble("effect"),oldEffect.getVariableInteger("spellClass"));
        }
        MagicEffect me = new MagicEffect(effectCode, effectPower, effectTime,school,texture,cutX,cutY);
        mes.add(me);
        return true;
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
    private static String[] skillsNames = {"skillHealth","skillStamina","skillRestoration","skillEquipLoad","skillAttackSpeed","skillDaggers","skillBlades","skillAxes","skillBlunts","skillSpears","skillBows","skillCrossbows","skillFireMagic","skillWaterMagic","skillEarthMagic","skillAirMagic","skillMentalMagic","skillWizardry","skillSacredMagic","skillLockPick"};
    private static final int LOSE_XP_PER_SKILL_AFTRE_RESSURECT = 50;
    public void reset() {
        setVariable("health",getVariableDouble("maxHealth"));
        setVariable("stamina",getVariableDouble("maxStamina"));
        setVariable("damageArms",0.0);
        setVariable("damageLegs",0.0);
        List<MagicEffect> effects = ((List<MagicEffect>)getVariable("effects"));
        for(MagicEffect ele : effects){
            if(ele.getVariableInteger("time") >= 0)
                MagicBase.removeEffect(this,ele.getVariableInteger("code"),ele.getVariableDouble("effect"),ele.getVariableInteger("spellClass"));
        }
        effects.clear();
        if(getVariable("dead").equals(1)){
            setVariable("dead",0);
            int totalSkillsVal = 0;
            for (int i = 0; i < skillsNames.length; i++) {
                totalSkillsVal += getVariableInteger(skillsNames[i]);
            }
            int curEXP = getVariableInteger("exp");
            curEXP -= totalSkillsVal * LOSE_XP_PER_SKILL_AFTRE_RESSURECT;
            if(curEXP < 0) curEXP = 0;
            setVariable("exp",curEXP);
            setVariable("locX", (double)StartGame.game.lastRessurect.getVariableInteger("locX"));
            setVariable("locY", (double)StartGame.game.lastRessurect.getVariableInteger("locY"));
            setVariable("moveAnimState",0.0);
            setVariable("attackProgress",0.0);
            setVariable("castingAnim",0);
            setVariable("moveTargetType",MOVE_TARGET_NON);
            setVariable("projectile", null);
            stop(this);

        }
    }
}
