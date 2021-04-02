package game.actor.player;

import engine.Mathp;
import game.StartGame;

import java.util.ArrayList;
import java.util.List;

import static game.actor.player.PlayerMovesSet.MOVE_TARGET_ENEMY;


public class AutoControl {
    public static final int BATTLEMODE_OVERWATCH = 0;
    public static final int BATTLEMODE_WAIT = 1;
    public static final int BATTLEMODE_BLOCK = 2;
    public static final int BATTLEMODE_ATTACK = 3;

    public static void changeBattleMode(Player player) {
        int rotate = player.getVariableInteger("battleMode");
        rotate++;
        if (rotate == 4) {
            rotate = 0;
        }
        player.setVariable("battleMode", rotate);
    }

    public static void changeBattleMode(Player player, int change) {
        player.setVariable("battleMode", change);
    }

    static void autoPilot(Player player) {
        switch (player.getVariableInteger("battleMode")){
            case BATTLEMODE_OVERWATCH:
                overwatch(player);
                break;
            case BATTLEMODE_ATTACK:
                counterAttack(player);
                break;
            case BATTLEMODE_BLOCK:
                blockStance(player);
                break;
        }
    }

    private static void overwatch(Player player) {
        if(player.getVariable("moveTargetType").equals(MOVE_TARGET_ENEMY)) return;
        for (Player el : StartGame.game.getNpcList()) {
            if (el.getVariable("dead").equals(1) || el.getVariable("good").equals(1)) continue;
            if (Mathp.inRange(el.getTileLoc()[0], el.getTileLoc()[1], player.getTileLoc()[0], player.getTileLoc()[1], player.getVariableInteger("vision") * 32) ) {
                if(PathFinding.getCollisionPoint(el.getTileLoc()[0],el.getTileLoc()[1],player.getTileLoc()[0],player.getTileLoc()[1],false) == null) {
                    PlayerMovesSet.setTargetAttack(player, el, -1);
                    PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_AUTOATTACK);
                }
            }
        }
    }

    private static void counterAttack(Player player) {
        if(player.getVariable("moveTargetType").equals(MOVE_TARGET_ENEMY)) return;
        for (Player el : StartGame.game.getNpcList()) {
            if (el.getVariable("dead").equals(1)) continue;
            Object target = el.getVariable("moveTarget");
            if (target instanceof Player) {
                if (el.getVariable("dead").equals(1) || el.getVariable("attack") != player) continue;
                if (StartGame.game.controlGroup.contains(target)) {
                    if(PathFinding.getCollisionPoint(el.getTileLoc()[0],el.getTileLoc()[1],player.getTileLoc()[0],player.getTileLoc()[1],false) == null) {
                        PlayerMovesSet.setTargetAttack(player, el, -1);
                        PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_AUTOATTACK);
                    }
                }
            }
        }
    }
    private static void blockStance(Player player) {
        List<Player> attacking = new ArrayList<>();
        for (Player el : StartGame.game.getNpcList()) {
            if (el.getVariable("dead").equals(1) || el.getVariable("attack") != player) continue;
            attacking.add(el);
        }
        attacking.sort((o1, o2) -> {
            double rast1 = Mathp.rast(o1.getTileLoc()[0],o1.getTileLoc()[1],player.getTileLoc()[0],player.getTileLoc()[1]);
            double rast2 = Mathp.rast(o2.getTileLoc()[0],o2.getTileLoc()[1],player.getTileLoc()[0],player.getTileLoc()[1]);
            return Double.compare(rast1,rast2);
        });
        if(attacking.size() > 0){
            double[] tar = attacking.get(0).getTileLoc();
            double angle = Math.atan2(tar[1] - player.getTileLoc()[1], tar[0] - player.getTileLoc()[0]);
            if (Math.abs(angle) <= Math.PI * 0.25) {
                player.setVariable("partY", 2);
            } else if (Math.abs(angle) >= Math.PI * 0.75) {
                player.setVariable("partY", 1);
            } else if (angle <= Math.PI * 0.75 && angle >= Math.PI * 0.25) {
                player.setVariable("partY", 0);
            } else {
                player.setVariable("partY", 3);
            }
        }
    }
}
