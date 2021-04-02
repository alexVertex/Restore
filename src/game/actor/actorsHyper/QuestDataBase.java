package game.actor.actorsHyper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

public class QuestDataBase {
    public static HashMap<String, HashMap<String,Object>> dataBase = new HashMap<>();
    public static void addData(String name, HashMap<String,Object> data){
        deleteLine(name);
        dataBase.put(name,data);
        save(name);
    }
    private static final String[] questData = {"ID","questName","questMap","questAbout","journalTargetX","journalTargetY","questTargetsData"};
    private static final String[] questType = {"S","S","S","S","I","I","S"};
    public static HashMap<String,Object> getData(String ID){
        return dataBase.getOrDefault(ID,null);
    }

    private static String resFile = "res/dat/quests.res";

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
                for(int i = 1; i < terms.length;i++){
                    if(questType[i - 1].equals("S")) data.put(questData[i-1],terms[i]);
                    if(questType[i - 1].equals("I")) data.put(questData[i-1],Integer.parseInt(terms[i]));
                    if(questType[i - 1].equals("D")) data.put(questData[i-1],Double.parseDouble(terms[i]));
                }
                dataBase.put(terms[1],data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void save(String id) {
        HashMap<String, Object> dataInfo = dataBase.get(id);
        String saver = saveSpell(dataInfo);
        try {
            Files.write(Paths.get(resFile), (saver+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String saveSpell(HashMap<String,Object> dataInfo){
        StringBuilder answer = new StringBuilder();
        for(String el : questData){
            answer.append(";").append(dataInfo.get(el));
        }
        return answer.toString();
    }
}
