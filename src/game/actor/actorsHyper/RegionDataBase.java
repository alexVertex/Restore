package game.actor.actorsHyper;


import engine.audio3.AudioManager;
import game.actor.game.Region;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegionDataBase {


    private static HashMap<String, String> dataBase = new HashMap<>();
    private static String resFile = "res/dat/regions.res";

    public static void addData(String name, String data){
        deleteLine(name);
        dataBase.put(name,data);
        save(name);
    }

    private static void deleteLine(String name){
        if(dataBase.containsKey(name)){
            try {
                List<String> lines = Files.readAllLines(Paths.get(resFile), StandardCharsets.UTF_8);
                PrintWriter writer = new PrintWriter(resFile);
                writer.print("");
                writer.close();
                for(String line : lines) {
                    String[] terms = line.split(";");
                    if(!terms[1].equals(name)){
                        Files.write(Paths.get(resFile), (line+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void save(String id) {
        String dataInfo = dataBase.get(id);
        try {
            Files.write(Paths.get(resFile), (dataInfo+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            List<String> lines = Files.readAllLines(Paths.get(resFile), StandardCharsets.UTF_8);
            for(String line : lines){
                String[] split = line.split(";");
                dataBase.put(split[0],line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String,String> getRegion(){
        return dataBase;
    }

    public static String loadRegion(String data){
        return dataBase.get(data);
    }

    public static Region createRegion(String regionName) {
        Region region = new Region();
        String data = RegionDataBase.loadRegion(regionName);
        String[] dataSplit = data.split(";");
        String name = (dataSplit[0]);
        for (int i = 0; i < 24; i++) {
            region.ambientLights[i][0] = Double.parseDouble(dataSplit[1 + i * 4]);
            region.ambientLights[i][1] = Double.parseDouble(dataSplit[1 + i * 4 + 1]);
            region.ambientLights[i][2] = Double.parseDouble(dataSplit[1 + i * 4 + 2]);
            region.visionMults[i] = Double.parseDouble(dataSplit[1 + i * 4 + 3]);
        }
        List<String> exp = new ArrayList<>();
        List<String> btl = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if(!dataSplit[97 + i].equals("пусто"))
                exp.add((dataSplit[97 + i]));
            if(!dataSplit[107 + i].equals("пусто"))
                btl.add((dataSplit[107 + i]));
        }

        region.exploreMusic =  exp.toArray(String[]::new);
        region.battleMusic =  btl.toArray(String[]::new);
        for(String el : region.battleMusic){
            //AudioManager.preloadMusicBuffer(el);
        }
        for(String el : region.exploreMusic){
            //AudioManager.preloadMusicBuffer(el);
        }
        return region;
    }
}
