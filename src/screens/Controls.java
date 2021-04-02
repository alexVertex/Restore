package screens;

import engine.Actor;
import engine.Screen;
import engine.Start;
import engine.audio3.AudioManager;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import game.StartGame;
import game.actor.enviroment.Activator;
import game.actor.player.AutoControl;
import game.actor.player.PlayerDrawAndUndercursor;
import game.actor.player.PlayerMovesSet;
import org.lwjgl.input.Keyboard;
import screens.camp.*;
import screens.ingame.ESCMenu;
import screens.ingame.GameScreen;
import screens.ingame.gameScreenInterface;

import java.util.ArrayList;
import java.util.List;

public class Controls {
    private static final int CAMERA_SPEED_LOSS = 1;
    private static final int CAMERA_SPEED_MAX = 15;
    public static int[] chooseCampScreen = {Keyboard.KEY_1,Keyboard.KEY_2,Keyboard.KEY_3,Keyboard.KEY_4,Keyboard.KEY_5};
    public static int[] chooseControlPlayer = {Keyboard.KEY_F1,Keyboard.KEY_F2,Keyboard.KEY_F3,Keyboard.KEY_F4,Keyboard.KEY_F5};
    public static int openCampMenu = Keyboard.KEY_TAB;
    public static int openESCMenu = Keyboard.KEY_ESCAPE;
    public static int[] keysForCameraControl = {Keyboard.KEY_LEFT,Keyboard.KEY_UP,Keyboard.KEY_RIGHT,Keyboard.KEY_DOWN};
    public static int keyForCameraShift = Keyboard.KEY_LMENU;
    public static int keyForLockCameraOnPlayer = Keyboard.KEY_HOME;
    public static int[] keysForGameSpeedControl = {Keyboard.KEY_SPACE,Keyboard.KEY_SUBTRACT,Keyboard.KEY_ADD};
    public static int[] keysForPlayerSpeedControl = {Keyboard.KEY_Z,Keyboard.KEY_X,Keyboard.KEY_C};
    public static int keyForStopPlayer = Keyboard.KEY_V;
    public static int[] keysForPlayerAutoControl = {Keyboard.KEY_G,Keyboard.KEY_H,Keyboard.KEY_B,Keyboard.KEY_N};
    public static int[] keysForPlayerTargetOn = {Keyboard.KEY_A,Keyboard.KEY_S,Keyboard.KEY_D,Keyboard.KEY_F};
    public static int[] keysForPlayerGetSpell = {Keyboard.KEY_Q,Keyboard.KEY_W,Keyboard.KEY_E,Keyboard.KEY_R,Keyboard.KEY_T,Keyboard.KEY_Y,Keyboard.KEY_U,Keyboard.KEY_I};
    public static int[] keysForPlayerGetItem = {Keyboard.KEY_1,Keyboard.KEY_2,Keyboard.KEY_3,Keyboard.KEY_4,Keyboard.KEY_5,Keyboard.KEY_6,Keyboard.KEY_7,Keyboard.KEY_8};
    public static int keyForWeaponShift = Keyboard.KEY_J;
    public static int keyForShieldShift = Keyboard.KEY_K;



    private static camp_inventory inventoryScreen = new camp_inventory();
    private static camp_skills skillsScreen = new camp_skills();
    private static camp_spells spellsScreen = new camp_spells();
    private static camp_journal journalScreen = new camp_journal();
    private static camp_transition transitionScreen = new camp_transition();

    public static Screen[] campScreens = {inventoryScreen,skillsScreen,spellsScreen,journalScreen, transitionScreen};
    public static void campScreenChoose() {
        if(StartGame.game.playerInRessurectionPoint()) {
            for (int i = 0; i < chooseCampScreen.length; i++) {
                if (InputMain.isKeyJustReleased(chooseCampScreen[i])) {
                    Start.setScreen(campScreens[i]);
                    AudioManager.playSoundInterface("interface/CampChange");
                }
            }
        }
    }

