package screens.dialogs;

import engine.Mathp;
import engine.Start;
import engine.control.InputMain;
import engine.control.interfaces.Scroll;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.actorsHyper.DialogsDataBase;
import game.actor.player.Player;
import game.actor.player.PlayerDrawAndUndercursor;
import game.actor.story.Dialog;
import org.newdawn.slick.Color;
import screens.Controls;
import screens.ingame.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class DialogScreen extends GameScreen {
    double wide = 850;
    double high = 250;
    double sizeScroll = 32;
    private final double borderSize = 4;
    double sizeScrollY = high-borderSize;
    private final double leftBorderX = Render2D.getWindowWidth() / 2 - wide / 2;
    private final double rightBorderX = Render2D.getWindowWidth() / 2 + wide / 2;
    private final double topbotBorderX = Render2D.getWindowWidth() / 2;
    private final double locScrollX =  rightBorderX-sizeScroll/2-borderSize/2;
    private final double locScrollY =  Render2D.getWindowHeight()  -250;
    private final double locBotBorderY = locScrollY + high / 2;
    private final double locTopBorderY = locScrollY - high / 2;
    private final double dialogStartLineX = leftBorderX + 10;
    private final double dialogStartLineY = locTopBorderY + 5;
    private final Scroll dialogScroll = new Scroll(locScrollX, locScrollY, sizeScroll, sizeScrollY, "interface/scroller");
    private final Scroll topicsScroll = new Scroll(locScrollX, locScrollY, sizeScroll, sizeScrollY, "interface/scroller");
    List<Dialog> dialogList = new ArrayList<>();
    Dialog active;
    Player speaker;
    private final double portretLocY = locScrollY;
    private final double portretLocX = leftBorderX - 100;

    public DialogScreen(Player speakWith) {
        dialogScroll.setMaxPosition(0);
        topicsScroll.setMaxPosition(0);
        startDialog(speakWith);
    }

    private void startDialog(Player spealWith) {
        dialogList.clear();
        for(Dialog dialog : DialogsDataBase.dialogs) {
            if (dialog.isAvaible(spealWith)) {
                dialogList.add(dialog);
            }
        }
        if(!spealWith.getVariableString("merc").equals("null")){
            Dialog addMert = new Dialog();
            addMert.setVariable("name","Взять наёмника в команду");
            addMert.setVariable("special","merc");
            dialogList.add(addMert);
        }
        if(!spealWith.getVariableString("trade").equals("null")){
            Dialog addTrade = new Dialog();
            addTrade.setVariable("name","Торговать");
            addTrade.setVariable("special","trade");
            dialogList.add(addTrade);
        }
        if(!spealWith.getVariableString("smith").equals("null")){
            Dialog addSmith = new Dialog();
            addSmith.setVariable("name","Заказать предмет");
            addSmith.setVariable("special","smith");
            dialogList.add(addSmith);
        }
        if(!spealWith.getVariableString("ench").equals("null")){
            Dialog addSmith = new Dialog();
            addSmith.setVariable("name","Заказать заклинание");
            addSmith.setVariable("special","ench");
            dialogList.add(addSmith);
        }
        Dialog end = new Dialog();
        end.setVariable("name","Поговорим позже...");
        end.setVariable("special","End");

        dialogList.add(end);
        speaker = spealWith;
    }

    private void finishDialog() {
        active.finish(speaker);
        Start.setScreen(new GameScreen());
    }

    private void setReplica(int replica){
        lastReplica = replica;
        int targetsScrolls = active.getScrollerPositions(lastReplica);
        if (targetsScrolls < 0) targetsScrolls = 0;
        dialogScroll.setMaxPosition(targetsScrolls);
        dialogScroll.setToLastPosition();
    }

    @Override
    public void input() {
        if (active != null) {
            dialogScroll.work();
            if (!Mathp.inRectangle(locScrollX-sizeScroll/2, locScrollY-sizeScrollY/2, locScrollX+sizeScroll/2, locScrollY+sizeScrollY/2, InputMain.getCursorX(), InputMain.getCursorY())) {
                if (InputMain.isKeyJustReleased(InputMain.LMB)) {
                    if (lastReplica < active.getSize() - 1) {
                        setReplica(lastReplica+1);
                    } else {
                        finishDialog();
                    }
                }
                if (InputMain.isKeyJustReleased(InputMain.RMB) || InputMain.isKeyJustReleased(Controls.openESCMenu)) {
                    if (lastReplica < active.getSize() - 1) {
                        setReplica(active.getSize() - 1);
                    } else {
                        finishDialog();
                    }
                }
            }
        } else {
            topicsScroll.work();
            if(InputMain.isKeyJustReleased(Controls.openESCMenu)){
                Start.setScreen(new GameScreen());
            }
        }
    }

    @Override
    public void logic() {

    }

    int lastReplica = 0;

    boolean hovered = false;
    @Override
    public void render() {
        super.render();

        Render2D.angleColorDraw("campBack", "interface/white", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2, Render2D.getWindowWidth(), Render2D.getWindowHeight(), 0, 0, 0, 0, 0.75);
        Render2D.angleDraw("campBorder1", "interface/borderBack", topbotBorderX, locTopBorderY, wide, borderSize, 0);
        Render2D.angleDraw("campBorder2", "interface/borderBack", topbotBorderX, locBotBorderY, wide, borderSize, 0);
        Render2D.angleDraw("campBorder3", "interface/borderBack", rightBorderX, locScrollY, high, borderSize, 90);
        Render2D.angleDraw("campBorder4", "interface/borderBack", leftBorderX, locScrollY, high, borderSize, 90);
        double perLine = 30;
        if (active != null) {
            active.draw(dialogStartLineX, dialogStartLineY, dialogScroll.getPosition(), lastReplica,speaker.getVariableString("name"),wide-20,high-20, perLine,Text.CAMBRIA_24);
            dialogScroll.draw();
        } else {
            Text.drawString(speaker.getVariable("name") + ":", dialogStartLineX, dialogStartLineY, Text.CAMBRIA_24, Color.white);
            int startRow = topicsScroll.getPosition();
            int row = 0;
            hovered = false;
            for (Dialog el : dialogList) {
                if (startRow == 0) {
                    hovered = false;
                    Color color = Color.darkGray;
                    double size = Text.textWidth(el.getVariable("name") + "",Text.CAMBRIA_24)+4;
                    if (Mathp.inRectangle(leftBorderX, dialogStartLineY + (row+1) * perLine +20 - perLine /2, dialogStartLineX+size, dialogStartLineY + (row+1) * perLine + perLine /2+10, InputMain.getCursorX(), InputMain.getCursorY())) {
                        hovered = true;
                        color = Color.gray;
                    }
                    if(hovered){
                        if(InputMain.isKeyJustReleased(InputMain.LMB)){
                            if(el.getVariable("special") != null){
                                switch (el.getVariableString("special")){
                                    case "End":
                                        Start.setScreen(new GameScreen());
                                        break;
                                    case "merc":
                                        addMercToTeam();
                                        break;
                                    case "trade":
                                        addTrade();
                                        break;
                                    case "smith":
                                        addSmith();
                                        break;
                                    case "ench":
                                        addEnch();
                                        break;
                                }
                            } else {
                                active = el;
                                StartGame.game.dialogPassed.add(el.getVariableString("ID"));
                            }
                        }
                        if(InputMain.isKeyPressed(InputMain.LMB)){
                            color = Color.white;
                        }
                    }
                    Text.drawString(el.getVariable("name") + "", dialogStartLineX, dialogStartLineY + (row+1) * perLine, Text.CAMBRIA_24, color);
                    row++;
                    int totalLines = 7;
                    if (row == totalLines) {
                        break;
                    }
                } else {
                    startRow--;
                }
            }
            topicsScroll.draw();
        }
        PlayerDrawAndUndercursor.drawPortret(speaker,portretLocX, portretLocY);
        String cursor = (active == null || active.getSize() - 1 == lastReplica) ?  "cursors/normal" : "cursors/talk";
        Render2D.simpleDraw("cursor", cursor, InputMain.getCursorX() + 16, InputMain.getCursorY() + 16, 32, 32);
    }

    private void addSmith() {
        Start.setScreen(new SmithScreen(speaker.getVariableString("smith")));

    }

    private void addMercToTeam() {
        if(StartGame.game.controlGroup.size() < 5){
            GameScreen.addDeleteRequest(speaker,StartGame.game.getNpcList());
            Player player = new Player(speaker.getVariableString("merc"),true,speaker.getVariableDouble("locX"),speaker.getVariableDouble("locY"));
            StartGame.game.controlGroup.add(player);
        }
        Start.setScreen(new GameScreen());
    }
    private void addTrade() {

        Start.setScreen(new TradeScreen(speaker.getVariableString("trade")));
    }

    private void addEnch() {

        Start.setScreen(new EnchScreen(speaker.getVariableString("ench")));
    }
}
