package screens.ingame;

import engine.Start;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.control.interfaces.Button;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import screens.editor.mainScreen.editorScreen;

import static screens.Controls.openESCMenu;

public class MainMenu extends GameScreen {

    double buttonLocX = Render2D.getWindowWidth()-250;
    double buttonLocYStart = Render2D.getWindowHeight()/2;
    double buttonLocYPer = 45;
    double buttonSizeX = 250;
    double buttonSizeY = 40;

    Button continueGame = new Button(buttonLocX,buttonLocYStart,buttonSizeX,buttonSizeY,"interface/text","interface/Click");
    Button newGame = new Button(buttonLocX,buttonLocYStart+buttonLocYPer,buttonSizeX,buttonSizeY,"interface/text","interface/Click");
    Button loadGame = new Button(buttonLocX,buttonLocYStart+buttonLocYPer*2,buttonSizeX,buttonSizeY,"interface/text","interface/Click");
    Button sets = new Button(buttonLocX,buttonLocYStart+buttonLocYPer*3,buttonSizeX,buttonSizeY,"interface/text","interface/Click");
    Button exitGame = new Button(buttonLocX,buttonLocYStart+buttonLocYPer*4,buttonSizeX,buttonSizeY,"interface/text","interface/Click");

    Button[] buttons = {continueGame,newGame,loadGame,sets,exitGame};
    String[] buttonsText = {"Продолжить","Новая игра","Редактор","Настройки","Выход"};

    @Override
    public void preLoad(){
        SetsScreen.loadSetsFile();
    }
    @Override
    public void input() {

    }

    @Override
    public void logic() {

    }

    public static boolean setsShow = false;
    @Override
    public void render() {

        Render2D.clearScreen();
        Render2D.simpleDraw("cursor","interface/StartScreen1",Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2,Render2D.getWindowWidth(),Render2D.getWindowHeight());
        ESCMenu.drawMenuButtons(buttons, buttonsText, buttonLocX, buttonLocYStart, buttonLocYPer);
        if(!AudioManager.isMusicPlaying()){
            AudioManager.playMusic("main");
        }

        Text.drawStringRigth("0.01",Render2D.getWindowWidth()-10,Render2D.getWindowHeight()-35,Text.CAMBRIA_24, Color.white);
        Render2D.simpleDraw("cursor","cursors/normal",InputMain.getCursorX()+16,InputMain.getCursorY()+16,32,32);
        if(setsShow){
            SetsScreen.work();
        }
        if(exitGame.isReleased()){
            System.exit(0);
        }
        if(sets.isReleased()){
            setsShow = true;
        }
        if(newGame.isReleased()){
            Start.setScreen(new LoadingScreen("Первая"));
            AudioManager.stopMusic();
        }
        if(loadGame.isReleased()){
            editorScreen.makeMap(5,5,"Западные горы","LimboRed","limb");
            Start.setScreen(new editorScreen());
            AudioManager.stopMusic();
        }

    }
}
