package screens.editor.storyEditor;

import engine.Mathp;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Textbox;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.actorsHyper.QuestDataBase;
import game.actor.story.Target;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static screens.editor.submenus.firstOpen;

public class editQuestScreen {

    static class TaskInfo{
        String name,about,taskParams;
        List<String> taks = new ArrayList<>();
        int taskType, taskNeed;
    }
    public static List<TaskInfo> taskInfoList = new ArrayList<>();
    static int targetX, targetY;
    public static String tasks="";
    static double[][] size = {{300,800}};

    private static Textbox textBoxMapName = new Textbox(Render2D.getWindowWidth() / 2+20,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*2,256,30,"interface/text","название карты",Text.CAMBRIA_14,4);
    private static Textbox textBoxMapID = new Textbox(Render2D.getWindowWidth() / 2+20,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*4,256,30,"interface/text","название карты",Text.CAMBRIA_14,4);
    private static Textbox textBoxMapAbout = new Textbox(Render2D.getWindowWidth() / 2,Render2D.getWindowHeight() / 2-size[0][1]/2+10+550,292,300,"interface/textLarge","описание",Text.CAMBRIA_14,4,true);

    public static boolean editTasks = false;
    private static Button loadtasks = new Button(Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2-size[0][1]/2+10+550+150+30,250,30,"interface/text");

    static Button OK = new Button(Render2D.getWindowWidth() / 2+size[0][0]/2 -18-32, Render2D.getWindowHeight() / 2+size[0][1]/2-18,32,32,"interface/butOK");
    static Button Cancel = new Button(Render2D.getWindowWidth() / 2+size[0][0]/2 - 18, Render2D.getWindowHeight() / 2+size[0][1]/2-18,32,32,"interface/butCancel");


    public static void work() {
        if(editTasks){
            editTaskScreen.work();
            return;
        }
        if (InputMain.isKeyJustReleased(Keyboard.KEY_ESCAPE)) {
            QuestEditor.currentRow = -1;
        }
        Render2D.angleColorDraw("windowCreatorBack", "interface/white", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2, size[0][0], size[0][1], 0, 0, 0, 0, 1);
        Render2D.angleDraw("creatorBorder1", "interface/Border", Render2D.getWindowWidth() / 2 - size[0][0] / 2, Render2D.getWindowHeight() / 2, size[0][1], 4, 90);
        Render2D.angleDraw("creatorBorder2", "interface/Border", Render2D.getWindowWidth() / 2 + size[0][0] / 2, Render2D.getWindowHeight() / 2, size[0][1], 4, 90);
        Render2D.angleDraw("creatorBorder3", "interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2 - size[0][1] / 2, size[0][0], 4, 0);
        Render2D.angleDraw("creatorBorder4", "interface/Border", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2 + size[0][1] / 2, size[0][0], 4, 0);
        Text.drawStringCenter("Основные параметры", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2 - size[0][1] / 2 + 10, Text.CAMBRIA_14, Color.white);
        if (firstOpen) {
            firstOpen = false;
            textBoxMapName.setText(QuestEditor.currentQuest.get("questName") + "");
            textBoxMapID.setText(QuestEditor.currentQuest.get("ID") + "");
            targetX = Integer.parseInt(QuestEditor.currentQuest.get("journalTargetX")+"");
            targetY = Integer.parseInt(QuestEditor.currentQuest.get("journalTargetY")+"");
            textBoxMapAbout.setText(QuestEditor.currentQuest.get("questAbout") + "");
            tasks = QuestEditor.currentQuest.get("questTargetsData") + "";
            taskInfoList.clear();
            if(tasks == null || tasks.equals("null")) return;
            String[] strings = tasks.split(":");
            for(String el : strings){
                String[] strings1 = el.split(",");
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.about = strings1[1];
                taskInfo.name = strings1[0];
                taskInfo.taskType = Integer.parseInt(strings1[2]);
                String[] strings2 = strings1[3].split("}");
                taskInfo.taks.addAll(Arrays.asList(strings2));
                taskInfo.taskNeed = Integer.parseInt(strings1[4]);
                taskInfo.taskParams = (strings1[5]);
                taskInfoList.add(taskInfo);
            }
        }
        textBoxMapID.work();
        textBoxMapID.draw();
        textBoxMapName.work();
        textBoxMapName.draw();
        textBoxMapAbout.work();
        textBoxMapAbout.draw();
        loadtasks.work();
        loadtasks.draw();
        if(loadtasks.isReleased()){
            editTasks = true;
        }
        Text.drawStringCenter("Настройка подзаданий",Render2D.getWindowWidth()/2,loadtasks.getY()-9,Text.CAMBRIA_14, Color.white);

        Text.drawString("Название",Render2D.getWindowWidth() / 2-size[0][0]/2 + 8,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30,Text.CAMBRIA_14,Color.white);
        Text.drawString("ID",Render2D.getWindowWidth() / 2-size[0][0]/2 + 8,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*3,Text.CAMBRIA_14,Color.white);
        Text.drawString("Метка на карте",Render2D.getWindowWidth() / 2-size[0][0]/2 + 8,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*5,Text.CAMBRIA_14,Color.white);
        Text.drawString("Описание",Render2D.getWindowWidth() / 2-size[0][0]/2 + 8,Render2D.getWindowHeight() / 2-size[0][1]/2+10+30*13,Text.CAMBRIA_14,Color.white);

        Render2D.simpleDraw("minimap", "minimaps/" + StartGame.game.minimapTexture, Render2D.getWindowWidth()/2, Render2D.getWindowHeight()/2-110, 200, 200);
        Render2D.simpleDraw("minimap1", "interface/journalTarget", Render2D.getWindowWidth()/2-100+targetX, Render2D.getWindowHeight()/2-110-100+targetY, 32, 32);
        if(InputMain.isKeyPressed(InputMain.LMB) && Mathp.inRectangle(Render2D.getWindowWidth()/2-100+16,Render2D.getWindowHeight()/2-110-100+16,Render2D.getWindowWidth()/2+100-16,Render2D.getWindowHeight()/2-110+100-16,InputMain.getCursorX(),InputMain.getCursorY())){
            targetX = (int) (InputMain.getCursorX() -  (Render2D.getWindowWidth()/2-100));
            targetY = (int) (InputMain.getCursorY() -  (Render2D.getWindowHeight()/2-110-100));
        }
        OK.work();
        OK.draw();
        Cancel.work();
        Cancel.draw();
        if(Cancel.isReleased()){
            QuestEditor.currentRow = -1;
        }
        if(OK.isReleased()){
            QuestEditor.currentRow = -1;
            HashMap<String, Object> data = new HashMap<>();
            data.put("ID",textBoxMapID.getText());
            data.put("questName",textBoxMapName.getText());
            data.put("journalTargetX",targetX);
            data.put("journalTargetY",targetY);
            data.put("questAbout",textBoxMapAbout.getText());
            data.put("questMap",StartGame.game.mapID);
            String saver = "";
            for(TaskInfo el : taskInfoList){
                String saver1 = "";
                if(el.taks.contains(Target.FINAL_TASK)) {
                    saver1 = Target.FINAL_TASK;
                } else {
                    for (String ele : el.taks) {
                        saver1 += ele + "}";
                    }
                }
                if(saver1.equals(""))
                    saver1 = Target.NO_NEXT_TASK;
                saver += el.name+","+el.about+","+el.taskType+","+saver1+","+el.taskNeed+","+el.taskParams+":";

            }
            data.put("questTargetsData",saver);

            QuestDataBase.addData(textBoxMapID.getText(),data);
        }
    }
}
