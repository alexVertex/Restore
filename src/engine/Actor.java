package engine;

import java.util.HashMap;
import java.util.Set;

public abstract class Actor {
    private String redID;

    public static final int INTEGER = 0;
    public static final int DOUBLE = 1;
    public static final int STRING = 2;
    public static final int ACTOR = 3;
    private HashMap<String, Object> variables = new HashMap<>();

    public Object getVariable(String key){

        return variables.get(key);
    }
    public int getVariableTrunked(String key){
        double var = (double)(variables.get(key));
        return (int)var;
    }
    public int getVariableInteger(String key){
        double var = (int)(variables.get(key));
        return (int)var;
    }
    public double getVariableDouble(String key){
        double var = (double)(variables.get(key));
        return var;
    }
    public int getVariableTrunked(String key, double mult){
        double var = (double)(variables.get(key))*mult;
        return (int)var;
    }
    public String getVariableString(String key){
        String var = (variables.get(key))+"";
        return var;
    }
    public void setVariable(String key, Object value){
        variables.put(key,value);
    }
    public void toDo(String command,String[] args){

    }
    public void changeVariable(String key, Object value){
        variables.put(key,value);
    }
    public abstract int logic();
    public abstract void draw();
    public abstract void draw(double x, double y, double s);

    public abstract void drawInfo(boolean b);

    public abstract boolean isUndercursor(int cursorX, int cursorY);

    public String getId() {
        return variables.get("ID")+"";
    }

    public void drawMiniMap(){}

    public String getRedID() {
        return getVariableString("refID");
    }

    public void setRedID(String refID) {
        setVariable("refID",refID);
    }

    protected Set<String> getVariables(){
        return variables.keySet();
    }
}
