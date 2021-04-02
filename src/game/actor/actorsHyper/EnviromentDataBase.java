package game.actor.actorsHyper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

public class EnviromentDataBase {
    public static HashMap<String, HashMap<String,Object>> dataBase = new HashMap<>();
    public static final int ANIM_TILE = 0,ACTIVATOR = 1,DOOR = 2,CHEST = 3,SOURCE= 4,STRUCTURE = 5;

    private static String[] animtileData = {"ID","texture","typeName","frames","animSpeed","redID"};
    private static String[] animtileType = {"S","S","S","I","D","S"};
    private static String[] activatorData = {"ID","texture","typeName","partX","partY","stateAnim","lockLevel","trap","redID","sound","keys"};
    private static String[] activatorType = {"S","S","S","I","I","D","I","I","S","S","S"};
    private static String[] doorData = {"ID","texture","typeName","partX","partY","stateAnim","lockLevel","lockOffX","lockOffY","redID","sound","keys"};
    private static String[] doorType = {"S","S","S","I","I","D","I","I","I","S","S","S"};
    private static String[] chestData = {"ID","texture","typeName","partX","partY","stateAnim","lockLevel","lockOffX","lockOffY","redID","sound","keys"};
    private static String[] chestType = {"S","S","S","I","I","D","I","I","I","S","S","S"};
    private static String[] sourceData = {"ID","texture","typeName","source","rast","redID"};
    private static String[] sourceType = {"S","S","S","S","D","S"};
    private static String[] structureData = {"ID","texture","typeName","sizeX","sizeY","sizeYSolid","partLeftSolid","partRightSolid","partTopSolid","partBotSolid","toptransperent","bottransperent","redID"};
    private static String[] structureType = {"S","S","S","I","I","I","D","D","D","D","D","D","S"};

    private static String[][] datas = {animtileData,activatorData,doorData,chestData,sourceData,structureData};
    private static String[][] types = {animtileType,activatorType,doorType,chestType,sourceType,structureType};

    private static String resFile = "res/dat/Env.res";

    public static void addData(String name, HashMap<String,Object> data){
        deleteLine(name);
        dataBase.put(name,data);
        save(name);
    }

    public static HashMap<String,Object> getData(String ID){
        return dataBase.getOrDefault(ID,null);
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

    static {
        try {
            List<String> lines = Files.readAllLines(Paths.get(resFile), StandardCharsets.UTF_8);
            for(String line : lines){
                HashMap<String,Object> data = new HashMap<>();
                String[] terms = line.split(";");
                if(terms.length == 1) break;
                int typeOfObject = Integer.parseInt(terms[0].replaceAll("\\uFEFF", ""));
                for(int i = 1; i < terms.length;i++){
                    if(types[typeOfObject][i - 1].equals("S")) data.put(datas[typeOfObject][i-1],terms[i]);
                    if(types[typeOfObject][i - 1].equals("I")) data.put(datas[typeOfObject][i-1],Integer.parseInt(terms[i]));
                    if(types[typeOfObject][i - 1].equals("D")) data.put(datas[typeOfObject][i-1],Double.parseDouble(terms[i]));
                }
                dataBase.put(terms[1],data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save(String name) {
        HashMap<String, Object> dataInfo = dataBase.get(name);
        String saver = saveObject(dataInfo, (Integer) dataInfo.get("SaveType"));
        try {
            Files.write(Paths.get(resFile), (saver+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String saveObject(HashMap<String, Object> dataInfo, int saveType) {
        StringBuilder answer = new StringBuilder(saveType + "");
        for(String el : datas[saveType]){
            answer.append(";").append(dataInfo.get(el));
        }
        return answer.toString();
    }
}
