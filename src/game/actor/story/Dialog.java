package game.actor.story;

import engine.Actor;
import engine.control.InputMain;
import engine.render.Text;
import game.StartGame;
import game.actor.enviroment.Item;
import game.actor.magic.Spell;
import game.actor.player.Player;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.List;

public class Dialog extends Actor {

    public Dialog(){
        List<String> replics = new ArrayList<>();
        List<String> names = new ArrayList<>();
        replics.add("Первая реплика / Первая реплика / Первая реплика / Первая реплика / Первая реплика / Первая реплика / Первая реплика / Первая реплика");
        replics.add("Вторая / Вторая / Вторая / Вторая / Вторая / Вторая / Вторая / Вторая / Вторая / Вторая / Вторая / Вторая / Вторая / Вторая /");
        replics.add("Третьи слова Третьи слова Третьи слова Третьи слова Третьи слова Третьи слова Третьи слова Третьи слова");
        replics.add("Третьи слова Третьи слова Третьи слова Третьи слова Третьи слова Третьи слова Третьи слова Третьи слова");
        replics.add("Первая реплика / Первая реплика / Первая реплика / Первая реплика / Первая реплика / Первая реплика / Первая реплика / Первая реплика");

        names.add("0");
        names.add("1");
        names.add("0");
        names.add("0");
        names.add("1");

        setVariable("replics",replics);
        setVariable("speakers",names);

        String nextSteps = "0:номер задания;1:Дубина:3;2:Дубина:3";
        setVariable("nextSteps",nextSteps);

        String reasons = "0:Враг;1:номер задания";
        setVariable("reasons",reasons);
        setVariable("name","О делах");

    }

    @Override
    public int logic() {
        return 0;
    }

    @Override
    public void draw() {

    }

    @Override
    public void draw(double x, double y, double startTarget) {
        List<String> replics = (List<String>) getVariable("replics");
        List<String> names = (List<String>) getVariable("speakers");
        int lines = 0;
        int linesPre = 0;
        int offsets = 1;
        for(int i = 0; i < replics.size();i++){
            if (startTarget > 0) {
                startTarget--;
            } else {
                linesPre += Text.linesStringWithOffset(names.get(i), Text.CAMBRIA_14, 600);
                linesPre += Text.linesStringWithOffset(replics.get(i), Text.CAMBRIA_14, 600);
                if (linesPre * 16 + offsets * 10 > 200 ) {
                    break;
                }
                Color color = Color.white;
                lines += Text.drawStringWithOffset(names.get(i), x, y + lines * 16 + offsets * 10, Text.CAMBRIA_14, color, 600, 16);
                lines += Text.drawStringWithOffset(replics.get(i), x, y + lines * 16 + offsets * 10, Text.CAMBRIA_14, Color.white, 600, 16);
                offsets++;
            }
        }
    }

    public void draw(double x, double y, double startTarget, int lastReplica, String speakerName, double lineSizeX,double maxSizeY, double perLineY, int font) {
        List<String> replics = (List<String>) getVariable("replics");
        List<String> names = (List<String>) getVariable("speakers");
        int lines = 0;
        int linesPre = 0;
        for(int i = 0; i < replics.size();i++){
            if (startTarget > 0) {
                startTarget--;
            } else {
                linesPre += Text.linesStringWithOffset(names.get(i), font, (int)lineSizeX);
                linesPre += Text.linesStringWithOffset(replics.get(i), font, (int)lineSizeX);
                if (linesPre * perLineY > maxSizeY || i > lastReplica) {
                    break;
                }
                Color color = Color.white;
                String name = (names.get(i).equals("0") ? StartGame.game.getControled().getVariableString("name") : speakerName)+":";
                lines += Text.drawStringWithOffset(name, x, y + lines * perLineY , font, color,(int) lineSizeX, 16);
                lines += Text.drawStringWithOffset(replics.get(i), x, y + lines * perLineY, font, Color.lightGray,(int) lineSizeX, 16);
            }
        }
    }

    @Override
    public void drawInfo(boolean b) {

    }

    @Override
    public boolean isUndercursor(int cursorX, int cursorY) {
        return false;
    }

    public int getSize(){
        List<String> replics = (List<String>) getVariable("replics");
        return replics.size();
    }
    public int getScrollerPositions(int lastReplica) {
        List<String> replics = (List<String>) getVariable("replics");
        List<String> names = (List<String>) getVariable("speakers");

        int linesPre = 0;
        int offsets = 0;
        int offsetsTotal = 0;
        for(int i = 0; i < replics.size();i++){
            if(i > lastReplica){
                break;
            }
            linesPre += Text.linesStringWithOffset(replics.get(i), Text.CAMBRIA_14, 600);
            linesPre += Text.linesStringWithOffset(names.get(i), Text.CAMBRIA_14, 600);
            if (linesPre * 16 + offsets * 10 < 200) {
                offsets++;
            }
            offsetsTotal++;
        }
        return offsetsTotal-offsets;
    }

    public void finish(Player speakWith) {
        String[] nexts = (getVariable("nextSteps")+"").split(";");
        for(String next : nexts){
            String[] params = next.split(":");
            if(params[0].equals("1")){
                StartGame.game.addQuest(params[1]);
            }
            else if(params[0].equals("2")){
                Item item = new Item("",0,0,0,0,params[1], Integer.parseInt(params[2]));
                StartGame.game.addItem(item);
                Event.eventPickup(( item.getVariable("name")+""),item.getVariableInteger("count"));

            }
            else if(params[0].equals("3")){
                StartGame.game.removeItem(params[1], Integer.parseInt(params[2]));
            }
            else if(params[0].equals("4")){
                Spell spell = new Spell(params[1]);
                StartGame.game.addSpell(spell);
                Event.eventGetSpell(spell.getVariableString("name"));
            }
            else if(params[0].equals("5")){
                int gold = Integer.parseInt(params[2]);
                StartGame.game.addGold(gold);
            }
            else if(params[0].equals("6")){
                int gold = Integer.parseInt(params[2]);
                StartGame.game.removeGold(gold);
            }

        }
    }

    public boolean isAvaible(Actor speakWith){
        String[] nexts = (getVariable("reasons")+"").split(";");
        if(!speakWith.getVariable("ID").equals(getVariable("speaker"))){
            return false;
        }
        if(StartGame.game.dialogPassed.contains(getVariableString("ID"))) return false;

        for(String next : nexts){
            String[] params = next.split(":");
            if(params[0].equals("1")){
                String questID = params[1];
                for(Quest el : StartGame.game.questInJournal){
                    if(el.getVariable("ID").equals(questID)){
                        return false;
                    }
                }
            }
            if(params[0].equals("2")){
                String questID = params[1];
                boolean questGained = false;
                for(Quest el : StartGame.game.questInJournal){
                    if(el.getVariable("ID").equals(questID)){
                        if(el.getVariableInteger("questStatus") != Target.STATUS_COMPLETED){
                            return false;
                        }
                        questGained = true;
                        break;
                    }
                }
                if(!questGained) return false;
            }
            if(params[0].equals("3")){
                int gold = Integer.parseInt(params[1]);
                if (StartGame.game.gold < gold){
                    return false;
                }
            }
            if(params[0].equals("4")){
                String dialog = (params[1]);
                if(!StartGame.game.dialogPassed.contains(dialog)) return false;
            }
        }
        return true;
    }
}
