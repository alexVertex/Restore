package screens.editor.creationWindows;

import engine.Mathp;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Droplist;

import engine.control.interfaces.Element;
import engine.control.interfaces.Textbox;
import engine.render.Render2D;
import engine.render.Text;
import game.actor.enviroment.Item;
import game.actor.game.Projectile;
import game.actor.actorsHyper.ItemDataBase;
import game.actor.actorsHyper.SpellDataBase;
import org.newdawn.slick.Color;

import java.io.File;
import java.util.HashMap;
import engine.Actor;
import screens.editor.mainScreen.editorScreen;

import static game.actor.enviroment.Item.ITEMS_IN_LINE;

public class createWindowItems {

    private static String[] itemClasses = {"Оружие","Броня","Щит","Дальнобойное оружие","Расходник","Ключевой","Трофей","Чертёж","Материал","Руна"};
    private static Droplist choosenItemClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+12,256,30,"interface/drop",0,itemClasses, Text.CAMBRIA_14,4);

    static Button OK = new Button(Render2D.getWindowWidth() / 2+250-16-32-4, Render2D.getWindowHeight() / 2+400-16-4,32,32,"interface/butOK");
    static Button Cancel = new Button(Render2D.getWindowWidth() / 2+250-16-4, Render2D.getWindowHeight() / 2+400-16-4,32,32,"interface/butCancel");

    private static String[] weaponClasses = {"Кинжал","Меч","Топор","Дробящее","Копьё"};
    private static String[] weaponClassesSkills = {"skillDaggers","skillBlades","skillAxes","skillBlunts","skillSears"};
    private static HashMap<String, String> weaponClassesTranslate = new HashMap<>();

    private static String[] damageClasses = {"Колющий","Рубящий","Дробящий"};
    private static String[] damageClassesSkills = {"armorPierce","armorSlash","armorStrike"};
    private static HashMap<String, String> damageClassesTranslate = new HashMap<>();

    private static String[] armorClasses = {"Голова","Тело","Руки","Ноги"};
    private static String[] shieldClasses = {"Легкий щит","Тяжелый щит"};

    private static String[] farweaponClasses = {"Лук","Арбалет"};
    private static String[] farweaponClassesSkills = {"skillBows","skillCrossbows"};
    private static HashMap<String, String> farweaponClassesTranslate = new HashMap<>();

    private static String[] usesClasses = {"Зелье","Бомба","Свиток"};


    static {
        for(int i = 0; i < weaponClasses.length;i++){
            weaponClassesTranslate.put(weaponClasses[i],weaponClassesSkills[i]);
        }
        for(int i = 0; i < damageClasses.length;i++){
            damageClassesTranslate.put(damageClasses[i],damageClassesSkills[i]);
        }
        for(int i = 0; i < farweaponClasses.length;i++){
            farweaponClassesTranslate.put(farweaponClasses[i],farweaponClassesSkills[i]);
        }
    }

    private static String[] weaponInfoRus = {"ID","Имя","Вес","Цена","Тип оружия","Урон","Тип урона","Дистанция","Скорость атак","Вид снаряда"};
    private static String[] armorInfoRus = {"ID","Имя","Вес","Цена","Тип брони","Защита от колющ.","Защита от рубящ.","Защита от дробящ.","Защита от огня","Защита от мороза","Защита от молнии","Защита от кислоты","Защита от псионики","Защита от волшеб.","Защита от чар.","Материал"};
    private static String[] shieldInfoRus = {"ID","Имя","Вес","Цена","Тип щита","Защита от колющ.","Защита от рубящ.","Защита от дробящ.","Стабильность"};
    private static String[] farweaponInfoRus = {"ID","Имя","Вес","Цена","Тип оружия","Урон","Тип урона","Дистанция","Скорость атак","Вид снаряда","Боеприпасы"};
    private static String[] usesInfoRus = {"ID","Имя","Вес","Цена","Тип расходника","Заклинание"};
    private static String[] keysInfoRus = {"ID","Имя","Вес","Цена"};
    private static String[] trophysInfoRus = {"ID","Имя","Вес","Цена"};
    private static String[] bluePrintsInfoRus = {"ID","Имя","Вес","Цена","Тип материала","Количество"};
    private static String[] materialsInfoRus = {"ID","Имя","Вес","Цена","Тип материала"};
    private static String[] runeInfoRus = {"ID","Имя","Вес","Цена","Параметр","Величина","Сложность","Мана"};

    private static String[][] allInfoRus = {weaponInfoRus,armorInfoRus,shieldInfoRus,farweaponInfoRus,usesInfoRus,keysInfoRus,trophysInfoRus,bluePrintsInfoRus,materialsInfoRus,runeInfoRus};

    private static Textbox textID = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+60,256,30,"interface/text","ИД предмета",Text.CAMBRIA_14,4);

    private static Textbox textBoxName = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90,256,30,"interface/text","название предмета",Text.CAMBRIA_14,4);
    private static Textbox textBoxWeight = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxPrice = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*2,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Droplist dropWeaponClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*3,256,30,"interface/drop",0,weaponClasses,Text.CAMBRIA_14,4);
    private static Textbox textBoxDamage = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*4,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Droplist dropDamageClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*5,256,30,"interface/drop",0,damageClasses,Text.CAMBRIA_14,4);
    private static Textbox textBoxDistant = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*6,256,30,"interface/text",Text.CAMBRIA_14,4,1,100);
    private static Textbox textBoxAttackSpeed = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*7,256,30,"interface/text",Text.CAMBRIA_14,4,1,100);

    private static String[] projectilesNames = {};

    static {
        projectilesNames = new String[ItemDataBase.projectiles.length];
        for(int i = 0; i < projectilesNames.length;i++){
            projectilesNames[i] = ItemDataBase.projectiles[i].getVariableString("texture");
            int index = projectilesNames[i].indexOf('/');
            projectilesNames[i] = projectilesNames[i].split("/")[1];
        }
    }
    private static Droplist dropProjectileClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*8,256,30,"interface/drop",0,projectilesNames,Text.CAMBRIA_14,4);

    private static Element[] weaponInfoInputs = {textID,textBoxName,textBoxWeight,textBoxPrice,dropWeaponClass,textBoxDamage,dropDamageClass,textBoxDistant,textBoxAttackSpeed,dropProjectileClass};

    private static Droplist dropFarWeaponClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*3,256,30,"interface/drop",0,farweaponClasses,Text.CAMBRIA_14,4);
    private static Textbox textBoxFires = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*9,256,30,"interface/text",Text.CAMBRIA_14,4,1,1000);
    private static Element[] farweaponInfoInputs = {textID,textBoxName,textBoxWeight,textBoxPrice,dropFarWeaponClass,textBoxDamage,dropDamageClass,textBoxDistant,textBoxAttackSpeed,dropProjectileClass,textBoxFires};


    private static Droplist dropArmorClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*3,256,30,"interface/drop",0,armorClasses,Text.CAMBRIA_14,4);
    private static Textbox textBoxArmorPierce = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*4,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxArmorSlash = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*5,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxArmorStrike = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*6,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxArmorFire = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*7,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxArmorFreaze = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*8,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxArmorElectro = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*9,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxArmorAcid = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*10,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxArmorMental = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*11,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxArmorWizardry = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*12,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxArmorSacred = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*13,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static String[] materialClasses = {"Кожа","Металл","Камень"};
    private static Droplist dropMaterialClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*14,256,30,"interface/drop",0,materialClasses,Text.CAMBRIA_14,4);

    private static Element[] armorInfoInputs = {textID,textBoxName,textBoxWeight,textBoxPrice,dropArmorClass,textBoxArmorPierce,textBoxArmorSlash,textBoxArmorStrike,
            textBoxArmorFire,textBoxArmorFreaze,textBoxArmorElectro,textBoxArmorAcid,textBoxArmorMental,textBoxArmorWizardry,textBoxArmorSacred,dropMaterialClass};

    private static Droplist dropShieldClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*3,256,30,"interface/drop",0,shieldClasses,Text.CAMBRIA_14,4);
    private static Textbox textBoxShieldStability = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*7,256,30,"interface/text",Text.CAMBRIA_14,4,0,100);
    private static Element[] shieldInfoInputs = {textID,textBoxName,textBoxWeight,textBoxPrice,dropShieldClass,textBoxArmorPierce,textBoxArmorSlash,textBoxArmorStrike,textBoxShieldStability};

    private static Droplist dropUsesClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*3,256,30,"interface/drop",0,usesClasses,Text.CAMBRIA_14,4);
    private static Droplist dropSpells = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*4,256,30,"interface/drop",0, SpellDataBase.getSpellsId(),Text.CAMBRIA_14,4);
    private static Element[] usesInfoInputs = {textID,textBoxName,textBoxWeight,textBoxPrice,dropUsesClass,dropSpells};

    private static Element[] keysInfoInputs = {textID,textBoxName,textBoxWeight,textBoxPrice};

    private static Element[] trophysInfoInputs = {textID,textBoxName,textBoxWeight,textBoxPrice};

    static String[] materialsNames = {"Камень","Кожа","Кость","Мех","Шкура","Металл","Ткань"};
    private static Droplist textBoxMaterialClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*3,256,30,"interface/drop",0,materialsNames,Text.CAMBRIA_14,4);
    private static Textbox textBoxMaterialCount = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*4,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);

    private static Element[] bluePrintInfoInputs = {textID,textBoxName,textBoxWeight,textBoxPrice,textBoxMaterialClass,textBoxMaterialCount};

    private static Element[] materialsInfoInputs = {textID,textBoxName,textBoxWeight,textBoxPrice,textBoxMaterialClass};

    static String[] paremetresNames = {"Эффект","Время","Запас сил","Дистанция","Цели","Скорость чтения","Радиус"};
    private static Droplist dropChangingParameter = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*3,256,30,"interface/drop",0,paremetresNames,Text.CAMBRIA_14,4);
    private static Textbox textBoxChangingValue = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*4,256,30,"interface/text",Text.CAMBRIA_14,4,-9999,9999);
    private static Textbox textBoxChangingDiff = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*5,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);
    private static Textbox textBoxChangingMana = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90+30*6,256,30,"interface/text",Text.CAMBRIA_14,4,0,9999999);

    private static Element[] runeInfoInputs = {textID,textBoxName,textBoxWeight,textBoxPrice,dropChangingParameter,textBoxChangingValue,textBoxChangingDiff,textBoxChangingMana};

    private static Element[][] allInfoInputs = {weaponInfoInputs,armorInfoInputs,shieldInfoInputs,farweaponInfoInputs,usesInfoInputs,keysInfoInputs,trophysInfoInputs,bluePrintInfoInputs,materialsInfoInputs,runeInfoInputs};

    private static Droplist droptextures = new Droplist(Render2D.getWindowWidth() / 2+130,Render2D.getWindowHeight() / 2-400+100+30*15,236,30,"interface/drop",0,damageClasses,Text.CAMBRIA_14,4);


    private static String itemFolder = "items/";
    private static String itemFile = "weapon";
    private static int choosenX = 0;
    private static int choosenY = 0;
    public static boolean drawMe = false;
    public static void loadData(Actor load){
        drawMe = true;
        textBoxName.setText(load.getVariable("name")+"");
        textBoxPrice.setText(load.getVariable("cost")+"");
        textBoxWeight.setText(load.getVariable("weight")+"");
        textID.setText(load.getVariable("ID")+"");
        itemFile = load.getVariable("texture")+"";
        choosenX = load.getVariableInteger("partX");
        choosenY = load.getVariableInteger("partY");
        droptextures.setValue(load.getVariable("texture")+"");
        choosenItemClass.setValue(load.getVariable("typeName")+"");
        if(load.getVariable("type").equals(ItemDataBase.TYPE_WEAPON)){
            loadWeapon(load);
        } else
        if(load.getVariable("type").equals(ItemDataBase.TYPE_ARMOR)){
            loadArmor(load);
        }else
        if(load.getVariable("type").equals(ItemDataBase.TYPE_SHIELD)){
            loadShield(load);
        }
        if(load.getVariable("type").equals(ItemDataBase.TYPE_BOWS)){
            loadFarweapon(load);
        }
        if(load.getVariable("type").equals(ItemDataBase.TYPE_USES)){
            loadUses(load);
        }
        if(load.getVariable("type").equals(ItemDataBase.TYPE_BLUEPRINTS)){
            loadBlueprint(load);
        }
        if(load.getVariable("type").equals(ItemDataBase.TYPE_MATERIALS)){
            loadMaterial(load);
        }
        if(load.getVariable("type").equals(ItemDataBase.TYPE_RUNE)){
            loadRuna(load);
        }
    }
    private static void loadRuna(Actor load){
        //private static String[] weaponInfo = {"name","weight","cost","weaponClass","damage","damageType","distant","attackSpeed"};
        dropChangingParameter.setPos(load.getVariableInteger("changingParameter"));
        textBoxChangingValue.setText(load.getVariableInteger("changingValue")+"");
        textBoxChangingDiff.setText(load.getVariableInteger("changingDifficult")+"");
        textBoxChangingMana.setText(load.getVariableInteger("changingMana")+"");
    }
    private static void loadBlueprint(Actor load){
        //private static String[] weaponInfo = {"name","weight","cost","weaponClass","damage","damageType","distant","attackSpeed"};
        dropMaterialClass.setPos(load.getVariableInteger("materialClass"));
        textBoxMaterialCount.setText(load.getVariableInteger("materialCount")+"");

    }
    private static void loadMaterial(Actor load){
        //private static String[] weaponInfo = {"name","weight","cost","weaponClass","damage","damageType","distant","attackSpeed"};
        dropMaterialClass.setPos(load.getVariableInteger("materialClass"));
    }
    private static void loadWeapon(Actor load){
        //private static String[] weaponInfo = {"name","weight","cost","weaponClass","damage","damageType","distant","attackSpeed"};
        textBoxDamage.setText(load.getVariableTrunked("damage")+"");
        textBoxDistant.setText(load.getVariableTrunked("distant")/32+"");
        textBoxAttackSpeed.setText(load.getVariableTrunked("attackSpeed",100)+"");
        dropDamageClass.setValue(load.getVariable("damageTypeName")+"");
        dropWeaponClass.setValue(load.getVariable("weaponClassName")+"");
        dropProjectileClass.setPos(load.getVariableInteger("projectileID"));
    }
    private static void loadArmor(Actor load){
        //private static String[] weaponInfo = {"name","weight","cost","weaponClass","damage","damageType","distant","attackSpeed"};
        textBoxArmorPierce.setText(load.getVariableInteger("armorPierce")+"");
        textBoxArmorSlash.setText(load.getVariableInteger("armorSlash")+"");
        textBoxArmorStrike.setText(load.getVariableInteger("armorStrike")+"");
        textBoxArmorFire.setText(load.getVariableInteger("armorFire")+"");
        textBoxArmorFreaze.setText(load.getVariableInteger("armorFreeze")+"");
        textBoxArmorElectro.setText(load.getVariableInteger("armorElectro")+"");
        textBoxArmorAcid.setText(load.getVariableInteger("armorAcid")+"");
        textBoxArmorMental.setText(load.getVariableInteger("armorMental")+"");
        textBoxArmorWizardry.setText(load.getVariableInteger("armorWizardry")+"");
        textBoxArmorSacred.setText(load.getVariableInteger("armorSacred")+"");
        dropArmorClass.setValue(load.getVariable("armorClassName")+"");
        dropMaterialClass.setPos(load.getVariableInteger("sound"));

    }
    private static void loadShield(Actor load){
        //private static String[] weaponInfo = {"name","weight","cost","weaponClass","damage","damageType","distant","attackSpeed"};
        textBoxArmorPierce.setText(load.getVariableInteger("armorPierce")+"");
        textBoxArmorSlash.setText(load.getVariableInteger("armorSlash")+"");
        textBoxArmorStrike.setText(load.getVariableInteger("armorStrike")+"");
        textBoxShieldStability.setText(load.getVariableTrunked("stability",100)+"");
        dropShieldClass.setValue(load.getVariable("armorClassName")+"");
    }
    private static void loadFarweapon(Actor load){
        //private static String[] weaponInfo = {"name","weight","cost","weaponClass","damage","damageType","distant","attackSpeed"};
        textBoxDamage.setText(load.getVariableTrunked("damage")+"");
        textBoxDistant.setText(load.getVariableTrunked("distant")/32+"");
        textBoxAttackSpeed.setText(load.getVariableTrunked("attackSpeed",100)+"");
        dropDamageClass.setValue(load.getVariable("damageTypeName")+"");
        dropWeaponClass.setValue(load.getVariable("weaponClassName")+"");
        textBoxFires.setText(load.getVariableInteger("fires")+"");
        dropProjectileClass.setPos(load.getVariableInteger("projectileID"));

    }
    private static void loadUses(Actor load){
        //private static String[] weaponInfo = {"name","weight","cost","weaponClass","damage","damageType","distant","attackSpeed"};
        dropUsesClass.setValue(load.getVariable("usesClassName")+"");
        dropSpells.setValue(load.getVariable("spellIn")+"");

    }
    private static int currentItemType = 0;
    public  static  boolean loaded = false;
    public static void draw(){
        if(!loaded) {
            File folder = new File("res/tex/items");
            File[] folderEntries = folder.listFiles();
            String[] fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                fileNames[i] = name;
            }
            droptextures.setVals(fileNames);
            loaded = true;
        }
        if(SpellDataBase.isChanged){
            dropSpells.setVals(SpellDataBase.getSpellsId());
            SpellDataBase.isChanged = false;
        }
        if(drawMe) {

            Render2D.angleColorDraw("windowCreatorBack","interface/white", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2, 500, 800, 0, 0, 0, 0, 1);
            Render2D.angleDraw("creatorBorder1","interface/Border", Render2D.getWindowWidth() / 2-250, Render2D.getWindowHeight() / 2, 800, 4, 90);
            Render2D.angleDraw("creatorBorder2","interface/Border", Render2D.getWindowWidth() / 2+250, Render2D.getWindowHeight() / 2, 800, 4, 90);
            Render2D.angleDraw("creatorBorder3","interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2-400, 500, 4, 0);
            Render2D.angleDraw("creatorBorder4","interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2+400, 500, 4, 0);
            choosenItemClass.draw();
            choosenItemClass.work();
            currentItemType = choosenItemClass.getVal();
            for(int i = 0; i < allInfoRus[currentItemType].length;i++){
                Text.drawString(allInfoRus[currentItemType][i],Render2D.getWindowWidth() / 2-250+10,Render2D.getWindowHeight() / 2-400+50+i*30,Text.CAMBRIA_24, Color.white);
                allInfoInputs[currentItemType][i].work();
                allInfoInputs[currentItemType][i].draw();
            }
            Render2D.simpleDraw("creatorTileSet",itemFolder+itemFile, Render2D.getWindowWidth() / 2-120, Render2D.getWindowHeight() / 2+270, 256, 256);
            Render2D.simpleDraw("creatorSelector","editor/selector", 8 + choosenX * 16+(Render2D.getWindowWidth() / 2-248), 8 + choosenY * 16+(Render2D.getWindowHeight() / 2+270-128), 16, 16);
            draw(Render2D.getWindowWidth() / 2-120+140, Render2D.getWindowHeight() / 2+270,32);
            if (InputMain.isKeyJustPressed(InputMain.LMB) || InputMain.isKeyJustReleased(InputMain.LMB)) {
                for (int i = 0; i < 16; i++) {
                    for (int j = 0; j < 16; j++) {
                        if (Mathp.inRectangle(0 + i * 16+(Render2D.getWindowWidth() / 2-248), 0 + j * 16+(Render2D.getWindowHeight() / 2+270-128), 16 + i * 16+(Render2D.getWindowWidth() / 2-248), 16 + j * 16+(Render2D.getWindowHeight() / 2+270-128), InputMain.getCursorX(), InputMain.getCursorY())) {
                            choosenX = i;
                            choosenY = j;
                        }
                    }
                }
            }
            droptextures.draw();
            droptextures.work();
            itemFile = droptextures.getText();

            OK.work();
            if(OK.isReleased()){
                switch (choosenItemClass.getText()){
                    case "Оружие":
                        createWeapon();
                        break;
                    case "Броня":
                        createArmor();
                        break;
                    case "Щит":
                        createShield();
                        break;
                    case "Дальнобойное оружие":
                        createFarweapon();
                        break;
                    case "Расходник":
                        createUses();
                        break;
                    case "Ключевой":
                        createKeys();
                        break;
                    case "Трофей":
                        createTrophys();
                        break;
                    case "Чертёж":
                        createBlueprint();
                        break;
                    case "Материал":
                        createMaterial();
                        break;
                    case "Руна":
                        createRuna();
                        break;
                }
                editorScreen.chosen = new Item("",0,0,0,0,textID.getText(),0);
                drawMe = false;
                editorScreen.mustRecreateShowItems = true;

            }
            OK.draw();
            Cancel.work();
            if(Cancel.isReleased()){
                drawMe = false;
            }
            Cancel.draw();

            if(choosenItemClass.getText().equals("Оружие")){
                drawProjectile();
            }
        }

    }
    private static double curFrame = 0.0;
    private static void drawProjectile(){
        Projectile dataForProjectile = ItemDataBase.projectiles[dropProjectileClass.getVal()];
        double totalFrames = (Double) dataForProjectile.getVariable("frames");
        curFrame += (Double) dataForProjectile.getVariable("animSpeed");
        if(curFrame >= totalFrames){
            curFrame -= totalFrames;
        }
        String texture = dataForProjectile.getVariable("texture")+"";
        double cutLeft = (int)curFrame/totalFrames;
        double cutRight = 1-((int)curFrame+1)/totalFrames;
        double cutUp = 0;
        double cutDown = 0;
        Render2D.angleCutDraw("projectileItem",texture,Render2D.getWindowWidth() / 2-120+240, Render2D.getWindowHeight() / 2+270, 64,64,0,cutLeft,cutRight,cutUp,cutDown);

    }
    private static void createWeapon(){
        HashMap<String,Object> weapon1 = new HashMap<>();
        weapon1.put("name",textBoxName.getText());
        weapon1.put("typeName","Оружие");
        weapon1.put("type",0);
        weapon1.put("weaponClass",weaponClassesTranslate.get(dropWeaponClass.getText()));
        weapon1.put("weaponClassName",dropWeaponClass.getText());
        weapon1.put("damage",(double)Integer.parseInt(textBoxDamage.getText()));
        weapon1.put("damageType",damageClassesTranslate.get(dropDamageClass.getText()));
        weapon1.put("damageTypeName",dropDamageClass.getText());
        weapon1.put("distant",Integer.parseInt(textBoxDistant.getText())*32.0);
        weapon1.put("attackSpeed",(double)Integer.parseInt(textBoxAttackSpeed.getText())/100.0);
        weapon1.put("texture",itemFile);
        weapon1.put("partX",choosenX);
        weapon1.put("partY",choosenY);
        weapon1.put("weight",Integer.parseInt(textBoxWeight.getText()));
        weapon1.put("cost",Integer.parseInt(textBoxPrice.getText()));
        weapon1.put("filter",1);

        weapon1.put("projectileID",dropProjectileClass.getVal());
        String ID = textID.getText();
        weapon1.put("ID",ID);
        ItemDataBase.addItem(ID,weapon1);
    }
    private static void createArmor(){
        HashMap<String,Object> armor1 = new HashMap<>();
        armor1.put("name",textBoxName.getText());
        armor1.put("typeName","Броня");
        armor1.put("type",1);
        armor1.put("armorClass",dropArmorClass.getVal());
        armor1.put("armorClassName",dropArmorClass.getText());
        armor1.put("armorPierce",Integer.parseInt(textBoxArmorPierce.getText()));
        armor1.put("armorSlash",Integer.parseInt(textBoxArmorSlash.getText()));
        armor1.put("armorStrike",Integer.parseInt(textBoxArmorStrike.getText()));
        armor1.put("armorFire",Integer.parseInt(textBoxArmorFire.getText()));
        armor1.put("armorFreeze",Integer.parseInt(textBoxArmorFreaze.getText()));
        armor1.put("armorElectro",Integer.parseInt(textBoxArmorElectro.getText()));
        armor1.put("armorAcid",Integer.parseInt(textBoxArmorAcid.getText()));
        armor1.put("armorMental",Integer.parseInt(textBoxArmorMental.getText()));
        armor1.put("armorWizardry",Integer.parseInt(textBoxArmorWizardry.getText()));
        armor1.put("armorSacred",Integer.parseInt(textBoxArmorSacred.getText()));
        armor1.put("sound",dropMaterialClass.getVal());

        armor1.put("texture",itemFile);
        armor1.put("partX",choosenX);
        armor1.put("partY",choosenY);
        armor1.put("weight",Integer.parseInt(textBoxWeight.getText()));
        armor1.put("cost",Integer.parseInt(textBoxPrice.getText()));
        armor1.put("filter",2);
        String ID = textID.getText();
        armor1.put("ID",ID);
        ItemDataBase.addItem(ID,armor1);
    }

    private static void createShield(){
        HashMap<String,Object> armor1 = new HashMap<>();
        armor1.put("name",textBoxName.getText());
        armor1.put("typeName","Щит");
        armor1.put("type",2);
        armor1.put("armorClass",dropShieldClass.getVal());
        armor1.put("armorClassName",dropShieldClass.getText());
        armor1.put("armorPierce",Integer.parseInt(textBoxArmorPierce.getText()));
        armor1.put("armorSlash",Integer.parseInt(textBoxArmorSlash.getText()));
        armor1.put("armorStrike",Integer.parseInt(textBoxArmorStrike.getText()));
        armor1.put("stability",(double)Integer.parseInt(textBoxShieldStability.getText())/100.0);
        armor1.put("texture",itemFile);
        armor1.put("partX",choosenX);
        armor1.put("partY",choosenY);
        armor1.put("weight",Integer.parseInt(textBoxWeight.getText()));
        armor1.put("cost",Integer.parseInt(textBoxPrice.getText()));
        armor1.put("filter",3);
        String ID = textID.getText();
        armor1.put("ID",ID);
        ItemDataBase.addItem(ID,armor1);
    }
    private static void createFarweapon(){
        HashMap<String,Object> weapon1 = new HashMap<>();
        weapon1.put("name",textBoxName.getText());
        weapon1.put("typeName","Дальнобойное оружие");
        weapon1.put("type",4);
        weapon1.put("weaponClass",farweaponClassesTranslate.get(dropFarWeaponClass.getText()));
        weapon1.put("weaponClassName",dropFarWeaponClass.getText());
        weapon1.put("damage",(double)Integer.parseInt(textBoxDamage.getText()));
        weapon1.put("damageType",damageClassesTranslate.get(dropDamageClass.getText()));
        weapon1.put("damageTypeName",dropDamageClass.getText());
        weapon1.put("distant",Integer.parseInt(textBoxDistant.getText())*32.0);
        weapon1.put("attackSpeed",(double)Integer.parseInt(textBoxAttackSpeed.getText())/100.0);
        weapon1.put("texture",itemFile);
        weapon1.put("partX",choosenX);
        weapon1.put("partY",choosenY);
        weapon1.put("weight",Integer.parseInt(textBoxWeight.getText()));
        weapon1.put("cost",Integer.parseInt(textBoxPrice.getText()));
        weapon1.put("filter",4);
        weapon1.put("fires",Integer.parseInt(textBoxFires.getText()));
        weapon1.put("firesMax",Integer.parseInt(textBoxFires.getText()));
        weapon1.put("projectileID",dropProjectileClass.getVal());
        String ID = textID.getText();
        weapon1.put("ID",ID);
        ItemDataBase.addItem(ID,weapon1);
    }

    private static void createUses(){
        HashMap<String,Object> weapon1 = new HashMap<>();
        weapon1.put("name",textBoxName.getText());
        weapon1.put("typeName","Расходник");
        weapon1.put("type",3);
        weapon1.put("usesClassName",dropUsesClass.getText());
        weapon1.put("spellIn",dropSpells.getText());
        weapon1.put("texture",itemFile);
        weapon1.put("partX",choosenX);
        weapon1.put("partY",choosenY);
        weapon1.put("weight",Integer.parseInt(textBoxWeight.getText()));
        weapon1.put("cost",Integer.parseInt(textBoxPrice.getText()));
        weapon1.put("filter",5);
        String ID = textID.getText();
        weapon1.put("ID",ID);
        ItemDataBase.addItem(ID,weapon1);
    }
    private static void createBlueprint(){
        HashMap<String,Object> weapon1 = new HashMap<>();
        weapon1.put("name",textBoxName.getText());
        weapon1.put("typeName","Чертёж");
        weapon1.put("type",7);
        weapon1.put("texture",itemFile);
        weapon1.put("partX",choosenX);
        weapon1.put("partY",choosenY);
        weapon1.put("weight",Integer.parseInt(textBoxWeight.getText()));
        weapon1.put("cost",Integer.parseInt(textBoxPrice.getText()));
        weapon1.put("materialClass",dropMaterialClass.getVal());
        weapon1.put("materialCount",Integer.parseInt(textBoxMaterialCount.getText()));
        weapon1.put("filter",6);
        String ID = textID.getText();
        weapon1.put("ID",ID);
        ItemDataBase.addItem(ID,weapon1);
    }
    private static void createMaterial(){
        HashMap<String,Object> weapon1 = new HashMap<>();
        weapon1.put("name",textBoxName.getText());
        weapon1.put("typeName","Материал");
        weapon1.put("type",8);
        weapon1.put("texture",itemFile);
        weapon1.put("partX",choosenX);
        weapon1.put("partY",choosenY);
        weapon1.put("weight",Integer.parseInt(textBoxWeight.getText()));
        weapon1.put("cost",Integer.parseInt(textBoxPrice.getText()));
        weapon1.put("materialClass",dropMaterialClass.getVal());
        weapon1.put("filter",7);
        String ID = textID.getText();
        weapon1.put("ID",ID);
        ItemDataBase.addItem(ID,weapon1);
    }
    private static void createRuna(){
        HashMap<String,Object> weapon1 = new HashMap<>();
        weapon1.put("name",textBoxName.getText());
        weapon1.put("typeName","Руна");
        weapon1.put("type",9);
        weapon1.put("texture",itemFile);
        weapon1.put("partX",choosenX);
        weapon1.put("partY",choosenY);
        weapon1.put("weight",Integer.parseInt(textBoxWeight.getText()));
        weapon1.put("cost",Integer.parseInt(textBoxPrice.getText()));

        weapon1.put("changingParameter",dropChangingParameter.getVal());
        weapon1.put("changingValue",Integer.parseInt(textBoxChangingValue.getText()));
        weapon1.put("changingDifficult",Integer.parseInt(textBoxChangingDiff.getText()));
        weapon1.put("changingMana",Integer.parseInt(textBoxChangingMana.getText()));

        weapon1.put("filter",8);
        String ID = textID.getText();
        weapon1.put("ID",ID);
        ItemDataBase.addItem(ID,weapon1);
    }
    private static void createKeys(){
        HashMap<String,Object> weapon1 = new HashMap<>();
        weapon1.put("name",textBoxName.getText());
        weapon1.put("typeName","Ключевой предмет");
        weapon1.put("type",5);
        weapon1.put("texture",itemFile);
        weapon1.put("partX",choosenX);
        weapon1.put("partY",choosenY);
        weapon1.put("weight",Integer.parseInt(textBoxWeight.getText()));
        weapon1.put("cost",Integer.parseInt(textBoxPrice.getText()));
        weapon1.put("filter",10);
        String ID = textID.getText();
        weapon1.put("ID",ID);
        ItemDataBase.addItem(ID,weapon1);
    }
    private static void createTrophys(){
        HashMap<String,Object> weapon1 = new HashMap<>();
        weapon1.put("name",textBoxName.getText());
        weapon1.put("typeName","Трофей");
        weapon1.put("type",6);
        weapon1.put("texture",itemFile);
        weapon1.put("partX",choosenX);
        weapon1.put("partY",choosenY);
        weapon1.put("weight",Integer.parseInt(textBoxWeight.getText()));
        weapon1.put("cost",Integer.parseInt(textBoxPrice.getText()));
        weapon1.put("filter",9);
        String ID = textID.getText();
        weapon1.put("ID",ID);
        ItemDataBase.addItem(ID,weapon1);
    }
    public static void draw(double lx, double ly,double s) {
        double[] part = {choosenX,choosenY};
        String texture = itemFolder+itemFile;
        double cutLeft = part[0]/ITEMS_IN_LINE;
        double cutRight = 1-(part[0]+1)/ITEMS_IN_LINE;
        double cutUp = part[1]/ITEMS_IN_LINE;
        double cutDown = 1-(part[1]+1)/ITEMS_IN_LINE;
        Render2D.angleCutDraw("creatorItemView",texture,lx,ly,s,s,0,cutLeft,cutRight,cutUp,cutDown);
    }

}
