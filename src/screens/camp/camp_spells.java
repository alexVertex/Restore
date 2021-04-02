package screens.camp;

import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.control.interfaces.Scroll;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.enviroment.Item;
import game.actor.player.Player;
import game.actor.magic.Spell;
import game.actor.player.PlayerCastMagic;
import org.newdawn.slick.Color;
import screens.Controls;
import screens.ingame.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class camp_spells extends GameScreen {
    private int choosenFilter = 0;
    private static final int ROW_LEN=4;
    private  Spell undercursor = null;
    private String[] itemsNames = {"spells1",
            "spells2",
            "spells3",
            "spells4",
            "spells5",
            "spells6",
            "spells7",
            "spells8",
    };

    private  Scroll invScroll = new Scroll(Render2D.getWindowWidth()/2+282,Render2D.getWindowHeight()/2-32,32,448,"interface/scroller");
    @Override
    public void input() {
        Controls.playerChoos();
        Controls.campScreenChoose();
        Controls.closeCampScreen();
        undercursor = null;
        int undercursorPart = -1;
        for (int i = 0; i < showList.size() && i < 7 * ROW_LEN; i++) {
            Spell el = showList.get(i);
            double locX = Render2D.getWindowWidth() / 2 + (i % ROW_LEN) * 64 + 40;
            double locY = (int) Render2D.getWindowHeight() / 2 + (i / ROW_LEN) * 64 - 95 - 128;
            if (Math.abs(locX - InputMain.getCursorX()) <= 32 && Math.abs(locY - InputMain.getCursorY()) <= 32) {
                undercursor = el;
            }
        }
        for (int i = 0; i < itemsNames.length; i++) {
            Spell el = (Spell) StartGame.game.controlGroup.get(StartGame.game.getChoosenHero()).getVariable(itemsNames[i]);
            if (el == null) {
                break;
            }
            double locX = Render2D.getWindowWidth() / 2 - 265;
            double locY = Render2D.getWindowHeight() / 2 + (i) * 64 - 95 - 128;
            if (Math.abs(locX - InputMain.getCursorX()) <= 32 && Math.abs(locY - InputMain.getCursorY()) <= 32) {
                undercursor = el;
                undercursorPart = i;
            }
        }
        if (InputMain.isKeyJustReleased(InputMain.LMB)) {
            for (int i = 0; i < 9; i++) {
                double locX = Render2D.getWindowWidth() / 2 + 234 - 80 - 144 + 18 + 36 * i;
                double locY = Render2D.getWindowHeight() / 2 + 210;
                if (Math.abs(locX - InputMain.getCursorX()) <= 18 && Math.abs(locY - InputMain.getCursorY()) <= 16) {
                    choosenFilter = i;
                    AudioManager.playSoundInterface("interface/Ok");
                    break;
                }
            }
            if (undercursor != null) {
                if (InputMain.getCursorX() > Render2D.getWindowWidth() / 2 - 40) {
                    dress();

                } else {
                    undres(undercursorPart);
                    AudioManager.playSoundInterface("interface/CampInventoryUndress");

                }
            }
        }
        invScroll.work();
    }

    private int SPELL_WEIGHT = 1;
    private void undres(int part) {
        if(StartGame.game.getControled().getVariable(itemsNames[part]) != null) {
            Spell undressed = (Spell) StartGame.game.getControled().getVariable(itemsNames[part]);
            double load = (double) StartGame.game.getControled().getVariable("equipLoad");
            load -= SPELL_WEIGHT;
            StartGame.game.getControled().setVariable("equipLoad",load);
            StartGame.game.addSpell(undressed);
            StartGame.game.getControled().setVariable(itemsNames[part], null);
        }
        for(int i = part; i < itemsNames.length-1;i++){
            StartGame.game.getControled().setVariable(itemsNames[i],StartGame.game.getControled().getVariable(itemsNames[i+1]));
        }
        StartGame.game.getControled().setVariable(itemsNames[itemsNames.length-1],null);
    }

    private void dress() {
        for (String itemsName : itemsNames) {
            Spell el = (Spell) StartGame.game.controlGroup.get(StartGame.game.getChoosenHero()).getVariable(itemsName);
            if (el == null) {
                if (!allowed(undercursor)) {
                    AudioManager.playSoundInterface("interface/Cancel");
                    return;
                }
                AudioManager.playSoundInterface("interface/CampInventoryDress");
                double load = (double) StartGame.game.getControled().getVariable("equipLoad");
                load += SPELL_WEIGHT;
                StartGame.game.getControled().setVariable("equipLoad", load);
                StartGame.game.getControled().setVariable(itemsName, undercursor);
                StartGame.game.removeSpell(undercursor);
                break;
            }
        }
    }

    private boolean allowed(Spell spell){
        int diff = spell.getVariableInteger("difficult");
        int mana = spell.getVariableInteger("mana");
        mana = PlayerCastMagic.manaCostThruMagic(mana,StartGame.game.getControled());
        int knowledge = StartGame.game.getControled().getVariableInteger(spell.getVariable("spellClass")+"");
        int staminaMax = StartGame.game.getControled().getVariableTrunked("maxStamina");
        return diff <= knowledge && staminaMax >= mana;
    }

    private List<Spell> showList = new ArrayList<>();
    @Override
    public void logic(){
        List<Spell> filterList = new ArrayList<>();
        if(choosenFilter == 0){
            filterList.addAll(StartGame.game.inventorySpells);
        } else {
            for(Spell el : StartGame.game.inventorySpells){
                if(el.getVariable("filter").equals(choosenFilter)){
                    filterList.add(el);
                }
            }
        }
        int lines = (int)Math.ceil((double) filterList.size()/ROW_LEN);
        lines-=7;
        if(lines<0) lines = 0;
        invScroll.setMaxPosition(lines);
        showList.clear();
        for(int i = invScroll.getPosition()*ROW_LEN; i < filterList.size();i++){
            Spell el = filterList.get(i);
            showList.add(el);
        }
    }

    private String[] params = {"Здоровье","Запас сил","Нагрузка","","Познания в видах магии","   магия огня","   магия воды","   магия земли","   магия воздуха","   псионика","   волшебство","   чары"};
    private List<String> getData(){
        List<String> answer = new ArrayList<>();
        Player player = StartGame.game.controlGroup.get(StartGame.game.getChoosenHero());
        String line = player.getVariableTrunked("health")+"/"+player.getVariableTrunked("maxHealth");
        answer.add(line);
        line = player.getVariableTrunked("stamina")+"/"+player.getVariableTrunked("maxStamina");
        answer.add(line);
        line = player.getVariableTrunked("equipLoad")+"/"+player.getVariableTrunked("maxEquipLoad");
        answer.add(line);
        line = "";
        answer.add(line);
        answer.add(line);
        line = player.getVariableInteger("magicFire")+"";
        answer.add(line);
        line = player.getVariableInteger("magicWater")+"";
        answer.add(line);
        line = player.getVariableInteger("magicEarth")+"";
        answer.add(line);
        line = player.getVariableInteger("magicAir")+"";
        answer.add(line);
        line = player.getVariableInteger("magicMind")+"";
        answer.add(line);
        line = player.getVariableInteger("magicWizardry")+"";
        answer.add(line);
        line = player.getVariableInteger("magicSacred")+"";
        answer.add(line);
        return answer;
    }

    @Override
    public void render(){
        super.render();
        double wide = 600;
        double high = 516;
        camp_Base.render(2);
        Render2D.angleDraw("campActive","interface/InvSpells", Render2D.getWindowWidth()/2-234-32,Render2D.getWindowHeight()/2,64,512,0);
        Render2D.angleDraw("campPassive","interface/InvItems", Render2D.getWindowWidth()/2+234-96,Render2D.getWindowHeight()/2,256,512,0);
        List<String> data = getData();
        Text.drawStringCenter("Параметры",Render2D.getWindowWidth()/2-234+150, (float) (Render2D.getWindowHeight()/2-high/2+30+-1.5*16),Text.CAMBRIA_14, Color.white);
        for(int i = 0; i < params.length;i++){
            Text.drawString(params[i],Render2D.getWindowWidth()/2-234+70, (float) (Render2D.getWindowHeight()/2-high/2+30+i*16),Text.CAMBRIA_14, Color.white);
            Text.drawStringRigth(data.get(i),Render2D.getWindowWidth()/2-234+235, (float) (Render2D.getWindowHeight()/2-high/2+30+i*16),Text.CAMBRIA_14, Color.white);
        }
        for(int i = 0; i < showList.size() && i <7*ROW_LEN;i++){
            Spell el =  showList.get(i);
            double[] part = el.getTextureLocation();
            String texture = el.getTexture();
            double locX = Render2D.getWindowWidth()/2 + (i % ROW_LEN)*64 + 42;
            double locY = (int)Render2D.getWindowHeight()/2+ (i / ROW_LEN)*64 - 95-129;
            double cutLeft = part[0]/ Item.ITEMS_IN_LINE;
            double cutRight = 1-(part[0]+1)/Item.ITEMS_IN_LINE;
            double cutUp = part[1]/Item.ITEMS_IN_LINE;
            double cutDown = 1-(part[1]+1)/Item.ITEMS_IN_LINE;
            Render2D.angleCutDraw("inventoryItem"+i, texture,locX,locY,48,48,0,cutLeft,cutRight,cutUp,cutDown);
        }
        for(int i = 0; i < itemsNames.length;i++){
            Spell el =  (Spell) StartGame.game.controlGroup.get(StartGame.game.getChoosenHero()).getVariable(itemsNames[i]);
            if(el == null){
                break;
            }
            double[] part = el.getTextureLocation();
            String texture = el.getTexture();
            double locX = Render2D.getWindowWidth()/2 - 266;
            double locY = Render2D.getWindowHeight()/2+ i*64 - 95-129;
            double cutLeft = part[0]/ Item.ITEMS_IN_LINE;
            double cutRight = 1-(part[0]+1)/Item.ITEMS_IN_LINE;
            double cutUp = part[1]/Item.ITEMS_IN_LINE;
            double cutDown = 1-(part[1]+1)/Item.ITEMS_IN_LINE;
            Render2D.angleCutDraw("dressedItem"+i,texture,locX,locY,48,48,0,cutLeft,cutRight,cutUp,cutDown);
        }
        invScroll.draw();
        Render2D.simpleDraw("filters","interface/splFilters",Render2D.getWindowWidth()/2+234-80,Render2D.getWindowHeight()/2+210,288,32);
        Render2D.simpleDraw("filterChoosen","interface/splFiltersChosen",Render2D.getWindowWidth()/2+234-80-144+18+36*choosenFilter,Render2D.getWindowHeight()/2+210,36,32);
        if(undercursor != null){
            undercursor.drawInfo(false);
        }
        Render2D.simpleDraw("cursor","cursors/normal",InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
    }
}
