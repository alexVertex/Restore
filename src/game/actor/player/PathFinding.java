package game.actor.player;

import engine.Mathp;
import game.StartGame;
import game.actor.enviroment.Item;
import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PathFinding {
    private static int[][] passment = null;
    public static void createPassmentTable(int x , int y){
        passment = new int[x][y];

    }

    private static double getPassmentMap(int x, int y){
        return StartGame.game.passment[x][y];
    }
    public static List<Point> start(double startX, double startY, double finishX, double finishY, boolean needFindWay){

        createPassmentTable(StartGame.game.sizeX,StartGame.game.sizeY);
        List<Vector3f> tests = new ArrayList<>();
        int staX = (int)(startX+16)/32;
        int staY = (int)(startY+16)/32;
        int curX = (int)(startX+16)/32;
        int curY = (int)(startY+16)/32;
        int finX = (int)(finishX+16)/32;
        int finY = (int)(finishY+16)/32;
        if(finX < 0 ){
            finX = 0;
        }
        if(finY < 0 ){
            finY = 0;
        } if( finX > StartGame.game.sizeX-1 ){
            finX = StartGame.game.sizeX -1;
        }
        if(finY > StartGame.game.sizeY-1){
            finY = StartGame.game.sizeY-1;
        }
        boolean finded = false;
        int closestX, closestY;
        if (finX != staX || finY != staY) {
            tests.add(new Vector3f(curX, curY, 1));
            closestX = curX;
            closestY = curY;
            passment[curX][curY] = 1;
            while (tests.size() > 0) {
                curX = (int) tests.get(0).x;
                curY = (int) tests.get(0).y;
                if (curX == finX && curY == finY) {
                    finded = true;
                    break;
                }
                if(Mathp.rast(curX,curY,finX,finY) <= Mathp.rast(closestX,closestY,finX,finY)){
                    closestX = curX;
                    closestY = curY;
                }
                List<Vector3f> add = getTestPoints(curX, curY, (int) tests.get(0).z + 1);
                tests.addAll(add);
                tests.remove(0);
            }
            if(!finded){
                if(needFindWay)
                    return null;
                curX = closestX;
                curY = closestY;
            }
            List<Point> path = new ArrayList<>();
            path.add(new Point(curX, curY));
            while (curX != staX || curY != staY){
                Point back = getBackPoint(curX, curY, passment[curX][curY] - 1,staX,staY);
                path.add(back);
                curX = back.getX();
                curY = back.getY();
            }
            Collections.reverse(path);
            path.remove(0);
            for (Point el : path) {
                el.setX(el.getX()*32);
                el.setY(el.getY()*32);
            }
            return path;
        } else {
            List<Point> path = new ArrayList<>();
            path.add(new Point((int)finishX,(int)finishY));
            return path;
        }
    }
    private static List<Item> pathSave = new ArrayList<>();
    private static boolean isObstackleInLine(double x, double y, double x1, double y1) {
        return testLine((int) x, (int) y, (int) x1, (int) y1);
    }
    private static boolean testLine(int x, int y, int x1, int y1) {
        if(x1 > x) x1++;
        if(x1 < x) x1--;
        if(y1 > y) y1++;
        if(y1 < y) y1--;
        int incX, incY;
        int errorX=0, errorY=0;
        int currentX, currentY;
        int dx = x1-x;
        int dy = y1-y;
        incX = Integer.compare(dx, 0);
        incY = Integer.compare(dy, 0);
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        int l = Math.max(dx,dy)+1;
        currentX = x;
        currentY = y;
        while (true){
            errorX+=dx;
            errorY+=dy;
            if(getPassmentMap(currentX,currentY)<=0){
                return true;
            }
            boolean changeX = false, changeY = false;
            if(errorX>l){
                errorX-=l;
                currentX+=incX;
                changeX = true;
            }
            if(errorY>l){
                errorY-=l;
                currentY+=incY;
                changeY = true;
            }
            if (currentX == x1 && currentY == y1) break;
            if(changeX && changeY){
                if(getPassmentMap(currentX-incX,currentY)<=0){
                    return true;
                }
                if(getPassmentMap(currentX,currentY-incY)<=0){
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Vector3f> getTestPoints(int x, int y,int z){
        List<Vector3f> tests = new ArrayList<>();
        if(x > 0) {
            if (passment[x - 1][y] == 0 && StartGame.game.passment[x - 1][y] > 0) {
                tests.add(new Vector3f(x - 1, y, z));
                passment[x - 1][y ] = z;
            }
        }
        if(x < StartGame.game.sizeX-1){
            if(passment[x+1][y] == 0&& StartGame.game.passment[x + 1][y] > 0){
                tests.add(new Vector3f(x+1,y,z));
                passment[x + 1][y] = z;
            }
        }
        if(y > 0){
            if(passment[x][y-1] == 0&& StartGame.game.passment[x][y - 1] > 0){
                tests.add(new Vector3f(x,y-1,z));
                passment[x][y - 1] = z;
            }
        }
        if(y < StartGame.game.sizeY-1){
            if(passment[x][y+1] == 0&& StartGame.game.passment[x][y + 1] > 0){
                tests.add(new Vector3f(x,y+1,z));
                passment[x][y + 1] = z;
            }
        }
        return tests;
    }
    private static Point getBackPoint(int x, int y,int z,int startX, int startY){
        List<Point> points = new ArrayList<>();
        if(x > 0) {
            if (passment[x - 1][y] == z) {
                points.add(new Point(x-1,y));
            }
        }
        if(x < StartGame.game.sizeX-1){
            if(passment[x+1][y] == z){
                points.add(new Point(x+1,y));
            }
        }
        if(y > 0){
            if(passment[x][y-1] == z){
                points.add(new Point(x,y-1));
            }
        }
        if(y < StartGame.game.sizeY-1){
            if(passment[x][y+1] == z){
                points.add(new Point(x,y+1));
            }
        }
        points.sort(Comparator.comparingDouble(o -> Mathp.rast(o.getX(), o.getY(), startX, startY)));
        return points.get(0);
    }

    public static double[] getCollisionPoint(double x, double y, double x1, double y1,boolean worked)
    {
        if(worked) {
            return findCollision((int)x, (int)y, (int)x1, (int)y1);
        } else {
            int curX = (int)(x+16)/32;
            int curY = (int)(y+16)/32;
            int finX = (int)(x1+16)/32;
            int finY = (int)(y1+16)/32;
            double[] answer = findCollision(curX, curY, finX, finY);
            if (answer != null) {
                answer[0] *= 32;
                answer[1] *= 32;
            }
            return answer;
        }
    }

    private static double[] findCollision(int x, int y, int x1, int y1) {
        if(x1 > x) x1++;
        if(x1 < x) x1--;
        if(y1 > y) y1++;
        if(y1 < y) y1--;
        int incX, incY;
        int errorX=0, errorY=0;
        int currentX, currentY;
        int dx = x1-x;
        int dy = y1-y;
        incX = Integer.compare(dx, 0);
        incY = Integer.compare(dy, 0);
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        int l = Math.max(dx,dy)+1;
        currentX = x;
        currentY = y;
        while (true){
            errorX+=dx;
            errorY+=dy;
            if(getPassmentMap(currentX,currentY)<0){

                return new double[]{currentX,currentY};
            }
            boolean changeX = false, changeY = false;
            if(errorX>l){
                errorX-=l;
                currentX+=incX;
                changeX = true;
            }
            if(errorY>l){
                errorY-=l;
                currentY+=incY;
                changeY = true;
            }
            if (currentX == x1 && currentY == y1) break;

            if(changeX && changeY){
                if(getPassmentMap(currentX-incX,currentY)<0){

                    return new double[]{currentX-incX,currentY};
                }
                if(getPassmentMap(currentX,currentY-incY)<0){
                    return new double[]{currentX,currentY-incY};
                }
            }
        }
        return null;
    }
}
