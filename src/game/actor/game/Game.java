package game.actor.game;

import engine.Actor;
import engine.Mathp;
import engine.audio3.AudioManager;
import game.StartGame;
import game.actor.actorsHyper.RegionDataBase;
import game.actor.enviroment.*;
import game.actor.magic.MagicProjectile;
import game.actor.magic.Spell;
import game.actor.player.*;
import game.actor.story.Event;
import game.actor.story.Quest;
import game.actor.story.Target;
import screens.ingame.gameScreenInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    public double time;
    public double timeMax = 144000;
    public double gameSpeedOld = 1;
    public double gameSpeed = 1;
    public int gold = 0;

    public String mapName = "", minimapTexture , mapID;
    public int sizeX=1000, sizeY=1000;

    public String regionName;
    public Region region = new Region();
    public List<Tile> tiles = new ArrayList<>();
    public double[][] passment;
    public int[][] tileMaterial;

    private int choosenHero = 0;
    public List<Player> controlGroup = new ArrayList<>();
    public List<Item> inventory = new ArrayList<>();
    public List<Spell> inventorySpells = new ArrayList<>();
    public List<Quest> questInJournal = new ArrayList<>();
    public List<String> dialogPassed = new ArrayList<>();
    public List<RessurectPoint> openRestPoints = new ArrayList<>();


    private List<Player> npcList = new ArrayList<>();
    public List<PlayerPrints> steps = new ArrayList<>();
    public List<PlayerPrints> bloods = new ArrayList<>();
    public List<Item> onMapItems = new ArrayList<>();
    public List<Door> doorsList = new ArrayList<>();
    public List<AnimTile> animtiles = new ArrayList<>();
    public List<Chest> chests = new ArrayList<>();
    public List<Activator> activators = new ArrayList<>();
    public List<Structure> structuresList = new ArrayList<>();
    public List<AudioSource> audioSources = new ArrayList<>();
    public List<MagicProjectile> projectiles = new ArrayList<>();
    public List<RessurectPoint> ressurections = new ArrayList<>();
    public List[] allActorsList = {steps,animtiles,bloods,structuresList,questInJournal,onMapItems,activators,chests,doorsList,controlGroup, getNpcList(),projectiles};
    public HashMap<String, Actor> allActors = new HashMap<>();

    private String[] npcData;
    private int nextRefID;

    public void clear() {
        tiles.clear() ;
        getNpcList().clear();
        onMapItems.clear() ;
        doorsList.clear() ;
        animtiles.clear();
        chests.clear();
        activators.clear();
        structuresList.clear();
        audioSources.clear();
        allActors.clear();
        ressurections.clear();
        bloods.clear();
        steps.clear();
    }

    public Game(){
        Player player1 = new Player(1,"Actor1",0,3,"chars/Player1",32*1,1*32,100,53,100,90,100,0,0.09);
        Spell spell = new Spell("Шар огня");
        player1.setVariable("spells1",spell);
        Spell spell1 = new Spell("Лечение");
        player1.setVariable("spells2",spell1);
        player1.setVariable("voice","heroMain");
        controlGroup.add(player1);
        gold = 100000;
        PathFinding.createPassmentTable(sizeX,sizeY);
    }

    public void addItem(Item item){
        for(int i = 0; i < item.getVariableInteger("count");i++){
            inventory.add(item);
        }

    }
    public void removeItem(String item, int count)
    {
        String name = "";
        int countReal = 0;
        for(int i = 0; i < count;i++){
            for (int i1 = 0, inventorySize = inventory.size(); i1 < inventorySize; i1++) {
                Item el = inventory.get(i1);
                if (el.getVariable("ID").equals(item)) {
                    inventory.remove(el);
                    name = el.getVariable("name")+"";
                    count--;
                    countReal++;
                    i1--;
                    if(count == 0) break;
                }
            }
        }
        Event.eventLose(name,countReal);
    }

    public void removeItem(Item item){
        inventory.remove(item);
    }
    public void addSpell(Spell item){
        inventorySpells.add(item);
    }
    public void removeSpell(Spell item){
        inventorySpells.remove(item);
    }
    public Player getControled(){
        return controlGroup.get(getChoosenHero());
    }

    public List<String> musicExplore = new ArrayList<>();
    public List<String> musicBattle = new ArrayList<>();

    private void addMusic(String path,boolean explore){
            if(explore)
                musicExplore.add(path);
            else
                musicBattle.add(path);
            AudioManager.preloadMusicBuffer(path);
    }

    public void saveMap(String text) {
        File file2 = new File("res/dat/map/", text + ".map");
        try {
            file2.delete();
            if (file2.createNewFile()) {
                FileWriter fstream1 = new FileWriter(file2.getAbsoluteFile());// конструктор с одним параметром - для перезаписи
                BufferedWriter out1 = new BufferedWriter(fstream1); //  создаём буферезированный поток
                out1.write(""); // очищаем, перезаписав поверх пустую строку
                out1.close(); // закрываем
            }
        } catch (Exception e) {
            System.err.println("Error in file cleaning: " + e.getMessage());
        }
        List<String> saveData = new ArrayList<>();
        saveData.add(mapName + ";" + sizeX + ";" + sizeY+";"+minimapTexture+";"+mapID+";"+nextRefID+";"+regionName);
        ////////////////////////////////////////////////////
        StringBuilder saveTileData = new StringBuilder();
        for (int i = 0; i < tiles.size(); i++) {
            double pass = passment[i / sizeX][i % sizeY];
            double[] part = tiles.get(i).getTextureLocation();
            int material = tileMaterial[i / sizeX][i % sizeY];
            saveTileData.append(tiles.get(i).getTextur1()).append(":").append(pass).append(":").append((int) part[0]).append(":").append((int) part[1]).append(":").append(material).append(";");
        }
        saveData.add(saveTileData.toString());
        ////////////////////////////////////////////////////
        StringBuilder saveItemData = new StringBuilder();
        for (Item onMapItem : onMapItems) {
            double[] loc = onMapItem.getTileLoc();
            saveItemData.append(onMapItem.getId()).append(":").append((int) loc[0]).append(":").append((int) loc[1]).append(":").append(onMapItem.getCount()).append(":").append(onMapItem.getVariableString("refID")).append(";");
        }
        saveData.add(saveItemData.toString());
        ////////////////////////////////////////////////////
        StringBuilder saveDoorData = new StringBuilder();
        for (Door door : doorsList) {
            double[] loc = door.getTileLoc();
            saveDoorData.append(door.getId()).append(":").append((int) loc[0]).append(":").append((int) loc[1]).append(":").append(door.getVariableString("refID")).append(";");
        }
        saveData.add(saveDoorData.toString());
        ////////////////////////////////////////////////////
        StringBuilder saveAtileData = new StringBuilder();
        for (AnimTile tiles : animtiles) {
            double[] loc = tiles.getTileLoc();
            saveAtileData.append(tiles.getId()).append(":").append((int) loc[0]).append(":").append((int) loc[1]).append(":").append(tiles.getVariableString("refID")).append(";");
        }
        saveData.add(saveAtileData.toString());
        ////////////////////////////////////////////////////
        StringBuilder saveActivatorsData = new StringBuilder();
        for (Activator tiles : activators) {
            double[] loc = tiles.getTileLoc();
            saveActivatorsData.append(tiles.getId()).append(":").append((int) loc[0]).append(":").append((int) loc[1]).append(":").append(tiles.getVariableString("refID")).append(";");
        }
        saveData.add(saveActivatorsData.toString());
        ////////////////////////////////////////////////////
        StringBuilder saveStructureData = new StringBuilder();
        for (Structure tiles : structuresList) {
            double[] loc = tiles.getTileLoc();
            saveStructureData.append(tiles.getId()).append(":").append((int) loc[0]).append(":").append((int) loc[1]).append(":").append(tiles.getVariableString("refID")).append(";");
        }
        saveData.add(saveStructureData.toString());
        ////////////////////////////////////////////////////
        StringBuilder saveSoundData = new StringBuilder();
        for (AudioSource tiles : audioSources) {
            double[] loc = tiles.getTileLoc();
            saveSoundData.append(tiles.getId()).append(":").append((int) loc[0]).append(":").append((int) loc[1]).append(":").append(tiles.getVariableString("refID")).append(";");
        }
        saveData.add(saveSoundData.toString());
        ////////////////////////////////////////////////////
        StringBuilder saveNPCData = new StringBuilder();
        for (Player tiles : getNpcList()) {
            double[] loc = tiles.getTileLoc();
            saveNPCData.append(tiles.getId()).append(":").append((int) loc[0]).append(":").append((int) loc[1]).append(":").append(tiles.getVariableString("refID")).append(":");
            NPCPackage npcPackage = (NPCPackage) tiles.getVariable("package");
            saveNPCData.append(npcPackage.save()).append(";");
        }
        saveData.add(saveNPCData.toString());
        ////////////////////////////////////////////////////
        StringBuilder saveChestData = new StringBuilder();
        for (Chest tiles : chests) {
            double[] loc = tiles.getTileLoc();
            saveChestData.append(tiles.getId()).append(":").append((int) loc[0]).append(":").append((int) loc[1]).append(":").append(tiles.saveContent()).append(":").append(tiles.getVariableString("refID")).append(":").append(tiles.getVariableString("goldIn")).append(";");
        }
        saveData.add(saveChestData.toString());
        ////////////////////////////////////////////////////
        StringBuilder saveRessurectData = new StringBuilder();
        for (RessurectPoint tiles : ressurections) {
            saveRessurectData.append(tiles.saveData()).append(";");
        }
        saveData.add(saveRessurectData.toString());
        try {
            for (String el : saveData) {
                Files.write(Paths.get(file2.toURI()), (el + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String text) {
        clear();
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("res/dat/map/"+text+".map"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert lines != null;
        String[] mainData = lines.get(0).split(";");
        mapName = mainData[0];
        minimapTexture = mainData[3];
        mapID = mainData[4];
        sizeX = Integer.parseInt(mainData[1]);
        sizeY = Integer.parseInt(mainData[2]);
        passment = new double[sizeX][sizeY];
        tileMaterial = new int[sizeX][sizeY];
        nextRefID = Integer.parseInt(mainData[5]);
        regionName = mainData[6];
        region = RegionDataBase.createRegion(regionName);
        tiles.clear();
        /////////////////////
        String[] tilesData = lines.get(1).split(";");
        for(int i = 0; i < tilesData.length;i++){
            String[] tileDataSplited = tilesData[i].split(":");
            Tile tile = new Tile(tileDataSplited[0],Integer.parseInt(tileDataSplited[2]),Integer.parseInt(tileDataSplited[3]));
            tiles.add(tile);
            passment[i/sizeX][i%sizeY] = Double.parseDouble(tileDataSplited[1]);
            tileMaterial[i/sizeX][i%sizeY] = Integer.parseInt(tileDataSplited[4]);

        }
        /////////////////////
        String[] itemsData = lines.get(2).split(";");
        for (String itemsDatum : itemsData) {
            String[] dataSplited = itemsDatum.split(":");
            if (dataSplited.length == 1) break;
            Item tile = new Item("", 0, 0, Integer.parseInt(dataSplited[1]), Integer.parseInt(dataSplited[2]), dataSplited[0], Integer.parseInt(dataSplited[3]));
            onMapItems.add(tile);
            allActorsAdd(dataSplited[4], tile);

        }
        /////////////////////
        String[] doorsData = lines.get(3).split(";");
        for (String doorsDatum : doorsData) {
            String[] dataSplited = doorsDatum.split(":");
            if (dataSplited.length == 1) break;
            Door tile = new Door(Integer.parseInt(dataSplited[1]), Integer.parseInt(dataSplited[2]), dataSplited[0]);
            doorsList.add(tile);
            allActorsAdd(dataSplited[3], tile);

        }
        /////////////////////
        String[] atilesData = lines.get(4).split(";");
        for (String atilesDatum : atilesData) {
            String[] dataSplited = atilesDatum.split(":");
            if (dataSplited.length == 1) break;
            AnimTile tile = new AnimTile("", Integer.parseInt(dataSplited[1]), Integer.parseInt(dataSplited[2]), dataSplited[0]);
            animtiles.add(tile);
            //allActors.put(dataSplited[3], tile);

        }
        /////////////////////
        String[] activatorsData = lines.get(5).split(";");
        for (String activatorsDatum : activatorsData) {
            String[] dataSplited = activatorsDatum.split(":");
            if (dataSplited.length == 1) break;
            Activator tile = new Activator(Integer.parseInt(dataSplited[1]), Integer.parseInt(dataSplited[2]), dataSplited[0]);
            activators.add(tile);
            allActorsAdd(dataSplited[3], tile);

        }
        /////////////////////
        String[] structureData = lines.get(6).split(";");
        for (String structureDatum : structureData) {
            String[] dataSplited = structureDatum.split(":");
            if (dataSplited.length == 1) break;
            Structure tile = new Structure(Integer.parseInt(dataSplited[1]), Integer.parseInt(dataSplited[2]), dataSplited[0]);
            structuresList.add(tile);
            //allActors.put(dataSplited[3], tile);

        }
        /////////////////////
        String[] sourceData = lines.get(7).split(";");
        for (String sourceDatum : sourceData) {
            String[] dataSplited = sourceDatum.split(":");
            if (dataSplited.length == 1) break;
            AudioSource tile = new AudioSource(Integer.parseInt(dataSplited[1]), Integer.parseInt(dataSplited[2]), dataSplited[0]);
            audioSources.add(tile);
            //allActors.put(dataSplited[3], tile);

        }
        /////////////////////
        npcData = lines.get(8).split(";");
        for (String npcDatum : npcData) {
            String[] dataSplited = npcDatum.split(":");
            if (dataSplited.length == 1) break;
            NPC tile = new NPC(dataSplited[0], Integer.parseInt(dataSplited[1]), Integer.parseInt(dataSplited[2]));
            getNpcList().add(tile);
            NPCPackage npcPackage = new NPCPackage(dataSplited[4]);
            tile.setVariable("package", npcPackage);
            allActorsAdd(dataSplited[3], tile);
        }
        /////////////////////
        String[] chestsData = lines.get(9).split(";");
        for (String chestsDatum : chestsData) {
            String[] dataSplited = chestsDatum.split(":");
            if (dataSplited.length == 1) break;
            Chest tile = new Chest(Integer.parseInt(dataSplited[1]), Integer.parseInt(dataSplited[2]), dataSplited[0], dataSplited[3], dataSplited[5]);
            chests.add(tile);
            allActorsAdd(dataSplited[4], tile);
        }
        /////////////////////
        String[] ressurectData = lines.get(10).split(";");
        for (String ressurectDatum : ressurectData) {
            String[] dataSplited = ressurectDatum.split(":");
            if (dataSplited.length == 1) break;
            RessurectPoint tile = new RessurectPoint(Integer.parseInt(dataSplited[0]), Integer.parseInt(dataSplited[1]), Integer.parseInt(dataSplited[2]),
                    Integer.parseInt(dataSplited[3]), Integer.parseInt(dataSplited[4]), Integer.parseInt(dataSplited[5]),dataSplited[8],dataSplited[9],dataSplited[6],dataSplited[7],dataSplited[10]);
            ressurections.add(tile);
        }
        controlGroup.get(0).setOnCamera();
    }

    private void allActorsAdd(String refID, Actor actor) {
        allActors.put(refID,actor);
        actor.setVariable("refID",refID);
    }

    public int getChoosenHero() {
        return choosenHero;
    }

    public void setChoosenHero(int choosenHero) {
        this.choosenHero = choosenHero;
        gameScreenInterface.dropActiveActor();
        AudioManager.playSoundInterface("interface/Click");

    }

    public void pauseWorks() {
        if (StartGame.game.gameSpeed > 0) {
            StartGame.game.gameSpeedOld = StartGame.game.gameSpeed;
            StartGame.game.gameSpeed = 0;
        } else {
            StartGame.game.gameSpeed = StartGame.game.gameSpeedOld;
        }
    }

    public void addQuest(String questID) {
        Quest quest = new Quest(questID);
        Event.eventQuestAdded(quest.getVariable("questName")+"");
        questInJournal.add(quest);
        List<Target> targets = (List<Target>) quest.getVariable("questTargets");
        targets.get(0).give();
    }

    public boolean allPlayersIsDead() {
        int playersInZone = 0;
        for (Player ele : controlGroup) {
            if (ele.getVariable("dead").equals(1)) {
                playersInZone++;
            }
        }
        return playersInZone == controlGroup.size();
    }

    void addRestPoint(RessurectPoint point){
        for(RessurectPoint el : openRestPoints){
            if(el.getVariable("ID").equals(point.getVariable("ID"))){
                return;
            }
        }
        openRestPoints.add(point);
    }

    public boolean playerInRessurectionPoint() {
        if(MusicControl.currentState == 1) return false;
        for(RessurectPoint el : ressurections){
            int playersInZone = 0;
            boolean newOpen = false;
            for(Player ele : controlGroup){
                int left = el.getVariableInteger("left");
                int top = el.getVariableInteger("top");
                int right = el.getVariableInteger("right");
                int bot = el.getVariableInteger("bot");
                double x = ele.getVariableTrunked("locX");
                double y = ele.getVariableTrunked("locY");
                if(Mathp.inRectangle(left,top,right,bot,x,y) || ele.getVariable("dead").equals(1)) {
                    playersInZone++;
                }


            }
            if(playersInZone == controlGroup.size()) { lastRessurect = el; addRestPoint(el); return true;}
        }
        return false;
    }

    public RessurectPoint lastRessurect = new RessurectPoint(0,0,32,32,0,0,"","","","","");
    public void setRessurectionPoint() {
        allResurect();
    }

    public void allResurect(){
        for(Player el : controlGroup){
            el.reset();
        }
        for(Door el : doorsList){
            el.reset();
        }
        for(Activator el : activators){
            el.reset();
        }
        bloods.clear();
        steps.clear();
        for(int i = 0; i < getNpcList().size();){
            Player el = getNpcList().get(i);
            if(el.getVariableInteger("nonResurrect")==0){
                getNpcList().remove(el);
            } else {i++;}
        }
        if(npcData == null) return;
        for (String npcDatum : npcData) {
            String[] dataSplited = npcDatum.split(":");
            if (dataSplited.length == 1) break;
            NPC tile = new NPC(dataSplited[0], Integer.parseInt(dataSplited[1]), Integer.parseInt(dataSplited[2]));
            if(tile.getVariableInteger("nonResurrect")==1) continue;
            boolean mercHired = false;
            for(Player el : controlGroup){
                if(el.getVariable("ID").equals(tile.getVariable("merc"))){
                    mercHired = true; break;
                }
            }
            if(mercHired) continue;
            getNpcList().add(tile);
            NPCPackage npcPackage = new NPCPackage(dataSplited[4]);
            tile.setVariable("package", npcPackage);
        }
    }

    public List<Player> getNpcList() {
        return npcList;
    }

    public void setNpcList(List<Player> npcList) {
        this.npcList = npcList;
    }



    public void addNPCInGame(NPC actor) {
        npcList.add(actor);
        addActorInAllActorList(actor);

    }
    public void addItemInGame(Item actor) {
        onMapItems.add(actor);
        addActorInAllActorList(actor);

    }
    public void addDoorInGame(Door actor) {
        doorsList.add(actor);
        addActorInAllActorList(actor);

    }
    public void addActivatorInGame(Activator actor) {
        activators.add(actor);
        addActorInAllActorList(actor);

    }
    public void addChestInGame(Chest actor) {
        chests.add(actor);
        addActorInAllActorList(actor);
    }
    private void addActorInAllActorList(Actor actor){
        String refID = nextRefID + "";
        nextRefID++;
        allActors.put(refID,actor);
        actor.setVariable("refID",refID);
    }

    public void addGold(int gold) {
        this.gold += gold;
        Event.eventAddGold(gold);
    }

    public void removeGold(int gold) {
        if(gold > this.gold) gold = this.gold;
        this.gold -= gold;
        Event.eventRemoveGold(gold);
    }
}
