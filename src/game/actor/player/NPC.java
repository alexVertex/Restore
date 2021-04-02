package game.actor.player;

import engine.Mathp;
import engine.Start;
import engine.render.Camera;
import engine.render.Render2D;
import game.StartGame;
import game.actor.enviroment.Item;
import game.actor.magic.MagicEffect;
import game.actor.actorsHyper.NPCDataBase;
import game.actor.story.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static game.actor.player.PlayerMovesSet.*;

public class NPC extends Player {
    public static final int VISION_NORMAL = 0;

    public NPC(int underPlayerControl, String portret, int portretX, int portretY, String ingameTexture, double locX, double locY, double maxHealth, double health, double maxStamina, double stamina, double maxLoad, double load, double attackSpeed, int exp, int vision, int visionType) {
        super(underPlayerControl, portret, portretX, portretY, ingameTexture, locX, locY, maxHealth, health, maxStamina, stamina, maxLoad, load, attackSpeed);
        setVariable("exp", exp);
        setVariable("vision", vision);
        setVariable("visionType", visionType);
        setVariable("searchTimeWait", 0);
        setVariable("noticed", 0);
        setVariable("alarmTimer", 0);


    }

    public NPC(String ID,double x, double y) {
        super(ID);
        setVariable("locX",x);
        setVariable("locY",y);
        setVariable("tarX",x);
        setVariable("tarY",y);
        setVariable("partX",1);
        setVariable("partY",0);
        setVariable("dead",0);
        setVariable("alarmed",0);
        setVariable("attackProgress",0.0);
        setVariable("timeBeforeRecovery",0);
        setVariable("moveAnimState",0.0);
        setVariable("searchTimeWait", 0);
        setVariable("noticed", 0);
        setVariable("castingAnim",0);
        setVariable("battleMode",0);
        setVariable("damageArms",0.0);
        setVariable("damageLegs",0.0);
        setVariable("alarmTimer", 0);
        setVariable("noticed", 0);
        setVariable("assistAlarmCoolDownTime", 0);
        setVariable("alarmX", 0.0);
        setVariable("alarmY", 0.0);
        List<MagicEffect> mes = new ArrayList<>();
        setVariable("effects",mes);
        if (ID != null) {
            HashMap<String, Object> itemVariables = NPCDataBase.getData(ID);

            for (String el : itemVariables.keySet()) {
                setVariable(el, itemVariables.get(el));
            }
        }
        setVariable("speed",getVariable("speedWalk"));
        List<Item> loot = new ArrayList<>();
        for(int i = 1; i <= 3;i++){
            String lootID = getVariable("loot"+i)+"";
            if(!lootID.equals("null")) {
                Item item = new Item(lootID, 0, 0, 0, 0, lootID, 1);
                loot.add(item);
            }
        }
        setVariable("loot",loot);
        setVariable("voiceTimeWait",0);

    }

    public void setBehavior(NPCPackage npcPackage) {
        setVariable("package", npcPackage);
        setVariable("alarmed", 0);
        setVariable("alarmTimer", 0);
        setVariable("attack", null);
    }


    void lootNPC() {
        for (Item el : (List<Item>) getVariable("loot")) {
            StartGame.game.addItem(el);
        }
        int minGold = getVariableInteger("lootGoldMin");
        int maxGold = getVariableInteger("lootGoldMax");
        int totalGold = (int)(Mathp.random()*(maxGold-minGold+1)+minGold);
        Event.eventAddGold(totalGold);
        StartGame.game.gold += totalGold;
    }


    private static final int CALM = 0;
    private static final int FULLALARM = 1;
    private static final int PARTALARM = 2;
    private static final int SEARCH = 3;
    private static final int ASSISTALARM = 4;


    private void attack(){
        Player target = getTarget();
        if(target != null) {
            PlayerMovesSet.setTargetAttack(this, target, -1);
            setVariable("attack", target);
        }
    }

