package screens.ingame;

import engine.Actor;
import engine.Screen;

import engine.Start;
import engine.control.InputMain;
import engine.render.Render2D;
import game.StartGame;
import game.actor.enviroment.*;
import game.actor.game.MusicControl;
import game.actor.game.Tile;
import game.actor.magic.Spell;
import game.actor.player.*;
import game.actor.story.Quest;
import org.lwjgl.input.Keyboard;
import screens.Controls;
import screens.editor.mainScreen.editorScreen;

import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_LIGHT1;


public class GameScreen extends Screen {

    public static Activator activator = new Activator(32,32,"Рычаг");
    public static Item item = new Item("", 0, 0, 64, 64, "Лук", 1);



    public GameScreen(){
    }

    @Override
    public void input() {
        gameScreenInterface.controlInterface();
        if (InputMain.isKeyJustReleased(Keyboard.KEY_P)) {
            Start.setScreen(new editorScreen());
        }
        Controls.cameraControl();
        Controls.playerChoos();
        Controls.timeControl();
        Controls.underCursorPlayerControl();
        Controls.menusOpenning();
    }

    @Override
    public void logic() {
        for (int i = 0; i < StartGame.game.gameSpeed; i++) {
            StartGame.game.time += 1*100;
            if (StartGame.game.time > StartGame.game.timeMax) {
                StartGame.game.time = 0;
            }
            if (Controls.playerOnCamera != -1) {
                StartGame.game.controlGroup.get(Controls.playerOnCamera).setOnCamera();
            }
            for(List<Actor> el : StartGame.game.allActorsList){
                for (Actor ele : el) {
                    ele.logic();
                }
            }
        }
        MusicControl.musicControl();
        for (AudioSource el : StartGame.game.audioSources) {
            el.logic();
        }
        deleteActorfrom();
    }

    @Override
    public void render() {
        Render2D.clearScreen();
        Tile.draws();
        for(List<Actor> el : StartGame.game.allActorsList){
            for (Actor ele : el) {
                ele.draw();
            }
        }

        for (Player player : StartGame.game.controlGroup) {
            player.drawProjectile();
            player.castingAnim();
        }
        for (Player el : StartGame.game.getNpcList()) {
            el.drawProjectile();
            el.castingAnim();
        }
        for (Structure el : StartGame.game.structuresList) {
            el.drawTransperent();
        }
        for (Player el : StartGame.game.getNpcList()) {
            PlayerDrawAndUndercursor.drawInterface(el);
        }
        Render2D.renderLight(StartGame.game.region.getAmbientLight());

        gameScreenInterface.drawInterface();
    }

    private static HashMap<Actor, List> deleteRequest = new HashMap<>();
    public static void addDeleteRequest(Actor delete, List removeFrom) {
        deleteRequest.put(delete, removeFrom);
    }

    private static void deleteActorfrom() {
        for (Actor el : deleteRequest.keySet()) {
            deleteRequest.get(el).remove(el);
        }
        deleteRequest.clear();
    }
}
