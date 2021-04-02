package engine;

import org.lwjgl.util.Point;

import java.util.Random;

public class Mathp {
    static class Line {
        private double k;//������� ����������� ������
        private double b;//����� ����������� ������ � ���� ������� (� ���� Y)
        private double x1;
        private double x2;
        private double y1;
        private double y2;

        public Line(double X1, double X2, double Y1, double Y2) {
            x1 = X1;
            x2 = X2;
            y1 = Y1;
            y2 = Y2;
            if(x2 == x1){
                x2+=0.1;
            }
            k = (y2 - y1) / (x2 - x1);
            b = y1 - k * x1;
        }
    }



    private static Random r = new Random();
    //��������� �� ����� a � b �� ���������� d?
    public static boolean inRange(double aX,double aY,double bX,double bY,double d){
        return (Math.sqrt((aY-bY)*(aY-bY)+(aX-bX)*(aX-bX))< d);
    }
    //����������� ���������� ����� ������� a � b
    public static double rast(double aX,double aY,double bX,double bY){
        return (Math.sqrt((aY-bY)*(aY-bY)+(aX-bX)*(aX-bX)));
    }
    //��������� ���������� ����� �� 0 �� 1
    public static double random(){
        return r.nextDouble();
    }
    //��������� �� ����� � ������������ (x;y) � �������������� � ��������� ��������� (left;top;right;bot)
    public static boolean inRectangle(double left, double top, double right, double bot,double x, double y){
        boolean inOSX = left <= x && right >= x;
        boolean inOSY = top <= y && bot >= y;
        return inOSX && inOSY;
    }
    //��������� ���������� �� ���� �������
    public static double getGipotenusa(double katetOne, double katetTwo){
        return Math.sqrt(katetOne*katetOne+katetTwo*katetTwo);
    }
    //��������� ������ �� ���������� � ����
    public static double[] getKetets(double gipotenusa, double angle){
        double[] answer= {0,0};
        answer[0] = gipotenusa*Math.cos(angle);
        answer[1] = gipotenusa*Math.sin(angle);
        return answer;
    }
    //��������� ������� ���� �� ���� �������
    public static double tangens(double katetOne, double katetTwo){
        return Math.atan2(katetOne,katetTwo);
    }
    //���������� ����� ����(� ��������) ����� ����� ������
    public static double angleBetwinAngles(double one, double two){
        double angleRotate = Math.abs(one - two);
        if(angleRotate > Math.PI)
            angleRotate = 2*Math.PI - angleRotate;
        return angleRotate;
    }
    //���������� ����� ����(� ��������) ����� ����� ������
    public static double arcCenter(double a, double b) {
        double len = (a + b) / 2;
        if (Math.abs(a) + Math.abs(b) > Math.PI && a * b < 0) {
            len -= Math.PI;
        }
        return len;
    }

    //���������� ����� ����������� ���� ������
    public static Point CrossPoint(double line1x1, double line1y1, double line1x2, double line1y2, double line2x1, double line2y1, double line2x2, double line2y2) {
        Line one = new Line(line1x1, line1x2, line1y1, line1y2);
        Line two = new Line(line2x1, line2x2, line2y1, line2y2);
        if (one.k != one.k)
            return null;
        if (two.k != two.k)
            return null;
        if (one.k != two.k) {
            double crossX = (one.b - two.b) / (two.k - one.k);
            double crossY = one.k * crossX + one.b;
            return new Point((int) crossX, (int) crossY);
        }
        return null;
    }
    //��������� �� ���� x � ��������� ����� ������ a � b
    public static boolean isAngleInRange(double x, double a, double b) {
        if (Math.abs(a) + Math.abs(b) > Math.PI && a * b < 0) {
            if (x > 0) x -= Math.PI;
            else x += Math.PI;
            if (a > 0) a -= Math.PI;
            else a += Math.PI;
            if (b > 0) b -= Math.PI;
            else b += Math.PI;
        }
        return (x > Math.min(a, b) && x < Math.max(a, b));
    }
    //���������� ���������� �� ����� �� �������
    public static double rastPointOtrezok(double linex1,double liney1,double linex2,double liney2,double  pointx,double pointy){
        double firstUpper = (pointx-linex1)*(linex2-linex1)+(pointy-liney1)*(liney2-liney1);
        double firstLower = (linex2-linex1)*(linex2-linex1)+(liney2-liney1)*(liney2-liney1);
        double t = firstUpper/firstLower;
        if(t < 0) return Integer.MAX_VALUE;
        if(t > 1) return Integer.MAX_VALUE;
        double secondLeft = (linex1-pointx + (linex2 - linex1)*t);
        double secondRight = (liney1-pointy + (liney2 - liney1)*t);
        double l = Math.sqrt(secondLeft*secondLeft+secondRight*secondRight);
        return l;
    }
    // ���������� ����� �����������
    public static Point getPerpendicularIntersection(Point point, Point line1, Point line2) {
        double x1 = line1.getX();
        double y1 = line1.getY();
        double x2 = line2.getX();
        double y2 = line2.getY();
        double x3 = point.getX();
        double y3 = point.getY();
        double k = ((y2 - y1) * (x3 - x1) - (x2 - x1) * (y3 - y1)) / (Math.pow(y2 - y1, 2.0D) + Math.pow(x2 - x1, 2.0D));
        double x4 = x3 - k * (y2 - y1);
        double y4 = y3 + k * (x2 - x1);
        return new Point((int)x4, (int)y4);
    }

    //������������� ���� (��������� ��� � �������� �� -PI �� +PI)
    public static double normalAngle(double in){
        double out = in;
        if(out>Math.PI)
            out-=2*Math.PI;
        else if(out<-Math.PI)
            out+=2*Math.PI;
        return out;
    }
    public static double getLine(double x, double y, double a, double b, double c){
        return a*x+b*y+c;
    }
}
