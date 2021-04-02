package engine.control.interfaces;


import engine.Mathp;
import engine.control.InputMain;
import engine.render.Render2D;
import engine.render.Text;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import java.awt.*;
import java.util.HashMap;

public class Textbox extends Element {

    boolean shifter = false;
    public Textbox(double x, double y, double sx, double sy, String t,String initText, int initFont,double ofX, boolean multiLine) {
        locx = x;
        locy = y;
        sizex = sx;
        sizey = sy;
        setTexture(t);
        text = initText;
        font = initFont;offsetX = ofX;
        shifter = true;
    }

    public String getText() {
        return text;
    }
    private int cursorPos;
    private boolean isDigitized = false;
    private int maximum = -1,minimum = -1;

    private String text = "";
    private int font = Text.CAMBRIA_14;
    private boolean focused = false;
    private char hide = ' ';
    private double offsetX;
    public Textbox(double x, double y, double sx, double sy, String t,String initText, int initFont,double ofX){
        locx = x;
        locy = y;
        sizex = sx;
        sizey = sy;
        setTexture(t);
        text = initText;
        font = initFont;offsetX = ofX;
    }
    public Textbox(double x, double y, double sx, double sy, String t, int initFont,double ofX,int min, int max){
        locx = x;
        locy = y;
        sizex = sx;
        sizey = sy;
        setTexture(t);
        text = min+"";
        font = initFont;offsetX = ofX;
        minimum = min;
        maximum = max;
        isDigitized = true;
    }
    public Textbox(double x, double y, double sx, double sy, String t,String initText, int initFont,char hideSymbol){
        locx = x;
        locy = y;
        sizex = sx;
        sizey = sy;
        setTexture(t);
        text = initText;
        font = initFont;
        hide = hideSymbol;
    }
    @Override
    public void draw() {
        double part = getStatus() == 4 ? 1 : getStatus();
        float plusy = 8;
        String print = ""+hide;
        Render2D.angleCutDraw("textbox"+locx+""+locy+""+sizex+""+sizey,getTexture(),locx,locy+plusy,sizex, sizey,0,0,0,part/MAX,(1 - (1+part)/MAX));
        if(hide != ' '){
            print = print.repeat(text.length());
        } else {
            print = text;
        }
        if(shifter){
            Text.drawStringWithOffset(print, (float) (locx - sizex / 2 + offsetX), (float) locy-sizey/2+8, font, Color.white,(int)sizex-8,20);
        } else {
            Text.drawString(print, (float) (locx - sizex / 2 + offsetX), (float) locy, font, Color.white);
        }
    }
    @Override
    public void work() {
        if(getStatus() == 0)
            return;
        if(Mathp.inRectangle(locx-sizex/2,locy-sizey/2,locx+sizex/2,locy+sizey/2, InputMain.getCursorX(),InputMain.getCursorY())){
            if(InputMain.getKeyState(-1) > -1){
                status = 3;
            } else {
                if(getStatus() == 3){

                    if(!focused)
                        cursorPos = text.length();
                    focused = true;

                } else {
                    status = 2;
                }
            }
        } else {
            status = 1;
        }
        if(focused) {
            status = 3;
            enterText();
            if (!Mathp.inRectangle(locx - sizex / 2, locy - sizey / 2, locx + sizex / 2, locy + sizey / 2, InputMain.getCursorX(), InputMain.getCursorY())) {
                if(InputMain.getKeyState(InputMain.LMB) > -1){
                    status = 1;
                    focused = false;
                }
            }
            InputMain.dropStates();

        } else {
            if (maximum > -1){
                if(text.length() == 0){
                    text = minimum+"";

                }
            }
        }
    }
    private int timePressed;
    private int lastKeyPressed;
    private void enterText(){
        int incode = InputMain.getLastPressedKey();
        if(incode == -1) {lastKeyPressed = -1; return;}
        char inchar = codes.getOrDefault(incode,'n');
        if(inchar == 'n') return;
        if(lastKeyPressed == incode){
            timePressed ++;
        } else {
            lastKeyPressed = incode;
            timePressed = 0;
        }
        if(timePressed == 0 || (timePressed > 20 && timePressed % 5 == 0)){
            executePrint(inchar);
        }
    }
    private static HashMap<Integer, Character> codes = new HashMap<>();
    private static HashMap<Character, Character> russian = new HashMap<>();

