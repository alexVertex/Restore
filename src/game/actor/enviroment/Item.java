package game.actor.enviroment;

import engine.Actor;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.actor.magic.Spell;
import game.actor.actorsHyper.ItemDataBase;
import game.actor.actorsHyper.SpellDataBase;
import org.newdawn.slick.Color;

import java.util.HashMap;

public class Item extends Actor {
    private static double SIZE = 32;
    public static double ITEMS_IN_LINE = 16;
    private static String ITEMS_FOLDER = "items/";

    public Item(String texture, int partX, int partY, int locX, int locY, String ID, int count) {
        setVariable("partX", partX);
        setVariable("partY", partY);
        setVariable("texture", texture);
        setVariable("locX", locX);
        setVariable("locY", locY);
        setVariable("ID", ID);
        setVariable("count", count);

        if (ID == null) {

        } else {
            HashMap<String, Object> itemVariables = ItemDataBase.getData(ID);

            for (String el : itemVariables.keySet()) {
                setVariable(el, itemVariables.get(el));
            }
        }
    }

    @Override
    public int logic() {

        return 0;
    }

    public String getTexture() {
        return ITEMS_FOLDER + getVariable("texture");
    }

    public double[] getTextureLocation() {
        return new double[]{(int) getVariable("partX"), (int) getVariable("partY")};
    }

    public double[] getTileLoc() {
        return new double[]{(int) getVariable("locX"), (int) getVariable("locY")};
    }

    public boolean isUndercursor(int curX, int curY) {
        double[] location = getTileLoc();
        if (Math.abs(curX - Camera.locX - location[0]) <= SIZE / 2 && Math.abs(curY - Camera.locY - location[1]) <= SIZE / 2) {
            return true;
        }
        return false;
    }

    @Override
    public void draw() {
        double[] location = getTileLoc();
        double[] part = getTextureLocation();
        String texture = getTexture();
        double locX = location[0] + Camera.locX;
        double locY = location[1] + Camera.locY;
        double cutLeft = part[0] / ITEMS_IN_LINE;
        double cutRight = 1 - (part[0] + 1) / ITEMS_IN_LINE;
        double cutUp = part[1] / ITEMS_IN_LINE;
        double cutDown = 1 - (part[1] + 1) / ITEMS_IN_LINE;
        Render2D.angleCutDraw("item", texture, locX, locY, SIZE, SIZE, 0, cutLeft, cutRight, cutUp, cutDown);
    }

    public void draw(double lx, double ly, double s) {
        double[] part = getTextureLocation();
        String texture = getTexture();
        double cutLeft = part[0] / ITEMS_IN_LINE;
        double cutRight = 1 - (part[0] + 1) / ITEMS_IN_LINE;
        double cutUp = part[1] / ITEMS_IN_LINE;
        double cutDown = 1 - (part[1] + 1) / ITEMS_IN_LINE;
        Render2D.angleCutDraw("item" + lx + "" + ly + "" + s, texture, lx, ly, s, s, 0, cutLeft, cutRight, cutUp, cutDown);
    }

