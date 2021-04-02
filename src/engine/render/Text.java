package engine.render;

import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glColor4d;

public class Text {
    public static final int CAMBRIA_34 = 2;
    public static final int CAMBRIA_54 = 3;

    static char[] russianSymbols = new char[66];//������ � �������� ���������

    static {
        for(int i = 1040; i < 1104;i++){
            russianSymbols[i-1040] = (char)i;//���������� ������� � �������� ���������
        }
        russianSymbols[64] = (char)1025;
        russianSymbols[65] = (char)1105;
    }
    //�������� �������, ������������: �������� ������, ����������, ������, � ����� ������������� ����������� � �������������� ��������
    static TrueTypeFont cambria_24 = new TrueTypeFont(new Font("cambria",Font.PLAIN,24),true,russianSymbols);
    public static int CAMBRIA_24 = 0;

    static TrueTypeFont cambria_34 = new TrueTypeFont(new Font("cambria",Font.PLAIN,34),true,russianSymbols);

    static TrueTypeFont cambria_14 = new TrueTypeFont(new Font("cambria",Font.PLAIN,14),true,russianSymbols);
    public static int CAMBRIA_14 = 1;
    static TrueTypeFont cambria_54 = new TrueTypeFont(new Font("cambria",Font.PLAIN,54),true,russianSymbols);

    //������ �� ��������
    private static TrueTypeFont[] fonts = {cambria_24,cambria_14,cambria_34,cambria_54};

    //����������� ������ ������
    public static float textWidth(String text, int font){
        return fonts[font].getWidth(text);
    }
    //��������� ������ � ��������� � ������ ����
    public static void drawString(String text, double lx, double ly, int font, org.newdawn.slick.Color color){
        fonts[font].drawString((float) lx,(float)ly,text,color);
        glColor4d(1,1,1,1);
    }
    //��������� ������ � ��������� � ������� ����
    public static void drawStringRigth(String text, double x, double y, int font, org.newdawn.slick.Color color){
        float width = textWidth(text,font);
        drawString(text,x-width,y,font,color);
    }
    //��������� ������ � ��������� �� ������
    public static void drawStringCenter(String text, double x, double y, int font, org.newdawn.slick.Color color){
        float width = textWidth(text,font);
        drawString(text,x-width/2,y,font,color);
    }
    public static int linesStringWithOffset(String text, int font, int lineWidth){
        String[] textparts = text.split(" ");
        String drawPart = "";
        int totallines = 1;
        for(String part : textparts){
            drawPart+=part+" ";
            if(lineWidth < textWidth(drawPart,font)){
                drawPart = part+ " ";
                totallines ++;
            }
        }
        return totallines;
    }
    public static int drawStringWithOffset(String text, double x, double y, int font, org.newdawn.slick.Color color, int lineWidth, int perLineY){
        String[] textparts = text.split(" ");
        String drawPart = "";
        int line = (int)y;
        int totallines = 1;
        for(String part : textparts){
            drawPart+=part+" ";
            if(lineWidth < textWidth(drawPart,font)){
                drawPart = drawPart.substring(0,drawPart.length() - part.length()-1);
                drawString(drawPart,x,line,font,color);
                line += perLineY;
                drawPart = part+ " ";
                totallines ++;
            }
        }
        drawString(drawPart,x,line,font,color);
        return totallines;
    }
}
