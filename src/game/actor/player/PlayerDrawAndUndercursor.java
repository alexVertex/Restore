package game.actor.player;

import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.enviroment.Item;
import game.actor.magic.MagicEffect;
import game.actor.magic.Spell;
import org.newdawn.slick.Color;

import java.util.List;

import static game.actor.game.Tile.SIZE;
import static game.actor.actorsHyper.ItemDataBase.TYPE_BOWS;
import static screens.ingame.gameScreenInterface.activeActor;

public class PlayerDrawAndUndercursor {
    private static final String PORTRET_PATH = "interface/portrets/";

    public static void changeWeaponShield(Player player, String name){
        if(player.getVariable("projectile") == null) {
            Object save = player.getVariable(name + "Cur");
            player.setVariable(name + "Cur", player.getVariable(name + "Spare"));
            player.setVariable(name + "Spare", save);
            AudioManager.playSoundInterface("interface/ChangeWeapon");
        }

    }

    public static void controlInterfaceDraw(Player player){
        int getSpeedGear = StartGame.game.getControled().getVariableInteger("speedGear");
        Render2D.angleDraw("speedGear","interface/comandsChooser",0, Render2D.getWindowHeight(),128,128,(getSpeedGear-1)*29);
        Item weaponCur = (Item)player.getVariable("weaponCur");
        if(weaponCur != null){
            weaponCur.draw(80,Render2D.getWindowHeight()-50,32);
            if(weaponCur.getVariable("type").equals(TYPE_BOWS)){
                int fires = weaponCur.getVariableInteger("fires");
                Text.drawString(fires+"",80,Render2D.getWindowHeight()-50,Text.CAMBRIA_14, Color.white);
            }
        }
        Item weaponSpare = (Item)player.getVariable("weaponSpare");
        if(weaponSpare != null){
            weaponSpare.draw(80,Render2D.getWindowHeight()-50+32,32);
            if(weaponSpare.getVariable("type").equals(TYPE_BOWS)){
                int fires = weaponSpare.getVariableInteger("fires");
                Text.drawString(fires+"",80,Render2D.getWindowHeight()-50+32,Text.CAMBRIA_14, Color.white);
            }
        }
        Item shiedCur = (Item)player.getVariable("shieldCur");
        if(shiedCur != null){
            shiedCur.draw(80+32,Render2D.getWindowHeight()-50,32);
        }
        Item shiedSpare = (Item)player.getVariable("shieldSpare");
        if(shiedSpare != null){
            shiedSpare.draw(80+32,Render2D.getWindowHeight()-50+32,32);
        }
        int rotate = player.getVariableInteger("battleMode");
        Render2D.angleDraw("battleMode","interface/battleMode",0,Render2D.getWindowHeight(),64,64,rotate*90);
        drawSpells();
    }

    public static String weaponShieldUnderCursor(Player player){
        Item weaponCur = (Item)player.getVariable("weaponCur");
        if(weaponCur != null && Math.abs(80- InputMain.getCursorX())<16 && Math.abs((Render2D.getWindowHeight()-50)- InputMain.getCursorY())<16){
            return "weaponCur";
        }
        Item weaponSpare = (Item)player.getVariable("weaponSpare");
        if(weaponSpare != null && Math.abs(80- InputMain.getCursorX())<16 && Math.abs((Render2D.getWindowHeight()-50+32)- InputMain.getCursorY())<16){
            return "weaponSpare";
        }
        Item shiedCur = (Item)player.getVariable("shieldCur");
        if(shiedCur != null && Math.abs(80+32- InputMain.getCursorX())<16 && Math.abs((Render2D.getWindowHeight()-50)- InputMain.getCursorY())<16){
            return "shieldCur";
        }
        Item shiedSpare = (Item)player.getVariable("shieldSpare");
        if(shiedSpare != null&& Math.abs(80+32- InputMain.getCursorX())<16 && Math.abs((Render2D.getWindowHeight()-50+32)- InputMain.getCursorY())<16){
            return "shieldSpare";
        }
        return undercursorSpell(player);
    }

    private static String[] itemsNames = {"spells1",
            "spells2",
            "spells3",
            "spells4",
            "spells5",
            "spells6",
            "spells7",
            "spells8",
    };
    private static String[] beltNames = {"uses1",
            "uses2",
            "uses3",
            "uses4",
            "uses5",
            "uses6",
            "uses7",
            "uses8",
    };

    private static void drawSpells(){
        for(int i = 0; i < itemsNames.length;i++){
            Spell el =  (Spell) StartGame.game.controlGroup.get(StartGame.game.getChoosenHero()).getVariable(itemsNames[i]);
            if(el == null){
                break;
            }
            if(activeActor == el){
                el.draw(80+64+40*i,Render2D.getWindowHeight()-20,40);
            } else {
                el.draw(80+64+40*i,Render2D.getWindowHeight()-20,32);
            }
        }
        for(int i = 0; i < beltNames.length;i++){
            Item el =  (Item) StartGame.game.controlGroup.get(StartGame.game.getChoosenHero()).getVariable(beltNames[i]);
            if(el == null){
                break;
            }
            if(activeActor == el){
                el.draw(80+64+40*i,Render2D.getWindowHeight()-60,40);
            } else {
                el.draw(80+64+40*i,Render2D.getWindowHeight()-60,32);
            }
        }
    }

