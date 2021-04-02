package game.actor.magic;

import game.actor.enviroment.Item;
import game.actor.player.NPC;
import game.actor.player.Player;
import game.actor.player.PlayerVoice;

public class MagicBase {

    private static Magics[] shools = {new FireMagic(),new FireMagic(),new FireMagic(),new FireMagic(),new FireMagic(),new FireMagic(),new SacralMagic()};

    public static boolean isEffectPeriodic(int effectCode, int shcool){
        return shools[shcool].addEffect(effectCode);
    }

    static double getEffectpowerPeriodic(Player affected, String armorType, double effectPower, int effectTime){
        double armor = 0;
        for (String itemsName : itemsNames) {
            Item headArmor = (Item) affected.getVariable(itemsName);
            if (headArmor != null) {
                armor += headArmor.getVariableInteger(armorType);
            }
        }
        effectPower -= armor/4;
        if(effectPower < 0) effectPower = 0;
        effectPower /= Math.abs(effectTime);
        return effectPower;
    }

    static void addEffect(Player affected, Player lastHit, int effectCode, double effectPower, int effectTime, boolean pereodic, int shcool, String textute, int cutX, int cutY) {
        if (isEffectPeriodic(effectCode, shcool)) {
            if (effectTime < 0) {
                shools[shcool].workEffect(affected, lastHit, effectCode, effectPower, effectTime, pereodic);
            } else {
                effectPower = getEffectpowerPeriodic(affected, "armorFire", effectPower, effectTime);
                affected.addEffect(effectCode, effectPower, effectTime,shcool,textute,cutX, cutY);
                shools[shcool].workEffect(affected, lastHit, effectCode, effectPower, effectTime, pereodic);
            }
        } else {
            affected.addEffect(effectCode, effectPower, effectTime,shcool,textute,cutX, cutY);
            shools[shcool].workEffect(affected, lastHit, effectCode, effectPower, effectTime, pereodic);
        }
    }
    static void workEffect(Player affected, int effectCode, double effectPower, int effectTime, boolean pereodic, int shcool){
        shools[shcool].workEffect(affected,null,  effectCode,  effectPower,  effectTime,pereodic);
    }
    public static void removeEffect(Player affected, int effectCode, double effectPower,int school) {
        shools[school].removeEffect(affected, effectCode, effectPower);
    }

    private static String[] itemsNames = {
            "armorHead",
            "armorArms",
            "armorTorso",
            "armorLegs",
    };

    static void magicDamage(Player affected, Player lastHit, double damage, String damageType, int time, boolean periodic){
        double armor = 0;
        for (String itemsName : itemsNames) {
            Item headArmor = (Item) affected.getVariable(itemsName);
            if (headArmor != null) {
                armor += headArmor.getVariableInteger(damageType);
            }
        }
        damage -= armor/4;
        if(damage < 0) damage = 0;
        if(time < 0)
            damage /= Math.abs(time);
        double health = (double)affected.getVariable("health");
        health -= damage;
        health = Math.ceil(health*100)/100;
        if(health <= 0){
            affected.death(lastHit);
            health = 0;
        }
        affected.setVariable("health",health);
        if(affected instanceof NPC && lastHit != null){
            ((NPC)affected).getAttaked(lastHit);
        }
        double arms = (double)affected.getVariable("damageArms");
        double legs = (double)affected.getVariable("damageLegs");
        legs += damage*0.5;
        arms += damage*0.5;
        affected.setVariable("damageArms",arms);
        affected.setVariable("damageLegs",legs);

        PlayerVoice.setVoiceIngame(affected,0,false);

    }
}
