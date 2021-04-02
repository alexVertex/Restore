package screens.camp;

import engine.Start;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.control.interfaces.Scroll;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.enviroment.Item;
import game.actor.player.Player;
import game.actor.actorsHyper.ItemDataBase;
import org.newdawn.slick.Color;
import screens.Controls;
import screens.ingame.GameScreen;

import java.util.ArrayList;
import java.util.List;

import static game.actor.player.Player.BARE_FIST_DISTANT;

public class camp_inventory extends GameScreen {
    private int choosenFilter = 0;
    private static final int ROW_LEN=4;
    private Item undercursor = null;
    private String[] itemsNames = {"weaponCur", "shieldCur", "weaponSpare", "shieldSpare", "armorHead", "armorArms", "armorTorso", "armorLegs", "uses1", "uses2", "uses3", "uses4", "uses5", "uses6", "uses7", "uses8"};
    private Scroll invScroll = new Scroll(Render2D.getWindowWidth()/2+282,Render2D.getWindowHeight()/2-32,32,448,"interface/scroller");

    @Override
    public void input(){
        Controls.playerChoos();
        Controls.campScreenChoose();
        Controls.closeCampScreen();
        undercursor = null;
        String undercursorPart = "";
        for(int i = 0; i < showList.size() && i <7*ROW_LEN;i++){
            Item el =  showList.get(i);
            int locX = (int)Render2D.getWindowWidth()/2 + (i % ROW_LEN)*64 + 40;
            int locY = (int)Render2D.getWindowHeight()/2+ (i / ROW_LEN)*64 - 95-128;
            if(Math.abs(locX-InputMain.getCursorX())<=32 && Math.abs(locY-InputMain.getCursorY())<=32){
                undercursor = el;
                break;
            }
        }
        for(int i = 0; i < itemsNames.length;i++){
            Item el =  (Item) StartGame.game.controlGroup.get(StartGame.game.getChoosenHero()).getVariable(itemsNames[i]);
            if(el != null) {
                int locX = (int) Render2D.getWindowWidth() / 2 + (i % 2) * 64 - 265;
                int locY = (int) Render2D.getWindowHeight() / 2 + (i / 2) * 64 - 95 - 128;
                if (Math.abs(locX - InputMain.getCursorX()) <= 32 && Math.abs(locY - InputMain.getCursorY()) <= 32) {
                    undercursor = el;
                    undercursorPart = itemsNames[i];
                }
            }
        }
        if(InputMain.isKeyJustReleased(InputMain.LMB)) {
            for (int i = 0; i < 11; i++) {
                double locX = Render2D.getWindowWidth() / 2 + 234 - 80 - 144 + 14 + 26 * i;
                double locY = Render2D.getWindowHeight() / 2 + 210;
                if (Math.abs(locX - InputMain.getCursorX()) <= 13 && Math.abs(locY - InputMain.getCursorY()) <= 16) {
                    choosenFilter = i;
                    AudioManager.playSoundInterface("interface/Ok");

                    break;
                }
            }
            if(undercursor != null) {
                if (InputMain.getCursorX() > Render2D.getWindowWidth() / 2 - 40) {
                    dress();
                    AudioManager.playSoundInterface("interface/CampInventoryDress");
                } else {
                    undres(undercursorPart);
                    AudioManager.playSoundInterface("interface/CampInventoryUndress");
                }
            }
        }
        invScroll.work();
    }

    private void undres(String partString) {
        if(StartGame.game.getControled().getVariable(partString) != null) {
            Item undressed = (Item) StartGame.game.getControled().getVariable(partString);
            double load = (double) StartGame.game.getControled().getVariable("equipLoad");
            load -= (int)undressed.getVariable("weight");
            StartGame.game.getControled().setVariable("equipLoad",load);
            StartGame.game.addItem(undressed);
            StartGame.game.getControled().setVariable(partString, null);
        }
        if(partString.contains("uses")){
            int part = Integer.parseInt(partString.charAt(4)+"")+7;
            for(int i = part; i < itemsNames.length-1;i++){
                StartGame.game.getControled().setVariable(itemsNames[i],StartGame.game.getControled().getVariable(itemsNames[i+1]));
            }
            StartGame.game.getControled().setVariable(itemsNames[itemsNames.length-1],null);
        }
    }

