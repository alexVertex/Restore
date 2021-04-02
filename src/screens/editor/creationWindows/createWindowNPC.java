package screens.editor.creationWindows;

import engine.Actor;
import engine.control.interfaces.Button;
import engine.control.interfaces.Droplist;
import engine.control.interfaces.Element;
import engine.control.interfaces.Textbox;
import engine.render.Render2D;
import engine.render.Text;
import game.actor.actorsHyper.*;
import game.actor.player.NPC;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import screens.editor.mainScreen.editorScreen;

import java.io.File;
import java.util.HashMap;

public class createWindowNPC {

    static Button OK = new Button(Render2D.getWindowWidth() / 2+250-16-32-4, Render2D.getWindowHeight() / 2+400-16-4,32,32,"interface/butOK");
    static Button Cancel = new Button(Render2D.getWindowWidth() / 2+250-16-4, Render2D.getWindowHeight() / 2+400-16-4,32,32,"interface/butCancel");

    private static String[] bloodTypes = {"Красная","Синяя","Зелёная"};


    private static String[] visionTypes = {"Обычное","Игнорирует препятствия"};

    private static String[] damageClasses = {"Колющий","Рубящий","Дробящий"};
    private static String[] skinClasses = {"Кожа","Металл","Камень"};

    private static String[] damageClassesSkills = {"armorPierce","armorSlash","armorStrike"};
    private static HashMap<String, String> damageClassesTranslate = new HashMap<>();

