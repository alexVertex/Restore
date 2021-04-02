package game.actor.actorsHyper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

public class TradesDataBase {
    public static HashMap<String, HashMap<String,Object>> dataBase = new HashMap<>();


    private static String resFile = "res/dat/Trades.csv";


    static {
        try {
            List<String> lines = Files.readAllLines(Paths.get(resFile), StandardCharsets.UTF_8);
            for(String line : lines){
                if(line.startsWith("//")) continue;
                HashMap<String,Object> data = new HashMap<>();
                String[] terms = line.split(";");
                if(terms.length == 1) break;
                data.put("ID",terms[0]);
                for(int i = 1; i < terms.length;i++){
                    data.put(i+"",terms[i]);
                }
                dataBase.put(terms[0],data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Object> getData(String id) {
        return dataBase.get(id);
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
