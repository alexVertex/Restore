package game.actor.story;

import engine.Actor;
import engine.audio3.AudioManager;
import engine.render.Render2D;
import engine.render.Text;
import org.newdawn.slick.Color;
import org.newdawn.slick.openal.Audio;

import java.util.ArrayList;
import java.util.List;

public class Event extends Actor {

    private static List<Event> eventRow = new ArrayList<>();
    private static int timeShow = 0;

    public static void eventQuestAdded(String taskName){
        Event event = new Event();
        event.setVariable("Text", "Получено задание: " + taskName);
        eventRow.add(event);
        AudioManager.playSoundInterface("interface/AddQuest");
    }
    public static void eventTaskAdded(String taskName){
        Event event = new Event();
        event.setVariable("Text", "Новая задача:  " + taskName);
        eventRow.add(event);
    }
    public static void eventTaskDone(String taskName){
        Event event = new Event();
        event.setVariable("Text", "Задача выполнена:  " + taskName);
        eventRow.add(event);
        AudioManager.playSoundInterface("interface/CompleteTask");

    }
    public static void eventQuestCompleted(String questName) {
        Event event = new Event();
        event.setVariable("Text", "Задание выполнено:  " + questName);
        AudioManager.playSoundInterface("interface/CompleteQuest");

        eventRow.add(event);
    }
    public static void eventPickup(String itemName,int count){
        Event event = new Event();
        if(count > 1){
            itemName += " (" + count + ")";
        }
        event.setVariable("Text", "Получен предмет: " + itemName);
        eventRow.add(event);
    }
    public static void eventGetSpell(String itemName){
        Event event = new Event();
        event.setVariable("Text", "Получено заклинание: " + itemName);
        eventRow.add(event);
    }
    public static void eventLose(String itemName,int count){
        Event event = new Event();
        if(count > 1){
            itemName += " (" + count + ")";
        }
        event.setVariable("Text", "Потерян предмет: " + itemName);
        eventRow.add(event);
    }

    public static void eventAddGold(int totalGold) {
        Event event = new Event();
        event.setVariable("Text", "Получено золото: " + totalGold);
        eventRow.add(event);
    }
    public static void eventRemoveGold(int totalGold) {
        Event event = new Event();
        event.setVariable("Text", "Потеряно золото: " + totalGold);
        eventRow.add(event);
    }


    public static void showEvents(){
        if(eventRow.size() > 0){
            for(int i = 0; i < 3 && i < eventRow.size();i++){
                Event cur = eventRow.get(i);
                cur.draw(i,0,0);
            }
            timeShow++;
            if(timeShow > 200){
                timeShow = 0;
                eventRow.remove(0);
            }
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
        Text.drawString(getVariable("Text")+"",10, Render2D.getWindowHeight()/2+100+30*x,Text.CAMBRIA_24, Color.white);

    }

    @Override
    public void drawInfo(boolean b) {

    }

    @Override
    public boolean isUndercursor(int cursorX, int cursorY) {
        return false;
    }
}
