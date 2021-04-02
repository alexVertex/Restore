package game.actor.player;

import engine.Mathp;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import org.newdawn.slick.Color;

import java.util.ArrayList;
import java.util.List;

import static game.actor.game.Tile.SIZE;

public class NPCPackage {
    public NPCPackage(String s) {
        String[] splitted = s.split(",");
        type = Integer.parseInt(splitted[0]);
        if(type == 0){
            for(int i = 1; i < splitted.length;i++){
                borders[i-1] = Double.parseDouble(splitted[i]);
            }
        } else if(type == 1){
            for(int i = 1; i < splitted.length;i+=3){
                Points points = new Points(Double.parseDouble(splitted[i]),Double.parseDouble(splitted[i +1]),Double.parseDouble(splitted[i +2]));
                patrol.add(points);
            }
        }
    }

    public int getType() {
        return type;
    }
    public void addPatrol(){
        Points last = patrol.get(patrol.size()-1);
        Points points = new Points(last.x+32,last.y+32,last.time);
        patrol.add(points);
    }

    public void removePatrol(){
        patrol.remove(patrol.size()-1);
    }

    public String save() {
        switch (type){
            case 0:
                return 0+","+borders[0]+","+borders[1]+","+borders[2]+","+borders[3]+","+borders[4]+","+borders[5];
            case 1:
                StringBuilder answer = new StringBuilder("1");
                for (Points points : patrol) {
                    answer.append(",").append(points.x).append(",").append(points.y).append(",").append(points.time);
                }
                return answer.toString();
        }
        return "";
    }

    class Points{
        double x,y,a,time;
        Points(double X, double Y, double Time){
            x = X;
            y = Y;
            time = Time;
        }
    }
    private static final int TYPE_WANDER = 0;
    private static final int TYPE_PATROL = 1;

    private int type;
    private int timeWait = 0;
    private int point = 0;

    private double[] borders = new double[6];
    private List<Points> patrol = new ArrayList<>();
    public NPCPackage(double borderLeft, double borderRight, double borderTop, double borderBot,double timeWainMin,double timeWaitMax){
        type = TYPE_WANDER;
        borders[0] = borderLeft;
        borders[1] = borderRight;
        borders[2] = borderTop;
        borders[3] = borderBot;
        borders[4] = timeWainMin;
        borders[5] = timeWaitMax;
    }

    public NPCPackage(double[][] points,boolean reversePatrol){
        type = TYPE_PATROL;
        for (double[] point1 : points) {
            Points points1 = new Points(point1[0], point1[1], point1[2]);
            patrol.add(points1);
        }
        if(reversePatrol){
            for(int i = points.length-2; i > 0;i--){
                Points points1 = new Points(points[i][0],points[i][1],points[i][2]);
                patrol.add(points1);
            }
        }
    }

    public void draw(){
        switch (type){
            case TYPE_WANDER:
                Render2D.simpleDraw("editor/waypoint",borders[0]+ Camera.locX,borders[2]+Camera.locY,32,32);
                Render2D.simpleDraw("editor/waypoint",borders[0]+ Camera.locX,borders[3]+Camera.locY,32,32);
                Render2D.simpleDraw("editor/waypoint",borders[1]+ Camera.locX,borders[2]+Camera.locY,32,32);
                Render2D.simpleDraw("editor/waypoint",borders[1]+ Camera.locX,borders[3]+Camera.locY,32,32);
                break;
            case TYPE_PATROL:
                for(int i =0; i < patrol.size();i++){
                    Render2D.simpleDraw("editor/waypoint",patrol.get(i).x+ Camera.locX,patrol.get(i).y+Camera.locY,32,32);
                    if(i>0){
                        double rast = Mathp.rast(patrol.get(i).x,patrol.get(i).y,patrol.get(i-1).x,patrol.get(i-1).y);
                        double angle = Math.atan2(patrol.get(i).y-patrol.get(i-1).y,patrol.get(i).x-patrol.get(i-1).x);
                        Render2D.angleDraw("editor/waypointArrow",(patrol.get(i).x+patrol.get(i-1).x)/2+ Camera.locX,(patrol.get(i).y+patrol.get(i-1).y)/2+Camera.locY,rast,32,Math.toDegrees(angle));
                    }
                    Text.drawStringCenter(patrol.get(i).time+"",patrol.get(i).x+ Camera.locX,patrol.get(i).y+Camera.locY+20,Text.CAMBRIA_14, Color.white);
                }
                break;
        }
    }