    static {
        codes.put(Keyboard.KEY_Q,'Q');        codes.put(Keyboard.KEY_H,'H');
        codes.put(Keyboard.KEY_W,'W');        codes.put(Keyboard.KEY_J,'J');
        codes.put(Keyboard.KEY_E,'E');        codes.put(Keyboard.KEY_K,'K');
        codes.put(Keyboard.KEY_R,'R');        codes.put(Keyboard.KEY_L,'L');
        codes.put(Keyboard.KEY_T,'T');        codes.put(Keyboard.KEY_Z,'Z');
        codes.put(Keyboard.KEY_Y,'Y');        codes.put(Keyboard.KEY_X,'X');
        codes.put(Keyboard.KEY_U,'U');        codes.put(Keyboard.KEY_C,'C');
        codes.put(Keyboard.KEY_I,'I');        codes.put(Keyboard.KEY_V,'V');
        codes.put(Keyboard.KEY_O,'O');        codes.put(Keyboard.KEY_B,'B');
        codes.put(Keyboard.KEY_P,'P');        codes.put(Keyboard.KEY_N,'N');
        codes.put(Keyboard.KEY_A,'A');        codes.put(Keyboard.KEY_M,'M');
        codes.put(Keyboard.KEY_S,'S');        codes.put(Keyboard.KEY_D,'D');
        codes.put(Keyboard.KEY_F,'F');        codes.put(Keyboard.KEY_G,'G');
        codes.put(Keyboard.KEY_1,'1');        codes.put(Keyboard.KEY_6,'6');
        codes.put(Keyboard.KEY_2,'2');        codes.put(Keyboard.KEY_7,'7');
        codes.put(Keyboard.KEY_3,'3');        codes.put(Keyboard.KEY_8,'8');
        codes.put(Keyboard.KEY_4,'4');        codes.put(Keyboard.KEY_9,'9');
        codes.put(Keyboard.KEY_5,'5');        codes.put(Keyboard.KEY_0,'0');
        codes.put(Keyboard.KEY_COMMA,',');    codes.put(Keyboard.KEY_PERIOD,'.');
        codes.put(Keyboard.KEY_SEMICOLON,';');    codes.put(Keyboard.KEY_LBRACKET,'[');
        codes.put(Keyboard.KEY_APOSTROPHE,'\'');    codes.put(Keyboard.KEY_RBRACKET,']');
        codes.put(Keyboard.KEY_SLASH,'/');   codes.put(Keyboard.KEY_MINUS,'-');
        codes.put(Keyboard.KEY_BACK,'b');     codes.put(Keyboard.KEY_DELETE,'d');
        codes.put(Keyboard.KEY_RETURN,'e');   codes.put(Keyboard.KEY_SPACE,' ');
        codes.put(Keyboard.KEY_LEFT,'l');     codes.put(Keyboard.KEY_RIGHT,'r');


        russian.put('Q','й');       russian.put('H','р');
        russian.put('W','ц');       russian.put('J','о');
        russian.put('E','у');       russian.put('K','л');
        russian.put('R','к');       russian.put('L','д');
        russian.put('T','е');       russian.put('Z','я');
        russian.put('Y','н');       russian.put('X','ч');
        russian.put('U','г');       russian.put('C','с');
        russian.put('I','ш');       russian.put('V','м');
        russian.put('O','щ');       russian.put('B','и');
        russian.put('P','з');       russian.put('N','т');
        russian.put('A','ф');       russian.put('M','ь');
        russian.put('S','ы');       russian.put('D','в');
        russian.put('F','а');       russian.put('G','п');
        russian.put('1','1');       russian.put('6','6');
        russian.put('2','2');       russian.put('7','7');
        russian.put('3','3');       russian.put('8','8');
        russian.put('4','4');       russian.put('9','9');
        russian.put('5','5');       russian.put('0','0');
        russian.put(',','б');       russian.put('.','ю');
        russian.put(';','ж');       russian.put('[','х');
        russian.put('\'','э');      russian.put(']','ъ');
        russian.put('/','.');       russian.put('-','-');
    }
    private void executePrint(char command){
        switch (command){
            case 'n':

                break;
            case 'l':
                if(cursorPos>0)
                    cursorPos--;
                break;
            case 'r':
                if(cursorPos<text.length())
                    cursorPos++;
                break;
            case 'd':
                if(cursorPos<text.length()) {
                    text = text.substring(0, cursorPos) + text.substring(cursorPos+1);

                }
                break;
            case 'b':
                if(cursorPos>0) {
                    text = text.substring(0, cursorPos - 1) + text.substring(cursorPos);
                    cursorPos--;

                }
                break;
            case 'e':
                status = 1;
                focused = false;

                break;
            default:
                if(!isDigitized) {
                    double textWide = Text.textWidth(text.substring(0, cursorPos) + command + text.substring(cursorPos), font);
                    String testText= text.substring(0, cursorPos) + command + text.substring(cursorPos);

                    if (textWide > sizex - offsetX * 2 && !shifter)
                        return;
                    if(shifter && Text.linesStringWithOffset(testText,font,(int)sizex-8) > sizey/20)
                        return;
                    command = russian.getOrDefault(command, command);
                    String add = command + "";
                    add = add.toLowerCase();
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                        add = add.toUpperCase();
                    }
                    text = text.substring(0, cursorPos) + add + text.substring(cursorPos);
                    cursorPos++;
                } else {
                    String testText= text.substring(0, cursorPos) + command + text.substring(cursorPos);
                    double textWide = Text.textWidth(testText, font);

                    if (textWide > sizex - offsetX * 2 && !shifter)
                        return;
                    if(shifter && Text.linesStringWithOffset(testText,font,(int)sizex-8) > sizey/20)
                        return;
                    command = russian.getOrDefault(command, command);
                    if(Character.isDigit(command)) {
                        String add = command + "";
                        add = add.toLowerCase();
                        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                            add = add.toUpperCase();
                        }
                        text = text.substring(0, cursorPos) + add + text.substring(cursorPos);
                        int val = Integer.parseInt(text);
                        if(val > maximum)
                            val = maximum;
                        if(val < minimum)
                            val = minimum;
                        if(text.length() == 0){
                            text = minimum+"";
                        }
                        text = val +"";
                        cursorPos++;
                        if(cursorPos > text.length()) cursorPos = text.length();
                    }
                    if(command == '-'){
                        if(!text.contains("-")) {
                            text = "-" + text;
                            if(text.length()==1) text+="0";
                            int val = Integer.parseInt(text);
                            if (val > maximum)
                                val = maximum;
                            if (val < minimum)
                                val = minimum;
                            if (text.length() == 0) {
                                text = minimum + "";
                            }
                            text = val + "";
                            cursorPos++;
                        } else {
                            text = text.substring(1);
                            int val = Integer.parseInt(text);
                            if (val > maximum)
                                val = maximum;
                            if (val < minimum)
                                val = minimum;
                            if (text.length() == 0) {
                                text = minimum + "";
                            }
                            text = val + "";
                            cursorPos--;
                        }
                    }
                }
                break;
        }
    }
    public void setText(String text){
        if (maximum > -1){
            if(text.equals("null")){
                text = minimum+"";
            }
        }
        this.text = text;
    }
}
