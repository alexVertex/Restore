package engine.audio3;

import engine.Mathp;
import engine.render.Camera;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.openal.AudioLoader;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.openal.ALC10.*;

public class AudioManager {
    private static long device;

    private static long context;

    private static SoundListener listener;

    private static Map<String, SoundBuffer> soundBufferList;

    private static Map<String, SoundSource> soundSourceMap;

    static SoundSource music = null;



    public static void init() {
        try {
            AL.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        listener = new SoundListener();
        soundBufferList = new HashMap<>();
        soundSourceMap = new HashMap<>();
        music = new SoundSource(false,false);
    }
    public static void playMusic(String name){
        int bufferID = 0;
        if(soundBufferList.containsKey(name)){
            bufferID = soundBufferList.get(name).getBufferId();

        } else {
            String path = "res/mus/"+name+".ogg";
            SoundBuffer sb = new SoundBuffer(path);
            soundBufferList.put(name,sb);
            bufferID = sb.getBufferId();
        }
        music.setBuffer(bufferID);
        music.play();
    }
    public static void preloadMusicBuffer(String name){
        if(!soundBufferList.containsKey(name)){
            String path = "res/mus/"+name+".ogg";
            SoundBuffer sb = new SoundBuffer(path);
            soundBufferList.put(name,sb);
        }
    }
    public static void preloadSoundBuffer(String name){
        if(!soundBufferList.containsKey(name)){
            String path = "res/snd/"+name+".ogg";
            SoundBuffer sb = new SoundBuffer(path);
            soundBufferList.put(name,sb);
        }
    }
    public static void stopMusic(){
        music.stop();
    }
    public static void setMusicVolume(double volume){
        music.setGain((float) volume*musicVolumeMult);
    }
    public static boolean isMusicPlaying(){
        return music.isPlaying();
    }

    private static int currentInterfaceSource = 0;
    private static int maxInterfaceSources = 10;
    public static SoundSource playSoundInterface(String name) {
        String namesource = "interface" + currentInterfaceSource;
        SoundSource sr = null;
        if(soundSourceMap.containsKey(namesource)){
            sr = soundSourceMap.get(namesource);
        } else {
            sr = new SoundSource(false,false);
            soundSourceMap.put(namesource,sr);
        }
        int bufferID = 0;
        if(soundBufferList.containsKey(name)){
            bufferID = soundBufferList.get(name).getBufferId();
        } else {
            String path = "res/snd/"+name+".ogg";
            SoundBuffer sb = new SoundBuffer(path);
            soundBufferList.put(name,sb);
            bufferID = sb.getBufferId();
        }
        sr.setBuffer(bufferID);
        sr.setGain(interfaceVolumeMult);
        sr.play();
        currentInterfaceSource++;
        if(currentInterfaceSource >= maxInterfaceSources){
            currentInterfaceSource = 0;
        }
        return sr;
    }
    static int currentSource;

    public static SoundSource playSound(String name) {
        String namesource = "game" + currentSource;
        SoundSource sr = null;
        if(soundSourceMap.containsKey(namesource)){
            sr = soundSourceMap.get(namesource);
        } else {
            sr = new SoundSource(false,false);
            soundSourceMap.put(namesource,sr);
        }
        int bufferID = 0;
        if(soundBufferList.containsKey(name)){
            bufferID = soundBufferList.get(name).getBufferId();
        } else {
            String path = "res/snd/"+name+".ogg";
            SoundBuffer sb = new SoundBuffer(path);
            soundBufferList.put(name,sb);
            bufferID = sb.getBufferId();
        }
        sr.setBuffer(bufferID);
        sr.play();
        currentSource++;
        if(currentSource >= 20){
            currentSource = 0;
        }
        return sr;
    }
    private static int currentAmbientSource = 0;
    private static int maxAmbientSources = 30;
    public static String playSoundAmbient(String name) {
        String namesource = "ambient" + currentAmbientSource;
        SoundSource sr = null;
        if(soundSourceMap.containsKey(namesource)){
            sr = soundSourceMap.get(namesource);
        } else {
            sr = new SoundSource(true,false);
            soundSourceMap.put(namesource,sr);
        }
        int bufferID = 0;
        if(soundBufferList.containsKey(name)){
            bufferID = soundBufferList.get(name).getBufferId();
        } else {
            String path = "res/snd/"+name+".ogg";
            SoundBuffer sb = new SoundBuffer(path);
            soundBufferList.put(name,sb);
            bufferID = sb.getBufferId();
        }
        sr.setBuffer(bufferID);
        sr.play();
        currentAmbientSource++;
        if(currentAmbientSource >= maxAmbientSources){
            currentAmbientSource = 0;
        }
        return namesource;
    }
    public static void stopAmbient(String name){
        SoundSource sr = null;
        if(soundSourceMap.containsKey(name)){
            sr = soundSourceMap.get(name);
            sr.stop();
        }
    }

    public static boolean workAmbient(String sound, int locX, int locY, int rast) {
        SoundSource sr = null;
        if(soundSourceMap.containsKey(sound)){
            sr = soundSourceMap.get(sound);
            double rastCur = Mathp.rast(Display.getWidth() / 2, Display.getHeight() / 2, locX + Camera.locX, locY + Camera.locY);
            double koefRast = 1 - (rastCur / rast);
            if (koefRast < 0) koefRast = 0;

            koefRast*=koefRast;
            if(koefRast>0) {
                sr.setGain((float) koefRast);
                return true;
            } else {
                sr.setGain((float) 0);
            }
        }

        return false;
    }

    static float musicVolumeMult = 1;
    static float interfaceVolumeMult = 1;
     static float soundVolumeMult = 1;

    public static void setMusicVolumeSet(float set) {
        musicVolumeMult = set;
        setMusicVolume(set);
    }

    public static void setInterfaceVolumeSet(float set) {
        interfaceVolumeMult = set;
    }
    public static void setSoundVolumeSet(float set) {
        soundVolumeMult = set;
    }
    public static float getSoundVolumeSet() {
       return soundVolumeMult;
    }
}
