package screens.editor.storyEditor;

import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.control.interfaces.Droplist;
import engine.control.interfaces.Textbox;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.actorsHyper.QuestDataBase;
import game.actor.enviroment.Activator;
import game.actor.enviroment.Item;
import game.actor.player.Player;
import game.actor.story.Target;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import screens.editor.submenus;

import java.util.HashMap;

public class editTaskScreen {

    private static Textbox textBoxMapAbout = new Textbox(Render2D.getWindowWidth()-150,Render2D.getWindowHeight() - 160,292,300,"interface/textLarge","описание", Text.CAMBRIA_14,4,true);
    static String[] tasks = {"Войти в область","Завершение задания","Выйти из области","Активатор","Предмет","Увидеть","Убить","Обыскать труп"};
    private static Droplist dropQuestType = new Droplist(Render2D.getWindowWidth()-150,Render2D.getWindowHeight() - 330,292,30,"interface/drop",0,tasks, Text.CAMBRIA_14,4);
    private static Textbox textQuestName = new Textbox(Render2D.getWindowWidth()-150,Render2D.getWindowHeight() - 370,292,30,"interface/text","задача", Text.CAMBRIA_14,4);

    public static Button addItem = new Button(Render2D.getWindowWidth()-20,20,32,32,"interface/butPlus");
    private static int currentRow = -1;