    private void dressItemUndercursor(String part){
        double load = (double) StartGame.game.getControled().getVariable("equipLoad");
        load += (int) undercursor.getVariable("weight");
        StartGame.game.getControled().setVariable("equipLoad", load);
        StartGame.game.controlGroup.get(StartGame.game.getChoosenHero()).setVariable(part, undercursor);
        StartGame.game.removeItem(undercursor);
    }

    private String[] weaponItemNames = {"weaponCur","weaponSpare"};
    private String[] shieldItemNames = {"shieldCur","shieldSpare"};
    private String[] armorItemNames = {"armorHead","armorTorso","armorArms","armorLegs"};
    private void dress() {
        String[] itemNamesList = (int)undercursor.getVariable("type") == ItemDataBase.TYPE_SHIELD ? shieldItemNames : weaponItemNames;
        switch ((int)undercursor.getVariable("type")){
            case ItemDataBase.TYPE_WEAPON:
            case ItemDataBase.TYPE_BOWS:
            case ItemDataBase.TYPE_SHIELD:
                for(String el : itemNamesList){
                    if(StartGame.game.getControled().getVariable(el) == null) {
                        dressItemUndercursor(el);
                        return;
                    }
                }
                break;
            case ItemDataBase.TYPE_ARMOR:
                int armorPartIndex = (int)undercursor.getVariable("armorClass");
                undres(armorItemNames[armorPartIndex]);
                dressItemUndercursor(armorItemNames[armorPartIndex]);
                break;
            case ItemDataBase.TYPE_USES:
                for(int i = 8; i < itemsNames.length;i++){
                    Item el =  (Item) StartGame.game.controlGroup.get(StartGame.game.getChoosenHero()).getVariable(itemsNames[i]);
                    if(el == null) {
                        dressItemUndercursor(itemsNames[i]);
                        return;
                    }
                }
                break;
        }
    }

