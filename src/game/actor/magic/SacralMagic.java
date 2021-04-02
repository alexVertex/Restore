package game.actor.magic;

import game.actor.player.Player;

public class SacralMagic extends Magics{
    private static final int SACRAL_MAGIC_RESTORE_HEALTH = 3;

    @Override
    boolean addEffect(int effectCode) {
        switch (effectCode){
            case SACRAL_MAGIC_RESTORE_HEALTH://Лечение
                return true;
        }
        return false;
    }
    @Override
    void workEffect(Player affected, Player lastHit, int effectCode, double effectPower, int effectTime, boolean periodic) {
        double curHealth = (double)affected.getVariable("health");
        double maxHealth = (double)affected.getVariable("maxHealth");
        double arms = (double)affected.getVariable("damageArms");
        double legs = (double)affected.getVariable("damageLegs");

        switch (effectCode){
            case SACRAL_MAGIC_RESTORE_HEALTH://Укрепление здоровья
                curHealth += effectPower;
                legs -= effectPower*0.5;
                arms -= effectPower*0.5;
                if(curHealth > maxHealth) curHealth = maxHealth;
                if(legs < 0) legs = 0;
                if(arms < 0) arms = 0;
                affected.setVariable("health",curHealth);
                affected.setVariable("damageLegs",legs);
                affected.setVariable("damageArms",arms);
                break;
        }
    }

    @Override
    void removeEffect(Player affected, int effectCode, double effectPower) {
        switch (effectCode){

        }
    }
}