    public static void work(){
        if (InputMain.isKeyJustReleased(Keyboard.KEY_RETURN)) {
            editQuestScreen.editTasks = false;
        }
        Render2D.angleColorDraw("rightBackEditor","interface/white",Render2D.getWindowWidth()-150,Render2D.getWindowHeight()/2,300,Render2D.getWindowHeight(),0,0.15,0.15,0.15,1);

        textBoxMapAbout.draw();
        textBoxMapAbout.work();
        dropQuestType.draw();
        dropQuestType.work();
        textQuestName.draw();
        textQuestName.work();
        addItem.draw();
        addItem.work();
        int row = 0;
        for(editQuestScreen.TaskInfo el : editQuestScreen.taskInfoList){
            if(currentRow != -1){
                if(editQuestScreen.taskInfoList.get(currentRow).taks.contains(row+"")){
                    Render2D.angleColorDraw("chooser","interface/white",Render2D.getWindowWidth() -128,40+row*16+10,256,16,0,1,0,1,0.5);
                }
            }
            Text.drawStringRigth(el.name,Render2D.getWindowWidth()-2,40+row*16,Text.CAMBRIA_14, Color.white);
            Text.drawString(el.taskNeed+"",Render2D.getWindowWidth()-300,40+row*16,Text.CAMBRIA_14, Color.white);

            if(InputMain.getCursorX() > Render2D.getWindowWidth() - 256 && InputMain.getCursorY() > 40+row*16-8+10  && InputMain.getCursorY() < 40+row*16+8+10){
                Render2D.angleColorDraw("chooser","interface/white",Render2D.getWindowWidth() -128,40+row*16+10,256,16,0,1,1,1,0.5);
                if(InputMain.isKeyJustReleased(InputMain.LMB)) {
                    currentRow = row;
                    textBoxMapAbout.setText(el.about);
                    textQuestName.setText(el.name);
                    dropQuestType.setPos(el.taskType);
                    switch (dropQuestType.getVal()) {
                        case Target.TASK_WALK_IN:
                        case Target.TASK_WALK_OUT:
                            String[] params = el.taskParams.split("}");
                            x1 = Integer.parseInt(params[0]);
                            y1 = Integer.parseInt(params[1]);
                            x2 = Integer.parseInt(params[2]);
                            y2 = Integer.parseInt(params[3]);
                            break;
                    }
                }
                if(InputMain.isKeyJustReleased(InputMain.RMB)){
                    if(currentRow != -1){
                        if(currentRow != row){
                            if(editQuestScreen.taskInfoList.get(currentRow).taks.contains(row+"")){
                                editQuestScreen.taskInfoList.get(currentRow).taks.remove(row+"");
                                editQuestScreen.taskInfoList.get(row).taskNeed--;
                            } else {
                                editQuestScreen.taskInfoList.get(currentRow).taks.add(row+"");
                                editQuestScreen.taskInfoList.get(row).taskNeed++;
                            }
                        } else {
                            if(!editQuestScreen.taskInfoList.get(currentRow).taks.contains(Target.FINAL_TASK)) {
                                for(String ele : editQuestScreen.taskInfoList.get(currentRow).taks){
                                    if(ele.equals("-1")) continue;
                                    if(ele.equals("-2")) continue;
                                    editQuestScreen.taskInfoList.get(Integer.parseInt(ele)).taskNeed--;
                                }
                                editQuestScreen.taskInfoList.get(currentRow).taks.clear();
                                editQuestScreen.taskInfoList.get(currentRow).taks.add(Target.FINAL_TASK);
                            } else {
                                editQuestScreen.taskInfoList.get(currentRow).taks.clear();
                            }
                        }
                    }
                }
            }
            if(row == currentRow) {
                if (editQuestScreen.taskInfoList.get(currentRow).taks.contains(Target.FINAL_TASK)) {
                    Render2D.angleColorDraw("chooser", "interface/white", Render2D.getWindowWidth() - 128, 40 + row * 16 + 10, 256, 16, 0, 0, 1, 0, 0.5);
                } else {
                    Render2D.angleColorDraw("chooser", "interface/white", Render2D.getWindowWidth() - 128, 40 + row * 16 + 10, 256, 16, 0, 1, 1, 0, 0.5);
                }
            }
            row++;
        }
        if(currentRow != -1){
            editQuestScreen.taskInfoList.get(currentRow).about = textBoxMapAbout.getText();
            editQuestScreen.taskInfoList.get(currentRow).name = textQuestName.getText();
            editQuestScreen.taskInfoList.get(currentRow).taskType = dropQuestType.getVal();
                if (dropQuestType.getVal() == Target.TASK_WALK_IN || dropQuestType.getVal() == Target.TASK_WALK_OUT) {
                    if(InputMain.getCursorX() > 256 && InputMain.getCursorX() < Render2D.getWindowWidth()-300) {

                        int x = InputMain.getCursorX() - Camera.locX + 16;
                        int y = InputMain.getCursorY() - Camera.locY + 16;
                        x = (x / 32) * 32;
                        y = (y / 32) * 32;
                        if (InputMain.isKeyPressed(InputMain.LMB)) {
                            x1 = x;
                            y1 = y;
                        }
                        if (InputMain.isKeyPressed(InputMain.RMB)) {
                            x2 = x;
                            y2 = y;
                        }
                    }
                    int left = Math.min(x1, x2);
                    int right = Math.max(x1, x2);
                    int top = Math.min(y1, y2);
                    int bot = Math.max(y1, y2);
                    String params = left + "}" + top + "}" + right + "}" + bot;
                    editQuestScreen.taskInfoList.get(currentRow).taskParams = params;
                    double x3 = (left + right) / 2 + Camera.locX;
                    double y3 = (top + bot) / 2 + Camera.locY;
                    double sx = (right - left);
                    double sy = (bot - top);
                    Render2D.angleColorDraw("qwert", "interface/white", x3, y3, sx, sy, 0, 1, 0, 1, 0.5);
                } else if(dropQuestType.getVal() == Target.TASK_ITEM) {
                    for (Item ele : StartGame.game.onMapItems) {
                        if(ele.isUndercursor(InputMain.getCursorX(),InputMain.getCursorY())){
                            if(InputMain.isKeyJustReleased(InputMain.LMB)){
                                editQuestScreen.taskInfoList.get(currentRow).taskParams = ele.getVariableString("refID");
                            }
                        }
                        if(ele.getRedID().equals(editQuestScreen.taskInfoList.get(currentRow).taskParams)){
                            Render2D.angleColorDraw("qwert", "interface/white", ele.getTileLoc()[0]+Camera.locX, ele.getTileLoc()[1]+Camera.locY, 64, 64, 0, 1, 0, 1, 0.5);
                        }
                    }
                }else if(dropQuestType.getVal() == Target.TASK_KILL || dropQuestType.getVal() == Target.TASK_LOOT  || dropQuestType.getVal() == Target.TASK_SEE) {
                    for (Player ele : StartGame.game.getNpcList()) {
                        if(ele.isUndercursorAll(InputMain.getCursorX(),InputMain.getCursorY()) && ele.getVariable("good").equals(0)){
                            if(InputMain.isKeyJustReleased(InputMain.LMB)){
                                editQuestScreen.taskInfoList.get(currentRow).taskParams = ele.getVariableString("refID");
                            }
                        }
                        if(ele.getRedID().equals(editQuestScreen.taskInfoList.get(currentRow).taskParams)){
                            Render2D.angleColorDraw("qwert", "interface/white", ele.getTileLoc()[0]+Camera.locX, ele.getTileLoc()[1]+Camera.locY, 64, 64, 0, 1, 0, 1, 0.5);
                        }
                    }
                }else if(dropQuestType.getVal() == Target.TASK_ACTIVATOR) {
                    for (Activator ele : StartGame.game.activators) {
                        if(ele.isUndercursor(InputMain.getCursorX(),InputMain.getCursorY())){
                            if(InputMain.isKeyJustReleased(InputMain.LMB)){
                                editQuestScreen.taskInfoList.get(currentRow).taskParams = ele.getVariableString("refID");
                            }
                        }
                        if(ele.getRedID().equals(editQuestScreen.taskInfoList.get(currentRow).taskParams)){
                            Render2D.angleColorDraw("qwert", "interface/white", ele.getTileLoc()[0]+Camera.locX, ele.getTileLoc()[1]+Camera.locY, 64, 64, 0, 1, 0, 1, 0.5);
                        }
                    }
                }
            }

        if(addItem.isReleased()){
            editQuestScreen.TaskInfo taskInfo = new editQuestScreen.TaskInfo();
            taskInfo.about = "описание";
            taskInfo.name = "название";
            taskInfo.taskParams = "0}0}0}0";
            editQuestScreen.taskInfoList.add(taskInfo);
        }
    }

    private static int x1,x2,y1,y2;
}
