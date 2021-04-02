package screens.editor.creationWindows;

import engine.Actor;
import engine.Mathp;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Droplist;
import engine.control.interfaces.Element;
import engine.control.interfaces.Textbox;
import engine.render.Render2D;
import engine.render.Text;
import game.actor.magic.Spell;
import game.actor.actorsHyper.SpellDataBase;
import org.newdawn.slick.Color;
import screens.editor.mainScreen.editorScreen;

import java.io.File;
import java.util.HashMap;

import static game.actor.enviroment.Item.ITEMS_IN_LINE;

public class createWindowMagic {
    private static final int FOLDERS_IN_SPELL = 2;
    private static String[] itemClasses = {"Магия огня","Магия воды","Магия воздуха","Магия земли","Псионика","Волшебство","Чары"};
    private static Droplist choosenItemClass = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+12,256,30,"interface/drop",0,itemClasses, Text.CAMBRIA_14,4);

    static Button OK = new Button(Render2D.getWindowWidth() / 2+250-16-32-4, Render2D.getWindowHeight() / 2+400-16-4,32,32,"interface/butOK");
    static Button Cancel = new Button(Render2D.getWindowWidth() / 2+250-16-4, Render2D.getWindowHeight() / 2+400-16-4,32,32,"interface/butCancel");

    private static String[] farweaponClassesSkills = {"magicFire","magicWater","magicAir","magicEarth","magicMind","magicWizardry","magicSacred"};
    private static HashMap<String, String> farweaponClassesTranslate = new HashMap<>();

    private static String[] targetTypes = {"Без цели","Одноцелевое","Область"};


    static {

        for(int i = 0; i < itemClasses.length;i++){
            farweaponClassesTranslate.put(itemClasses[i],farweaponClassesSkills[i]);
        }
    }


    private static String[] spellInfoRus = {"ID","Имя","Магический эффект","Сила эффекта","Время эффекта","Сложность","Запас сил",
    "Дистанция","Цели","Тип цели","Радиус","Скорость снаряда","Скорость каста","Размер снаряда (Х:У)","Кадры (снар.:взрыв)","Звук (снар.:взрыв)","","",""};


    private static Textbox textID = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+60,256,30,"interface/text","ИД предмета",Text.CAMBRIA_14,4);
    private static Textbox textBoxName = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+90,256,30,"interface/text","название предмета",Text.CAMBRIA_14,4);
    private static Droplist dropMagicEffect = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+120,256,30,"interface/drop",0, SpellDataBase.effectNames, Text.CAMBRIA_14,4);
    private static Textbox textBoxPower = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+150,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textBoxTime = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+180,256,30,"interface/text",Text.CAMBRIA_14,4,-1000000,1000000);
    private static Textbox textBoxDifficult = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+210,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000000);
    private static Textbox textBoxManaCost = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+240,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000000);
    private static Textbox textBoxDistant = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+270,256,30,"interface/text",Text.CAMBRIA_14,4,0,100);
    private static Textbox textBoxTargets = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+300,256,30,"interface/text",Text.CAMBRIA_14,4,0,10);
    private static Droplist dropTypeTarget = new Droplist(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+330,256,30,"interface/drop",0, targetTypes, Text.CAMBRIA_14,4);
    private static Textbox textBoxRadius = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+360,256,30,"interface/text",Text.CAMBRIA_14,4,1,100);
    private static Textbox textBoxSpeed = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+390,256,30,"interface/text",Text.CAMBRIA_14,4,0,1000);
    private static Textbox textBoxCastSpeed = new Textbox(Render2D.getWindowWidth() / 2+115,Render2D.getWindowHeight() / 2-400+420,256,30,"interface/text",Text.CAMBRIA_14,4,0,100);
    private static Textbox textBoxSizeX = new Textbox(Render2D.getWindowWidth() / 2+115-64,Render2D.getWindowHeight() / 2-400+450,128,30,"interface/text",Text.CAMBRIA_14,4,1,128);
    private static Textbox textBoxSizeY = new Textbox(Render2D.getWindowWidth() / 2+115+64,Render2D.getWindowHeight() / 2-400+450,128,30,"interface/text",Text.CAMBRIA_14,4,1,128);
    private static Textbox textBoxFramesProjectile = new Textbox(Render2D.getWindowWidth() / 2+115-64,Render2D.getWindowHeight() / 2-400+480,128,30,"interface/text",Text.CAMBRIA_14,4,1,128);
    private static Textbox textBoxFramesExplode = new Textbox(Render2D.getWindowWidth() / 2+115+64,Render2D.getWindowHeight() / 2-400+480,128,30,"interface/text",Text.CAMBRIA_14,4,1,128);
    private static Droplist dropExplodeSound = new Droplist(Render2D.getWindowWidth() / 2+115+64,Render2D.getWindowHeight() / 2-400+510,128,30,"interface/drop",0, targetTypes, Text.CAMBRIA_14,4);
    private static Droplist dropProjSound = new Droplist(Render2D.getWindowWidth() / 2+115-64,Render2D.getWindowHeight() / 2-400+510,128,30,"interface/drop",0, targetTypes, Text.CAMBRIA_14,4);


    private static Element[] spellInfoInputs = {textID,textBoxName,dropMagicEffect,textBoxPower,textBoxTime,textBoxDifficult,textBoxManaCost,
            textBoxDistant,textBoxTargets,dropTypeTarget,textBoxRadius,textBoxSpeed,textBoxCastSpeed,textBoxSizeX,textBoxSizeY,textBoxFramesProjectile,textBoxFramesExplode,dropProjSound,dropExplodeSound};



    private static Droplist droptextures = new Droplist(Render2D.getWindowWidth() / 2+130,Render2D.getWindowHeight() / 2-400+100+30*15,236,30,"interface/drop",0,spellInfoRus,Text.CAMBRIA_14,4);


    private static String itemFolder = "spells/";
    private static String itemFile = "fire";
    private static int choosenX = 0;
    private static int choosenY = 0;
    public static boolean drawMe = false;
    public static void loadData(Actor load){
        drawMe = true;
        textBoxName.setText(load.getVariable("name")+"");
        textID.setText(load.getVariable("ID")+"");
        itemFile = load.getVariable("texture")+"";
        choosenX = load.getVariableInteger("partX");
        choosenY = load.getVariableInteger("partY");
        droptextures.setValue(load.getVariable("texture")+"");
        choosenItemClass.setValue(load.getVariable("typeName")+"");
        choosenItemClass.setValue(load.getVariable("spellClassName")+"");
        dropMagicEffect.setPos(load.getVariableInteger("effect"));
        textBoxPower.setText(load.getVariableInteger("power")+"");
        textBoxTime.setText(load.getVariableInteger("time")+"");
        textBoxDifficult.setText(load.getVariableInteger("difficult")+"");
        textBoxManaCost.setText(load.getVariableInteger("mana")+"");
        textBoxTargets.setText(load.getVariableInteger("targets")+"");
        textBoxDistant.setText(load.getVariableTrunked("distant")/32+"");
        dropTypeTarget.setPos(load.getVariableInteger("targetType"));
        textBoxRadius.setText(load.getVariableInteger("projectileExplodeRadius")/32+"");
        textBoxSizeX.setText(load.getVariableInteger("projectileSizeX")+"");
        textBoxSizeY.setText(load.getVariableInteger("projectileSizeY")+"");
        textBoxFramesProjectile.setText(load.getVariableInteger("projectileFrames")+"");
        textBoxFramesExplode.setText(load.getVariableInteger("projectileExplodeFrames")+"");

        droptexturesCast.setValue(load.getVariable("castAnim")+"");
        droptexturesExplode.setValue(load.getVariable("projectileExplode")+"");
        droptexturesProjectile.setValue(load.getVariable("projectileTexture")+"");

        textBoxSpeed.setText(load.getVariableTrunked("projectileSpeed")+"");
        textBoxCastSpeed.setText(load.getVariableTrunked("attackSpeed")*100+"");

        dropExplodeSound.setValue(load.getVariable("soundExplode")+"");
        dropProjSound.setValue(load.getVariable("soundProjectile")+"");

    }


    private static Droplist droptexturesProjectile = new Droplist(Render2D.getWindowWidth() / 2+130,Render2D.getWindowHeight() / 2-400+100+30*15+30,236,30,"interface/drop",0,spellInfoRus,Text.CAMBRIA_14,4);
    private static Droplist droptexturesExplode = new Droplist(Render2D.getWindowWidth() / 2+130,Render2D.getWindowHeight() / 2-400+100+30*15+60,236,30,"interface/drop",0,spellInfoRus,Text.CAMBRIA_14,4);
    private static Droplist droptexturesCast = new Droplist(Render2D.getWindowWidth() / 2+130,Render2D.getWindowHeight() / 2-400+100+30*15+90,236,30,"interface/drop",0,spellInfoRus,Text.CAMBRIA_14,4);

    private static int currentItemType = 0;
    public  static  boolean loaded = false;
    public static void draw(){
        if(!loaded) {
            File folder = new File("res/tex/spells");
            File[] folderEntries = folder.listFiles();
            String[] fileNames = new String[folderEntries.length-FOLDERS_IN_SPELL];
            int drops = 0;
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName();
                if(name.contains(".")) {
                    name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                    fileNames[i-drops] = name;
                } else {
                    drops++;
                }
            }
            droptextures.setVals(fileNames);

            folder = new File("res/tex/spells/projectiles");
            folderEntries = folder.listFiles();
            fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                fileNames[i] = name;
            }
            droptexturesProjectile.setVals(fileNames);
            droptexturesExplode.setVals(fileNames);

            folder = new File("res/tex/spells/casts");
            folderEntries = folder.listFiles();
            fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                fileNames[i] = name;
            }
            droptexturesCast.setVals(fileNames);

            folder = new File("res/snd/spells/projectiles");
            folderEntries = folder.listFiles();
            fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                fileNames[i] = name;
            }
            dropProjSound.setVals(fileNames);
            folder = new File("res/snd/spells/explodes");
            folderEntries = folder.listFiles();
            fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName().substring(0, folderEntries[i].getName().length() - 4);
                fileNames[i] = name;
            }
            dropExplodeSound.setVals(fileNames);

            loaded = true;
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
            for(int i = 0; i < spellInfoRus.length;i++){
                Text.drawString(spellInfoRus[i],Render2D.getWindowWidth() / 2-250+10,Render2D.getWindowHeight() / 2-400+50+i*30,Text.CAMBRIA_24, Color.white);
                spellInfoInputs[i].work();
                spellInfoInputs[i].draw();
            }
            Render2D.simpleDraw("creatorTileSet",itemFolder+itemFile, Render2D.getWindowWidth() / 2-120, Render2D.getWindowHeight() / 2+270, 256, 256);
            Render2D.simpleDraw("creatorSelector","editor/selector", 8 + choosenX * 16+(Render2D.getWindowWidth() / 2-248), 8 + choosenY * 16+(Render2D.getWindowHeight() / 2+270-128), 16, 16);
            draw(Render2D.getWindowWidth() / 2-120+150, Render2D.getWindowHeight() / 2+290,32);
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
            droptexturesProjectile.draw();
            droptexturesProjectile.work();
            droptexturesExplode.draw();
            droptexturesExplode.work();
            droptexturesCast.draw();
            droptexturesCast.work();
            itemFile = droptextures.getText();

            OK.work();
            if(OK.isReleased()){
                createWeapon();
                editorScreen.chosen = new Spell(textID.getText());
                drawMe = false;
                editorScreen.mustRecreateShowItems = true;

            }
            OK.draw();
            Cancel.work();
            if(Cancel.isReleased()){
                drawMe = false;
            }
            Cancel.draw();
        }

    }
    private static void createWeapon(){
        HashMap<String,Object> weapon1 = new HashMap<>();
        weapon1.put("name",textBoxName.getText());
        weapon1.put("typeName","Заклинание");
        weapon1.put("type",4);
        weapon1.put("spellClass",farweaponClassesTranslate.get(choosenItemClass.getText()));
        weapon1.put("spellClassName",choosenItemClass.getText());
        weapon1.put("effect",dropMagicEffect.getVal());
        weapon1.put("power",Integer.parseInt(textBoxPower.getText()));
        weapon1.put("time",Integer.parseInt(textBoxTime.getText()));
        weapon1.put("difficult",Integer.parseInt(textBoxDifficult.getText()));
        weapon1.put("mana",Integer.parseInt(textBoxManaCost.getText()));
        weapon1.put("distant",Integer.parseInt(textBoxDistant.getText())*32.0);
        weapon1.put("targets",Integer.parseInt(textBoxTargets.getText()));
        weapon1.put("targetType",dropTypeTarget.getVal());
        weapon1.put("projectileExplodeRadius",Integer.parseInt(textBoxRadius.getText())*32);
        weapon1.put("projectileSpeed",(double)Integer.parseInt(textBoxSpeed.getText()));
        weapon1.put("attackSpeed",(double)(Integer.parseInt(textBoxCastSpeed.getText()))/100.0);
        weapon1.put("projectileSizeX",Integer.parseInt(textBoxSizeX.getText()));
        weapon1.put("projectileSizeY",Integer.parseInt(textBoxSizeY.getText()));
        weapon1.put("projectileFrames",Integer.parseInt(textBoxFramesProjectile.getText()));
        weapon1.put("projectileExplodeFrames",Integer.parseInt(textBoxFramesExplode.getText()));
        weapon1.put("projectileTexture",droptexturesProjectile.getText());
        weapon1.put("projectileExplode",droptexturesExplode.getText());
        weapon1.put("castAnim",droptexturesCast.getText());
        weapon1.put("texture",itemFile);
        weapon1.put("partX",choosenX);
        weapon1.put("partY",choosenY);
        weapon1.put("filter",choosenItemClass.getVal()+1);
        weapon1.put("soundExplode",dropExplodeSound.getText());
        weapon1.put("soundProjectile",dropProjSound.getText());

        String ID = textID.getText();
        weapon1.put("ID",ID);
        SpellDataBase.addData(ID,weapon1);
    }

    public static void draw(double lx, double ly,double s) {
        progress += 0.01;
        if(progress >1) progress = 0;
        double[] part = {choosenX,choosenY};
        String texture = itemFolder+itemFile;
        double cutLeft = part[0]/ITEMS_IN_LINE;
        double cutRight = 1-(part[0]+1)/ITEMS_IN_LINE;
        double cutUp = part[1]/ITEMS_IN_LINE;
        double cutDown = 1-(part[1]+1)/ITEMS_IN_LINE;
        Render2D.angleCutDraw("creatorItemView",texture,lx,ly,s,s,0,cutLeft,cutRight,cutUp,cutDown);


        double[] size = {0,0};
        double frames = 1;
        double framesExplode = 1;
        try {
            size = new double[]{Integer.parseInt(textBoxSizeX.getText()),Integer.parseInt(textBoxSizeY.getText())};
            frames =  Integer.parseInt(textBoxFramesProjectile.getText());
            framesExplode =  Integer.parseInt(textBoxFramesExplode.getText());

        } catch (NumberFormatException ex){

        }

        int curFrame = (int)(progress*frames);
        texture = "spells/projectiles/"+droptexturesProjectile.getText();
        double locX = lx+16+size[0]/2;
        double locY = ly;
        cutLeft = curFrame/frames;
        cutRight = 1-(curFrame+1)/frames;
        cutUp = 0;
        cutDown = 0;
        Render2D.angleCutDraw("creatorProjectileView",texture,locX,locY,size[0],size[1],0,cutLeft,cutRight,cutUp,cutDown);


        curFrame = (int)(progress*framesExplode);
        texture = "spells/projectiles/"+droptexturesExplode.getText();
        locX = lx+32;
        locY = ly+16+40;
        cutLeft = curFrame/framesExplode;
        cutRight = 1-(curFrame+1)/framesExplode;
        cutUp = 0;
        cutDown = 0;
        Render2D.angleCutDraw("creatorProjectileView",texture,locX,locY,64,64,0,cutLeft,cutRight,cutUp,cutDown);

        curFrame = (int)(progress*8.0*5);
        texture = "spells/casts/"+droptexturesCast.getText();
        locX = lx+32+64;
        locY = ly+16+40;
        cutLeft = curFrame/8.0;
        cutRight = 1-(curFrame+1)/8.0;
        cutUp = 0;
        cutDown = 0;
        Render2D.angleCutDraw("creatorProjectileView",texture,locX,locY,64,64,0,cutLeft,cutRight,cutUp,cutDown);
    }
    private static double progress = 0.0;
}
