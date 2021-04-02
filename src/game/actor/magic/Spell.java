package game.actor.magic;

import engine.Actor;
import engine.control.InputMain;
import engine.render.Render2D;
import engine.render.Text;
import game.actor.actorsHyper.SpellDataBase;
import org.newdawn.slick.Color;

import java.util.HashMap;

import static game.actor.enviroment.Item.ITEMS_IN_LINE;

public class Spell extends Actor {
    public Spell(String ID){
        if(ID == null){

        } else {
            HashMap<String, Object> itemVariables = SpellDataBase.getData(ID);

            for (String el : itemVariables.keySet()) {
                setVariable(el, itemVariables.get(el));
            }
        }
    }

    public Spell(Spell blueprintIn) {
        super();
        for (String el : blueprintIn.getVariables()) {
            setVariable(el, blueprintIn.getVariable(el));
        }
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
        double[] part = getTextureLocation();
        String texture = getTexture();
        double cutLeft = part[0]/ITEMS_IN_LINE;
        double cutRight = 1-(part[0]+1)/ITEMS_IN_LINE;
        double cutUp = part[1]/ITEMS_IN_LINE;
        double cutDown = 1-(part[1]+1)/ITEMS_IN_LINE;
        Render2D.angleCutDraw("item"+x+""+y+""+s,texture,x,y,s,s,0,cutLeft,cutRight,cutUp,cutDown);
    }

    public static String[] types = {"Без цели","Одноцелевое","Область","Конус"};

    public String[][] info(){
        int time = getVariableInteger("time")/100;
        String timeText = time != 0 ? Math.abs(time)+"" : "мгновенно";
        int radius = getVariableInteger("projectileExplodeRadius")/32;
        String radiusText = radius > 0 ? radius+"" : "-";
        int distant = getVariableTrunked("distant")/32;
        String distantText = distant > 0 ? distant+"" : "-";
        return new String[][]{
                {"Название:", getVariable("name") + ""},
                {"Школа:", getVariable("spellClassName") + ""},
                {"Сложность:", getVariable("difficult") + ""},
                {"Запас сил:", getVariable("mana") + ""},
                {"Эффект:", getVariable("power") + ""},
                {"Время:", timeText},
                {"Радиус:", radiusText},
                {"Дальность:", distantText},
                {"Цели:", getVariable("targets") + ""},
                {"Тип цели:", types[getVariableInteger("targetType")] + ""},
        };
    }

    @Override
    public void drawInfo(boolean onlyName) {
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

    @Override
    public boolean isUndercursor(int cursorX, int cursorY) {
        return false;
    }
    public String getTexture(){
        return  "spells/"+getVariable("texture");
    }
    public double[] getTextureLocation(){
        return new double[]{(int)getVariable("partX"),(int)getVariable("partY")};
    }
    public double[] getTileLoc(){
        return new double[]{(int)getVariable("locX"),(int)getVariable("locY")};
    }
}
