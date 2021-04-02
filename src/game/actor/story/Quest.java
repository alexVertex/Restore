package game.actor.story;

import engine.Actor;
import engine.render.Text;
import game.actor.actorsHyper.QuestDataBase;
import org.newdawn.slick.Color;
import screens.editor.storyEditor.editQuestScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Quest extends Actor {


    public Quest(String name, String mapName, String aboutQuest, int journalTargetX, int journalTargetY){
        setVariable("questAbout", aboutQuest);
        setVariable("questName", name);
        setVariable("questMap", mapName);
        setVariable("questAbout", aboutQuest);
        setVariable("questStatus", Target.STATUS_GAINED);
        List<Target> targets = new ArrayList<>();
        targets.add(new Target("Прогуляться","Дойдите в отмеченную зону, чтобы переместить персонажа кликните по экрану",
                Target.TASK_WALK_IN,"400}400}500}500", "1}2",this,0));
        targets.add(new Target("Дойти до точки один","Теперь дойдите в другую отмеченную зону",
                Target.TASK_WALK_IN,"300}500}400}600", "3",this,1));
        setVariable("questTargets", targets);
        targets.add(new Target("Дойти до точки два","Теперь дойдите в другую отмеченную зону",
                Target.TASK_WALK_IN,"700}200}800}300", "3",this,1));
        targets.add(new Target("Дойти до точки три","Вы можете поменять скорость движения персонажа, для воспользуйтесь панелью управления в нижнем левом углу карты. Движение бегом тратит запас сил, а движение крадучись уменьшает расстояние обнаружения персонажа на слух",
                Target.TASK_WALK_IN,"450}450}550}550", "4",this,2));
        targets.add(new Target("Выйти из области","Покинте данную область",
                Target.TASK_WALK_OUT,"150}150}850}850", "5",this,1));
        targets.add(new Target("Найти бандита","Тут в углу ходит бандит, найдите его",
                Target.TASK_SEE,"enemy1", "6",this,1));
        targets.add(new Target("Убить бандита","Бандит должен умереть!",
                Target.TASK_KILL,"enemy1", "7",this,1));
        targets.add(new Target("Обыскать бандита","Бандит должен умереть!",
                Target.TASK_LOOT,"enemy1", "8",this,1));
        targets.add(new Target("Задание выполнено","Обучение завершено, удачи в этом мире",
                Target.TASK_COMPLEDET,"", Target.FINAL_TASK,this,1));
        targets.get(0).setVariable("targetStatus",Target.STATUS_GAINED);
        setVariable("questTargets", targets);
        setVariable("journalTargetX", journalTargetX);
        setVariable("journalTargetY", journalTargetY);
    }

    public Quest(String questID) {
        HashMap<String, Object> data = QuestDataBase.getData(questID);
        setVariable("questStatus", Target.STATUS_GAINED);
        setVariable("questName", data.get("questName"));
        setVariable("questMap", data.get("questMap"));
        setVariable("questAbout", data.get("questAbout"));
        setVariable("journalTargetX", data.get("journalTargetX"));
        setVariable("journalTargetY", data.get("journalTargetY"));
        setVariable("ID", data.get("ID"));

        String tasks = data.get("questTargetsData")+"";
        List<Target> targets = new ArrayList<>();
        if(tasks.equals("null")) return;
        String[] strings = tasks.split(":");
        for(String el : strings){
            String[] strings1 = el.split(",");
            targets.add(new Target(strings1[0],strings1[1],
                    Integer.parseInt(strings1[2]),strings1[5], strings1[3],this,Integer.parseInt(strings1[4])));
        }
        setVariable("questTargets", targets);
    }

    @Override
    public void drawMiniMap(){
        for(Target el : (List<Target>) getVariable("questTargets")){
            if(el.getVariable("targetStatus").equals(Target.STATUS_GAINED)){
                el.drawMiniMap();
            }
        }
    }

    @Override
    public int logic() {
        for(Target el : (List<Target>) getVariable("questTargets")){
            if(el.getVariable("targetStatus").equals(Target.STATUS_GAINED)){
                el.logic();
            }
        }
        return 0;
    }

    @Override
    public void draw() {
        for(Target el : (List<Target>) getVariable("questTargets")){
            if(el.getVariable("targetStatus").equals(Target.STATUS_GAINED)){
                el.draw();
            }
        }
    }

    public int getScrollerPositions(){
        int linesPre = Text.linesStringWithOffset(getVariable("questAbout")+"",  Text.CAMBRIA_14, 240);
        int offsets = 1;
        int offsetsTotal = 1;
        for(Target el : (List<Target>) getVariable("questTargets")){
            if(!el.getVariable("targetStatus").equals(Target.STATUS_NOT_GAINED)) {
                linesPre += Text.linesStringWithOffset(el.getVariable("targetName") + "", Text.CAMBRIA_14, 240);
                linesPre += Text.linesStringWithOffset(el.getVariable("targetAbout") + "", Text.CAMBRIA_14, 240);
                if (linesPre * 16 + offsets * 10 < 500) {
                    offsets++;
                }
                offsetsTotal++;
            }
        }
        return offsetsTotal-offsets;
    }

    @Override
    public void draw(double x, double y, double startTarget) {
        int lines = Text.linesStringWithOffset(getVariable("questAbout")+"",  Text.CAMBRIA_14, 240);
        int linesPre = lines;
        Text.drawStringWithOffset(getVariable("questAbout")+"", x,y, Text.CAMBRIA_14, Color.white, 240,16);
        int offsets = 1;
        for(Target el : (List<Target>) getVariable("questTargets")){
            if(!el.getVariable("targetStatus").equals(Target.STATUS_NOT_GAINED)) {
                if (startTarget > 0) {
                    startTarget--;
                } else {
                    linesPre += Text.linesStringWithOffset(el.getVariable("targetName") + "", Text.CAMBRIA_14, 240);
                    linesPre += Text.linesStringWithOffset(el.getVariable("targetAbout") + "", Text.CAMBRIA_14, 240);
                    if (linesPre * 16 + offsets * 10 > 500) {
                        break;
                    }
                    Color color = Color.white;
                    if(el.getVariable("targetStatus").equals(Target.STATUS_FAILED)) color = Color.red;
                    if(el.getVariable("targetStatus").equals(Target.STATUS_COMPLETED)) color = Color.green;
                    lines += Text.drawStringWithOffset(el.getVariable("targetName") + "", x, y + lines * 16 + offsets * 10, Text.CAMBRIA_14, color, 240, 16);
                    lines += Text.drawStringWithOffset(el.getVariable("targetAbout") + "", x, y + lines * 16 + offsets * 10, Text.CAMBRIA_14, Color.white, 240, 16);
                    offsets++;
                }
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

    public void setTargetActive(int index) {
        List<Target> targets = (List<Target>) getVariable("questTargets");
        Target target = targets.get(index);
        int targetSignals = target.getVariableInteger("tasksToGainTarget");
        int targetSignalsNeeds = target.getVariableInteger("needTaskToGainTarget");
        targetSignals++;
        target.setVariable("tasksToGainTarget",targetSignals);
        if(targetSignals == targetSignalsNeeds){
            target.give();
        }
    }

    public void setCompleted() {
        setVariable("questStatus", Target.STATUS_COMPLETED);
        Event.eventQuestCompleted(getVariable("questName")+"");

    }
}