    private static GameScreen gameScreen = new GameScreen();
    public static void closeCampScreen(){
        if (InputMain.isKeyJustReleased(openCampMenu) || InputMain.isKeyJustReleased(openESCMenu)) {
            Start.setScreen(gameScreen);
            AudioManager.playSoundInterface("interface/Cancel");
        }
    }

    public static boolean playerChoos() {
        for(int i = 0; i < chooseControlPlayer.length;i++){
            if (InputMain.isKeyJustReleased(chooseControlPlayer[i]) && StartGame.game.controlGroup.size() > i) {
                StartGame.game.setChoosenHero(i);
                return true;
            }
        }
        return false;
    }

    public static int playerOnCamera = -1;
    private static final int CAMERA_SPEED_WHEEL = 1;
    private static final int CAMERA_SPEED_KEYBOARD = 2;
    private static final int CAMERA_SPEED_MOUSE = 2;
    private static final int CAMERA_OFFSET_MOUSE_BORDER = 5;
    private static final int[][] controlCameraDirections = {{1,0},{0,1},{-1,0},{0,-1}};
    static int moveCameraSpeedX= 0;
    static int moveCameraSpeedY= 0;

    public static void cameraControl() {
        List<Integer> directions = new ArrayList<>();
        int moveCameraSpeedAdd = 0;
        for(int i = 0; i < keysForCameraControl.length;i++){
            if (InputMain.isKeyPressed(keysForCameraControl[i])) {
                directions.add(i);
                moveCameraSpeedAdd = CAMERA_SPEED_KEYBOARD;
            }
        }
        if (InputMain.getCursorX() < CAMERA_OFFSET_MOUSE_BORDER) {
            directions.add(0);
            moveCameraSpeedAdd = CAMERA_SPEED_MOUSE;
        }
        if (InputMain.getCursorX() > Render2D.getWindowWidth() - CAMERA_OFFSET_MOUSE_BORDER) {
            directions.add(2);moveCameraSpeedAdd = CAMERA_SPEED_MOUSE;
        }
        if (InputMain.getCursorY() < CAMERA_OFFSET_MOUSE_BORDER) {
            directions.add(1);moveCameraSpeedAdd = CAMERA_SPEED_MOUSE;
        }
        if (InputMain.getCursorY() > Render2D.getWindowHeight() - CAMERA_OFFSET_MOUSE_BORDER) {
            directions.add(3);moveCameraSpeedAdd = CAMERA_SPEED_MOUSE;
        }

        if(directions.size() > 0){
            for(Integer el : directions){
                moveCameraSpeedX += controlCameraDirections[el][0]*moveCameraSpeedAdd;
                moveCameraSpeedY += controlCameraDirections[el][1]*moveCameraSpeedAdd;
            }
            playerOnCamera = -1;
        }
        if(moveCameraSpeedX > CAMERA_SPEED_MAX) moveCameraSpeedX = CAMERA_SPEED_MAX;
        if(moveCameraSpeedX < -CAMERA_SPEED_MAX) moveCameraSpeedX = -CAMERA_SPEED_MAX;
        if(moveCameraSpeedY > CAMERA_SPEED_MAX) moveCameraSpeedY = CAMERA_SPEED_MAX;
        if(moveCameraSpeedY < -CAMERA_SPEED_MAX) moveCameraSpeedY = -CAMERA_SPEED_MAX;
        moveCameraSpeedX -= Math.signum(moveCameraSpeedX)*CAMERA_SPEED_LOSS;
        moveCameraSpeedY -= Math.signum(moveCameraSpeedY)*CAMERA_SPEED_LOSS;

        Camera.changeLocation(moveCameraSpeedX, moveCameraSpeedY);

        if (InputMain.isKeyPressed(InputMain.WMB) || InputMain.isKeyPressed(keyForCameraShift)) {
            Camera.changeLocation(InputMain.getCursorDX() * CAMERA_SPEED_WHEEL, InputMain.getCursorDY() * CAMERA_SPEED_WHEEL);
            playerOnCamera = -1;
        }
        if (InputMain.isKeyJustReleased(keyForLockCameraOnPlayer)) {
            playerOnCamera = StartGame.game.getChoosenHero();
            AudioManager.playSoundInterface("interface/Click");

        }
    }