    public static String undercursorSpell(Player player){
        for(int i = 0; i < itemsNames.length;i++){
            Spell el =  (Spell) player.getVariable(itemsNames[i]);
            if(el == null){
                break;
            }
            if(Math.abs(80+64+40*i- InputMain.getCursorX())<20 && Math.abs((Render2D.getWindowHeight()-20)- InputMain.getCursorY())<20){
                return itemsNames[i];
            }
        }
        for(int i = 0; i < beltNames.length;i++){
            Item el =  (Item) player.getVariable(beltNames[i]);
            if(el == null){
                break;
            }
            if(Math.abs(80+64+40*i- InputMain.getCursorX())<20 && Math.abs((Render2D.getWindowHeight()-60)- InputMain.getCursorY())<20){
                return beltNames[i];
            }
        }
        return null;
    }

    public static void drawPortret(Player player, int count, boolean choosen){
        if(choosen) {
            String borderTexture = "interface/Border";
            Render2D.angleDraw("chosenPortretBorder1",borderTexture, 58, 2 + count * 96, 108, 4, 0);
            Render2D.angleDraw("chosenPortretBorder2",borderTexture, 58, 98 + count * 96, 108, 4, 0);
            Render2D.angleDraw("chosenPortretBorder3",borderTexture, 2, 50 + count * 96, 100, 4, 90);
            Render2D.angleDraw("chosenPortretBorder4",borderTexture, 114, 50 + count * 96, 100, 4, 90);
        }
        double size = 92;
        double[] part = new double[]{(int)player.getVariable("portretX"),(int)player.getVariable("portretY")};
        double portretX = size/2+4;
        double portretY = size/2+count*96+4;
        double cutLeft = part[0]/PORTRETS_IN_LINE;
        double cutRight = 1-(part[0]+1)/PORTRETS_IN_LINE;
        double cutUp = part[1]/PORTRETS_IN_LINE;
        double cutDown = 1-(part[1]+1)/PORTRETS_IN_LINE;
        String texture = PORTRET_PATH+player.getVariable("portret");
        Render2D.angleCutDraw("portret"+count,texture,portretX,portretY,size,size,0,cutLeft,cutRight,cutUp,cutDown);
        double percent = (Double) player.getVariable("health")/(Double)player.getVariable("maxHealth");
        int height = (int) (percent*92);
        int offset = (int) ((92-height)/2);
        Render2D.simpleDraw("life"+count,"interface/GreenBar",100,portretY+offset,8,height);
        percent = (Double) player.getVariable("stamina")/(Double)player.getVariable("maxStamina");
        height = (int) (percent*92);
        offset = (int) ((92-height)/2);
        Render2D.simpleDraw("stamina"+count,"interface/BlueBar",108,portretY+offset,8,height);
        double armsDamage = player.damageArmsPercent();
        int i = 0;
        for(MagicEffect el : (List<MagicEffect>)player.getVariable("effects")){
            el.draw(108+30+i*32,portretY-32,30);
            i++;
        }
        if(armsDamage > 0.9){
            Render2D.angleColorDraw("arms"+count,"interface/attackArms",108+20,portretY,32,32,0,1,0,0,1);
        } else if (armsDamage > 0.65){
            Render2D.angleColorDraw("arms"+count,"interface/attackArms",108+20,portretY,32,32,0,1,0.5,0,1);
        }else if (armsDamage > 0.4){
            Render2D.angleColorDraw("arms"+count,"interface/attackArms",108+20,portretY,32,32,0,1,1,0,1);
        }
        double legsDamage = player.damageLegsPercent();
        if(legsDamage > 0.9){
            Render2D.angleColorDraw("legs"+count,"interface/attackLegs",108+20,portretY+32,32,32,0,1,0,0,1);
        } else if (legsDamage > 0.65){
            Render2D.angleColorDraw("legs"+count,"interface/attackLegs",108+20,portretY+32,32,32,0,1,0.5,0,1);
        }else if (legsDamage > 0.4){
            Render2D.angleColorDraw("legs"+count,"interface/attackLegs",108+20,portretY+32,32,32,0,1,1,0,1);
        }
        //if(player.damageArmsPercent() > )
    }

    public static void drawPortret(Player player, double x, double y){
        double size = 92;

        String borderTexture = "interface/Border";
        Render2D.angleDraw("chosenPortretBorder1",borderTexture, x, y-size/2-4, 108, 4, 0);
        Render2D.angleDraw("chosenPortretBorder2",borderTexture, x, y+size/2+4, 108, 4, 0);
        Render2D.angleDraw("chosenPortretBorder3",borderTexture, x-size/2-8, y, 100, 4, 90);
        Render2D.angleDraw("chosenPortretBorder4",borderTexture, x+size/2+8, y, 100, 4, 90);

        double[] part = new double[]{(int)player.getVariable("portretX"),(int)player.getVariable("portretY")};
        double cutLeft = part[0]/PORTRETS_IN_LINE;
        double cutRight = 1-(part[0]+1)/PORTRETS_IN_LINE;
        double cutUp = part[1]/PORTRETS_IN_LINE;
        double cutDown = 1-(part[1]+1)/PORTRETS_IN_LINE;
        String texture = PORTRET_PATH+player.getVariable("portret");
        Render2D.angleCutDraw("portretDialog",texture, (double) x, (double) y,size,size,0,cutLeft,cutRight,cutUp,cutDown);


    }

    private static final double PORTRETS_IN_LINE = 4;
    public static void drawInterface(Player player){
        if(!player.isVisible()) return;
        double percent = (Double) player.getVariable("health")/(Double)player.getVariable("maxHealth");
        double wide = percent*SIZE;
        if(wide<1 && wide != 0)wide =1;
        double offset = (SIZE-wide)/2;
        double locX = player.getTileLoc()[0] - offset+ Camera.locX;
        double locY = player.getTileLoc()[1] + SIZE/2+4+Camera.locY;
        if(player instanceof NPC && player.getVariable("good").equals(0)){
            String texture = "interface/RedBar";
            Render2D.angleDraw("interface",texture,locX,locY,8,wide,90);
        }
    }
}
