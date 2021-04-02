package engine.script;

import java.util.HashMap;
import java.util.List;
import engine.Actor;
import engine.Start;
import game.StartGame;

public class ExecuteScripts {
    private static HashMap<String, List<LoadScripts.Blob>> scripts = new HashMap<>();
    public static void loadScript(String name, List<LoadScripts.Blob> lines){
        scripts.put(name,lines);
    }

    static  int onLine = 0;
    public static void execute(String scriptID, Actor executer){
        List<LoadScripts.Blob> commands = scripts.get(scriptID);
        for(onLine = 0; onLine < commands.size();onLine++){
            LoadScripts.Blob el = commands.get(onLine);
            if(!execute(el,executer)){
                break;
            }
        }
    }
    private static boolean execute(LoadScripts.Blob blob, Actor executer){
        switch (blob.command){
            case "DO":
                executer.toDo(blob.left.command,blob.right.command.split(" "));
                break;
            case "SET":
                String setWhat = blob.left.command;
                String setTo = blob.right.command;
                String actorTo = null;
                Actor to = executer;
                if(setTo.contains(".") && !isDouble(setTo)){
                    actorTo= setTo.split("\\.")[0];
                    setTo= setTo.split("\\.")[1];
                }
                if(actorTo != null){
                    to = StartGame.game.allActors.get(actorTo);
                }
                String actorWhat = null;
                Actor what = executer;
                if(setWhat.contains(".") && !isDouble(setWhat)){
                    actorWhat= setWhat.split("\\.")[0];
                    setWhat= setWhat.split("\\.")[1];
                }
                if(actorWhat != null){
                    what = StartGame.game.allActors.get(actorWhat);
                }
                if(isString(setTo)){
                    setTo = setTo.substring(1,setTo.length()-1);
                    what.setVariable(setWhat, setTo);
                } else if(isDouble(setTo)){
                    what.setVariable(setWhat, Double.parseDouble(setTo));
                }else if(isInteger(setTo)){
                    what.setVariable(setWhat, Integer.parseInt(setTo));
                }else if(isCommand(setTo)){
                    what.setVariable(setWhat, count(blob.right,executer));
                } else{
                    what.setVariable(setWhat, to.getVariable(setTo));
                }
                break;
            case "LOCAL":
                String name = blob.left.command;
                if(executer.getVariable(name) != null){
                    break;
                }
                String set = blob.right.command;
                if(isString(set)){
                    set = set.substring(1,set.length()-1);
                    executer.setVariable(name, set);
                } else if(isDouble(set)){
                    executer.setVariable(name, Double.parseDouble(set));
                }else if(isInteger(set)){
                    executer.setVariable(name, Integer.parseInt(set));
                }else if(isCommand(set)){
                    executer.setVariable(name, count(blob.right,executer));
                } else{
                    executer.setVariable(name, executer.getVariable(set));
                }
                break;
            case "STOP":
                return false;
            case "IF":
                boolean test = test(blob.testCondition,executer);
                if(!test){
                    onLine = Integer.parseInt(blob.left.command)-1;
                }
                break;
            case "ELSE":
                onLine = Integer.parseInt(blob.left.command);
                break;
        }
        return true;
    }
    private static boolean test(LoadScripts.Blob blob, Actor executer){
        String left = blob.left.command;
        if(isCommand(left)){
            left = count(blob.right,executer)+"";
        }
        if(isString(left)){
            left = left.substring(1,left.length()-1);
        } else if(isDouble(left)){

        }else if(isInteger(left)){

        }else{
            String setTo = left;
            String actorTo = null;
            Actor to = executer;
            if(setTo.contains(".") && !isDouble(setTo)){
                actorTo= setTo.split("\\.")[0];
                setTo= setTo.split("\\.")[1];
            }
            if(actorTo != null){
                to = StartGame.game.allActors.get(actorTo);
            }
            left = to.getVariable(setTo)+"";
        }
        String right = blob.right.command;
        if(isCommand(right)){
            right = count(blob.right,executer)+"";
        }
        if(isString(right)){

            right = right.substring(1,left.length()-1);
        } else if(isDouble(right)){

        }else if(isInteger(right)){

        }else{
            String setTo = right;
            String actorTo = null;
            Actor to = executer;
            if(setTo.contains(".") && !isDouble(setTo)){
                actorTo= setTo.split("\\.")[0];
                setTo= setTo.split("\\.")[1];
            }
            if(actorTo != null){
                to = StartGame.game.allActors.get(actorTo);
            }
            right = to.getVariable(setTo)+"";
        }
        switch (blob.command){
            case ">":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) > Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) > Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) > Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) > Double.parseDouble(right);
                } else {
                    return false;
                }
            case "<":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) < Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) < Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) < Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) < Double.parseDouble(right);
                } else {
                    return false;
                }
            case "=":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) == Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) == Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) == Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) == Double.parseDouble(right);
                } else {
                    return left.equals(right);
                }
            case "<>":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) != Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) != Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) != Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) != Double.parseDouble(right);
                } else {
                    return !left.equals(right);
                }
            case "<=":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) <= Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) <= Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) <= Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) <= Double.parseDouble(right);
                } else {
                    return false;
                }
            case ">=":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) >= Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) >= Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) >= Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) >= Double.parseDouble(right);
                } else {
                    return false;
                }
        }
        return true;
    }
    private static Object count(LoadScripts.Blob blob, Actor executer){
        String left = blob.left.command;
        if(isString(left)){
            left = left.substring(1,left.length()-1);
        } else if(isDouble(left)){

        }else if(isInteger(left)){

        }else{
            String setTo = left;
            String actorTo = null;
            Actor to = executer;
            if(setTo.contains(".") && !isDouble(setTo)){
                actorTo= setTo.split("\\.")[0];
                setTo= setTo.split("\\.")[1];
            }
            if(actorTo != null){
                to = StartGame.game.allActors.get(actorTo);
            }
            left = to.getVariable(setTo)+"";
        }
        String right = blob.right.command;
        if(isString(right)){

            right = right.substring(1,left.length()-1);
        } else if(isDouble(right)){

        }else if(isInteger(right)){

        }else{
            String setTo = right;
            String actorTo = null;
            Actor to = executer;
            if(setTo.contains(".") && !isDouble(setTo)){
                actorTo= setTo.split("\\.")[0];
                setTo= setTo.split("\\.")[1];
            }
            if(actorTo != null){
                to = StartGame.game.allActors.get(actorTo);
            }
            right = to.getVariable(setTo)+"";
        }
        switch (blob.command) {
            case "+":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) + Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) + Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) + Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) + Double.parseDouble(right);
                } else {
                    return left+right;
                }
            case "-":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) - Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) - Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) - Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) - Double.parseDouble(right);
                } else {
                    return left.replaceAll(right,"");
                }
            case "*":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) * Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) * Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) * Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) * Double.parseDouble(right);
                } else {
                    return left+right;
                }
            case "/":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) / Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) / Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) / Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) / Double.parseDouble(right);
                } else {
                    return left.replaceAll(right,"");
                }
            case "%":
                if (isDouble(left) && isDouble(right)) {
                    return Double.parseDouble(left) % Double.parseDouble(right);
                } else if (isInteger(left) && isInteger(right)) {
                    return Integer.parseInt(left) % Integer.parseInt(right);
                } else if (isDouble(left) && isInteger(right)) {
                    return Double.parseDouble(left) % Integer.parseInt(right);
                }else if (isInteger(left) && isDouble(right)) {
                    return Integer.parseInt(left) % Double.parseDouble(right);
                } else {
                    return left.replaceAll(right,"");
                }
        }
        return null;
    }
    private static boolean isCommand(String word){
        return word.equals("+") || word.equals("-")|| word.equals("*")|| word.equals("/")|| word.equals("%");
    }
    private static boolean isString(String word){
        return word.contains("'");
    }
    private static boolean isDouble(String word){
        String regex = "[-+]?\\d+(\\.\\d+)+";
        return word.matches(regex);
    }
    private static boolean isInteger(String word){
        String regex = "[-+]?\\d+";
        return word.matches(regex);
    }
}
