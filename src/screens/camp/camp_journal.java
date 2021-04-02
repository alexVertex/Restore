package screens.camp;

import engine.Mathp;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.control.interfaces.Scroll;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.story.Quest;
import game.actor.story.Target;
import org.newdawn.slick.Color;
import screens.Controls;
import screens.ingame.GameScreen;

import java.util.List;

public class camp_journal extends GameScreen {

    Quest undercursor, active;
    private Scroll questScroll = new Scroll(Render2D.getWindowWidth()/2+282,Render2D.getWindowHeight()/2+174,32,165,"interface/scroller");
    private Scroll targetScroll = new Scroll(Render2D.getWindowWidth()/2-20,Render2D.getWindowHeight()/2,32,510,"interface/scroller");

    @Override
    public void input() {
        undercursor = null;
        int startRow = questScroll.getPosition();
        int row = 0;
        for(Quest el : StartGame.game.questInJournal){
            if(el.getVariable("questMap").equals(StartGame.game.mapID)) {
                if (startRow == 0) {
                    if (Mathp.inRectangle(Render2D.getWindowWidth() / 2 + 10, Render2D.getWindowHeight() / 2 + 100 + row * 18 - 9, Render2D.getWindowWidth() / 2 + 260, Render2D.getWindowHeight() / 2 + 100 + row * 18 + 9, InputMain.getCursorX(), InputMain.getCursorY())) {
                        undercursor = el;
                    }
                    row++;
                    if (row == 9) {
                        break;
                    }
                } else {
                    startRow--;
                }
            }
        }
        if(undercursor != null){
            if(InputMain.isKeyJustPressed(InputMain.LMB)){
                active = undercursor;
                AudioManager.playSoundInterface("interface/Ok");

            }
        }
        Controls.campScreenChoose();
        Controls.closeCampScreen();
        questScroll.work();
        targetScroll.work();
    }

    @Override
    public void logic() {
        int quests = 0;
        for(Quest el : StartGame.game.questInJournal) {
            if (el.getVariable("questMap").equals(StartGame.game.mapID)) {
                quests++;
            }
        }
        int lines = (int)Math.ceil((double) quests);
        lines-=9;
        if(lines<0) lines = 0;
        questScroll.setMaxPosition(lines);
        if(active != null) {
            int targetsScrolls = active.getScrollerPositions();
            if(targetsScrolls<0) targetsScrolls = 0;
            targetScroll.setMaxPosition(targetsScrolls);
        }
    }

    @Override
    public void render() {
        super.render();
        float wide = 600;
        float high = 516;
        camp_Base.render(3);
        Render2D.angleDraw("campBorder5","interface/borderBack", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2,high,4,90);
        Render2D.angleDraw("campBorder6","interface/borderBack", Render2D.getWindowWidth()/2+wide/4,Render2D.getWindowHeight()/2+65 ,wide/2,4,0);
        Text.drawStringCenter(StartGame.game.mapName,Render2D.getWindowWidth()/2+wide/4,Render2D.getWindowHeight()/2-260,Text.CAMBRIA_24, Color.white);
        Render2D.simpleDraw("map","minimaps/"+StartGame.game.minimapTexture, Render2D.getWindowWidth()/2+wide/4,Render2D.getWindowHeight()/2+65-150 ,296,296);
        Text.drawStringCenter("Задания",Render2D.getWindowWidth()/2+wide/4,Render2D.getWindowHeight()/2+65,Text.CAMBRIA_24, Color.white);
        int startRow = questScroll.getPosition();
        int row = 0;
        for(Quest el : StartGame.game.questInJournal){
            if (el.getVariable("questMap").equals(StartGame.game.mapID)) {
                if (startRow == 0) {
                    Color color = Color.white;
                    if (el.getVariable("questStatus").equals(Target.STATUS_FAILED)) color = Color.red;
                    if (el.getVariable("questStatus").equals(Target.STATUS_COMPLETED)) color = Color.green;
                    Text.drawString(el.getVariable("questName") + "", Render2D.getWindowWidth() / 2 + 10, Render2D.getWindowHeight() / 2 + 90 + row * 18, Text.CAMBRIA_14, color);
                    if (el.equals(undercursor))
                        Render2D.angleColorDraw("questChooser", "interface/white", Render2D.getWindowWidth() / 2 + 135, Render2D.getWindowHeight() / 2 + 100 + row * 18, 250, 18, 0, 0.5, 0.5, 0.5, 0.25);
                    if (el.equals(active))
                        Render2D.angleColorDraw("questChooser", "interface/white", Render2D.getWindowWidth() / 2 + 135, Render2D.getWindowHeight() / 2 + 100 + row * 18, 250, 18, 0, 0.5, 0.5, 0.5, 0.15);
                    row++;
                    if (row == 9) {
                        break;
                    }
                } else {
                    startRow--;
                }
            }
        }
        questScroll.draw();
        if(active != null) {
            double x = Render2D.getWindowWidth()/2+wide/4 - 148+active.getVariableInteger("journalTargetX")*1.48;
            double y = Render2D.getWindowHeight()/2+65-150 - 148+active.getVariableInteger("journalTargetY")*1.48;
            Render2D.simpleDraw("questTarget","interface/journalTarget", x,y ,32,32);
            Color color = Color.white;
            if(active.getVariable("questStatus").equals(Target.STATUS_FAILED)) color = Color.red;
            if(active.getVariable("questStatus").equals(Target.STATUS_COMPLETED)) color = Color.green;
            Text.drawString(active.getVariable("questName") + "", Render2D.getWindowWidth()/2-wide/2+10,Render2D.getWindowHeight()/2-260,Text.CAMBRIA_24, color);
            active.draw(Render2D.getWindowWidth()/2-wide/2+10,Render2D.getWindowHeight()/2-230,targetScroll.getPosition());
        }
        targetScroll.draw();
        Render2D.simpleDraw("cursor","cursors/normal", InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
    }
}