    private Player getTarget(){
        List<Player> targets = new ArrayList<>();
        for(Player el : StartGame.game.controlGroup){
            if(el.getVariable("dead").equals(1)) continue;
            if(visualContact(this,el)){
                targets.add(el);
            }
        }
        if(targets.size() == 0){
            setVariable("alarmed",PARTALARM);

            if(getVariableInteger("alarmTimer") > 50)
                setVariable("alarmTimer", 0);
            return null;
        }
        targets.sort((o1, o2) -> {
            double rast1 = Mathp.rast(o1.getTileLoc()[0],o1.getTileLoc()[1],getTileLoc()[0],getTileLoc()[1]);
            double rast2 = Mathp.rast(o2.getTileLoc()[0],o2.getTileLoc()[1],getTileLoc()[0],getTileLoc()[1]);
            return Double.compare(rast1,rast2);
        });
        setVariable("alarmX", targets.get(0).getVariable("locX"));
        setVariable("alarmY", targets.get(0).getVariable("locY"));
        return targets.get(0);
    }



    @Override
    public int logic() {
        if (getVariable("dead").equals(1) || StartGame.game.controlGroup.contains(this)) {
            return 0;
        }
        if(getVariableInteger("assistAlarmCoolDownTime") >0 ){
            setVariable("assistAlarmCoolDownTime",getVariableInteger("assistAlarmCoolDownTime")-1);
        }
        if(getVariable("alarmed").equals(CALM) || getVariableInteger("good") == 1){
            NPCPackage packageCur = (NPCPackage)getVariable("package");
            if(packageCur != null) {
                double[] tar = packageCur.use(getVariableTrunked("locX"), getVariableTrunked("locY"), getVariableTrunked("tarX"), getVariableTrunked("tarY"));
                setTarget(this, tar[0], tar[1]);
            } else {
                setTarget(this, getTileLoc()[0], getTileLoc()[1]);
            }
            setSpeed(this,2);
            scanningForEnemys();
        } else
        if(getVariable("alarmed").equals(PARTALARM)) {
            if(getVariable("alarmed").equals(ASSISTALARM)){
                boolean needDropAssist = true;

                for (Player el : StartGame.game.getNpcList()) {
                    boolean obstackle = PathFinding.getCollisionPoint(getTileLoc()[0],getTileLoc()[1],el.getTileLoc()[0],el.getTileLoc()[1],false) != null;
                    if(obstackle) continue;
                    if(el.getVariable("dead").equals(1)) continue;
                    double rastTarget = Mathp.rast(this.getTileLoc()[0], this.getTileLoc()[1], el.getTileLoc()[0], el.getTileLoc()[1]);
                    if (rastTarget < getVariableInteger("vision")*32 && ( el.getVariable("alarmed").equals(FULLALARM))) {
                        setVariable("alarmTimer", 0);
                        setVariable("alarmed", ASSISTALARM);
                        setVariable("alarmX", el.getVariable("locX"));
                        setVariable("alarmY", el.getVariable("locY"));
                        setVariable("assistAlarmCoolDownTime", 500);
                        needDropAssist = false;
                    }
                }
                if(needDropAssist){
                    setVariable("alarmed", CALM);
                }
            }
            int alarmTimer = getVariableInteger("alarmTimer");
            alarmTimer++;
            if (alarmTimer > 50) {
                double x = getVariableDouble("alarmX");
                double y = getVariableDouble("alarmY");
                setTarget(this, x, y);
            }
            if (alarmTimer > 500) {
                setVariable("alarmed",CALM);
            }
            setVariable("alarmTimer", alarmTimer);
            scanningForEnemys();
        }else
        if(getVariable("alarmed").equals(FULLALARM)){
            attack();
        }
        super.logic();
        return 0;
    }

