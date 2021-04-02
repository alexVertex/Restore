package engine.render;

import engine.Sets;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Set;

import static engine.render.Render2D.getWindowHeight;
import static engine.render.Render2D.getWindowWidth;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    public static void init(){
        try {
            Display.setDisplayMode(new DisplayMode((int) getWindowWidth(), (int) getWindowHeight()));//Задать разрешение окна
            Display.setTitle(engine.Sets.getScreenName());//Задать имя окна
            Display.create();//Создать окно
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        glMatrixMode(GL_PROJECTION);//Инициация рабочей области для рисования двумерных объектов
        glLoadIdentity();
        glOrtho(0, getWindowWidth(), getWindowHeight(), 0, 1, -1);
        glViewport(0, 0,  (int)getWindowWidth(), (int)getWindowHeight());
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_ALPHA);
        glEnable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Mouse.setGrabbed(true);
    }

}
