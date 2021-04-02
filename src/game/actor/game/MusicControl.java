package game.actor.game;

import engine.Mathp;
import engine.audio3.AudioManager;
import game.StartGame;
import game.actor.player.Player;

import static game.actor.player.PlayerMovesSet.MOVE_TARGET_ENEMY;

public class MusicControl {
    private static String music = null;
    private static int timeBeforeChangeMusic = 100;
    static int currentState = 0;
    private static boolean isPlayerAttacked(){
        boolean playerAttacked = false;
        for (Player el : StartGame.game.getNpcList()) {
            if (el.getVariable("dead").equals(1)) continue;
            Object target = el.getVariable("moveTargetAttack");
            if (target instanceof Player && el.getVariable("moveTargetType").equals( MOVE_TARGET_ENEMY)) {
                if (StartGame.game.controlGroup.contains(target)) {
                    return true;
                }
            }
            if(currentState == 1){
                if(el.getVariableInteger("alarmed") == 2 && el.isVisible()){
                    return true;
                }
            }
        }
        return false;
    }

    private static void setCurrentState(){
        boolean isAttacked = isPlayerAttacked();
        boolean isGameOver = StartGame.game.allPlayersIsDead();
        if(isGameOver) {
            currentState = 2;
        } else if(isAttacked){
            currentState = 1;
        } else {
            currentState = 0;
        }
    }

    private static float musicVolumeFade = 1.0f;
    private static float musicVolumeFadeAdd = 0.0f;

    public static void musicControl() {
        int oldState = currentState;
        setCurrentState();
        if (currentState < 2 && !AudioManager.isMusicPlaying()) {
            if(currentState == 1){
                String music = StartGame.game.region.getMusic(true);
                AudioManager.playMusic(music);
            } else {
                String music = StartGame.game.region.getMusic(false);
                AudioManager.playMusic(music);
            }
        }
        if(oldState != currentState){
            if(currentState == 2){
                AudioManager.playMusic("gameOver");
            } else if(currentState == 1){
                if(musicVolumeFade == 1) {
                    String music = StartGame.game.region.getMusic(true);
                    AudioManager.playMusic(music);
                } else {
                    musicVolumeFadeAdd = +0.004f;
                }
            } else {
                if(oldState == 1 ){
                    musicVolumeFadeAdd = -0.004f;
                }
                if(oldState == 2 ){
                    musicVolumeFadeAdd = -0.008f;
                }
            }
        }
        if(musicVolumeFadeAdd > 0){
            musicVolumeFade += musicVolumeFadeAdd;
            if(musicVolumeFade > 1){
                musicVolumeFade = 1;
                musicVolumeFadeAdd = 0;
            }
            AudioManager.setMusicVolume(musicVolumeFade);
        } else  if (musicVolumeFadeAdd < 0){
            musicVolumeFade += musicVolumeFadeAdd;
            if(musicVolumeFade < 0){
                musicVolumeFade = 1;
                musicVolumeFadeAdd = 0;
                AudioManager.stopMusic();
            }
            AudioManager.setMusicVolume(musicVolumeFade);
        }
    }
}
