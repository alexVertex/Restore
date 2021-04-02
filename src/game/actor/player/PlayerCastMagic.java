package game.actor.player;

import engine.Actor;
import engine.Mathp;
import game.StartGame;
import game.actor.enviroment.Item;
import game.actor.magic.MagicProjectile;
import game.actor.magic.Spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static game.actor.player.PlayerMovesSet.*;
import static game.actor.player.RolePlay.RECOVERY_TIME_AFTER_RUN;

public class PlayerCastMagic {
     static void setTargetEnemy(Player player, Player targetEnemy){
        Spell spell = (Spell) player.getVariable("castingSpell");
        double weaponRange = (double)spell.getVariable("distant");
        boolean clear = PathFinding.getCollisionPoint(player.getTileLoc()[0],player.getTileLoc()[1],targetEnemy.getTileLoc()[0],targetEnemy.getTileLoc()[1],false) == null;
        if(Mathp.inRange(player.getTileLoc()[0],player.getTileLoc()[1],targetEnemy.getTileLoc()[0],targetEnemy.getTileLoc()[1],weaponRange) && clear){
            player.setVariable("path",null);
            generateProjectile(player,spell);
            player.setVariable("partX",1);
            double[] tar = targetEnemy.getTileLoc();
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
        } else {
            if(targetEnemy.getVariable("dead").equals(1)){
                PlayerMovesSet.stop(player);
                return;
            }
            player.setVariable("moveTarget",targetEnemy);
            player.setVariable("moveTargetType",MOVE_TARGET_CAST);
            PlayerMovesSet.setTargetMagic(player,targetEnemy,spell);
        }
    }

    private static void generateProjectile(Player player, Spell spell){
        if(((Actor)player.getVariable("moveTarget")).getVariable("dead").equals(1)){
            PlayerMovesSet.setTarget(player, player.getTileLoc()[0],player.getTileLoc()[1]);
            player.setVariable("moveTargetType",MOVE_TARGET_NON);
            return;
        }
        if(player.getVariable("castingAnim").equals(0)) {
            double attackProgressAdd = (double) player.getVariable("attackSpeed") + (double) spell.getVariable("attackSpeed");
            attackProgressAdd*=0.1;
            player.setVariable("castingAnim", 1);
            player.setVariable("attackProgressAdd", attackProgressAdd);
            player.setVariable("attackProgress", 0.001);
        }
    }

    static void castProgress(Player player) {
        double attackProgress = (double) player.getVariable("attackProgress");
        double attackProgressAdd = (double) player.getVariable("attackProgressAdd");
        double armsDamage = player.damageArmsPercent();//damage mode by armsDamage
        if(armsDamage > 0.9){
            attackProgressAdd *= 0.5;
        } else if (armsDamage > 0.65){
            attackProgressAdd *= 0.7;
        }else if (armsDamage > 0.4){
            attackProgressAdd *= 0.9;
        }
        attackProgress += attackProgressAdd;
        Spell spell = (Spell) player.getVariable("castingSpell");
        spell.setVariable("locX",player.getVariable("locX"));
        spell.setVariable("locY",player.getVariable("locY"));

        PlayerVoice.setVoiceIngame(spell, "spells/"+spell.getVariableString("spellClass"), false);
        PlayerVoice.workVoiceInGame(spell);
        if(attackProgress >= 1){
            attackProgress = 0.0;
            player.setVariable("castingAnim",0);
            double curStamina = (double) player.getVariable("stamina");
            int mana = ((Actor) player.getVariable("castingSpell")).getVariableInteger("mana");
            mana = manaCostThruMagic(mana,player);
            curStamina -= mana;
            player.setVariable("timeBeforeRecovery", (RECOVERY_TIME_AFTER_RUN));
            player.setVariable("stamina", curStamina);
            String name = spell.getVariable("itemName")+"";
            if(!Objects.equals(name, "null")){
                spell.setVariable("itemName",null);
                int part = Integer.parseInt(name.charAt(4)+"")+7;
                String[] itemsNames = {"weaponCur",
                        "shieldCur",
                        "weaponSpare",
                        "shieldSpare",
                        "armorHead",
                        "armorArms",
                        "armorTorso",
                        "armorLegs",
                        "uses1",
                        "uses2",
                        "uses3",
                        "uses4",
                        "uses5",
                        "uses6",
                        "uses7",
                        "uses8"};
                if(StartGame.game.getControled().getVariable(itemsNames[part]) != null) {
                    Item undressed = (Item) StartGame.game.getControled().getVariable(itemsNames[part]);
                    double load = (double) StartGame.game.getControled().getVariable("equipLoad");
                    load -= (int)undressed.getVariable("weight");
                    StartGame.game.getControled().setVariable("equipLoad",load);
                    StartGame.game.getControled().setVariable(itemsNames[part], null);
                }
                for(int i = part; i < itemsNames.length-1;i++){
                    StartGame.game.getControled().setVariable(itemsNames[i],StartGame.game.getControled().getVariable(itemsNames[i+1]));
                }
                StartGame.game.getControled().setVariable(itemsNames[itemsNames.length-1],null);
            }
            int targets = spell.getVariableInteger("targets");
            if(targets > 1){
                multiSpell(spell, targets, (Player)player.getVariable("moveTarget"),player);
            }
            MagicProjectile mp = new MagicProjectile(spell.getVariable("projectileTexture")+"",spell.getVariable("projectileExplode")+"",
                    (int)player.getTileLoc()[0],(int)player.getTileLoc()[1],spell.getVariableInteger("projectileSizeX"),spell.getVariableInteger("projectileSizeY"),
                    (double)spell.getVariable("projectileSpeed"),spell.getVariableInteger("projectileFrames"),
                    spell.getVariableInteger("projectileExplodeFrames"),(Player)player.getVariable("moveTarget"),spell.getVariableInteger("projectileExplodeRadius"),player,
                    spell.getVariableInteger("effect"),spell.getVariableInteger("power"),spell.getVariableInteger("time"),spell.getVariableString("spellClass"),
                    spell.getVariableString("soundExplode"),spell.getVariableString("soundProjectile"),spell.getTexture(),(int)spell.getVariable("partX"),(int)spell.getVariable("partY"));
            StartGame.game.projectiles.add(mp);
            PlayerMovesSet.stop(player);
        }
        player.setVariable("attackProgress",attackProgress);
    }

