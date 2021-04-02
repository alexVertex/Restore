package game.actor.actorsHyper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

public class MercDataBase {
    public static HashMap<String, HashMap<String,Object>> dataBase = new HashMap<>();
    public static void addData(String name, HashMap<String,Object> data){
        deleteLine(name);
        dataBase.put(name,data);
        save(name);
    }

    private static final String[] spellsData = {"ID","Name","portret","portretX","portretY","ingameTexture","weaponCur","shieldCur","weaponSpare","shieldSpare","armorHead","armorTorso","armorArms","armorLegs","uses1","uses2","uses3","uses4","uses5","uses6","uses7","uses8","spells1","spells2","spells3","spells4","spells5","spells6","spells7","spells8","totalXP","skillHealth","skillStamina","skillRestoration","skillEquipLoad","skillAttackSpeed","skillLockPick","skillDaggers","skillBlades","skillAxes","skillBlunts","skillHandToHand","skillSpears","skillBows","skillCrossbows","skillFireMagic","skillWaterMagic","skillEarthMagic","skillAirMagic","skillMentalMagic","skillWizardry","skillSacredMagic","attributeStrength","attributeAgility","attributeIntellect","vision","bloodTexture","voice","backNPC"};
    private static final String[] spellsType = {"S","S","S","I","I","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","S","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","I","S","S"};

    private static String resFile = "res/dat/Mercenaries.csv";

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
                if(line.startsWith("//")) continue;
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

    public static String[] getMercsID() {
        String[] ids = new String[dataBase.size()+1];
        ids[0] = "null";
        int i = 1;
        for(String el : dataBase.keySet()){
            ids[i] = el;
            i++;
        }
        return ids;
    }
}