    public String[][] info() {
        String[][] info = null;
        if (this.getVariable("type").equals(ItemDataBase.TYPE_WEAPON)) {
            info = new String[][]{
                    {"Название:", getVariable("name") + ""},
                    {"Класс:", getVariable("typeName") + ""},
                    {"Тип оружия:", getVariable("weaponClassName") + ""},
                    {"Урон:", getVariableTrunked("damage") + ""},
                    {"Тип урона:", getVariable("damageTypeName") + ""},
                    {"Дистанция:", getVariableTrunked("distant") / 32 + ""},
                    {"Скорость атак:", getVariableTrunked("attackSpeed", 100) + ""},
                    {"Вес:", getVariable("weight") + ""},
                    {"Цена:", getVariable("cost") + ""}


            };
        }
        if (this.getVariable("type").equals(ItemDataBase.TYPE_BOWS)) {
            info = new String[][]{
                    {"Название:", getVariable("name") + ""},
                    {"Класс:", getVariable("typeName") + ""},
                    {"Тип оружия:", getVariable("weaponClassName") + ""},
                    {"Урон:", getVariableTrunked("damage") + ""},
                    {"Тип урона:", getVariable("damageTypeName") + ""},
                    {"Дистанция:", getVariableTrunked("distant") / 32 + ""},
                    {"Скорость атак:", getVariableTrunked("attackSpeed", 100) + ""},
                    {"Боеприпас:", getVariableInteger("fires") + ""},

                    {"Вес:", getVariable("weight") + ""},
                    {"Цена:", getVariable("cost") + ""}


            };
        }
        if (this.getVariable("type").equals(ItemDataBase.TYPE_ARMOR)) {
            info = new String[][]{
                    {"Название:", getVariable("name") + ""},
                    {"Класс:", getVariable("typeName") + ""},
                    {"Часть тела:", getVariable("armorClassName") + ""},
                    {"Защита от колющ.:", getVariable("armorPierce") + ""},
                    {"Защита от рубящ.:", getVariable("armorSlash") + ""},
                    {"Защита от дробящ.:", getVariable("armorStrike") + ""},
                    {"Защита от огня:", getVariable("armorFire") + ""},
                    {"Защита от мороза:", getVariable("armorFreeze") + ""},
                    {"Защита от кислот:", getVariable("armorAcid") + ""},
                    {"Защита от молнии:", getVariable("armorElectro") + ""},
                    {"Защита от магии:", getVariable("armorWizardry") + ""},
                    {"Защита от негатива:", getVariable("armorSacred") + ""},
                    {"Защита разума:", getVariable("armorMental") + ""},

                    {"Вес:", getVariable("weight") + ""},
                    {"Цена:", getVariable("cost") + ""}

            };
        }
        if (this.getVariable("type").equals(ItemDataBase.TYPE_SHIELD)) {
            info = new String[][]{
                    {"Название:", getVariable("name") + ""},
                    {"Класс:", getVariable("typeName") + ""},
                    {"Тип щита:", getVariable("armorClassName") + ""},
                    {"Защита от колющ.:", getVariable("armorPierce") + ""},
                    {"Защита от рубящ.:", getVariable("armorSlash") + ""},
                    {"Защита от дробящ.:", getVariable("armorStrike") + ""},
                    {"Стабильность:", getVariableTrunked("stability", 100) + ""},
                    {"Вес:", getVariable("weight") + ""},
                    {"Цена:", getVariable("cost") + ""}


            };
        }
        if (this.getVariable("type").equals(ItemDataBase.TYPE_USES)) {
            HashMap<String, Object> in = SpellDataBase.getData(getVariable("spellIn") + "");
            int time = (int) in.get("time") / 100;
            String timeText = time != 0 ? Math.abs(time) + "" : "мгновенно";
            int radius = (int) in.get("projectileExplodeRadius") / 32;
            String radiusText = radius > 0 ? radius + "" : "-";
            int distant = (int) ((double) in.get("distant") / 32);
            String distantText = distant > 0 ? distant + "" : "-";
            info = new String[][]{
                    {"Название:", getVariable("name") + ""},
                    {"Класс:", getVariable("usesClassName") + ""},

                    {"Эффект:", (int) in.get("power") + ""},
                    {"Время:", timeText},
                    {"Радиус:", radiusText},
                    {"Дальность:", distantText},
                    {"Тип цели:", Spell.types[(int) in.get("targetType")] + ""},
                    {"Вес:", getVariable("weight") + ""},
                    {"Цена:", getVariable("cost") + ""}


            };
        }
        if (this.getVariable("type").equals(ItemDataBase.TYPE_KEYS)) {
            info = new String[][]{
                    {"Название:", getVariable("name") + ""},
                    {"Класс:", getVariable("typeName") + ""},
            };
        }
        if (this.getVariable("type").equals(ItemDataBase.TYPE_TROPHY)) {
            info = new String[][]{
                    {"Название:", getVariable("name") + ""},
                    {"Класс:", getVariable("typeName") + ""},
                    {"Цена:", getVariable("cost") + ""},

            };
        }
        if (this.getVariable("type").equals(ItemDataBase.TYPE_BLUEPRINTS)) {
            String[] materialsNames = {"Камень", "Кожа", "Кость", "Мех", "Шкура", "Металл", "Ткань"};
            info = new String[][]{
                    {"Название:", getVariable("name") + ""},
                    {"Класс:", getVariable("typeName") + ""},
                    {"Материал:", materialsNames[getVariableInteger("materialClass")] + ""},
                    {"Количество:", getVariable("materialCount") + ""},
                    {"Цена:", getVariable("cost") + ""},

            };
        }
        if (this.getVariable("type").equals(ItemDataBase.TYPE_MATERIALS)) {
            String[] materialsNames = {"Камень", "Кожа", "Кость", "Мех", "Шкура", "Металл", "Ткань"};
            info = new String[][]{
                    {"Название:", getVariable("name") + ""},
                    {"Класс:", getVariable("typeName") + ""},
                    {"Материал:", materialsNames[getVariableInteger("materialClass")] + ""},
                    {"Цена:", getVariable("cost") + ""},

            };
        }
        if (this.getVariable("type").equals(ItemDataBase.TYPE_RUNE)) {
            String[] paremetresNames = {"Эффект", "Время", "Запас сил", "Дистанция", "Цели", "Скорость чтения", "Радиус"};

            info = new String[][]{
                    {"Название:", getVariable("name") + ""},
                    {"Класс:", getVariable("typeName") + ""},
                    {"Параметр:", paremetresNames[getVariableInteger("changingParameter")] + ""},
                    {"Величина:", getVariable("changingValue") + ""},
                    {"Сложность:", getVariable("changingDifficult") + ""},
                    {"Величина:", getVariable("changingMana") + ""},

                    {"Цена:", getVariable("cost") + ""},

            };
        }
        return info;
    }


