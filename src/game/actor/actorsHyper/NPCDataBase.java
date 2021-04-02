package game.actor.actorsHyper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

public class NPCDataBase {
    public static HashMap<String, HashMap<String,Object>> dataBase = new HashMap<>();
    public static void addData(String name, HashMap<String,Object> data){
        deleteLine(name);
        dataBase.put(name,data);
        save(name);
    }

    private static final String[] spellsData = {"ID","damage","stepsTexture","stamina","maxStamina","NPCClassName","speedCrouch","maxHealth","skillRestoration","exp",
            "ingameTexture","portret","visionType","lootGoldMin","distant","health","speedWalk","loot2","bloodTexture","loot3","portretX",
            "portretY","loot1","lootGoldMax","vision","attackSpeed","name","speedRun","damageType",
        "armorPierce","armorSlash","armorStrike","armorFire","armorFreeze","armorElectro","armorAcid","armorMental","armorWizardry","armorSacred","good","redID","skinType","nonResurrect","voice",
    "merc","trade","smith","ench"};
    private static final String[] spellsType = {"S","D","S","D","D","S","D","D","I","I",
            "S","S","I","I","D","D","D","S","I","S","I",
            "I","S","I","I","D","S","D","S"
            ,"I","I","I","I","I","I","I","I","I","I","I","S","I","I","S",
            "S","S","S","S"};

    private static String resFile = "res/dat/NPC.res";

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
                    if(spellsType[i - 1].equals("S")) data.put(spellsData[i-1],terms[i]);
                    if(spellsType[i - 1].equals("I")) data.put(spellsData[i-1],Integer.parseInt(terms[i]));
                    if(spellsType[i - 1].equals("D")) data.put(spellsData[i-1],Double.parseDouble(terms[i]));
                }
                dataBase.put(terms[1],data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Object> getData(String id) {
        return dataBase.get(id);
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
        StringBuilder answer = new StringBuilder("0");
        for(String el : spellsData){
            answer.append(";").append(dataInfo.get(el));
        }
        return answer.toString();
    }
}
