package screens.editor.mainScreen;

import engine.Mathp;
import engine.control.InputMain;
import engine.control.interfaces.Droplist;
import engine.control.interfaces.Radio;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.game.Tile;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.List;

public class tileMode {
    static String tileTexture = "A0";
    static String tilesFolder = "tiles/";
    static int chosenTileX = 0;
    static int chosenTileY = 0;

    static  String[] drawTypes = {"Карандаш","Линия","Прямоугольник закрашенный","Прямоугольник","Эллипс закрашенный","Эллипс","Заливка"};
    static double[] xS = {10,10,10,10,10,10,10};
    static double[] yS = {285+40,285+60,285+80,285+100,285+120,285+140,285+160};
    static  Radio radioDrawType = new Radio(xS,yS,15,15,"interface/flag",0);

    static String[] drawTypes1 = {"-1","0","0.2","0.5","0.7","1.0"};
    static  double[] xS1 = {10,50,90,130,170,210};
    static double[] yS1 = {285+200,285+200,285+200,285+200,285+200,285+200};
    static Radio radioPassment = new Radio(xS1,yS1,15,15,"interface/flag",0);
    static Droplist dropType = new Droplist(128,285+230,256,30,"interface/drop",0,new String[]{"Земля","Трава","Камень","Песок","Снег","Дерево","Грязь","Вода"},Text.CAMBRIA_14,4);

    static int drawMode = 0;
    static boolean isFirstClick = true;
    static int firstX, firstY;
    static int secondX, secondY;