    private static void multiSpell(Spell spell,int targets, Player firstTarget,Player caster){
        List<Player> posibleTargets = new ArrayList<>();
        for(Player el : StartGame.game.controlGroup){
            if(el.getVariable("dead").equals(0)) {
                boolean clear = PathFinding.getCollisionPoint(caster.getTileLoc()[0],caster.getTileLoc()[1],el.getTileLoc()[0],el.getTileLoc()[1],false) == null;
                if(clear) {
                    posibleTargets.add(el);
                }
            }
        }
        for(Player el : StartGame.game.getNpcList()){
            if(el.getVariable("dead").equals(0)) {
                boolean clear = PathFinding.getCollisionPoint(caster.getTileLoc()[0],caster.getTileLoc()[1],el.getTileLoc()[0],el.getTileLoc()[1],false) == null;
                if(clear) {
                    posibleTargets.add(el);
                }
            }
        }
        posibleTargets.sort((o1, o2) -> {
            double rast1 = Mathp.rast(o1.getTileLoc()[0],o1.getTileLoc()[1],firstTarget.getTileLoc()[0],firstTarget.getTileLoc()[1]);
            double rast2 = Mathp.rast(o2.getTileLoc()[0],o2.getTileLoc()[1],firstTarget.getTileLoc()[0],firstTarget.getTileLoc()[1]);
            return Double.compare(rast1,rast2);
        });
        boolean homingSpell = spell.getVariable("targetType").equals(1);
        posibleTargets.remove(firstTarget);
        for(int i = 0;i < targets-1 && posibleTargets.size() > 0;i++){
            Player target = posibleTargets.get(0);
            if(!homingSpell){
                target = new Player(target.getTileLoc()[0],target.getTileLoc()[1]);
            }
            MagicProjectile mp = new MagicProjectile(spell.getVariable("projectileTexture")+"",spell.getVariable("projectileExplode")+"",
                    (int)caster.getTileLoc()[0],(int)caster.getTileLoc()[1],spell.getVariableInteger("projectileSizeX"),spell.getVariableInteger("projectileSizeY"),
                    (double)spell.getVariable("projectileSpeed"),spell.getVariableInteger("projectileFrames"),
                    spell.getVariableInteger("projectileExplodeFrames"),target,spell.getVariableInteger("projectileExplodeRadius"),caster,
                    spell.getVariableInteger("effect"),spell.getVariableInteger("power"),spell.getVariableInteger("time"),spell.getVariableString("spellClass"),
                    spell.getVariableString("soundExplode"),spell.getVariableString("soundProjectile"),spell.getTexture(),(int)spell.getVariable("partX"),(int)spell.getVariable("partY"));
            StartGame.game.projectiles.add(mp);
            posibleTargets.remove(0);
        }
    }

    public static int manaCostThruMagic(int mana, Player player){
        int intellect = player.getVariableInteger("attributeIntellect");
        double koef = 1 + (25-intellect)*0.03;
        return (int)(mana*koef);
    }
}