    private static Textbox textID = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60,256,30,"interface/text","ИД предмета",Text.CAMBRIA_14,4);
    private static Textbox textName = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30,256,30,"interface/text","ИД предмета",Text.CAMBRIA_14,4);
    private static Droplist dropIngameTexture = new Droplist(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*2,256,30,"interface/drop",0, SpellDataBase.effectNames, Text.CAMBRIA_14,4);
    private static Droplist dropBloodTexture = new Droplist(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*3,256,30,"interface/drop",0, bloodTypes, Text.CAMBRIA_14,4);
    private static Droplist dropStepsTexture = new Droplist(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*4,256,30,"interface/drop",0, SpellDataBase.effectNames, Text.CAMBRIA_14,4);
    private static Droplist dropPortretTexture = new Droplist(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*5,256,30,"interface/drop",0, SpellDataBase.effectNames, Text.CAMBRIA_14,4);
    private static Textbox textPortretX = new Textbox(Render2D.getWindowWidth() / 2+115-250-64,Render2D.getWindowHeight() / 2-400+60+30*6,128,30,"interface/text",Text.CAMBRIA_14,4,0,3);
    private static Textbox textPortretY = new Textbox(Render2D.getWindowWidth() / 2+115-250+64,Render2D.getWindowHeight() / 2-400+60+30*6,128,30,"interface/text",Text.CAMBRIA_14,4,0,3);
    private static Textbox textHealth = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*7,256,30,"interface/text",Text.CAMBRIA_14,4,1,5000);
    private static Textbox textStamina = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*8,256,30,"interface/text",Text.CAMBRIA_14,4,1,5000);
    private static Textbox textRecovery = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*9,256,30,"interface/text",Text.CAMBRIA_14,4,0,10);
    private static Textbox textWalkSpeed = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*10,256,30,"interface/text",Text.CAMBRIA_14,4,1,100);
    private static Textbox textRunSpeed = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*11,256,30,"interface/text",Text.CAMBRIA_14,4,1,100);
    private static Textbox textVision = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*12,256,30,"interface/text",Text.CAMBRIA_14,4,0,100);
    private static Droplist dropVisionType = new Droplist(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*13,256,30,"interface/drop",0, visionTypes, Text.CAMBRIA_14,4);
    private static Textbox textGoldMin = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*14,256,30,"interface/text",Text.CAMBRIA_14,4,0,10000000);
    private static Textbox textGoldMax = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*15,256,30,"interface/text",Text.CAMBRIA_14,4,0,10000000);
    private static Droplist dropLoot1 = new Droplist(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*16,256,30,"interface/drop",0, SpellDataBase.effectNames, Text.CAMBRIA_14,4);
    private static Droplist dropLoot2 = new Droplist(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*17,256,30,"interface/drop",0, SpellDataBase.effectNames, Text.CAMBRIA_14,4);
    private static Droplist dropLoot3 = new Droplist(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*18,256,30,"interface/drop",0, SpellDataBase.effectNames, Text.CAMBRIA_14,4);
    private static Textbox textDistant = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*19,256,30,"interface/text",Text.CAMBRIA_14,4,1,1000);
    private static Textbox textAttackSpeed = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*20,256,30,"interface/text",Text.CAMBRIA_14,4,1,100);
    private static Textbox textDamage = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*21,256,30,"interface/text",Text.CAMBRIA_14,4,1,1000);
    private static Droplist dropDamageType = new Droplist(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*22,256,30,"interface/drop",0, damageClasses, Text.CAMBRIA_14,4);
    private static Textbox textEXP = new Textbox(Render2D.getWindowWidth() / 2+115-250,Render2D.getWindowHeight() / 2-400+60+30*23,256,30,"interface/text",Text.CAMBRIA_14,4,0,10000000);

    private static Element[] elements1 = {textID,textName,dropIngameTexture,dropBloodTexture,dropStepsTexture,dropPortretTexture,textPortretX,textPortretY,
            textHealth,textStamina,textRecovery,textWalkSpeed,textRunSpeed,textVision,dropVisionType,textGoldMin,textGoldMax,dropLoot1,dropLoot2,dropLoot3,textDistant,
            textAttackSpeed,textDamage,dropDamageType,textEXP};

    private static Textbox textArmorPierce = new Textbox(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*0,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textArmorSlash = new Textbox(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*1,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textArmorStrike = new Textbox(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*2,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textArmorFire = new Textbox(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*3,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textArmorFreeze = new Textbox(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*4,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textArmorElectro = new Textbox(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*5,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textArmorAcid = new Textbox(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*6,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textArmorMental = new Textbox(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*7,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textArmorWizardry = new Textbox(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*8,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textArmorSacred = new Textbox(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*9,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Droplist dropResponse = new Droplist(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*10,256,30,"interface/drop",0,new String[]{"Враждебный","Дружественный"},Text.CAMBRIA_14,4);
    private static Droplist dropSkin = new Droplist(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*11,256,30,"interface/drop",0,skinClasses,Text.CAMBRIA_14,4);
    private static Droplist dropResurrect = new Droplist(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*12,256,30,"interface/drop",0,new String[]{"Да","Нет"},Text.CAMBRIA_14,4);
    private static Droplist dropVoice = new Droplist(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*13,256,30,"interface/drop",0,new String[]{"Да","Нет"},Text.CAMBRIA_14,4);
    private static Droplist dropPaysMerc = new Droplist(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*14,256,30,"interface/drop",0,new String[]{"Да","Нет"},Text.CAMBRIA_14,4);
    private static Droplist dropPaysTrade = new Droplist(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*15,256,30,"interface/drop",0,new String[]{"Да","Нет"},Text.CAMBRIA_14,4);
    private static Droplist dropPaysSmith = new Droplist(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*16,256,30,"interface/drop",0,new String[]{"Да","Нет"},Text.CAMBRIA_14,4);
    private static Droplist dropPaysEnch = new Droplist(Render2D.getWindowWidth() / 2+115+250,Render2D.getWindowHeight() / 2-400+60+30*17,256,30,"interface/drop",0,new String[]{"Да","Нет"},Text.CAMBRIA_14,4);

    private static Element[] elements2 = {textArmorPierce,textArmorSlash,textArmorStrike,textArmorFire,textArmorFreeze,textArmorElectro,textArmorAcid,textArmorMental,
            textArmorWizardry,textArmorSacred,dropResponse,dropSkin,dropResurrect,dropVoice,dropPaysMerc,dropPaysTrade,dropPaysSmith,dropPaysEnch};
    private static String[] names1 = {"ИД","Имя","Игровая текстура","Текстура крови","Текстура следов","Портрет текстура","Портрет позиция","Здоровье","Запас сил",
            "Восстановление","Скорость ходьбы","Скорость бега","Зрение","Тип зрения","Золото мин.","Золото макс.","Трофей 1","Трофей 2","Трофей 3",
            "Дистанция атаки","Скорость атаки","Урон","Тип урона","Опыт",""};
    private static String[] names2 = {"Защита от колющ.","Защита от рубящ.","Защита от дробящ.","Защита от огня","Защита от мороза","Защита от молнии","Защита от кислоты","Защита от псионики","Защита от волшеб.","Защита от чар.",
    "Отношение","Тип кожи","Возрождение","Голос","Услуга-наёмник","Услуга-торговец","Услуга-кузнец","Услуга-зачарование"};

    static {

        for(int i = 0; i < damageClasses.length;i++){
            damageClassesTranslate.put(damageClasses[i],damageClassesSkills[i]);
        }
        for(int i = 0; i < damageClasses.length;i++){
            damageClassesTranslate.put(damageClassesSkills[i],damageClasses[i]);
        }

    }
    public static boolean drawMe = false;
    public static void loadData(Actor load){
        drawMe = true;
        textID.setText(load.getVariable("ID")+"");
        dropPortretTexture.setValue(load.getVariable("portret")+"");
        dropPortretTexture.setValue((load.getVariable("ingameTexture")+"").substring(6));
        dropBloodTexture.setPos(load.getVariableInteger("bloodTexture"));
        dropStepsTexture.setValue(load.getVariable("stepsTexture")+"");
        textHealth.setText(load.getVariableTrunked("maxHealth")+"");
        textStamina.setText(load.getVariableTrunked("maxStamina")+"");
        textRunSpeed.setText(load.getVariableTrunked("speedRun",10)+"");
        textWalkSpeed.setText(load.getVariableTrunked("speedWalk",10)+"");
        textEXP.setText(load.getVariableInteger("exp")+"");
        textName.setText(load.getVariable("name")+"");
        textVision.setText(load.getVariableInteger("vision")+"");
        dropVisionType.setPos(load.getVariableInteger("visionType"));
        textGoldMin.setText(load.getVariableInteger("lootGoldMin")+"");
        textGoldMax.setText(load.getVariableInteger("lootGoldMax")+"");
        textPortretX.setText(load.getVariableInteger("portretX")+"");
        textPortretY.setText(load.getVariableInteger("portretY")+"");
        dropLoot1.setValue(load.getVariable("loot1")+"");
        dropLoot2.setValue(load.getVariable("loot2")+"");
        dropLoot3.setValue(load.getVariable("loot3")+"");
        textDamage.setText(load.getVariableTrunked("damage")+"");
        textAttackSpeed.setText(load.getVariableTrunked("attackSpeed",100)+"");
        textDistant.setText(load.getVariableTrunked("distant")/32+"");
        textRecovery.setText(load.getVariableInteger("skillRestoration")+"");
        String type = load.getVariable("damageType")+"";
        dropDamageType.setValue(damageClassesTranslate.get(type));

        textArmorPierce.setText((load.getVariableInteger("armorPierce")+""));
        textArmorSlash.setText((load.getVariableInteger("armorSlash")+""));
        textArmorStrike.setText((load.getVariableInteger("armorStrike")+""));
        textArmorFire.setText((load.getVariableInteger("armorFire")+""));
        textArmorFreeze.setText((load.getVariableInteger("armorFreeze")+""));
        textArmorElectro.setText((load.getVariableInteger("armorElectro")+""));
        textArmorAcid.setText((load.getVariableInteger("armorAcid")+""));
        textArmorMental.setText((load.getVariableInteger("armorMental")+""));
        textArmorWizardry.setText((load.getVariableInteger("armorWizardry")+""));
        textArmorSacred.setText((load.getVariableInteger("armorSacred")+""));
        dropResponse.setPos(load.getVariableInteger("good"));
        dropSkin.setPos(load.getVariableInteger("skinType"));
        dropResurrect.setPos(load.getVariableInteger("nonResurrect"));
        dropVoice.setValue(load.getVariableString("voice"));
        dropPaysMerc.setValue(load.getVariableString("merc"));
        dropPaysTrade.setValue(load.getVariableString("trade"));
        dropPaysSmith.setValue(load.getVariableString("smith"));
        dropPaysEnch.setValue(load.getVariableString("ench"));

    }

    public  static  boolean loaded = false;
    public static void draw(){
        if(!loaded) {
            loaded = true;
            File folder = new File("res/tex/chars");
            File[] folderEntries = folder.listFiles();
            String[] fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                fileNames[i] = name;
            }
            dropIngameTexture.setVals(fileNames);

             folder = new File("res/tex/interface/portrets");
             folderEntries = folder.listFiles();
             fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                fileNames[i] = name;
            }
            dropPortretTexture.setVals(fileNames);

            folder = new File("res/snd/playerVoice");
            folderEntries = folder.listFiles();
            fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName();
                fileNames[i] = name;
            }
            dropVoice.setVals(fileNames);

            dropLoot1.setVals(ItemDataBase.getSpellsId());
            dropLoot2.setVals(ItemDataBase.getSpellsId());
            dropLoot3.setVals(ItemDataBase.getSpellsId());

            dropPaysMerc.setVals(MercDataBase.getMercsID());
            dropPaysTrade.setVals(TradesDataBase.getTradeID());
            dropPaysSmith.setVals(TradesDataBase.getTradeID());
            dropPaysEnch.setVals(TradesDataBase.getTradeID());
        }
        if(drawMe) {

            Render2D.angleColorDraw("windowCreatorBack","interface/white", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2, 1000, 800, 0, 0, 0, 0, 1);
            Render2D.angleDraw("creatorBorder1","interface/Border", Render2D.getWindowWidth() / 2-500, Render2D.getWindowHeight() / 2, 800, 4, 90);
            Render2D.angleDraw("creatorBorder2","interface/Border", Render2D.getWindowWidth() / 2+500, Render2D.getWindowHeight() / 2, 800, 4, 90);
            Render2D.angleDraw("creatorBorder3","interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2-400, 1000, 4, 0);
            Render2D.angleDraw("creatorBorder4","interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2+400, 1000, 4, 0);
            for(int i = 0; i < names1.length;i++){
                Text.drawString(names1[i],Render2D.getWindowWidth() / 2-500+10,Render2D.getWindowHeight() / 2-400+50+i*30,Text.CAMBRIA_24, Color.white);
                elements1[i].work();
                elements1[i].draw();

            }
            for(int i = 0; i < names2.length;i++){
                elements2[i].work();
                elements2[i].draw();
                Text.drawString(names2[i],Render2D.getWindowWidth() / 2-0+10,Render2D.getWindowHeight() / 2-400+50+i*30,Text.CAMBRIA_24, Color.white);

            }

            OK.work();
            if(OK.isReleased()){
                createNPC();
                editorScreen.chosen = new NPC(textID.getText(),0,0);
                drawMe = false;
                editorScreen.mustRecreateShowItems = true;

            }
            OK.draw();
            Cancel.work();
            drawPlayer();
            if(Cancel.isReleased()){
                drawMe = false;
            }
            Cancel.draw();
        }

    }
    private static void createNPC(){
        HashMap<String,Object> data = new HashMap<>();

        data.put("portret",dropPortretTexture.getText());
        data.put("portretX",Integer.parseInt(textPortretX.getText()));
        data.put("portretY",Integer.parseInt(textPortretY.getText()));
        data.put("ingameTexture","chars/"+dropIngameTexture.getText());
        data.put("bloodTexture",dropBloodTexture.getVal());
        data.put("stepsTexture",dropStepsTexture.getText());
        data.put("maxHealth",(double)Integer.parseInt(textHealth.getText()));
        data.put("health",(double)Integer.parseInt(textHealth.getText()));
        data.put("stamina",(double)Integer.parseInt(textStamina.getText()));
        data.put("maxStamina",(double)Integer.parseInt(textStamina.getText()));
        data.put("speedRun",(double)(Integer.parseInt(textRunSpeed.getText()))/10);
        data.put("speedWalk",(double)(Integer.parseInt(textWalkSpeed.getText()))/10);
        data.put("speedCrouch",(double)(Integer.parseInt(textWalkSpeed.getText()))/10);
        data.put("exp",Integer.parseInt(textEXP.getText()));
        data.put("name",textName.getText());
        data.put("vision",Integer.parseInt(textVision.getText()));
        data.put("visionType", dropVisionType.getVal());
        data.put("lootGoldMin", Integer.parseInt(textGoldMin.getText()));
        data.put("lootGoldMax", Integer.parseInt(textGoldMax.getText()));
        data.put("loot1",dropLoot1.getText());
        data.put("loot2",dropLoot2.getText());
        data.put("loot3",dropLoot3.getText());

        data.put("damage",(double)Integer.parseInt(textDamage.getText()));
        data.put("damageType",damageClassesTranslate.get(dropDamageType.getText()));
        data.put("distant",Integer.parseInt(textDistant.getText())*32.0);
        data.put("attackSpeed",(double)Integer.parseInt(textAttackSpeed.getText())/100.0);

        data.put("skillRestoration",Integer.parseInt(textRecovery.getText()));

        data.put("armorPierce",Integer.parseInt(textArmorPierce.getText()));
        data.put("armorSlash",Integer.parseInt(textArmorSlash.getText()));
        data.put("armorStrike",Integer.parseInt(textArmorStrike.getText()));
        data.put("armorFire",Integer.parseInt(textArmorFire.getText()));
        data.put("armorFreeze",Integer.parseInt(textArmorFreeze.getText()));
        data.put("armorElectro",Integer.parseInt(textArmorElectro.getText()));
        data.put("armorAcid",Integer.parseInt(textArmorAcid.getText()));
        data.put("armorMental",Integer.parseInt(textArmorMental.getText()));
        data.put("armorWizardry",Integer.parseInt(textArmorWizardry.getText()));
        data.put("armorSacred",Integer.parseInt(textArmorSacred.getText()));

        data.put("good",dropResponse.getVal());
        data.put("skinType",dropSkin.getVal());
        data.put("nonResurrect",dropResurrect.getVal());
        data.put("voice",dropVoice.getText());

        data.put("merc",dropPaysMerc.getText());
        data.put("trade",dropPaysTrade.getText());
        data.put("smith",dropPaysSmith.getText());
        data.put("ench",dropPaysEnch.getText());

        data.put("NPCClassName","Персонаж");

        String ID = textID.getText();
        data.put("ID",ID);
        NPCDataBase.addData(ID,data);
    }
    private static void drawPlayer(){
        Render2D.simpleDraw("ingame","chars/"+dropIngameTexture.getText(), Display.getWidth()/2+200,Display.getHeight()/2+300,128,128);

        int partX = 0;
        int partY = 0;
        try {
            partX = Integer.parseInt(textPortretX.getText());
            partY = Integer.parseInt(textPortretY.getText());

        } catch (NumberFormatException ex){

        }
        double cutLeft = partX/4.0;
        double cutRight = 1-(partX+1)/4.0;
        double cutUp = partY/4.0;
        double cutDown = 1-(partY+1)/4.0;
        Render2D.angleCutDraw("ingame1","interface/portrets/"+dropPortretTexture.getText(), Display.getWidth()/2+400,Display.getHeight()/2+300,128,128,0,cutLeft,cutRight,cutUp,cutDown);

    }
}
