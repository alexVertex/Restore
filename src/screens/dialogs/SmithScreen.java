package screens.dialogs;

import de.gurkenlabs.litiengine.entities.Material;
import engine.Mathp;
import engine.Start;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Scroll;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.actorsHyper.ItemDataBase;
import game.actor.actorsHyper.SmithDataBase;
import game.actor.actorsHyper.TradesDataBase;
import game.actor.enviroment.Item;
import game.actor.player.Player;
import org.newdawn.slick.Color;
import screens.Controls;
import screens.ingame.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmithScreen extends GameScreen {
    private static final int ROW_LEN = 4;
    static float wide = 900;
    static float high = 516;
    private int choosenFilterTrader = 0;
    private int choosenFilterPlayer = 0;
    private List<Item> showListTrader = new ArrayList<>();
    private List<Item> inventoryTrader = new ArrayList<>();
    private List<Item> showListPlayer = new ArrayList<>();

    private List<Item> materialList = new ArrayList<>();

    private Button ok = new Button(Render2D.getWindowWidth()/2-100,Render2D.getWindowHeight()/2+high/2-20,32,32,"interface/butOK","interface/ok");
    private Button cancel = new Button(Render2D.getWindowWidth()/2+100,Render2D.getWindowHeight()/2+high/2-20,32,32,"interface/butCancel","interface/cancel");

    private Item underCursor;
    private Item blueprintIn ;
    private Item finalItem;

    public SmithScreen(String tradesID){
        HashMap<String, Object> data = TradesDataBase.getData(tradesID);
        for(String el : data.keySet()){
            if(el.equals("ID")) continue;
            String[] dataItem = ((String)data.get(el)).split(":");
            for(int i = 0; i < Integer.parseInt(dataItem[1]);i++){
                Item item = new Item( "",  0,  0,  0,  0,dataItem[0],1);
                inventoryTrader.add(item);
            }
            if(Integer.parseInt(dataItem[1]) == -1){
                Item item = new Item( "",  0,  0,  0,  0,dataItem[0],-1);
                inventoryTrader.add(item);
            }
        }
    }

    @Override
    public void input() {
        if(InputMain.isKeyJustReleased(Controls.openESCMenu)){
            backMatrial();
            Start.setScreen(new GameScreen());
        }
        if(InputMain.isKeyJustReleased(InputMain.LMB)) {
            for (int i = 0; i < 11; i++) {
                double locX = Render2D.getWindowWidth()/2-wide/2 + 14 + 26 * i;
                double locY = Render2D.getWindowHeight() / 2 + 210;
                if (Math.abs(locX - InputMain.getCursorX()) <= 13 && Math.abs(locY - InputMain.getCursorY()) <= 16) {
                    choosenFilterTrader = i;
                    AudioManager.playSoundInterface("interface/Ok");

                    break;
                }
                locX = Render2D.getWindowWidth()/2+wide/2-288 + 14 + 26 * i;
                locY = Render2D.getWindowHeight() / 2 + 210;
                if (Math.abs(locX - InputMain.getCursorX()) <= 13 && Math.abs(locY - InputMain.getCursorY()) <= 16) {
                    choosenFilterPlayer = i;
                    AudioManager.playSoundInterface("interface/Ok");
                    break;
                }
            }
        }
    }


    @Override
    public void logic() {
        List<Item> filterList = new ArrayList<>();
        if(choosenFilterTrader == 0){
            filterList.addAll(inventoryTrader);
        } else {
            for(Item el : inventoryTrader){
                if(el.getVariable("filter").equals(choosenFilterTrader)){
                    filterList.add(el);
                }
            }
        }
        int lines = (int)Math.ceil((double) filterList.size()/ROW_LEN);
        lines-=7;
        if(lines<0) lines = 0;
        tradeScroll.setMaxPosition(lines);
        showListTrader.clear();
        for(int i = tradeScroll.getPosition()*ROW_LEN; i < filterList.size();i++){
            Item el = filterList.get(i);
            showListTrader.add(el);
        }

        filterList = new ArrayList<>();
        if(choosenFilterPlayer == 0){
            filterList.addAll(StartGame.game.inventory);
        } else {
            for(Item el : StartGame.game.inventory){
                if(el.getVariable("filter").equals(choosenFilterPlayer)){
                    filterList.add(el);
                }
            }
        }
        lines = (int)Math.ceil((double) filterList.size()/ROW_LEN);
        lines-=7;
        if(lines<0) lines = 0;
        invScroll.setMaxPosition(lines);
        showListPlayer.clear();
        for(int i = invScroll.getPosition()*ROW_LEN; i < filterList.size();i++){
            Item el = filterList.get(i);
            showListPlayer.add(el);
        }

    }



    private Scroll tradeScroll = new Scroll(Render2D.getWindowWidth()/2-wide/2+256+16,Render2D.getWindowHeight()/2-32,32,448,"interface/scroller");
    private Scroll invScroll = new Scroll(Render2D.getWindowWidth()/2+wide/2-16,Render2D.getWindowHeight()/2-32,32,448,"interface/scroller");

    @Override
    public void render() {
        super.render();

        Render2D.angleColorDraw("campBack","interface/white", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2,Render2D.getWindowWidth(),Render2D.getWindowHeight(),0,0,0,0,0.75);
        Render2D.angleDraw("campBorder1","interface/borderBack", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2-high/2,wide,4,0);
        Render2D.angleDraw("campBorder2","interface/borderBack", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2+high/2,wide,4,0);
        Render2D.angleDraw("campBorder3","interface/borderBack", Render2D.getWindowWidth()/2+wide/2,Render2D.getWindowHeight()/2,high,4,90);
        Render2D.angleDraw("campBorder4","interface/borderBack", Render2D.getWindowWidth()/2-wide/2,Render2D.getWindowHeight()/2,high,4,90);

        Render2D.angleDraw("trader","interface/InvItems", Render2D.getWindowWidth()/2-wide/2+128,Render2D.getWindowHeight()/2,256,512,0);
        Render2D.angleDraw("playerItems","interface/InvItems", Render2D.getWindowWidth()/2+wide/2-128-32,Render2D.getWindowHeight()/2,256,512,0);
        Render2D.angleColorDraw("tradeTable","interface/smith", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2,256,512,0,0.5,0.5,0.5,1);

        tradeScroll.work(); tradeScroll.draw();
        invScroll.work(); invScroll.draw();

        Render2D.simpleDraw("filters","interface/invFilters",Render2D.getWindowWidth()/2-wide/2+144,Render2D.getWindowHeight()/2+210,288,32);
        Render2D.simpleDraw("filterChoosen","interface/invFiltersChosen",Render2D.getWindowWidth()/2-wide/2+14+26*choosenFilterTrader,Render2D.getWindowHeight()/2+210,32,32);


        Render2D.simpleDraw("filters","interface/invFilters",Render2D.getWindowWidth()/2+wide/2-144,Render2D.getWindowHeight()/2+210,288,32);
        Render2D.simpleDraw("filterChoosen","interface/invFiltersChosen",Render2D.getWindowWidth()/2+wide/2-288+14+26*choosenFilterPlayer,Render2D.getWindowHeight()/2+210,32,32);

        underCursor = null;
        for(int i = 0; i < showListTrader.size() && i <7*ROW_LEN;i++){
            Item el =  showListTrader.get(i);
            int locX = (int)Render2D.getWindowWidth()/2 + (i % ROW_LEN)*64 + +32-(int) wide/2;
            int locY = (int)Render2D.getWindowHeight()/2+ (i / ROW_LEN)*64 - 95-128;
            drawItem("inventoryItema"+i, el, locX, locY);
        }

        for(int i = 0; i < showListPlayer.size() && i <7*ROW_LEN;i++){
            Item el =  showListPlayer.get(i);
            int locX = (int)Render2D.getWindowWidth()/2 + (i % ROW_LEN)*64 + +32+(int) wide/2-64*4-32;
            int locY = (int)Render2D.getWindowHeight()/2+ (i / ROW_LEN)*64 - 95-128;
            drawItem("inventoryItemb"+i, el, locX, locY);
        }

        for(int i = 0; i < materialList.size() && i <7*ROW_LEN;i++){
            Item el =  materialList.get(i);
            int locX = (int)Render2D.getWindowWidth()/2 + (i % ROW_LEN)*64 + +32+(int) -64*2;
            int locY = (int)Render2D.getWindowHeight()/2+ (i / ROW_LEN)*64 - 95-128+64*3;
            drawItem("inventoryItemb"+i, el, locX, locY);
        }

        if(blueprintIn != null){
            int locX = (int)Render2D.getWindowWidth()/2 + (4 % 4)*64 -128+32;
            int locY = (int)Render2D.getWindowHeight()/2+ (4 / 4)*64 - 95-128;
            drawItem("inventoryItemd", blueprintIn, locX, locY);
        }
        if(finalItem != null){
            int locX = (int)Render2D.getWindowWidth()/2 + (7 % 4)*64 -128+32;
            int locY = (int)Render2D.getWindowHeight()/2+ (7 / 4)*64 - 95-128;
            drawItem("inventoryItemq", finalItem, locX, locY);
        }

        if(InputMain.isKeyJustReleased(InputMain.LMB)) {
            if (underCursor != null) {
                if (underCursor.equals(blueprintIn)) {
                    blueprintIn = null;
                    backMatrial();
                    AudioManager.playSoundInterface("interface/CampInventoryUndress");
                } else if (underCursor.getVariableInteger("type") == ItemDataBase.TYPE_BLUEPRINTS) {
                    blueprintIn = underCursor;
                    backMatrial();
                    AudioManager.playSoundInterface("interface/CampInventoryDress");

                } else if (blueprintIn != null) {
                    int needMaterialCount = blueprintIn.getVariableInteger("materialCount");
                    int needMaterialClass = blueprintIn.getVariableInteger("materialClass");
                    if (underCursor.getVariableInteger("type") == ItemDataBase.TYPE_MATERIALS) {
                        if(Math.abs(InputMain.getCursorX()-Render2D.getWindowWidth()/2) > 140) {
                            AudioManager.playSoundInterface("interface/CampInventoryDress");

                            if (needMaterialCount == materialList.size()) {

                            } else {
                                int currentaterialClass = underCursor.getVariableInteger("materialClass");
                                if (materialList.size() == 0) {
                                    if (currentaterialClass == needMaterialClass) {
                                        addMatrial(needMaterialCount);
                                    }
                                } else {
                                    String materialInID = materialList.get(0).getVariableString("ID");
                                    String materialUndercursor = underCursor.getVariableString("ID");
                                    if (materialInID.equals(materialUndercursor)) {
                                        addMatrial(needMaterialCount);
                                    }
                                }
                            }
                        } else {
                            backMatrial();
                            AudioManager.playSoundInterface("interface/CampInventoryUndress");

                        }
                    }
                }
            }
        }

        ok.work(); ok.draw();
        if(ok.isReleased()){
            smithItem();
        }
        cancel.work(); cancel.draw();
        if(cancel.isReleased()){
            backMatrial();
            blueprintIn = null;
        }
        Text.drawString("????????????: " + StartGame.game.gold,Render2D.getWindowWidth()/2+170,Render2D.getWindowHeight()/2+high/2-25,Text.CAMBRIA_14, Color.white);

        Text.drawStringCenter("???????? ????????????: " + tradeCost,Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2+high/2-60,Text.CAMBRIA_14, Color.white);
        if(underCursor != null){
            underCursor.drawInfo(false);
        }
        Render2D.simpleDraw("cursor", "cursors/normal", InputMain.getCursorX() + 16, InputMain.getCursorY() + 16, 32, 32);

    }

    private void smithItem() {
        if(-tradeCost <= StartGame.game.gold && finalItem != null){
            StartGame.game.addItem(finalItem);
            finalItem = null;
            blueprintIn = null;
            materialList.clear();
            StartGame.game.gold += tradeCost;
            tradeCost = 0;
        }
    }

    int tradeCost = 0;

    private void addMatrial(int needMaterial){
        materialList.add(underCursor);
        if (InputMain.getCursorX() < Render2D.getWindowWidth() / 2) {
            underCursor.setVariable("owner", "trader");
            if(underCursor.getVariableInteger("count") == -1){

            } else {
                inventoryTrader.remove(underCursor);
            }
        } else {
            underCursor.setVariable("owner", "player");
            StartGame.game.removeItem(underCursor);
        }
        if(needMaterial == materialList.size()){
            String getFinalItemID = SmithDataBase.getCraftedID(blueprintIn.getVariableString("ID"),materialList.get(0).getVariableString("ID"));
            finalItem = new Item("",0,0,0,0,getFinalItemID,1);
            for(Item el : materialList){
                if(el.getVariableString("owner").equals("trader")){
                    tradeCost -= el.getVariableInteger("cost");
                }
            }
            tradeCost -= finalItem.getVariableInteger("cost");
        }
    }

    private void backMatrial(){
        for(Item el : materialList){
            if(el.getVariableString("owner").equals("trader")){
                if(el.getVariableInteger("count") > -1) {
                    inventoryTrader.add(el);
                }
            } else {
                StartGame.game.addItem(el);
            }
        }
        finalItem = null;
        materialList.clear();
        tradeCost = 0;
    }

    private void drawItem(String nameOfSprite, Item el, int locX, int locY){
        double[] part = el.getTextureLocation();
        String texture = el.getTexture();
        double cutLeft = part[0]/ Item.ITEMS_IN_LINE;
        double cutRight = 1-(part[0]+1)/Item.ITEMS_IN_LINE;
        double cutUp = part[1]/Item.ITEMS_IN_LINE;
        double cutDown = 1-(part[1]+1)/Item.ITEMS_IN_LINE;
        Render2D.angleCutDraw(nameOfSprite,texture,locX,locY,48,48,0,cutLeft,cutRight,cutUp,cutDown);
        if(Mathp.inRectangle(locX-24,locY-24,locX+24,locY+24,InputMain.getCursorX(),InputMain.getCursorY())){
            underCursor = el;
        }
    }
}
