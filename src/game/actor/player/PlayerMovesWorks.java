package game.actor.player;

import engine.Mathp;
import engine.audio3.AudioManager;
import engine.audio3.SoundSource;
import engine.render.Camera;
import game.StartGame;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Point;

import java.util.List;

import static game.actor.player.PlayerMovesSet.*;
import static game.actor.player.RolePlay.RECOVERY_TIME_AFTER_RUN;

class PlayerMovesWorks {
    static double getSpeed(Player player,double speed){
        double overLoad = player.overload();
        double legsDamage = player.damageLegsPercent();
        player.setVariable("timeBeforeRecovery", (RECOVERY_TIME_AFTER_RUN));
        if (player.getVariable("speed").equals(player.getVariable("speedRun"))) {
            if (overLoad > 1) {
                PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_CANT_RUN_OVERLOAD);

                PlayerMovesSet.setSpeed(player,1);
            } else if (legsDamage > 0.9){
                PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_CANT_RUN_CRIPLEG);

                PlayerMovesSet.setSpeed(player,1);

            }else {
                double curStamina = (double) player.getVariable("stamina");
                double maxStamina = (double) player.getVariable("maxStamina");

                double STAMINA_FOR_RUNNING = 0.01;
                double staminaMinus = maxStamina * STAMINA_FOR_RUNNING;
                curStamina -= staminaMinus;
                if (curStamina <= 0) {
                    curStamina = 0;
                    PlayerMovesSet.setSpeed(player,1);
                    PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_CANT_RUN_STAMINA);

                }
                player.setVariable("stamina", curStamina);
            }
        }
        if (legsDamage > 0.9) {
            speed *= 0.5;
        } else if (legsDamage > 0.65) {
            speed *= 0.7;
        } else if (legsDamage > 0.4) {
            speed *= 0.9;
        }
        if (overLoad > 1.2) {
            speed *= 0.7;
        } else if (overLoad > 0.7) {
            speed *= 0.8;
        } else if (overLoad > 0.3) {
            speed *= 0.9;
        }
        return speed;
    }

    static String[] stepsSounds = {"earth","grass","stone","sand","snow","wood","dirt","water"};
    static void playStep(Player player, double angle){
        double rastCur = Mathp.rast(Display.getWidth() / 2, Display.getHeight() / 2, player.getVariableDouble("locX") + Camera.locX, player.getVariableDouble("locY") + Camera.locY);
        double volume = 1-rastCur/512.0;
        if(volume < 0) volume = 0;
        int sound = (int) (Mathp.random()*6+1);
        int staX = (int)(player.getVariableDouble("locX")+16)/32;
        int staY = (int)(player.getVariableDouble("locY")+16)/32;
        SoundSource sr = AudioManager.playSound("steps/"+ stepsSounds[StartGame.game.tileMaterial[staX][staY]]+"/"+sound);
        sr.setGain((float) volume);

        PlayerPrints pr = new PlayerPrints(player.getVariableDouble("locX"),player.getVariableDouble("locY"),0, Math.toDegrees(angle)+90);
        StartGame.game.steps.add(pr);
    }
    static void move(Player player, double[] tar, double[] loc, double rast, double speed, List<Point> path){
        if(rast > 0){
        }
        if (rast > speed) {

            double angle = Math.atan2(tar[1] - loc[1], tar[0] - loc[0]);
            int oldPart = player.getVariableInteger("partY");
            if (Math.abs(angle) <= Math.PI * 0.25) {
                player.setVariable("partY", 2);
            } else if (Math.abs(angle) >= Math.PI * 0.75) {
                player.setVariable("partY", 1);
            } else if (angle <= Math.PI * 0.75 && angle >= Math.PI * 0.25) {
                player.setVariable("partY", 0);
            } else {
                player.setVariable("partY", 3);
            }
            int newPart = player.getVariableInteger("partY");
            if(oldPart != newPart){
                PlayerPrints pr = new PlayerPrints(player.getVariableDouble("locX"),player.getVariableDouble("locY"),0, Math.toDegrees(angle)+90);
                StartGame.game.steps.add(pr);
            }
            double moveAnimState = (double) player.getVariable("moveAnimState");
            moveAnimState += 0.02 * speed;
            if (moveAnimState >= 1) {
                moveAnimState -= 1;
            }
            if (moveAnimState < 0.5) {
                player.setVariable("partX", 0);
                if(player.playedHalfStep) {
                    player.playedHalfStep = false;
                    playStep(player,angle);
                }
            } else {
                if(!player.playedHalfStep){
                    player.playedHalfStep = true;
                    playStep(player,angle);
                }
                player.setVariable("partX", 2);
            }
            player.setVariable("moveAnimState", moveAnimState);
            loc[0] += speed * Math.cos(angle);
            loc[1] += speed * Math.sin(angle);
            player.setVariable("locX", loc[0]);
            player.setVariable("locY", loc[1]);
        } else {
            player.setVariable("locX", tar[0]);
            player.setVariable("locY", tar[1]);
            if(path.size() == 1) {
                if (player.getVariable("moveTargetType").equals(MOVE_TARGET_LOOT)) {
                    PlayerMovesSet.loot(player);
                }
                if (player.getVariable("moveTargetType").equals(MOVE_TARGET_ITEM)) {
                    PlayerMovesSet.pickup(player);
                }
                if (player.getVariable("moveTargetType").equals(MOVE_TARGET_CHEST)) {
                    PlayerMovesSet.openChest(player);
                }
                if (player.getVariable("moveTargetType").equals(MOVE_TARGET_DOOR)) {
                    PlayerMovesSet.openDoor(player);
                }
                if (player.getVariable("moveTargetType").equals(MOVE_TARGET_ACTIVATOR)) {
                    PlayerMovesSet.activate(player);
                }
                player.setVariable("partX", 1);
                player.setVariable("moveAnimState", 0.0);
                player.playedHalfStep = true;

                player.setVariable("path",null);
            } else {
                path.remove(0);
            }
        }
    }
}
