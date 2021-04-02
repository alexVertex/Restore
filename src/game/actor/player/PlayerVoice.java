package game.actor.player;

import engine.Actor;
import engine.Mathp;
import engine.audio3.AudioManager;
import engine.audio3.SoundSource;
import engine.render.Camera;
import org.lwjgl.opengl.Display;

public class PlayerVoice {

    public static final int VOICE_CANT_RUN_CRIPLEG = 4;
    public static final int VOICE_CANT_RUN_OVERLOAD = 5;
    public static int VOICE_CANT_RUN_STAMINA = 9;

    public static int VOICE_TARGET_ATTACK = 10;
    public static int VOICE_TARGET_CAST = 11;
    public static int VOICE_TARGET_LOOT = 12;
    public static int VOICE_TARGET_MOVE = 13;
    public static int VOICE_TARGET_OPEN = 14;

    public static final int VOICE_CHANGE_GEAR = 6;

    public static int VOICE_AUTOATTACK = 0;
    public static int VOICE_CANTCAST_STAMINA = 1;
    public static int VOICE_CANT_FIND_WAY = 2;
    public static int VOICE_CANT_OPEN = 3;
    public static int VOICE_KILLED = 7;
    public static int VOICE_LOSS_TARGET = 8;


    static String[] voicesStrings = {"AutoAttack","CantCast","CantFindWay","CantOpen","CantRun","CantRunOverload",
            "ChangeGear","KilledEnemy","LossTarget","NoStamina","TargetAttack","TargetCast","TargetLoot","TargetMove","TargetOpen"};
    static double[] voicesSize = {2,5,3,2,2,3,2,3,3,2,5,5,5,5,5};
    static boolean[] voicesWild = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};

    public static void setVoiceInterface(Player player, int voice){
        if(player instanceof NPC) return;
        if((player.getVariable("voiceSource")) == null || !((SoundSource)player.getVariable("voiceSource")).isPlaying() || voicesWild[voice]){

            double current = (Mathp.random()*voicesSize[voice])+1;
            int currentInt = (int) current;
            String playerVoiceBank = player.getVariableString("voice");
            if(playerVoiceBank.equals("null")) return;
            SoundSource sr = AudioManager.playSoundInterface("playerVoice/"+playerVoiceBank+"/"+voicesStrings[voice]+"/"+currentInt);
            player.setVariable("voiceSource",sr);
            player.setVariable("voiceTimeWait",500);
        }
    }
    static String[] voicesIngameStrings = {"getHited","getDied","getAttacked"};
    public static void setVoiceIngame(Player player, int voice, boolean forceChange){
        if(player.getVariable("voiceSourceInGame") != null){
            SoundSource st = (SoundSource) player.getVariable("voiceSourceInGame");
            if(st.isPlaying() && !forceChange){
                return;
            }
        }
        double current = (Mathp.random()*4)+1;
        int currentInt = (int) current;
        String playerVoiceBank = player.getVariableString("voice");
        if(playerVoiceBank.equals("null")) return;
        SoundSource sr = AudioManager.playSound("playerVoice/"+playerVoiceBank+"/"+voicesIngameStrings[voice]+"/"+currentInt);
        player.setVariable("voiceSourceInGame",sr);
        workVoiceInGame(player);
    }
    public static void setVoiceIngame(Actor player, String voice, boolean forceChange){
        if(player.getVariable("voiceSourceInGame") != null){
            SoundSource st = (SoundSource) player.getVariable("voiceSourceInGame");
            if(st.isPlaying() && !forceChange){
                return;
            }
        }
        SoundSource sr = AudioManager.playSound(voice);
        player.setVariable("voiceSourceInGame",sr);
        workVoiceInGame(player);
    }
    public static void workVoiceInGame(Actor player){
        if(player.getVariable("voiceTimeWait")!= null)
            player.setVariable("voiceTimeWait",player.getVariableInteger("voiceTimeWait")-1);
        if(player.getVariable("voiceSourceInGame") != null){
            SoundSource st = (SoundSource) player.getVariable("voiceSourceInGame");
            if(!st.isPlaying()){
                return;
            }
            double rastCur = Mathp.rast(Display.getWidth() / 2, Display.getHeight() / 2, player.getVariableDouble("locX") + Camera.locX, player.getVariableDouble("locY") + Camera.locY);
            double volume = 1-rastCur/512.0;
            if(volume < 0) volume = 0;
            volume *= AudioManager.getSoundVolumeSet();
            st.setGain((float) volume);
        }
    }
}
