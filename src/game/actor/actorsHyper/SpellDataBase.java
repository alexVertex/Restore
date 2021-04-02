package game.actor.actorsHyper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

public class SpellDataBase {
    public static boolean isChanged = true;

    public static String[] getSpellsId(){
        String[] ids = new String[dataBase.size()];
        int i = 0;
        for(String el : dataBase.keySet()){
            ids[i] = el;
            i++;
        }
        return ids;
    }
    public static final String[] effectNames = {"Усиление","Огненный урон","Колющий урон","Восстановление здоровья"};

    public static HashMap<String, HashMap<String,Object>> dataBase = new HashMap<>();
    public static void addData(String name, HashMap<String,Object> data){
        deleteLine(name);
        dataBase.put(name,data);
        isChanged = true;
        save(name);
    }
    private static final String[] spellsData = {"ID","texture","partX","partY","name","filter","typeName","type","spellClass","spellClassName",
            "power","time","effect","difficult","mana","distant","targets","targetType","castAnim","attackSpeed","projectileTexture",
            "projectileSizeX","projectileSizeY","projectileSpeed","projectileFrames","projectileExplode","projectileExplodeFrames","projectileExplodeRadius",
    "soundExplode","soundProjectile"};
    private static final String[] spellsType = {"S","S","I","I","S","I","S","I","S","S",
            "I","I","I","I","I","D","I","I","S","D","S",
            "I","I","D","I","S","I","I","S","S"};
    public static HashMap<String,Object> getData(String ID){
        return dataBase.getOrDefault(ID,null);
    }

    private static String resFile = "res/dat/spells.res";

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
