package game.actor.player;

import engine.Actor;
import engine.Mathp;
import game.StartGame;
import game.actor.enviroment.Activator;
import game.actor.enviroment.Chest;
import game.actor.enviroment.Door;
import game.actor.enviroment.Item;
import game.actor.story.Event;
import org.lwjgl.util.Point;
import screens.ingame.GameScreen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerMovesSet {
    static final int MOVE_TARGET_NON = 0;
    public static final int MOVE_TARGET_ENEMY = 1;
    static final int MOVE_TARGET_LOOT = 2;
    static final int MOVE_TARGET_ITEM = 3;
    static final int MOVE_TARGET_CHEST = 4;
    static final int MOVE_TARGET_DOOR = 5;
    static final int MOVE_TARGET_ACTIVATOR = 6;
    public static final int MOVE_TARGET_CAST = 7;

    public static void stop(Player player) {
        player.setVariable("tarX", player.getVariable("locX"));
        player.setVariable("tarY", player.getVariable("locY"));
        player.setVariable("moveTargetType", MOVE_TARGET_NON);
        player.setVariable("path", null);
        player.setVariable("partX", 1);
    }

    private static void setPath(Player player, double tarX, double tarY) {
        List<Point> path = PathFinding.start(player.getTileLoc()[0], player.getTileLoc()[1], tarX, tarY, false);
        player.setVariable("path", path);
        player.setVariable("pathPoint", 0);
        if(path == null){
            PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_CANT_FIND_WAY);
            player.setVariable("moveTargetType", MOVE_TARGET_NON);
            player.setVariable("moveTargetAttack", null);

        }
    }
    private static void setPath(Player player, List<Point> path) {
        player.setVariable("path", path);
        player.setVariable("pathPoint", 0);
    }


    private static way getPath(Player player, double tarX, double tarY, boolean needFindWay) {
        List<Point> path = PathFinding.start(player.getTileLoc()[0], player.getTileLoc()[1], tarX, tarY,needFindWay);
        player.setVariable("path", path);
        player.setVariable("pathPoint", 0);
        int lenght = 0;
        if(path != null) {
            for (int i = 0; i < path.size() - 1; i++) {
                lenght += Mathp.rast(path.get(i).getX(), path.get(i).getY(), path.get(i + 1).getX(), path.get(i + 1).getY());
            }
        }
        way Way = new way();
        Way.path = path;
        Way.length = lenght;
        return Way;
    }
    public static void setTarget(Player player, double tarX, double tarY) {
        int finX = (int)((tarX+16)/32);
        int finY = (int)((tarY+16)/32);
        if(finX < 0 ){
            finX = 0;
        }
        if(finY < 0 ){
            finY = 0;
        } if( finX > StartGame.game.sizeX-1 ){
            finX = StartGame.game.sizeX -1;
        }
        if(finY > StartGame.game.sizeY-1){
            finY = StartGame.game.sizeY-1;
        }
        player.setVariable("tarX", finX*32.0);
        player.setVariable("tarY", finY*32.0);
        player.setVariable("moveTargetType", MOVE_TARGET_NON);
        setPath(player, tarX, tarY);
        PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_TARGET_MOVE);

    }

    public static void setTargetAttack(Player player, Player target, int targetOn) {
        if (target.getVariable("dead").equals(1)) {
            player.setVariable("tarX", target.getTileLoc()[0]);
            player.setVariable("tarY", target.getTileLoc()[1]);
            player.setVariable("moveTargetType", MOVE_TARGET_LOOT);
            player.setVariable("moveTarget", target);
            setPath(player, target.getTileLoc()[0], target.getTileLoc()[1]);
            PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_TARGET_LOOT);
            return;
        }

        player.setVariable("targetOn", targetOn);
        setPath(player, target.getTileLoc()[0], target.getTileLoc()[1]);
        if( player.getVariable("moveTargetAttack") == null) {
            if (target.getVariableInteger("good") == 0)
                PlayerVoice.setVoiceInterface(player, PlayerVoice.VOICE_TARGET_ATTACK);
            else
                PlayerVoice.setVoiceInterface(player, PlayerVoice.VOICE_TARGET_MOVE);
        }
        player.setVariable("moveTargetAttack", target);
        player.setVariable("moveTargetType", MOVE_TARGET_ENEMY);

    }

    public static void setTargetPickUp(Player player, Item undercursor) {
        player.setVariable("tarX", undercursor.getTileLoc()[0]);
        player.setVariable("tarY", undercursor.getTileLoc()[1]);
        player.setVariable("moveTargetType", MOVE_TARGET_ITEM);
        player.setVariable("moveTarget", undercursor);
        setPath(player, undercursor.getTileLoc()[0], undercursor.getTileLoc()[1]);
        PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_TARGET_MOVE);

    }

    public static void setTargetOpenChest(Player player, Chest undercursor) {
        player.setVariable("tarX", undercursor.getOpenLoc()[0]);
        player.setVariable("tarY", undercursor.getOpenLoc()[1]);
        player.setVariable("moveTargetType", MOVE_TARGET_CHEST);
        player.setVariable("moveTarget", undercursor);
        setPath(player, undercursor.getOpenLoc()[0], undercursor.getOpenLoc()[1]);
        PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_TARGET_OPEN);

    }

    private static int[][] doorOffsets = {{32,0},{-32,0},{0,32},{0,-32}};
    static class way {
        List<Point> path;
        int length = 0;
        double tar[];
    }
    public static void setTargetOpenDoor(Player player, Door undercursor) {
        List<way> ways = new ArrayList<>();
        double[] target = undercursor.getTileLoc();
        for(int i = 0; i < doorOffsets.length;i++){

            way Way = getPath(player, target[0]+doorOffsets[i][0], target[1]+doorOffsets[i][1],true);
            Way.tar = new double[]{target[0]+doorOffsets[i][0],target[1]+doorOffsets[i][1]};
            if(Way.path != null && Way.path.size() != 0){
                ways.add(Way);
            }
        }
        ways.sort(Comparator.comparingInt(o -> o.length));
        if(ways.size() > 0){
            player.setVariable("tarX", ways.get(0).tar[0]);
            player.setVariable("tarY", ways.get(0).tar[1]);
            player.setVariable("moveTargetType", MOVE_TARGET_DOOR);
            player.setVariable("moveTarget", undercursor);
            setPath(player,ways.get(0).path);
        }
        PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_TARGET_OPEN);

    }

    public static void setTargetActivator(Player player, Activator undercursor) {
        player.setVariable("tarX", undercursor.getTileLoc()[0]);
        player.setVariable("tarY", undercursor.getTileLoc()[1]);
        player.setVariable("moveTargetType", MOVE_TARGET_ACTIVATOR);
        player.setVariable("moveTarget", undercursor);
        setPath(player, undercursor.getTileLoc()[0], undercursor.getTileLoc()[1]);
        PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_TARGET_OPEN);

    }

    private static String[] speedNames = {"speedRun", "speedWalk", "speedCrouch"};

    public static void setSpeed(Player player, int speed) {
        if(speed == 0 && player.getVariable("stamina").equals(0.0)){
            speed = 1;
        }
        player.setVariable("speedGear", speed);
        player.setVariable("speed", player.getVariable(speedNames[speed]));
        PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_CHANGE_GEAR);

    }

    static void activate(Player player) {
        ((Activator) player.getVariable("moveTarget")).useActivator(player);
        player.setVariable("moveTarget", null);
        player.setVariable("moveTargetType", MOVE_TARGET_NON);
    }

    static void openDoor(Player player) {
        ((Door) player.getVariable("moveTarget")).useDoor(player);
        player.setVariable("moveTarget", null);
        player.setVariable("moveTargetType", MOVE_TARGET_NON);
    }

    static void openChest(Player player) {
        ((Chest) player.getVariable("moveTarget")).open(player);
        player.setVariable("moveTarget", null);
        player.setVariable("moveTargetType", MOVE_TARGET_NON);
    }

    static void pickup(Player player) {
        GameScreen.addDeleteRequest((Actor) player.getVariable("moveTarget"),StartGame.game.onMapItems);
        Item item = (Item) player.getVariable("moveTarget");
        StartGame.game.addItem(item);
        player.setVariable("moveTarget", null);
        player.setVariable("moveTargetType", MOVE_TARGET_NON);
        PlayerVoice.setVoiceIngame(player,"interface/loot",true);
        Event.eventPickup(( item.getVariable("name")+""),item.getVariableInteger("count"));

    }

    static void loot(Player player) {
        GameScreen.addDeleteRequest((Actor) player.getVariable("moveTarget"), StartGame.game.getNpcList());
        Player lootWho = (Player) player.getVariable("moveTarget");
        if (lootWho instanceof NPC) {
            ((NPC) lootWho).lootNPC();
        }
        player.setVariable("moveTarget", null);
        player.setVariable("moveTargetType", MOVE_TARGET_NON);
        PlayerVoice.setVoiceIngame(player,"interface/loot",true);
    }

    public static void setTargetMagic(Player player, Player target, Actor activeActor) {
        if (target.getVariable("dead").equals(0)) {
            setMagicTargetFinal(player, target, activeActor);

        }
    }

    public static void setTargetMagic(Player player, int cursorX, int cursorY, Actor activeActor) {
        Player target = new Player(cursorX, cursorY);
        setMagicTargetFinal(player, target, activeActor);

    }

    private static void setMagicTargetFinal(Player player, Player target, Actor activeActor) {
        double curStamina = (double) player.getVariable("stamina");
        int mana = activeActor.getVariableInteger("mana");
        mana = PlayerCastMagic.manaCostThruMagic(mana, player);
        if (curStamina < mana) {
            PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_CANTCAST_STAMINA);
            return;
        }
        if(player.getVariableInteger("moveTargetType") != MOVE_TARGET_CAST)
            PlayerVoice.setVoiceInterface(player,PlayerVoice.VOICE_TARGET_CAST);

        player.setVariable("tarX", target.getTileLoc()[0]);
        player.setVariable("tarY", target.getTileLoc()[1]);
        player.setVariable("moveTargetType", MOVE_TARGET_CAST);
        player.setVariable("moveTarget", target);
        player.setVariable("castingSpell", activeActor);
        setPath(player, target.getTileLoc()[0], target.getTileLoc()[1]);
    }
}
