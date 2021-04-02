package engine.script;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LoadScripts {
    static class Blob{
        String command;
        Blob left,right,testCondition,next;
        public Blob(String com){
            command = com;
        }
        public Blob(){

        }
    }
    private static final Character COMMENTS_START = ';';
    static String[] scriptLines = {

    };
    private static int lineOn = 0;
    public static File[] scriptNames(){
        File folder = new File("res/spt");
        File[] folderEntries = folder.listFiles();
        return folderEntries;
    }
    public static void loadScripts(){
        File[] scripts = scriptNames();
        for(File el : scripts){
            try {
                List<String> lines = Files.readAllLines(Paths.get(el.toURI()), StandardCharsets.UTF_8);
                scriptLines = lines.toArray(scriptLines);
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Blob> commands = new ArrayList<>();
            String name = el.getName();
            for(String line : scriptLines){
                if(line == null) continue;
                int codeEnd = line.indexOf(COMMENTS_START);
                if(codeEnd != -1) {
                    line = line.substring(0, codeEnd);
                }
                Blob command = makeCommand(line);
                commands.add(command);
                lineOn++;
            }

            ExecuteScripts.loadScript(name,commands);
        }

    }
    private static Blob makeCommand(String line){
        int firstSpace = line.indexOf(" ");
        int secondSpace = line.indexOf(" ",firstSpace+1);
        String comand = "";
        String firstArg = "";
        String secondArg = "";
        if(firstSpace == -1){
            comand = line;
        } else {
            if(secondSpace == -1){
                comand = line.substring(0, firstSpace);
                firstArg = line.substring(firstSpace + 1);
            } else {
                comand = line.substring(0, firstSpace);
                firstArg = line.substring(firstSpace + 1, secondSpace);
                secondArg = line.substring(secondSpace + 1);
            }
        }
        Blob blob = new Blob();

        switch (comand){
            case "IF":
                blob.command = "IF";
                blob.testCondition = new Blob();
                blob.testCondition.command = firstArg;
                secondArg = secondArg.replaceFirst(" ", ":");
                String[] whatTest = secondArg.split(":");
                blob.testCondition.left = makeCommand(whatTest[0]);
                blob.testCondition.right = makeCommand (whatTest[1]);
                blob.left = new Blob(findJumpLine(0)+"");
                break;
            case "ELSE":
                blob.command = "ELSE";
                blob.left = new Blob(findJumpLine(2)+"");
                break;
            case "SET":
                blob.command = comand;
                blob.left = makeCommand(firstArg);
                blob.right = makeCommand(secondArg);
                break;
            case "DO":
                blob.command = comand;
                blob.left = new Blob(firstArg);
                blob.right = new Blob(secondArg);
                break;
            case "LOCAL":
                blob.command = comand;
                blob.left = makeCommand(firstArg);
                blob.right = makeCommand(secondArg);
                break;
            case "STOP":
                blob.command = comand;
                blob.left = makeCommand(firstArg);
                blob.right = makeCommand(secondArg);
                break;
            case "+":
                blob.command = comand;
                blob.left = makeCommand(firstArg);
                blob.right = makeCommand(secondArg);
                break;
            case "-":
                blob.command = comand;
                blob.left = makeCommand(firstArg);
                blob.right = makeCommand(secondArg);
                break;case "*":
                blob.command = comand;
                blob.left = makeCommand(firstArg);
                blob.right = makeCommand(secondArg);
                break;case "/":
                blob.command = comand;
                blob.left = makeCommand(firstArg);
                blob.right = makeCommand(secondArg);
                break;case "%":
                blob.command = comand;
                blob.left = makeCommand(firstArg);
                blob.right = makeCommand(secondArg);
                break;
            default:
                blob.command = comand;
                break;

        }
        return blob;
    }
    private static int findJumpLine(int startLevel){
        int lvl = startLevel;
        for(int i = lineOn; i < scriptLines.length;i++){
            String term = scriptLines[i].split(" ")[0];
            if(term.equals("ENDIF")|| term.equals("ELSE")){
                lvl--;
            }
            if(term.equals("IF")){
                if(startLevel == 2 && i == lineOn+1){

                } else {
                    lvl++;
                }
            }
            if(lvl == 0){
                if(term.equals("ELSE")){
                    return i+1;
                }
                return i;
            }
        }
        //System.err.println("Ошибка при компиляции скрипта");
        //System.exit(1);
        return -1;
    }
}
