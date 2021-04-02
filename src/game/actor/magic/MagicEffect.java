package game.actor.magic;

import engine.Actor;
import engine.render.Render2D;
import game.actor.player.Player;

import static game.actor.enviroment.Item.ITEMS_IN_LINE;

public class MagicEffect extends Actor {

    public MagicEffect(int code, double effect, int time,int school,String texture, int cutX, int cutY){
        setVariable("code",code);
        setVariable("effect",effect);
        setVariable("time",time);
        setVariable("spellClass",school);
        setVariable("texture",texture);
        setVariable("cutX",cutX);
        setVariable("cutY",cutY);

    }

    @Override
    public int logic() {
        return 0;
    }

    @Override
    public void draw() {

    }

    @Override
    public void draw(double x, double y, double s) {
        int timeLeft = getVariableInteger("time");
        double alpha = 1;
        if(timeLeft < 50){
            alpha = timeLeft/50.0;
        }
        double[] part = new double[]{(int)getVariable("cutX"),(int)getVariable("cutY")};
        String texture = getVariableString("texture");
        double cutLeft = part[0]/ITEMS_IN_LINE;
        double cutRight = 1-(part[0]+1)/ITEMS_IN_LINE;
        double cutUp = part[1]/ITEMS_IN_LINE;
        double cutDown = 1-(part[1]+1)/ITEMS_IN_LINE;
        Render2D.angleCutColorDraw(texture,x,y,s,s,0,cutLeft,cutRight,cutUp,cutDown,1,1,1,alpha);
    }

    @Override
    public void drawInfo(boolean b) {

    }

    @Override
    public boolean isUndercursor(int cursorX, int cursorY) {
        return false;
    }

    public int work(Player player) {
        int code = getVariableInteger("code");
        double effect = getVariableDouble("effect");
        int time = getVariableInteger("time");
        int school = getVariableInteger("spellClass");

        if(time > 0) {
            time--;
            if(MagicBase.isEffectPeriodic(code,school)){
                MagicBase.workEffect(player,code,effect,time,true,school);
            }
        }
        if(time == 0){
            MagicBase.removeEffect(player,code,effect,school);
            return 1;
        }
        setVariable("time",time);
        return 0;
    }
}
