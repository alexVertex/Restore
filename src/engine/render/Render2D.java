package engine.render;

import engine.Sets;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Render2D {
    private static float bright = 1.0f;

    public static void clearScreen(){ //clears screen into black square
        glClear(GL_COLOR_BUFFER_BIT);
    }//������� ������

    static void draw(String Binded, double Width, double Height, double leftCut, double topCut, double rightCut, double botCut, int filter){

        TextureControl.loadTexture(Binded).bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
        glBegin(GL_QUADS);
        glTexCoord2d(leftCut,topCut);
        glVertex2d(-Width/2,-Height/2); // Upper left
        glTexCoord2d(1-rightCut,topCut);
        glVertex2d(+Width/2,-Height/2); // Upper right
        glTexCoord2d(1-rightCut,1-botCut);
        glVertex2d(+Width/2,+Height/2); // Lower right
        glTexCoord2d(leftCut,1-botCut);
        glVertex2d(-Width/2,+Height/2); // Lower left
        glEnd();
    }
    public static void simpleDraw(String Binded, double Locx, double Locy, double Width, double Height){
        glPushMatrix();
        glTranslated((int)Locx, (int)Locy,0);
        draw(Binded, Width,Height,0,0,0,0,GL_LINEAR);
        glPopMatrix();
    }
    public static void angleDraw(String Binded, double Locx, double Locy, double Width, double Height, double Angle){
        glPushMatrix();
        glTranslated((int)Locx, (int)Locy,0);
        glRotated(Angle,0,0,1);
        draw(Binded, Width,Height,0,0,0,0,GL_LINEAR);
        glPopMatrix();
    }
    public static void angleCutDraw(String Binded, double Locx, double Locy, double Width, double Height, double Angle, double CutLeft, double CutRight, double CutTop, double CutBot){
        glPushMatrix();
        glTranslated((int)Locx, (int)Locy,0);
        glRotated(Angle,0,0,1);
        draw(Binded, Width,Height,CutLeft,CutTop,CutRight,CutBot ,GL_LINEAR);
        glPopMatrix();
    }
    public static  void angleColorDraw(String Binded, double Locx, double Locy, double Width, double Height, double Angle, double red, double green, double blue, double alpha){
        glPushMatrix();
        glColor4d(red,green,blue,alpha);
        glTranslated(Locx, Locy,0);
        glRotated(Angle,0,0,1);
        draw(Binded, Width,Height,0,0,0,0,GL_LINEAR);
        glColor4d(1,1,1,1);
        glPopMatrix();
    }

    public static void angleCutColorDraw(String Binded, double Locx, double Locy, double Width, double Height, double Angle, double CutLeft, double CutRight, double CutTop, double CutBot, double red, double green, double blue, double alpha) {
        glPushMatrix();
        glColor4d(red,green,blue,alpha);
        glTranslated(Locx, Locy,0);
        glRotated(Angle,0,0,1);
        draw(Binded, Width,Height,CutLeft,CutTop,CutRight,CutBot,GL_LINEAR);
        glColor4d(1,1,1,1);
        glPopMatrix();
    }

    public static void renderLight(double[] ambientLight) {
        glBlendFunc(GL_ZERO, GL_ONE_MINUS_SRC_COLOR);
        Render2D.angleColorDraw("ambientLight","interface/white",getWindowWidth()/2,getWindowHeight()/2,getWindowWidth(),getWindowHeight(),0,1-ambientLight[0],1-ambientLight[1],1-ambientLight[2],0.0);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    static void renderLight(double red, double green, double blue){
        glBlendFunc(GL_ZERO, GL_ONE_MINUS_SRC_COLOR);
        Render2D.angleColorDraw("ambientLight","interface/white",getWindowWidth()/2,getWindowHeight()/2,getWindowWidth(),getWindowHeight(),0,1-red,1-green,1-blue,0.0);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    static double wide = 1600;
    static double high = 900;

    public static double getWindowHeight() {
        return high;
    }

    public static double getWindowWidth() {
        return wide;
    }


    static double getWindowHeightBase() {
        return 900.0;
    }

    static double getWindowWidthBase() {
        return 1600.0;
    }

    public static double getSizeOtn() {
        return getWindowWidth()/getWindowWidthBase();
    }
    public static double getHighOtn() {
        return getWindowHeight()/getWindowHeightBase();
    }
    private static float[] createVericles(float locX, float locY, float width, float height,double angle){
        angle = Math.toRadians(angle);
        double angleBase = Math.atan2(height,width);
        double angleBase1 = Math.PI -  Math.atan2(height,width);

        float rast = (float) (Math.sqrt(height*height+width*width)/2);
        float[] vertices = new float[]{
                locX+(float) Math.cos(angleBase+angle)*rast,locY+(float) Math.sin(angleBase+angle)*rast,0,  //TOP LEFT
                locX+(float) Math.cos(angleBase1+angle)*rast,locY+(float) Math.sin(angleBase1+angle)*rast,0,//TOP RIGHT
                locX+(float) Math.cos(-angleBase1+angle)*rast,locY+(float) Math.sin(-angleBase1+angle)*rast,0,//BOTTOM RIGHT
                locX+(float) Math.cos(-angleBase+angle)*rast,locY+(float) Math.sin(-angleBase+angle)*rast,0,//BOTTOM LEFT

        };
        return vertices;
    }
    private static float[] createTexCoords(float left, float top, float right, float bot){
        float[] vertices = new float[]{
                right,bot,//BOTTOM RIGHT
                left,bot,//BOTTOM LEFT
                left,top, //TOP LEFT
                right,top,//TOP RIGHT

        };
        return vertices;
    }
    private static float[] createColors(float red, float green, float blue, float alpha){
        float[] vertices = new float[]{
                red,green,blue,alpha, //TOP LEFT
                red,green,blue,alpha,//TOP RIGHT
                red,green,blue,alpha,//BOTTOM RIGHT
                red,green,blue,alpha,//BOTTOM LEFT

        };
        return vertices;
    }
    private static int[] createIndeces(){
        int[] index = new int[]{
                0,1,2,
                2,3,0,

        };
        return index;
    }
    static Model createModel(double locX, double locY, double width, double height){
        Model model = new Model(createVericles((float) locX,(float)locY,(float)width,(float)height,0),createTexCoords((float)0,(float)0,(float)1,(float)1),createColors(1,1,1,1),createIndeces());
        model.render();
        return model;
    }
    public static void simpleDraw(String modelName, String texture, double locX, double locY, double width, double height){
        Model model = makeModel(modelName, texture, (int)locX, (int)locY, (int)width, (int)height, 0);
        model.render();
    }
    public static void angleDraw(String modelName, String texture, double locX, double locY, double width, double height, double angle){
        Model model = makeModel(modelName, texture, (int)locX, (int)locY, (int)width, (int)height, angle);
        model.render();
    }



    public static void angleCutDraw(String modelName, String texture, double locX, double locY, double width, double height, double angle, double CutLeft, double CutRight, double CutTop, double CutBot){
        Model model = makeModel(modelName, texture, (int)locX, (int)locY, (int)width, (int)height, angle);
        model.setTextureCoords(createTexCoords((float) CutLeft,(float)CutTop,1-(float)CutRight,1-(float)CutBot));
        model.render();
    }
    public static void angleColorDraw(String modelName, String texture, double locX, double locY, double width, double height, double angle, double red, double green, double blue, double alpha){
        Model model = makeModel(modelName, texture, (int)locX, (int)locY, (int)width, (int)height, angle);
        model.setColors(createColors((float) red,(float)green,(float)blue,(float)alpha));
        model.render();
    }

    static Model makeModel(String modelName, String texture, int locX, int locY, int width, int height, double angle) {
        TextureControl.loadTexture(texture).bind();
        Model model = Model.getModel(modelName);

        if(model == null){
            model = createModel( locX,  locY,  width,  height);
            Model.addModel(modelName,model);
        }
        model.setPosition(createVericles( locX,locY,width,height,angle));
        return model;
    }


    public static void setRes(double w, double h) {
        wide = w;
        high = h;
    }

    public static void setBright(float bright) {
        Render2D.bright = bright;
    }

    public static float getBright() {
        return bright;
    }

    public static void drawBright() {

    }
}
