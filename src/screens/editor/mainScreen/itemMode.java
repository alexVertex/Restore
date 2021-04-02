package screens.editor.mainScreen;

import engine.Mathp;
import engine.control.InputMain;
import engine.control.interfaces.Droplist;
import engine.control.interfaces.Textbox;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.actorsHyper.ItemDataBase;
import game.actor.enviroment.Chest;
import game.actor.enviroment.Item;
import org.newdawn.slick.Color;
import screens.editor.creationWindows.createWindowItems;

import java.util.Comparator;
import java.util.List;

public class itemMode {


    static Textbox count = new Textbox(226,20+64,60,20,"interface/text", Text.CAMBRIA_14,2,1,99999);
    static String[] filters = {"Оружие","Броня","Щит","Дальнобойное оружие","Расходник","Ключевой предмет","Трофей","Чертёж","Материал","Руна"};
    static Droplist filter = new Droplist(128,20+32,256,32,"interface/drop",0,filters,Text.CAMBRIA_14,4);
    static Textbox goldIn = new Textbox(1600-30,10,60,20,"interface/text",Text.CAMBRIA_14,2,0,9999999);

    public static void control() {
        editorScreen.addItem.work();
        if(editorScreen.addItem.isReleased()){
            createWindowItems.drawMe = !createWindowItems.drawMe;
        }
        filter.work();
        if(filter.isReleased()){
            editorScreen.mustRecreateShowItems = true;
        }
        count.work();


        editorScreen.undercursor = null;
        for (int i=0; i < editorScreen.showItems.size();i++){
            Item el = (Item)editorScreen.showItems.get(i);
            int x = i % 7;
            int y = i / 7;
            if(Mathp.inRectangle(0+32*x,104+y*32,32+32*x,136+y*32, InputMain.getCursorX(),InputMain.getCursorY())){
                editorScreen.undercursor = el;
                break;
            }
        }
        for(Item el : StartGame.game.onMapItems){
            if(el.isUndercursor(InputMain.getCursorX(),InputMain.getCursorY())){
                editorScreen.undercursor = el;
                break;
            }
        }
        if(editorScreen.chestContentMode) {
            List<Item> content = (List<Item>)editorScreen.workWithActor.getVariable("content");
            int i = 0;
            for (Item el : content) {
                if(Mathp.inRectangle(Render2D.getWindowWidth()-256,40+i*32-16,Render2D.getWindowWidth(),40+i*32+16,InputMain.getCursorX(),InputMain.getCursorY())){
                    editorScreen.undercursor = el;
                    break;
                }
                i++;
            }
        }
        if(editorScreen.chosen != null){
            if(Mathp.inRectangle(0,120-48,32,120-16,InputMain.getCursorX(),InputMain.getCursorY())){
                editorScreen.undercursor = editorScreen.chosen;
            }
        }
        if(InputMain.isKeyJustReleased(InputMain.LMB)){
            if(editorScreen.undercursor != null){
                editorScreen.chosen = editorScreen.undercursor;
            }
            if(InputMain.getCursorX() > 256) {
                if(editorScreen.chestContentMode){
                    if(editorScreen.chosen != null) {
                        List<Item> content = (List<Item>) editorScreen.workWithActor.getVariable("content");
                        int counts = Integer.parseInt(count.getText());
                        content.add(new Item("", 0, 0, 0, 0, editorScreen.chosen.getVariable("ID") + "", counts));
                    }
                } else {
                    if (editorScreen.chosen != null) {
                        int pointX = (InputMain.getCursorX() - Camera.locX + 16) / 32 * 32;
                        int pointY = (InputMain.getCursorY() - Camera.locY + 16) / 32 * 32;
                        int counts = Integer.parseInt(count.getText());
                        Item item = new Item("", 0, 0, pointX, pointY, editorScreen.chosen.getVariable("ID") + "", counts);
                        StartGame.game.addItemInGame(item);
                    }
                }
            }
        }
        if(InputMain.isKeyJustReleased(InputMain.RMB)){
            if(editorScreen.chestContentMode){
                if (editorScreen.undercursor != null) {
                    List<Item> content = (List<Item>) editorScreen.workWithActor.getVariable("content");
                    content.remove(editorScreen.undercursor);
                }
            } else {
                if (editorScreen.undercursor != null) {
                    if (InputMain.getCursorX() > 256) {
                        StartGame.game.onMapItems.remove(editorScreen.undercursor);
                    } else {
                        createWindowItems.loadData(editorScreen.undercursor);
                    }
                }
            }
        }
    }

    public static void logic() {
        editorScreen.showItems.clear();
        for (String id : ItemDataBase.dataBase.keySet()) {
            Item item = new Item("", 0, 0, 0, 0, id, 0);
            if (item.getVariable("typeName").equals(filter.getText())) {
                editorScreen.showItems.add(item);
            }
        }
        editorScreen.showItems.sort(Comparator.comparing(o -> (o.getVariable("ID") + "")));
    }

    public static void draw() {
        editorScreen.addItem.draw();
        count.draw();
        filter.draw();

        createWindowItems.draw();

        for (int i=0; i < editorScreen.showItems.size();i++){
            Item el = (Item)editorScreen.showItems.get(i);
            int x = i % 7;
            int y = i / 7;
            el.draw(16+32*x,120+y*32,32);
        }
        if(editorScreen.chestContentMode){
            Render2D.angleColorDraw("rightBackEditor","interface/white",Render2D.getWindowWidth()-128,Render2D.getWindowHeight()/2,256,Render2D.getWindowHeight(),0,0.15,0.15,0.15,1);
            Text.drawString("Уровень замка: " + editorScreen.workWithActor.getVariable("lockLevel") + ", золото: ",Render2D.getWindowWidth()-250,6,Text.CAMBRIA_14, Color.white);
            Chest work = (Chest) editorScreen.workWithActor;
            work.drawSelect();
            List<Item> content = (List<Item>)work.getVariable("content");
            for(int i = 0; i < content.size();i++ ){
                content.get(i).draw(Render2D.getWindowWidth()-240,40+i*32,32);
            }
            goldIn.work(); goldIn.draw();
            work.setVariable("goldIn",goldIn.getText());
        }

        if(editorScreen.undercursor != null && !createWindowItems.drawMe){
            (editorScreen.undercursor).drawInfo(InputMain.getCursorX() > 256 && !editorScreen.chestContentMode);
        }
        if(editorScreen.chosen != null){
            (editorScreen.chosen).draw(16,120-32,32);
            Text.drawString(editorScreen.chosen.getVariable("name")+"",36,120-40,Text.CAMBRIA_14,Color.white);
        }

    }
}
