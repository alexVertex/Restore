package screens.ingame;

import engine.Screen;
import engine.Start;
import engine.audio3.AudioManager;
import engine.render.Render2D;
import engine.render.Text;
import engine.render.TextureControl;
import game.StartGame;
import game.actor.game.Game;
import org.newdawn.slick.Color;

import java.io.File;

public class LoadingScreen extends Screen {
    int stage = 0;
    String mapLoading = "";
    public LoadingScreen(){

    }
    public LoadingScreen(String map){
        stage = 3;
        mapLoading = map;
    }
    @Override
    public void input() {

    }

    @Override
    public void logic() {
        if(stage == 0) {
            stage = 1;
        } else
        if(stage == 1){
            stage = 2;
            AudioManager.preloadMusicBuffer("main");
            AudioManager.preloadMusicBuffer("gameOver");
            File folder = new File("res/tex/interface");
            File[] folderEntries = folder.listFiles();
            String[] fileNames = new String[folderEntries.length];
            for (int i = 0; i < folderEntries.length; i++) {
                String name = folderEntries[i].getName();
                if(name.contains(".")) {
                    name = name.substring(0, folderEntries[i].getName().length() - 4);
                    fileNames[i] = name;
                } else {
                    fileNames[i] = "null";
                }
            }
            for(String  el : fileNames){
                if (!el.equals("null")) {
                    Render2D.simpleDraw("interface/"+el,-0,-0,500,500);
                }
            }
            Start.setScreen(new MainMenu());
        }
        if(stage == 3){
            stage = 4;
        } else
        if(stage == 4){
            StartGame.game.loadMap(mapLoading);
            Start.setScreen(new GameScreen());
        }
    }

    @Override
    public void render() {
        Render2D.clearScreen();
        Render2D.simpleDraw("cursor","interface/StartScreen1",Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2,Render2D.getWindowWidth(),Render2D.getWindowHeight());
        Text.drawStringRigth("Загрузка",Render2D.getWindowWidth()-50,Render2D.getWindowHeight()-50,Text.CAMBRIA_34, Color.lightGray);
    }
}
