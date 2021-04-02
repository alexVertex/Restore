package engine.control;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.HashMap;

public class InputMain {
    public static final int PRESSED = 1;
    public static final int RELEASED = -1;
    public static final int HOLD = 0;
    public static final int NO_USE = -2;

    private static HashMap<Integer,Integer> keyStates = new HashMap<>();
    private static ArrayList<Integer> keyRemove = new ArrayList<>();

    public static void Control(){
        keyStates.forEach((k, v) -> {
            if(v == 1) keyStates.put(k,HOLD);
            if(v == -1) keyRemove.add(k);
        });
        for (Integer el: keyRemove) {
            keyStates.remove(el);
        }
        keyRemove.clear();

        controlKeyboard();
        controlMouse();
    }
    public static int getLastPressedKey(){//����������, ����� ������� ���� ������ � ��������� ���
        return lastPressedKey;

    }

    private static int lastPressedKey = -1;
    private static void controlKeyboard(){

        while (Keyboard.next()) {//���� ��������� ���������� �������� ������� ��������� � �����������
            int key = Keyboard.getEventKey();//���������� �������, ������� ������� �������
            int event = Keyboard.getEventKeyState() ? PRESSED : RELEASED; //���������� ��� �������
            if(event == PRESSED){//���� ������ ������, �� ��� ����� ��������� ��������� �������
                lastPressedKey = key;
            } else if(lastPressedKey == key){//���� ������ �������� � ��� ���� ��������� �������, �� ���������: ��������� ������� ������ ���
                lastPressedKey = -1;
            }
            keyStates.put(key,event);//���������� � ������� ��������� ������ ������� �������
        }
    }

    public static final int LMB = -1;//����� ������
    public static final int RMB = -2;//������ ������
    public static final int WMB = -3;//������ ����


    private static int dWheel = 0;//���� ���� ���������� ������ ����
    private static int cursorX = 0;//���������� ������� ���� � ����
    private static int cursorY = 0;

    private static int cursorOldX = 0;//���������� ������� ���� � ����
    private static int cursorOldY = 0;
    private static void controlMouse(){
        //��������� ������� � ����
        dWheel = 0;//�������� ����������� �������� ������ ����
        while (Mouse.next()) {// ���� ���� �������, ���������� � ����
            int key = Mouse.getEventButton();//���������� ������, ������� ������� �������
            if(key != -1) {
                int event = Mouse.getEventButtonState() ? PRESSED : RELEASED;//���������� ��� �������: "������" ��� "��������"
                keyStates.put(-(key+1), event);//�������� ������� � ������� ��������� ������
            }
        }
        dWheel = Mouse.getDWheel(); // ����������, � ����� ������� �������� ������ ����
        cursorOldX = cursorX;
        cursorOldY = cursorY;
        cursorX = (int) (Mouse.getX());//���������� ������� ��������� ������� ����
        cursorY = (int) ((Display.getHeight() - Mouse.getY()));
    }

    public static int getdWheel(){
        return dWheel;
    }
    public static int getCursorX() {
        return cursorX;
    }
    public static int getCursorY() {
        return cursorY;
    }
    public static int getCursorDX() {
        return cursorX-cursorOldX;
    }
    public static int getCursorDY() {
        return cursorY-cursorOldY;
    }

    public static int getKeyState(Integer Key){
        return keyStates.getOrDefault(Key, NO_USE);
    }
    public static boolean isKeyJustPressed(Integer Key){
        return keyStates.getOrDefault(Key, NO_USE) == PRESSED;
    }
    public static boolean isKeyJustReleased(Integer Key){
        return keyStates.getOrDefault(Key, NO_USE) == RELEASED;
    }
    public static boolean isKeyPressed(Integer Key){
        return keyStates.getOrDefault(Key, NO_USE) > NO_USE;
    }
    public static boolean isKeyReleased(Integer Key){
        return keyStates.getOrDefault(Key, NO_USE) == NO_USE;
    }
    public static boolean isKeyHolded(Integer Key){
        return keyStates.getOrDefault(Key, NO_USE) == HOLD;
    }

    public static void dropStates() {
        keyStates.clear();
    }
}