    private List<Item> showList = new ArrayList<>();
    @Override
    public void logic(){
        List<Item> filterList = new ArrayList<>();
        if(choosenFilter == 0){
            filterList.addAll(StartGame.game.inventory);
        } else {
            for(Item el : StartGame.game.inventory){
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
            Item el = filterList.get(i);
            showList.add(el);
        }
    }

    private String[] params = {"Здоровье","Запас сил","Нагрузка","","Скорость атак","Атака: Сила","Атака: Ловкость","Атака: Навык","Опыт"};
    private List<String> getData(){
        List<String> answer = new ArrayList<>();
        Player player = StartGame.game.controlGroup.get(StartGame.game.getChoosenHero());
        String line = player.getVariableTrunked("health")+"/"+player.getVariableTrunked("maxHealth");
        answer.add(line);
        line = player.getVariableTrunked("stamina")+"/"+player.getVariableTrunked("maxStamina");
        answer.add(line);
        line = player.getVariableTrunked("equipLoad")+"/"+player.getVariableTrunked("maxEquipLoad");
        answer.add(line);
        line = Math.round(player.overload()*100)+"%";
        answer.add(line);
        line = Math.round(player.getVariableTrunked("attackSpeed",100))+"";
        answer.add(line);
        line = Math.round(player.getVariableTrunked("attackStrength",100))+"";
        answer.add(line);
        line = Math.round(player.getVariableTrunked("attackAgility",100))+"";
        answer.add(line);
        Item weaponCur = (Item)StartGame.game.getControled().getVariable("weaponCur");
        if(weaponCur == null){
            weaponCur = new Item(null,0,0,0,0,null,0);
            weaponCur.setVariable("weaponClass","skillHandToHand");
        }
        String weaponSkill = weaponCur.getVariable("weaponClass")+"";
        line = Math.round(100*(player.getVariableInteger(weaponSkill)*Player.DAMAGE_WEAPON_SKILL_MULT+1))+"";
        answer.add(line);
        line = player.getVariableInteger("exp")+"";
        answer.add(line);
        return answer;
    }

    private String[] paramsWeapon = {"Урон","Тип урона","Скорость атак","Дистанция"};
    private List<String> getDataWeapon(){
        List<String> answer = new ArrayList<>();
        Player player = StartGame.game.controlGroup.get(StartGame.game.getChoosenHero());
        Item weaponCur = (Item)StartGame.game.getControled().getVariable("weaponCur");
        if(weaponCur == null){
            weaponCur = new Item(null,0,0,0,0,null,0);
            weaponCur.setVariable("distant",BARE_FIST_DISTANT);
            weaponCur.setVariable("attackSpeed",Player.BARE_FIST_ATTACKSPEED);
            weaponCur.setVariable("damage",Player.BARE_FIST_DAMAGE);
            weaponCur.setVariable("damageType",Player.BARE_FIST_DAMAGE_TYPE);
            weaponCur.setVariable("damageTypeName","Дробящий");
            weaponCur.setVariable("weaponClass","skillHandToHand");
        }
        double damage = (double)weaponCur.getVariable("damage");
        String weaponSkill = weaponCur.getVariable("weaponClass")+"";
        int attribute;
        if(weaponSkill.equals("skillBlades") || weaponSkill.equals("skillAxes") || weaponSkill.equals("skillBlunts") || weaponSkill.equals("skillHandToHand")){
            attribute = StartGame.game.getControled().getVariableTrunked("attackStrength");
        } else {
            attribute = StartGame.game.getControled().getVariableTrunked("attackAgility");
        }
        double weaponSkillVal = StartGame.game.getControled().getVariableInteger(weaponSkill);
        damage *= attribute;
        damage *= 1+weaponSkillVal*Player.DAMAGE_WEAPON_SKILL_MULT;
        String line = (int)damage+"";
        answer.add(line);
        line = weaponCur.getVariable("damageTypeName")+"";
        answer.add(line);
        double attackProgressAdd = (double) player.getVariable("attackSpeed") + (double) weaponCur.getVariable("attackSpeed");
        attackProgressAdd*=100;
        line = (int)attackProgressAdd+"";
        answer.add(line);
        int distant = weaponCur.getVariableTrunked("distant")/32;
        line = distant+"";
        answer.add(line);
        return answer;
    }

    private String[] paramsArmor = {"","Колющ.","Рубящ.","Дроб.","Огонь","Мороз","Молн.","Кисл.","Магия","Негат.","Разум"};
    private String[] armorInfo(String armor,String head){
        Item headArmor = (Item)StartGame.game.getControled().getVariable(armor);
        if(headArmor == null){
            return new String[]{head, "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        }else {
            return new String[]{head, headArmor.getVariableInteger("armorPierce")+""
                    , headArmor.getVariableInteger("armorSlash")+""
                    , headArmor.getVariableInteger("armorStrike")+""
                    , headArmor.getVariableInteger("armorFire")+""
                    , headArmor.getVariableInteger("armorFreeze")+""
                    , headArmor.getVariableInteger("armorAcid")+""
                    , headArmor.getVariableInteger("armorElectro")+""
                    , headArmor.getVariableInteger("armorWizardry")+""
                    , headArmor.getVariableInteger("armorSacred")+""
                    , headArmor.getVariableInteger("armorMental")+""};
        }
    }

    @Override
    public void render(){
        super.render();
        double wide = 600;
        double high = 516;
        camp_Base.render(0);
        Render2D.angleDraw("campActive","interface/InvArmors", Render2D.getWindowWidth()/2-234,Render2D.getWindowHeight()/2,128,512,0);
        Render2D.angleDraw("campPassive","interface/InvItems", Render2D.getWindowWidth()/2+234-96,Render2D.getWindowHeight()/2,256,512,0);
        List<String> data = getData();
        Text.drawStringCenter("Параметры",Render2D.getWindowWidth()/2-234+150, (float) (Render2D.getWindowHeight()/2-high/2+30+-1.5*16),Text.CAMBRIA_14, Color.white);
        for(int i = 0; i < params.length;i++){
            Text.drawString(params[i],Render2D.getWindowWidth()/2-234+70, (float) (Render2D.getWindowHeight()/2-high/2+30+i*16),Text.CAMBRIA_14, Color.white);
            Text.drawStringRigth(data.get(i),Render2D.getWindowWidth()/2-234+235, (float) (Render2D.getWindowHeight()/2-high/2+30+i*16),Text.CAMBRIA_14, Color.white);
        }
        Text.drawStringCenter("Оружие",Render2D.getWindowWidth()/2-234+150, (float) (Render2D.getWindowHeight()/2-high/2+30+10*16),Text.CAMBRIA_14, Color.white);
        List<String> dataWeapon = getDataWeapon();
        for(int i = 0; i < paramsWeapon.length;i++){
            Text.drawString(paramsWeapon[i],Render2D.getWindowWidth()/2-234+70, (float) (Render2D.getWindowHeight()/2-high/2+216+i*16),Text.CAMBRIA_14, Color.white);
            Text.drawStringRigth(dataWeapon.get(i),Render2D.getWindowWidth()/2-234+235, (float) (Render2D.getWindowHeight()/2-high/2+216+i*16),Text.CAMBRIA_14, Color.white);
        }
        Text.drawStringCenter("Броня",Render2D.getWindowWidth()/2-234+150, (float) (Render2D.getWindowHeight()/2-high/2+30+16*16),Text.CAMBRIA_14, Color.white);
        String[] headInfo = armorInfo("armorHead","Гол.");
        String[] torsoInfo = armorInfo("armorTorso","Тел.");
        String[] armsInfo = armorInfo("armorArms","Рук.");
        String[] legsInfo = armorInfo("armorLegs","Ног.");
        for(int i = 0; i < paramsArmor.length;i++){
            Text.drawString(paramsArmor[i],Render2D.getWindowWidth()/2-234+70, (float) (Render2D.getWindowHeight()/2-high/2+216+96+i*16),Text.CAMBRIA_14, Color.white);
            Text.drawString(headInfo[i],Render2D.getWindowWidth()/2-234+70+55, (float) (Render2D.getWindowHeight()/2-high/2+216+96+i*16),Text.CAMBRIA_14, Color.white);
            Text.drawString(torsoInfo[i],Render2D.getWindowWidth()/2-234+70+85, (float) (Render2D.getWindowHeight()/2-high/2+216+96+i*16),Text.CAMBRIA_14, Color.white);
            Text.drawString(armsInfo[i],Render2D.getWindowWidth()/2-234+70+115, (float) (Render2D.getWindowHeight()/2-high/2+216+96+i*16),Text.CAMBRIA_14, Color.white);
            Text.drawString(legsInfo[i],Render2D.getWindowWidth()/2-234+70+145, (float) (Render2D.getWindowHeight()/2-high/2+216+96+i*16),Text.CAMBRIA_14, Color.white);
        }
        for(int i = 0; i < showList.size() && i <7*ROW_LEN;i++){
            Item el =  showList.get(i);
            int locX = (int)Render2D.getWindowWidth()/2 + (i % ROW_LEN)*64 + 40;
            int locY = (int)Render2D.getWindowHeight()/2+ (i / ROW_LEN)*64 - 95-128;
            drawItem("inventoryItem"+i, el, locX, locY);
        }
        for(int i = 0; i < itemsNames.length;i++){
            Item el =  (Item) StartGame.game.controlGroup.get(StartGame.game.getChoosenHero()).getVariable(itemsNames[i]);
            if(el != null){
                int locX = (int)Render2D.getWindowWidth()/2 + (i % 2)*64 - 265;
                int locY = (int)Render2D.getWindowHeight()/2+ (i / 2)*64 - 95-128;
                drawItem("dressedItem"+i, el, locX, locY);
            }
        }
        invScroll.draw();
        Render2D.simpleDraw("filters","interface/invFilters",Render2D.getWindowWidth()/2+234-80,Render2D.getWindowHeight()/2+210,288,32);
        Render2D.simpleDraw("filterChoosen","interface/invFiltersChosen",Render2D.getWindowWidth()/2+234-80-144+14+26*choosenFilter,Render2D.getWindowHeight()/2+210,32,32);
        if(undercursor != null){
            undercursor.drawInfo(false);
        }
        Text.drawString("Золото: " + StartGame.game.gold,Render2D.getWindowWidth()/2+234-80-144,Render2D.getWindowHeight()/2+230,Text.CAMBRIA_14,Color.white);
        Render2D.simpleDraw("cursor","cursors/normal",InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
    }

    private void drawItem(String nameOfSprite, Item el, int locX, int locY){
        double[] part = el.getTextureLocation();
        String texture = el.getTexture();
        double cutLeft = part[0]/ Item.ITEMS_IN_LINE;
        double cutRight = 1-(part[0]+1)/Item.ITEMS_IN_LINE;
        double cutUp = part[1]/Item.ITEMS_IN_LINE;
        double cutDown = 1-(part[1]+1)/Item.ITEMS_IN_LINE;
        Render2D.angleCutDraw(nameOfSprite,texture,locX,locY,48,48,0,cutLeft,cutRight,cutUp,cutDown);
    }
}
