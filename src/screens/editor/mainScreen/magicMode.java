package screens.editor.mainScreen;

import engine.Actor;
import engine.Mathp;
import engine.control.InputMain;
import engine.control.interfaces.Droplist;
import engine.render.Text;
import game.actor.actorsHyper.SpellDataBase;
import game.actor.magic.Spell;
import org.newdawn.slick.Color;
import screens.editor.creationWindows.createWindowMagic;

import java.util.Comparator;

public class magicMode {
    static String[] filtersMag = {"Магия огня","Магия воды","Магия воздуха","Магия земли","Псионика","Волшебство","Чары"};
    static Droplist filterMag = new Droplist(128,20+32,256,32,"interface/drop",0,filtersMag, Text.CAMBRIA_14,4);
    public static void control() {
        filterMag.work();
        if(filterMag.isReleased()){
            editorScreen.mustRecreateShowItems = true;
        }
        editorScreen.addItem.work();
        if(editorScreen.addItem.isReleased()){
            createWindowMagic.drawMe = !createWindowMagic.drawMe;
        }
        editorScreen.undercursor = null;
        for (int i=0; i < editorScreen.showItems.size();i++){
            Actor el = editorScreen.showItems.get(i);
            int x = i % 7;
            int y = i / 7;
            if(Mathp.inRectangle(0+32*x,130-16+y*32,32+32*x,130+16+y*32, InputMain.getCursorX(),InputMain.getCursorY())){
                editorScreen.undercursor = el;
                break;
            }
        }
        if(editorScreen.chosen != null){
            if(Mathp.inRectangle(0,120-48,32,120-16,InputMain.getCursorX(),InputMain.getCursorY())){
                editorScreen.undercursor = editorScreen.chosen;
            }
        }
        if(InputMain.isKeyJustReleased(InputMain.LMB)) {
            if (editorScreen.undercursor != null) {
                editorScreen.chosen = editorScreen.undercursor;
            }
        }
        if(InputMain.isKeyJustReleased(InputMain.RMB)) {
            if (editorScreen.undercursor != null) {
                createWindowMagic.loadData(editorScreen.undercursor);
            }
        }
    }

    public static void logic() {
        editorScreen.showItems.clear();
        for (String id : SpellDataBase.dataBase.keySet()) {
            Spell item = new Spell(id);
            if (item.getVariable("spellClassName").equals(filterMag.getText())) {
                editorScreen.showItems.add(item);
            }
        }
        editorScreen.showItems.sort(Comparator.comparing(o -> (o.getVariable("ID") + "")));
    }

    public static void draw() {
        editorScreen.addItem.draw();
        filterMag.draw();
        for (int i=0; i < editorScreen.showItems.size();i++){
            Actor el = editorScreen.showItems.get(i);
            int x = i % 7;
            int y = i / 7;
            el.draw(16+32*x,130+y*32,32);
        }
        if(editorScreen.undercursor != null && !createWindowMagic.drawMe){
            editorScreen.undercursor.drawInfo(InputMain.getCursorX() > 256);
        }
        if(editorScreen.chosen != null){
            (editorScreen.chosen).draw(16,120-28,32);
            Text.drawString(editorScreen.chosen.getVariable("ID")+"",36,120-40,Text.CAMBRIA_14, Color.white);
        }
        createWindowMagic.draw();
    }
}
