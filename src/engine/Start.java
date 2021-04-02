package engine;

import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.render.Render2D;
import engine.render.Window;
import engine.script.LoadScripts;
import game.StartGame;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;




public class Start {
    public static void Test(){
    }
    private static Screen screen = null;
    public static void setScreen(Screen set){
        screen = set;
        screen.preLoad();
    }
    public static void main(String[] args){
        Test();
        init();
        mainCycle();
        exit();
    }
    private static void init(){
        LoadScripts.loadScripts();
        Window.init();
        AudioManager.init();
        StartGame.init();
    }
    private static void mainCycle(){
        while(!Display.isCloseRequested()) {//Пока не будет получен запрос на закрытие окна
            InputMain.Control();
            screen.workScreen();
            Render2D.drawBright();
            Display.update();
            Display.sync(Sets.getScreenFPS());
        }
    }
    private static void exit(){
        Display.destroy();
        AL.destroy();
        System.exit(0);
    }

    public static Object getScreen() {
        return screen;
    }
}