    public static void control() {
        if (InputMain.isKeyJustPressed(InputMain.LMB) || InputMain.isKeyJustReleased(InputMain.LMB)) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    if (Mathp.inRectangle(0 + i * 16, 0 + j * 16, 16 + i * 16, 16 + j * 16, InputMain.getCursorX(), InputMain.getCursorY())) {
                        chosenTileX = i;
                        chosenTileY = j;
                    }
                }
            }
        }

        dropType.work();
        radioDrawType.work();
        drawMode = (int) radioDrawType.getSignal();
        radioPassment.work();

        if (InputMain.isKeyPressed(InputMain.LMB)) {
            if (InputMain.getCursorX() > 256) {
                int pointX = (InputMain.getCursorX() - Camera.locX + 16) / 32;
                int pointY = (InputMain.getCursorY() - Camera.locY + 16) / 32;
                if (pointX < 0 || pointY < 0 || pointX >= StartGame.game.sizeX || pointY >= StartGame.game.sizeY)
                    return;
                if (drawMode == 0) {
                    if (isFirstClick) {
                        firstX = pointX;
                        firstY = pointY;
                        isFirstClick = false;
                    } else {
                        secondX = firstX;
                        secondY = firstY;
                        firstX = pointX;
                        firstY = pointY;
                        line(firstX, secondX, firstY, secondY);

                    }
                } else {
                    if (isFirstClick) {
                        firstX = pointX;
                        firstY = pointY;
                        isFirstClick = false;
                    } else {
                        secondX = pointX;
                        secondY = pointY;
                    }
                }

            }
        } else {
            if (!isFirstClick) {
                isFirstClick = true;
                if (drawMode == 1) {
                    line(firstX, secondX, firstY, secondY);
                } else if (drawMode == 2) {
                    rectangle(firstX, firstY, secondX, secondY, true);
                } else if (drawMode == 3) {
                    rectangle(firstX, firstY, secondX, secondY, false);
                } else if (drawMode == 4) {
                    int cx = (firstX + secondX) / 2;
                    int cy = (firstY + secondY) / 2;
                    int rx = (int) Math.ceil(Math.abs(firstX - secondX) / 2.0);
                    int ry = (int) Math.ceil(Math.abs(firstY - secondY) / 2.0);
                    circle(cx, cy, rx, ry, true);
                } else if (drawMode == 5) {
                    int cx = (firstX + secondX) / 2;
                    int cy = (firstY + secondY) / 2;
                    int rx = (int) Math.ceil(Math.abs(firstX - secondX) / 2.0);
                    int ry = (int) Math.ceil(Math.abs(firstY - secondY) / 2.0);
                    circle(cx, cy, rx, ry, false);
                } else if (drawMode == 6) {
                    fill(secondX, secondY);
                }
            }
        }
    }



    public static void draw() {
        dropType.draw();
        radioDrawType.draw();
        radioPassment.draw();
        Render2D.simpleDraw("tilesCurrent",tilesFolder + tileTexture, 128, 128, 256, 256);
        Render2D.simpleDraw("editorSelector","editor/selector", 8 + chosenTileX * 16, 8 + chosenTileY * 16, 16, 16);
        Text.drawString("Метод рисования", 4, 295, Text.CAMBRIA_14, Color.white);
        for (int i = 0; i < drawTypes.length; i++) {
            Text.drawString(drawTypes[i], 4 + 24, 295 + i * 20 + 20, Text.CAMBRIA_14, Color.white);

        }
        drawTIle(tilesFolder + tileTexture, 20, 276, 32, 32, 16, 16);
        if (!isFirstClick) {
            drawTIle(tilesFolder + tileTexture, firstX * 32 + Camera.locX, firstY * 32 + Camera.locY, 32, 32, 16, 16);
            drawTIle(tilesFolder + tileTexture, secondX * 32 + Camera.locX, secondY * 32 + Camera.locY, 32, 32, 16, 16);
        }
        Text.drawString("Проходимость", 4, 295 + 20 * 8, Text.CAMBRIA_14, Color.white);
        for (int i = 0; i < drawTypes1.length; i++) {
            Text.drawString(drawTypes1[i], (float) xS1[i] + 12, (float) yS1[i] - 9, Text.CAMBRIA_14, Color.white);
        }
    }
    static void drawTIle(String texture, double locX,double locY, double sizeX, double sizeY,double tilesInLine,double tilesInColomn) {

        double cutLeft = chosenTileX/tilesInLine;
        double cutRight = 1-(chosenTileX+1)/tilesInLine;
        double cutUp = chosenTileY/tilesInColomn;
        double cutDown = 1-(chosenTileY+1)/tilesInColomn;
        Render2D.angleCutDraw("editorChoosen",texture,locX,locY,sizeX,sizeY,0,cutLeft,cutRight,cutUp,cutDown);
    }


    static void changeTile(int x, int y){
        int getTileIndex = y + x * StartGame.game.sizeX;
        StartGame.game.tiles.get(getTileIndex).setVariable("partX", chosenTileX);
        StartGame.game.tiles.get(getTileIndex).setVariable("partY", chosenTileY);
        double pass = radioPassment.getSignal();
        if(pass > 1){
            pass = (pass-1)*0.25;
        } else if(pass == 1){
            pass = 0;
        } else {
            pass = -1;
        }
        StartGame.game.passment[x][y] = pass;
        StartGame.game.tileMaterial[x][y] = dropType.getVal();
    }
    static void fill(int x, int y) {
        int getTileIndex = y + x * StartGame.game.sizeX;
        Tile start = StartGame.game.tiles.get(getTileIndex);
        if (start.getVariable("partX").equals(chosenTileX) && start.getVariable("partY").equals(chosenTileY) && start.getVariable("texture").equals(tilesFolder+tileTexture)) {

        } else
            fill(x, y, start.getVariable("texture") + "", start.getVariableInteger("partX"), start.getVariableInteger("partY"), 0);

    }
    static void fill(int x1, int y1, String startTexture, int startX, int startY,int deep){
        List<int[]> points = new ArrayList<>();
        points.add(new int[]{x1,y1});
        while (points.size()>0){
            int x = points.get(0)[0];
            int y = points.get(0)[1];
            int getTileIndex = y + x * StartGame.game.sizeX;
            Tile under = StartGame.game.tiles.get(getTileIndex);
            if(under.getVariable("partX").equals(startX) && under.getVariable("partY").equals(startY) && under.getVariable("texture").equals(startTexture)){
                changeTile(x,y);
                if(x-1 >=0 && neadFill(x-1,y,startTexture,startX,startY)) {
                    points.add(new int[]{x-1,y});
                }
                if(y-1 >=0 && neadFill(x,y-1,startTexture,startX,startY)) {
                    points.add(new int[]{x,y-1});                }
                if(x+1 <=StartGame.game.sizeX-1&& neadFill(x+1,y,startTexture,startX,startY)) {
                    points.add(new int[]{x+1,y});                }
                if(y+1 <=StartGame.game.sizeY-1&& neadFill(x,y+1,startTexture,startX,startY)) {
                    points.add(new int[]{x,y+1});                }
            }
            points.remove(0);
        }

    }
    static boolean neadFill(int x, int y, String startTexture, int startX, int startY) {
        int getTileIndex = y + x * StartGame.game.sizeX;
        Tile under = StartGame.game.tiles.get(getTileIndex);
        if (under.getVariable("partX").equals(startX) && under.getVariable("partY").equals(startY) && under.getVariable("texture").equals(startTexture)) {
            return true;
        }
        return false;
    }
    static void rectangle(int x,int y,int x1,int y1,boolean fill){
        int startX = Math.min(x,x1);
        int finishX = Math.max(x,x1)+1;
        int startY = Math.min(y,y1);
        int finishY = Math.max(y,y1)+1;
        for(int i = startX; i < finishX;i++){
            for(int j = startY; j < finishY;j++){
                if(fill || i == startX || j == startY || i == finishX-1||j == finishY-1)
                    changeTile(i,j);
            }
        }
    }
    static void circle(int x, int y, int a, int b,boolean fill){

        int _x = 0; // Компонента x
        int _y = b; // Компонента y
        int a_sqr = a * a; // a^2, a - большая полуось
        int b_sqr = b * b; // b^2, b - малая полуось
        int delta = 4 * b_sqr * ((_x + 1) * (_x + 1)) + a_sqr * ((2 * _y - 1) * (2 * _y - 1)) - 4 * a_sqr * b_sqr; // Функция координат точки (x+1, y-1/2)
        while (a_sqr * (2 * _y - 1) > 2 * b_sqr * (_x + 1)) // Первая часть дуги
        {
            changeTile(x+_x,y+_y);
            changeTile(x+_x,y-_y);
            changeTile(x-_x,y-_y);
            changeTile(x-_x,y+_y);
            if(fill){
                for(int i = x-_x;i <= x+_x;i++){
                    for(int j = y-_y;j <= y+_y;j++){
                        changeTile(i,j);
                    }
                }
            }
            if (delta < 0) // Переход по горизонтали
            {
                _x++;
                delta += 4 * b_sqr * (2 * _x + 3);
            }
            else // Переход по диагонали
            {
                _x++;
                delta = delta - 8 * a_sqr * (_y - 1) + 4 * b_sqr * (2 * _x + 3);
                _y--;
            }
        }
        delta = b_sqr * ((2 * _x + 1) * (2 * _x + 1)) + 4 * a_sqr * ((_y + 1) * (_y + 1)) - 4 * a_sqr * b_sqr; // Функция координат точки (x+1/2, y-1)
        while (_y + 1 != 0) // Вторая часть дуги, если не выполняется условие первого цикла, значит выполняется a^2(2y - 1) <= 2b^2(x + 1)
        {
            changeTile(x+_x,y+_y);
            changeTile(x+_x,y-_y);
            changeTile(x-_x,y-_y);
            changeTile(x-_x,y+_y);
            if(fill){
                for(int i = x-_x;i <= x+_x;i++){
                    for(int j = y-_y;j <= y+_y;j++){
                        changeTile(i,j);
                    }
                }
            }
            if (delta < 0) // Переход по вертикали
            {
                _y--;
                delta += 4 * a_sqr * (2 * _y + 3);
            }
            else // Переход по диагонали
            {
                _y--;
                delta = delta - 8 * b_sqr * (_x + 1) + 4 * a_sqr * (2 * _y + 3);
                _x++;
            }
        }
    }
    static void line(int x, int x1, int y, int y1) {

        if(x1 > x) x1++;
        if(x1 < x) x1--;
        if(y1 > y) y1++;
        if(y1 < y) y1--;
        int incX, incY;
        int errorX=0, errorY=0;
        int currentX, currentY;
        int dx = x1-x;
        int dy = y1-y;

        if(dx > 0) incX = 1;
        else if(dx == 0) incX = 0;
        else incX = -1;
        if(dy > 0) incY = 1;
        else if(dy == 0) incY = 0;
        else incY = -1;
        dx = Math.abs(dx);
        dy = Math.abs(dy);

        int l = Math.max(dx,dy)+1;

        currentX = x;
        currentY = y;

        while (true){
            errorX+=dx;
            errorY+=dy;
            changeTile(currentX,currentY);
            if(errorX>l){
                errorX-=l;
                currentX+=incX;
            }
            if(errorY>l){
                errorY-=l;
                currentY+=incY;
            }
            if (currentX == x1 && currentY == y1) break;



        }
    }
}
