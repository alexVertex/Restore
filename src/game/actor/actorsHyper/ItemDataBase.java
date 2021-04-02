package game.actor.actorsHyper;

import game.actor.game.Projectile;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemDataBase {

    private static final Projectile melee = new Projectile("melee1",0,0,32,16,4,0,null);
    private static final Projectile far = new Projectile("arrow1",0,0,32,1,0,0,null);

    public static final Projectile[] projectiles = {melee,far};

    public static final int TYPE_WEAPON = 0;
    public static final int TYPE_ARMOR = 1;
    public static final int TYPE_SHIELD = 2;
    public static final int TYPE_USES = 3;
    public static final int TYPE_BOWS = 4;
    public static final int TYPE_KEYS = 5;
    public static final int TYPE_TROPHY = 6;
    public static final int TYPE_BLUEPRINTS = 7;
    public static final int TYPE_MATERIALS = 8;
    public static final int TYPE_RUNE = 9;

    public static final int ARMOR_CLASS_HEAD = 0;
    public static final int ARMOR_CLASS_TORSO = 1;
    public static final int ARMOR_CLASS_ARMS = 2;
    public static final int ARMOR_CLASS_LEGS = 3;

    public static String[] getSpellsId(){
        String[] ids = new String[dataBase.size()+1];
        ids[0] = "null";
        int i = 1;
        for(String el : dataBase.keySet()){
            ids[i] = el;
            i++;
        }
        return ids;
    }
    public static String[] getItemIdKeys(){
        List<String> idsList = new ArrayList<>();
        String[] ids = new String[dataBase.size()+1];
        ids[0] = "null";
        int i = 1;
        for(String el : dataBase.keySet()){
            if(dataBase.get(el).get("type").equals(5))
                idsList.add(dataBase.get(el).get("ID")+"");
            i++;
        }
        String[] ids1 =new String[idsList.size()];
                idsList.toArray(ids1);
        return ids1;
    }
    public static HashMap<String, HashMap<String,Object>> dataBase = new HashMap<>();
    public static void addItem(String name, HashMap<String,Object> data){
        deleteLine(name);
        dataBase.put(name,data);
        save(name);
    }
    public static HashMap<String,Object> getData(String ID){
        return dataBase.getOrDefault(ID,null);
    }
    private static String[] weaponData = {"ID","texture","typeName","type","filter","partX","partY","name","weight","cost","weaponClass","weaponClassName","damage","damageType","damageTypeName","distant","attackSpeed","projectileID","redID"};
    private static String[] weaponType = {"S","S","S","I","I","I","I","S","I","I","S","S","D","S","S","D","D","I","S"};
    private static String[] farweaponData = {"ID","texture","typeName","type","filter","partX","partY","name","weight","cost","weaponClass","weaponClassName","damage","damageType","damageTypeName","distant","attackSpeed","fires","firesMax","projectileID","redID"};
    private static String[] farweaponType = {"S","S","S","I","I","I","I","S","I","I","S","S","D","S","S","D","D","I","I","I","S"};
    private static String[] shieldData = {"ID","texture","typeName","type","filter","partX","partY","name","weight","cost","armorClass","armorClassName","armorPierce","armorSlash","armorStrike","stability","redID"};
    private static String[] shieldType = {"S","S","S","I","I","I","I","S","I","I","S","S","I","I","I","D","S"};
    private static String[] armorData = {"ID","texture","typeName","type","filter","partX","partY","name","weight","cost","armorClass","armorClassName","armorPierce","armorSlash","armorStrike","armorFire","armorFreeze","armorElectro","armorAcid","armorMental","armorWizardry","armorSacred","redID","sound"};
    private static String[] armorType = {"S","S","S","I","I","I","I","S","I","I","I","S","I","I","I","I","I","I","I","I","I","I","S","I"};
    private static String[] usesData = {"ID","texture","typeName","type","filter","partX","partY","name","weight","cost","usesClassName","spellIn","redID"};
    private static String[] usesType = {"S","S","S","I","I","I","I","S","I","I","S","S","S"};
    private static String[] keysData = {"ID","texture","typeName","type","filter","partX","partY","name","weight","cost","redID"};
    private static String[] keysType = {"S","S","S","I","I","I","I","S","I","I","S"};
    private static String[] trophyData = {"ID","texture","typeName","type","filter","partX","partY","name","weight","cost","redID"};
    private static String[] trophyType = {"S","S","S","I","I","I","I","S","I","I","S"};
    private static String[] blueprintData = {"ID","texture","typeName","type","filter","partX","partY","name","weight","cost","redID","materialClass","materialCount"};
    private static String[] blueprintType = {"S","S","S","I","I","I","I","S","I","I","S","I","I"};
    private static String[] materialData = {"ID","texture","typeName","type","filter","partX","partY","name","weight","cost","redID","materialClass"};
    private static String[] materialType = {"S","S","S","I","I","I","I","S","I","I","S","I"};
    private static String[] runeData = {"ID","texture","typeName","type","filter","partX","partY","name","weight","cost","redID","changingParameter","changingValue","changingDifficult","changingMana"};
    private static String[] runeType = {"S","S","S","I","I","I","I","S","I","I","S","I","I","I","I"};
    private static String[][] datas = {weaponData, armorData, shieldData, usesData, farweaponData, keysData, trophyData, blueprintData, materialData, runeData};
    private static String[][] types = {weaponType, armorType, shieldType, usesType, farweaponType, keysType, trophyType, blueprintType, materialType, runeType};

    private static String resFile = "res/dat/items.res";

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

    private static void save(String id) {
        HashMap<String, Object> dataInfo = dataBase.get(id);
        String saver = saveObject(dataInfo,(int) dataInfo.get("type"));
        try {
            Files.write(Paths.get(resFile), (saver+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            e.printStackTrace();
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
