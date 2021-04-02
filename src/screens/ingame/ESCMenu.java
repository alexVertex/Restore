package screens.ingame;

import engine.Start;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import org.newdawn.slick.Color;
import screens.editor.mainScreen.editorScreen;

import static screens.Controls.openESCMenu;
import static screens.ingame.MainMenu.setsShow;

public class ESCMenu extends GameScreen {

    double buttonLocX = Render2D.getWindowWidth()-250;
    double buttonLocYStart = Render2D.getWindowHeight()/2;
    double buttonLocYPer = 45;
    double buttonSizeX = 250;
    double buttonSizeY = 40;

    Button newGame = new Button(buttonLocX,buttonLocYStart,buttonSizeX,buttonSizeY,"interface/text","interface/Click");
    Button loadGame = new Button(buttonLocX,buttonLocYStart+buttonLocYPer,buttonSizeX,buttonSizeY,"interface/text","interface/Click");
    Button sets = new Button(buttonLocX,buttonLocYStart+buttonLocYPer*2,buttonSizeX,buttonSizeY,"interface/text","interface/Click");
    Button exitGame = new Button(buttonLocX,buttonLocYStart+buttonLocYPer*3,buttonSizeX,buttonSizeY,"interface/text","interface/Click");
    Button[] buttons = {newGame,loadGame,sets,exitGame};
    String[] buttonsText = {"Продолжить","Редактор","Настройки","Выход"};
    @Override
    public void input() {

    }

    @Override
    public void logic() {

    }

    @Override
    public void render() {
        super.render();
        Render2D.angleColorDraw("campBack","interface/white", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2,Render2D.getWindowWidth(),Render2D.getWindowHeight(),0,0,0,0,0.75);
        drawMenuButtons(buttons, buttonsText, buttonLocX, buttonLocYStart, buttonLocYPer);
        if (InputMain.isKeyJustReleased(openESCMenu)) {
            Start.setScreen(new GameScreen());
        }
        if(setsShow){
            SetsScreen.work();
        }
        if(exitGame.isReleased()){
            Start.setScreen(new MainMenu());
            AudioManager.stopMusic();
        }
        if(sets.isReleased()){
            setsShow = true;
        }
        if(newGame.isReleased()){
            Start.setScreen(new GameScreen());
        }
        if(loadGame.isReleased()){
            Start.setScreen(new editorScreen());
        }
        Render2D.simpleDraw("cursor","cursors/normal",InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
    }

    static void drawMenuButtons(Button[] buttons, String[] buttonsText, double buttonLocX, double buttonLocYStart, double buttonLocYPer) {
        for(int i = 0; i < buttons.length; i++){
            buttons[i].work();
            buttons[i].draw();
            double textOffset = 15;
            Text.drawStringCenter(buttonsText[i], buttonLocX, buttonLocYStart + buttonLocYPer *i- textOffset,Text.CAMBRIA_24, Color.white);
        }
    }
}