    private static final double SPEED_NORMAL = 1;
    private static final double SPEED_FASTER = 2;
    private static final double[] gameSpeeds = {0,SPEED_NORMAL,SPEED_FASTER};
    public static void timeControl() {
        for(int i = 0; i < keysForGameSpeedControl.length;i++){
            if (InputMain.isKeyJustReleased(keysForGameSpeedControl[i])) {
                AudioManager.playSoundInterface("interface/Clock");
                if (i == 0) {
                    StartGame.game.pauseWorks();
                } else {
                    StartGame.game.gameSpeed = gameSpeeds[i];
                }
            }
        }
    }

    private static int[] autocontrolStates = {AutoControl.BATTLEMODE_BLOCK, AutoControl.BATTLEMODE_WAIT, AutoControl.BATTLEMODE_ATTACK, AutoControl.BATTLEMODE_OVERWATCH};
    public static void underCursorPlayerControl() {
        for(int i = 0; i < keysForPlayerSpeedControl.length;i++) {
            if (InputMain.isKeyJustReleased(keysForPlayerSpeedControl[i])) {
                PlayerMovesSet.setSpeed(StartGame.game.getControled(), i);
                AudioManager.playSoundInterface("interface/Click");

            }
        }
        if (InputMain.isKeyJustReleased(keyForStopPlayer) || InputMain.isKeyJustReleased(InputMain.RMB)) {
            PlayerMovesSet.stop(StartGame.game.getControled());
        }
        for(int i = 0; i < keysForPlayerAutoControl.length;i++) {
            if (InputMain.isKeyJustReleased(keysForPlayerAutoControl[i])) {
                AutoControl.changeBattleMode(StartGame.game.getControled(), autocontrolStates[i]);
                AudioManager.playSoundInterface("interface/Click");

            }
        }
    }

    public static void menusOpenning() {
        if (InputMain.isKeyJustReleased(openCampMenu)) {
            openJournal();
            AudioManager.playSoundInterface("interface/Click");
        }
        if (InputMain.isKeyJustReleased(openESCMenu)) {
            Start.setScreen(new ESCMenu());
            AudioManager.playSoundInterface("interface/Click");

        }
    }

    public static void openJournal(){
        if(StartGame.game.playerInRessurectionPoint()){
            Start.setScreen(inventoryScreen);
            StartGame.game.setRessurectionPoint();
        } else {
            Start.setScreen(journalScreen);
        }
    }

    private static int[] targetsOn = {gameScreenInterface.TARGET_HEAD,gameScreenInterface.TARGET_TORSO,gameScreenInterface.TARGET_ARMS,gameScreenInterface.TARGET_LEGS};
    public static int getTargetOn() {
        int targetOn = -1;
        for(int i = 0; i < keysForPlayerTargetOn.length;i++) {
            if (InputMain.isKeyPressed(keysForPlayerTargetOn[i])) {
                targetOn = targetsOn[i];
            }
        }
        return targetOn;
    }

    public static String getUnderkeyboardItem() {
        for(int i = 0; i < keysForPlayerGetItem.length;i++) {
            if (InputMain.isKeyJustPressed(keysForPlayerGetItem[i])) {
                AudioManager.playSoundInterface("interface/Click");
                return "uses"+(i+1);
            }
        }
        return null;
    }

    public static String getUnderkeyboardSpell() {
        for(int i = 0; i < keysForPlayerGetSpell.length;i++) {
            if (InputMain.isKeyJustPressed(keysForPlayerGetSpell[i])) {
                AudioManager.playSoundInterface("interface/Click");
                return "spells"+(i+1);
            }
        }
        return null;
    }

    public static void keyboardChangeGear() {
        if (InputMain.isKeyJustPressed(keyForWeaponShift)) {
            PlayerDrawAndUndercursor.changeWeaponShield(StartGame.game.getControled(),"weapon");
        }
        if (InputMain.isKeyJustPressed(keyForShieldShift)) {
            PlayerDrawAndUndercursor.changeWeaponShield(StartGame.game.getControled(),"shield");
        }
    }
}
