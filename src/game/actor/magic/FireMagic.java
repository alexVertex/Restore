package game.actor.magic;

import game.actor.player.Player;

class FireMagic extends Magics{
    private static final int FIRE_MAGIC_FORTIFY = 0;
    private static final int FIRE_MAGIC_DAMAGE = 1;
    private static final int PIERCE_MAGIC_DAMAGE = 2;

    @Override
    boolean addEffect(int effectCode) {
        switch (effectCode){
            case FIRE_MAGIC_DAMAGE://Урон от огня
            case PIERCE_MAGIC_DAMAGE://Колющий урон
                return true;
        }
        return false;
    }

    @Override
    void workEffect(Player affected, Player lastHit, int effectCode, double effectPower, int effectTime, boolean periodic) {
        double curHealth = (double)affected.getVariable("maxHealth");
        switch (effectCode){
            case FIRE_MAGIC_FORTIFY://Укрепление здоровья
                curHealth += effectPower;
                affected.setVariable("maxHealth",curHealth);
                break;
            case FIRE_MAGIC_DAMAGE://Урон от огня
                MagicBase.magicDamage(affected,lastHit, effectPower,"armorFire",effectTime,periodic);
                break;
            case PIERCE_MAGIC_DAMAGE://Колющий урон
                MagicBase.magicDamage(affected,lastHit, effectPower,"armorPierce",effectTime,periodic);
                break;
        }
    }

    @Override
    void removeEffect(Player affected, int effectCode, double effectPower) {
        switch (effectCode){
            case FIRE_MAGIC_FORTIFY://Укрепление здоровья
                double curHealth = (double)affected.getVariable("maxHealth");
                curHealth -= effectPower;
                affected.setVariable("maxHealth",curHealth);
                break;
        }
    }
}
