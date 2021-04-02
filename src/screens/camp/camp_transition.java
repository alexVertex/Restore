package screens.camp;

import engine.Mathp;
import engine.Start;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.control.interfaces.Scroll;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.enviroment.RessurectPoint;
import game.actor.player.Player;
import game.actor.story.Quest;
import game.actor.story.Target;
import org.newdawn.slick.Color;
import screens.Controls;
import screens.ingame.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class camp_transition extends GameScreen {
    private Scroll questScroll = new Scroll(Render2D.getWindowWidth() / 2 + 282, Render2D.getWindowHeight() / 2 + 174, 32, 165, "interface/scroller");
    private Scroll targetScroll = new Scroll(Render2D.getWindowWidth() / 2 - 20, Render2D.getWindowHeight() / 2, 32, 510, "interface/scroller");

    String currentMap = "", currentPoint = "",currentMapFile = "";
    RessurectPoint choosen = null;
    @Override
    public void input() {

    }

    @Override
    public void logic() {

    }

    @Override
    public void render() {
        super.render();
        float wide = 600;
        float high = 516;
        camp_Base.render(4);
        Render2D.angleDraw("campBorder5", "interface/borderBack", Render2D.getWindowWidth() / 2, Render2D.getWindowHeight() / 2, high, 4, 90);
        Text.drawStringCenter("Локация",Render2D.getWindowWidth() / 2-wide/4,Render2D.getWindowHeight()/2-high/2+10,Text.CAMBRIA_24,Color.white );
        Text.drawStringCenter("Точки отдыха",Render2D.getWindowWidth() / 2+wide/4,Render2D.getWindowHeight()/2-high/2+10,Text.CAMBRIA_24,Color.white );

        Render2D.simpleDraw("cursor", "cursors/normal", InputMain.getCursorX() + 16, InputMain.getCursorY() + 16, 32, 32);

        List<String> mapsIn = new ArrayList<>();


        for(RessurectPoint el : StartGame.game.openRestPoints){
            String mapName = el.getVariableString("mapTo");
            if(!mapsIn.contains(mapName)){
                mapsIn.add(mapName);
                Text.drawString(mapName,Render2D.getWindowWidth() / 2-wide/2+10,Render2D.getWindowHeight()/2-high/2+25+mapsIn.size()*20,Text.CAMBRIA_14,Color.lightGray );
                if(Mathp.inRectangle(Render2D.getWindowWidth() / 2-wide/2,Render2D.getWindowHeight()/2-high/2+25+30-10,Render2D.getWindowWidth() / 2,Render2D.getWindowHeight()/2-high/2+25+20+10,InputMain.getCursorX(),InputMain.getCursorY())){
                    Text.drawString(mapName,Render2D.getWindowWidth() / 2-wide/2+10,Render2D.getWindowHeight()/2-high/2+25+mapsIn.size()*20,Text.CAMBRIA_14,Color.white );
                    if(InputMain.isKeyJustReleased(InputMain.LMB)){
                        currentMap = mapName;
                    }

                }
                if(currentMap.equals(mapName))
                    Text.drawString(mapName,Render2D.getWindowWidth() / 2-wide/2+10,Render2D.getWindowHeight()/2-high/2+25+mapsIn.size()*20,Text.CAMBRIA_14,Color.red );

            }
        }
        int i = 0;
        for(RessurectPoint el : StartGame.game.openRestPoints){
            String toName = el.getVariableString("transitionTo");
            if(el.getVariable("mapTo").equals(currentMap) && !StartGame.game.lastRessurect.getVariable("name").equals(el.getVariable("transitionTo"))){
                Text.drawString(toName,Render2D.getWindowWidth() / 2+10,Render2D.getWindowHeight()/2-high/2+25+(i+1)*20,Text.CAMBRIA_14,Color.lightGray );
                if(Mathp.inRectangle(Render2D.getWindowWidth() / 2,Render2D.getWindowHeight()/2-high/2+25+10-10+(i+1)*20,Render2D.getWindowWidth() / 2 + wide/2,Render2D.getWindowHeight()/2-high/2+25+10+(i+1)*20,InputMain.getCursorX(),InputMain.getCursorY())){
                    Text.drawString(toName,Render2D.getWindowWidth() / 2+10,Render2D.getWindowHeight()/2-high/2+25+(i+1)*20,Text.CAMBRIA_14,Color.white );
                    if(InputMain.isKeyJustReleased(InputMain.LMB)){
                        currentPoint = toName;
                        currentMapFile = el.getVariableString("mapToFileName");

                    }
                }
                if(currentPoint.equals(toName))
                    Text.drawString(currentPoint,Render2D.getWindowWidth() / 2+10,Render2D.getWindowHeight()/2-high/2+25+(i+1)*20,Text.CAMBRIA_14,Color.red );
                i++;
            }

        }

        if(currentPoint != ""){
            Text.drawStringCenter("Нажмите здесь, чтобы перейти на другую точку отдыха",Render2D.getWindowWidth() / 2,Render2D.getWindowHeight()/2+high/2+20,Text.CAMBRIA_24,Color.lightGray );
            if(Mathp.inRectangle(Render2D.getWindowWidth() / 2-wide/2,Render2D.getWindowHeight()/2+high/2+20-10+10,Render2D.getWindowWidth() / 2 + wide/2,Render2D.getWindowHeight()/2+high/2+20+10+10,InputMain.getCursorX(),InputMain.getCursorY())){
                Text.drawStringCenter("Нажмите здесь, чтобы перейти на другую точку отдыха",Render2D.getWindowWidth() / 2,Render2D.getWindowHeight()/2+high/2+20,Text.CAMBRIA_24,Color.white );
                if(InputMain.isKeyJustReleased(InputMain.LMB)){
                    changeLocation();
                }
            }

        }
    }

    private void changeLocation(){

        StartGame.game.loadMap(currentMapFile);
        for(RessurectPoint ele : StartGame.game.ressurections){
            if(ele.getVariable("name").equals(currentPoint)){
                choosen = ele;
                break;
            }
        }
        for(Player el : StartGame.game.controlGroup) {
            el.setVariable("locX", (double) choosen.getVariableInteger("locX"));
            el.setVariable("locY", (double) choosen.getVariableInteger("locY"));
        }
        choosen = null;
        currentPoint = "";
        StartGame.game.controlGroup.get(0).setOnCamera();
        Start.setScreen(new GameScreen());
    }
}