    public int pointUndercursor(){
        switch (type){
            case TYPE_WANDER:
                if(Math.abs(InputMain.getCursorX() -Camera.locX-borders[0])<SIZE/2 && Math.abs(InputMain.getCursorY()-Camera.locY-borders[2])<SIZE/2){
                    return 0;
                }
                if(Math.abs(InputMain.getCursorX() -Camera.locX-borders[0])<SIZE/2 && Math.abs(InputMain.getCursorY()-Camera.locY-borders[3])<SIZE/2){
                    return 1;
                }
                if(Math.abs(InputMain.getCursorX() -Camera.locX-borders[1])<SIZE/2 && Math.abs(InputMain.getCursorY()-Camera.locY-borders[2])<SIZE/2){
                    return 2;
                }
                if(Math.abs(InputMain.getCursorX() -Camera.locX-borders[1])<SIZE/2 && Math.abs(InputMain.getCursorY()-Camera.locY-borders[3])<SIZE/2){
                    return 3;
                }
                break;
            case TYPE_PATROL:
                for(int i =0; i < patrol.size();i++){
                    if(Math.abs(InputMain.getCursorX() -Camera.locX-patrol.get(i).x)<SIZE/2 && Math.abs(InputMain.getCursorY()-Camera.locY-patrol.get(i).y)<SIZE/2){
                        return i;
                    }
                }
                break;
        }
        return -1;
    }

    public int[] getTimeWaitWander(){
        return new int[]{(int)borders[4],(int)borders[5]};
    }
    public void setPoint(int point, int x, int y,int time) {
        switch (type) {
            case TYPE_WANDER:
                switch (point) {
                    case 0:
                        borders[0] = x;
                        borders[2] = y;
                        break;
                    case 1:
                        borders[0] = x;
                        borders[3] = y;
                        break;
                    case 2:
                        borders[1] = x;
                        borders[2] = y;
                        break;
                    case 3:
                        borders[1] = x;
                        borders[3] = y;
                        break;
                    case 4:
                        borders[4] = x;
                        borders[5] = y;
                        break;
                }
                break;
            case TYPE_PATROL:
                patrol.get(point).x = x;
                patrol.get(point).y = y;
                patrol.get(point).time = time;
                break;
        }
    }

    double[] use(double locX, double locY, double tarX, double tarY){
        double[] setCoords = {tarX,tarY,0.5};
        double rast = Mathp.rast(locX,locY,tarX,tarY);
        if(rast == 0) {
            switch (type) {
                case TYPE_WANDER:
                    if(timeWait == 0) {
                        do {
                            setCoords[0] = borders[0] + (borders[1] - borders[0]) * Mathp.random();
                            setCoords[1] = borders[2] + (borders[3] - borders[2]) * Mathp.random();
                            timeWait = (int) (borders[4] + (borders[5] - borders[4]) * Mathp.random());
                            setCoords[0] = (int) (setCoords[0] / 32) * 32;
                            setCoords[1] = (int) (setCoords[1] / 32) * 32;
                        } while (StartGame.game.passment[(int) (setCoords[0]/32)][(int) (setCoords[1]/32)] <= 0);
                    } else {
                        timeWait--;
                    }
                    break;
                case TYPE_PATROL:
                    if(timeWait == 0) {
                        setCoords[0] = patrol.get(point).x;
                        setCoords[1] = patrol.get(point).y;
                        timeWait = (int)patrol.get(point).time;
                        point++;
                        if(patrol.size() == point){
                            point = 0;
                        }
                    } else {
                        timeWait--;
                    }
                    break;
            }
        }
        return setCoords;
    }
}