    private void scanningForEnemys() {
        boolean toFullAlarm = getVariableInteger("alarmed") == FULLALARM;

        for(Player el : StartGame.game.controlGroup){
            if(el.getVariable("dead").equals(1)) continue;
            if(!toFullAlarm) {
                boolean ear = earContact(this, el);
                if (ear) {

                    setVariable("alarmed", PARTALARM);
                    setVariable("assistAlarmCoolDownTime", 500);
                    setVariable("alarmX", el.getVariable("locX"));
                    setVariable("alarmY", el.getVariable("locY"));
                    if(getVariableInteger("alarmTimer") > 50)
                        setVariable("alarmTimer", 0);
                }
            }
            boolean visual = visualContact(this, el);
            if(visual){
                setVariable("alarmed",FULLALARM);
                setVariable("assistAlarmCoolDownTime", 500);
                toFullAlarm = true;
            }
        }
        if(!toFullAlarm){
            if(getVariable("alarmed").equals(CALM)) {
                for (Player el : StartGame.game.getNpcList()) {
                    if(el.getVariableInteger("good") == 1) continue;
                    boolean obstackle = PathFinding.getCollisionPoint(getTileLoc()[0],getTileLoc()[1],el.getTileLoc()[0],el.getTileLoc()[1],false) != null;
                    if(obstackle) continue;
                    if(el.getVariable("dead").equals(1)) continue;
                    if (visualContact(this, el) && el.getVariable("dead").equals(1) && el.getVariable("noticed").equals(0)) {
                        el.setVariable("noticed", 1);
                        setVariable("alarmTimer", 50);
                        setVariable("alarmed", PARTALARM);
                        setVariable("alarmX", el.getVariable("locX"));
                        setVariable("alarmY", el.getVariable("locY"));
                        setVariable("assistAlarmCoolDownTime", 500);

                    }
                    double rastTarget = Mathp.rast(this.getTileLoc()[0], this.getTileLoc()[1], el.getTileLoc()[0], el.getTileLoc()[1]);
                    if (rastTarget < getVariableInteger("vision")*32 && ( el.getVariable("alarmed").equals(FULLALARM))) {
                        setVariable("alarmTimer", 0);
                        setVariable("alarmed", PARTALARM);
                        setVariable("alarmX", el.getVariable("alarmX"));
                        setVariable("alarmY", el.getVariable("alarmY"));
                        setVariable("assistAlarmCoolDownTime", 500);
                    }
                }
            }
        }
    }

    private static double[] speedEarMult = {0,2,1,0.2};
    private static boolean earContact(Player player,Player target){
        double rastVision = (int)player.getVariable("vision")*16;
        double rastTarget = Mathp.rast(target.getTileLoc()[0],target.getTileLoc()[1],player.getTileLoc()[0],player.getTileLoc()[1]);
        boolean obstackle = PathFinding.getCollisionPoint(target.getTileLoc()[0],target.getTileLoc()[1],player.getTileLoc()[0],player.getTileLoc()[1],false) != null;
        if(obstackle){
            rastVision *= 0.5;
        }
        int index = target.isWalking()+1;
        rastVision *= speedEarMult[index];
        return rastTarget < rastVision;
    }

    private static boolean visualContact(Player player,Player target){
        double angleTarget = Math.atan2(target.getTileLoc()[1]-player.getTileLoc()[1],target.getTileLoc()[0]-player.getTileLoc()[0]);
        double playerLookAngle = getAngleLook(player);
        double angle = Mathp.angleBetwinAngles(angleTarget,playerLookAngle);
        double rastTarget = Mathp.rast(target.getTileLoc()[0],target.getTileLoc()[1],player.getTileLoc()[0],player.getTileLoc()[1]);
        boolean obstackle = PathFinding.getCollisionPoint(target.getTileLoc()[0],target.getTileLoc()[1],player.getTileLoc()[0],player.getTileLoc()[1],false) != null;
        double rastVision = (int)player.getVariable("vision")*32;
        int visualType = player.getVariableInteger("visionType");
        if(visualType == 0) {
            if(angle > Math.PI / 3){
                rastVision*=0.5;
            }
            rastVision*= StartGame.game.region.getVisionMult();
            return angle < Math.PI / 2 && rastTarget < rastVision && !obstackle;
        } else if (visualType == 1){
            return rastTarget < rastVision;
        }
        return false;
    }

    private static double[] partsToAngles = {+Math.PI/2,Math.PI,0,-Math.PI/2};

    private static double getAngleLook(Player player){
        return partsToAngles[player.getVariableInteger("partY")];
    }

    public void getAttaked(Player attaker){
        setVariable("alarmed", PARTALARM);
        setVariable("assistAlarmCoolDownTime", 500);
        setVariable("alarmX", attaker.getVariable("locX"));
        setVariable("alarmY", attaker.getVariable("locY"));
        if(getVariableInteger("alarmTimer") > 50)
            setVariable("alarmTimer", 0);
    }

    public void drawSelect() {
        double[] location = getTileLoc();
        double x = location[0] + Camera.locX;
        double y = location[1] + Camera.locY;
        Render2D.simpleDraw("editorInMapSelector","editor/selector", x, y,32,32);
    }
}
