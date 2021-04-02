package game.actor.story;

import engine.Actor;
import engine.Mathp;
import engine.audio3.AudioManager;
import engine.render.Camera;
import engine.render.Render2D;
import game.StartGame;
import game.actor.player.Player;

public class Target extends Actor {

    public static final String NO_NEXT_TASK = "-1";
    public static final String FINAL_TASK = "-2";

    public static final int STATUS_NOT_GAINED = 0;
    public static final int STATUS_GAINED = 1;
    public static final int STATUS_FAILED = 2;
    public static final int STATUS_COMPLETED = 3;

    public static final int TASK_WALK_IN = 0;
    public static final int TASK_COMPLEDET = 1;
    public static final int TASK_WALK_OUT = 2;
    public static final int TASK_ACTIVATOR = 3;
    public static final int TASK_ITEM = 4;
    public static final int TASK_SEE = 5;
    public static final int TASK_KILL = 6;
    public static final int TASK_LOOT = 7;


    public Target(String name, String about, int task, String taskParams, String nextTasks, Quest quest,int needTasks){
        setVariable("targetName", name);//
        setVariable("targetAbout", about);//
        setVariable("targetStatus", STATUS_NOT_GAINED);
        setVariable("taskType", task);//
        setVariable("taskParams", taskParams);
        setVariable("taskNexts", nextTasks);
        setVariable("quest", quest);
        setVariable("needTaskToGainTarget", needTasks);
        setVariable("tasksToGainTarget", 0);

    }

    public void getNextTasks(){
        setVariable("targetStatus", STATUS_COMPLETED);

        String[] tasks = (getVariable("taskNexts")+"").split("}");
        for(String taskIndex : tasks){
            if(taskIndex.equals(NO_NEXT_TASK)){

            } else if(taskIndex.equals(FINAL_TASK)){
                Quest quest = (Quest) getVariable("quest");
                quest.setCompleted();
            }  else {
                Event.eventTaskDone(getVariable("targetName")+"");
                int index = Integer.parseInt(taskIndex);
                Quest quest = (Quest) getVariable("quest");
                quest.setTargetActive(index);
            }
        }
    }

    @Override
    public int logic() {
        String[] params = (getVariable("taskParams") + "").split("}");
        switch (getVariableInteger("taskType")) {
            case TASK_WALK_IN:
                int left = Integer.parseInt(params[0]);
                int top = Integer.parseInt(params[1]);
                int right = Integer.parseInt(params[2]);
                int bot = Integer.parseInt(params[3]);
                for (Player el : StartGame.game.controlGroup) {
                    double x = el.getVariableTrunked("locX");
                    double y = el.getVariableTrunked("locY");
                    if (Mathp.inRectangle(left, top, right, bot, x, y)) {
                        getNextTasks();
                    }
                }
                break;
            case TASK_COMPLEDET:
                getNextTasks();
                break;
            case TASK_WALK_OUT:
                String[] params1 = (getVariable("taskParams") + "").split("}");
                int left1 = Integer.parseInt(params1[0]);
                int top1 = Integer.parseInt(params1[1]);
                int right1 = Integer.parseInt(params1[2]);
                int bot1 = Integer.parseInt(params1[3]);
                for (Player el : StartGame.game.controlGroup) {
                    double x = el.getVariableTrunked("locX");
                    double y = el.getVariableTrunked("locY");
                    if (Mathp.inRectangle(left1, top1, right1, bot1, x, y)) {
                        return 0;
                    }
                }
                getNextTasks();
                break;
            case TASK_ACTIVATOR:
                Actor activator = StartGame.game.allActors.get(params[0]);
                if (activator.getVariableInteger("openingClosing") == 1) {
                    getNextTasks();
                }
                break;
            case TASK_ITEM:
                Actor item = StartGame.game.allActors.get(params[0]);
                if (StartGame.game.inventory.contains(item)) {
                    getNextTasks();
                }
                break;
            case TASK_SEE:
                Player enemySee = (Player) StartGame.game.allActors.get(params[0]);
                if (enemySee.isVisible()) {
                    getNextTasks();
                }
                break;
            case TASK_KILL:
                Player enemyKill = (Player) StartGame.game.allActors.get(params[0]);
                if (enemyKill.getVariableInteger("dead") == 1) {
                    getNextTasks();
                }
                break;
            case TASK_LOOT:
                Player enemyLoot = (Player) StartGame.game.allActors.get(params[0]);
                if (!StartGame.game.getNpcList().contains(enemyLoot)) {
                    getNextTasks();
                }
                break;
        }
        return 0;
    }

    public int[] getTargetLoc() {
        String[] params = (getVariable("taskParams") + "").split("}");
        switch (getVariableInteger("taskType")) {
            case TASK_WALK_IN:
                int left = Integer.parseInt(params[0]);
                int top = Integer.parseInt(params[1]);
                int right = Integer.parseInt(params[2]);
                int bot = Integer.parseInt(params[3]);
                int x = (left + right) / 2;
                int y = (top + bot) / 2;
                return new int[]{x, y};
            case TASK_COMPLEDET:
            case TASK_WALK_OUT:
            case TASK_SEE:
                return new int[]{-10000, 0};
            case TASK_ACTIVATOR:
                Actor activator = StartGame.game.allActors.get(params[0]);
                int x1 = activator.getVariableInteger("locX");
                int y1 = activator.getVariableInteger("locY");
                return new int[]{x1, y1};
            case TASK_ITEM:
                Actor item = StartGame.game.allActors.get(params[0]);
                int x2 = item.getVariableInteger("locX");
                int y2 = item.getVariableInteger("locY");
                return new int[]{x2, y2};
            case TASK_KILL:
            case TASK_LOOT:
                Actor enemy = StartGame.game.allActors.get(params[0]);
                int x3 = enemy.getVariableTrunked("locX");
                int y3 = enemy.getVariableTrunked("locY");
                return new int[]{x3, y3};
        }
        return new int[]{0, 0};
    }

    @Override
    public void drawMiniMap() {
        if (getVariable("targetStatus").equals(STATUS_GAINED)) {
            int[] coords = getTargetLoc();
            double otnX = (coords[0]) / (StartGame.game.sizeX * 32.0);
            double otnY = (coords[1]) / (StartGame.game.sizeY * 32.0);
            Render2D.angleColorDraw("questTarget", "interface/minimapPlayer", Render2D.getWindowWidth() - 200 + (int) (200 * otnX), (int) (200 * otnY), 8, 8, 0, 0.5, 0.0, 0.5, 1);
        }
    }

    @Override
    public void draw() {
        if (getVariable("targetStatus").equals(STATUS_GAINED)) {
            int[] coords = getTargetLoc();
            Render2D.angleColorDraw("questTarget", "interface/minimapQuest", coords[0]+Camera.locX, coords[1]+Camera.locY, 250, 250, 0, 0.5, 0.0, 0.5, 1);
        }
    }

    @Override
    public void draw(double x, double y, double s) {

    }

    @Override
    public void drawInfo(boolean b) {

    }

    @Override
    public boolean isUndercursor(int cursorX, int cursorY) {
        return false;
    }

    public void give() {
        setVariable("targetStatus",1);
        if (!getVariable("taskNexts").equals(FINAL_TASK)) {
            Event.eventTaskAdded(getVariable("targetName") + "");
        }
    }
}
