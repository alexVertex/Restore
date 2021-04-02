package game.actor.actorsHyper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class SmithDataBase {
    public static HashMap<String, HashMap<String,String>> dataBase = new HashMap<>();


    private static String resFile = "res/dat/Smith.csv";


    static {
        try {
            List<String> lines = Files.readAllLines(Paths.get(resFile), StandardCharsets.UTF_8);
            for(String line : lines){
                if(line.startsWith("//")) continue;
                HashMap<String,Object> data = new HashMap<>();
                String[] terms = line.split(";");
                if(terms.length == 1) break;
                data.put("ID",terms[0]);
                HashMap<String, String> data1 = new HashMap<>();
                for(int i = 1; i < terms.length;i++){
                    String terms1[] = terms[i].split(":");
                    data1.put(terms1[0],terms1[1]);
                }
                dataBase.put(terms[0],data1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCraftedID(String blueprint, String material) {
        return dataBase.get(blueprint).get(material);
    }





    public static String[] getTradeID() {
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