    @Override
    public void drawInfo(boolean onlyName){
        String[][] info = info();
        double sizeX = 212;
        double sizeY = info.length*20+10;
        if(onlyName) sizeY=30;

        float locX = InputMain.getCursorX()+28;
        float locY = InputMain.getCursorY()+24;
        if(locY + sizeY > Render2D.getWindowHeight()){
            locY -= sizeY+24;
        }
        if(locX + sizeX > Render2D.getWindowWidth()){
            locX += Render2D.getWindowWidth() - (locX + sizeX);
        }
        Render2D.angleColorDraw("infoBack","interface/white",locX+sizeX/2,locY+sizeY/2,sizeX,sizeY,0,0,0,0,1);

        Render2D.angleDraw("infoBorder1","interface/borderBack",locX+sizeX/2,locY,sizeX,4,0);
        Render2D.angleDraw("infoBorder2","interface/borderBack",locX+sizeX/2,locY+sizeY,sizeX,4,0);
        Render2D.angleDraw("infoBorder3","interface/borderBack",locX,locY+sizeY/2,sizeY,4,90);
        Render2D.angleDraw("infoBorder4","interface/borderBack",locX+sizeX,locY+sizeY/2,sizeY,4,90);

        for(int i =0; i < info.length;i++){
            Text.drawString(info[i][0],locX+4, locY+8+i*20,Text.CAMBRIA_14, Color.white);
            Text.drawStringRigth(info[i][1],locX+4+200, locY+8+i*20,Text.CAMBRIA_14, Color.white);
            if(onlyName) break;
        }
    }
    public String getId(){
        return getVariable("ID")+"";
    }
    public int getCount(){
        return getVariableInteger("count");
    }
}
