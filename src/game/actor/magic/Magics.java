package game.actor.magic;

import game.actor.player.Player;

public abstract class Magics {
    abstract boolean addEffect(int effectCode);
    abstract void workEffect(Player affected, Player lastHit, int effectCode, double effectPower, int effectTime, boolean periodic);
    abstract void removeEffect(Player affected, int effectCode, double effectPower);
}
