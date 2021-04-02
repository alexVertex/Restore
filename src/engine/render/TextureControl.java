package engine.render;

import org.lwjgl.Sys;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class TextureControl {

    private static final String TEXTURES_PATH = "res/tex/";
    private static final String TEXTURES_TYPE = "png";
    private static final String TEXTURES_PATH_ERROR = "res/tex/error";

    private static HashMap<String, org.newdawn.slick.opengl.Texture> textures = new HashMap<>();
    static org.newdawn.slick.opengl.Texture loadTexture(String key){
        if(textures.containsKey(key)){
            return textures.get(key);
        } else {
            String texturePath = TEXTURES_PATH + key +"."+ TEXTURES_TYPE;
            try {
                FileInputStream textureStream = new FileInputStream(texturePath);

                org.newdawn.slick.opengl.Texture load = TextureLoader.getTexture(TEXTURES_TYPE,textureStream);
                textures.put(key,load);
                return load;
            } catch (IOException e) {
                System.err.println("Файл не найден: " + texturePath);
                texturePath = TEXTURES_PATH_ERROR +"."+ TEXTURES_TYPE;
                try {
                    FileInputStream textureStream = new FileInputStream(texturePath);
                    org.newdawn.slick.opengl.Texture load = TextureLoader.getTexture(TEXTURES_TYPE,textureStream);
                    textures.put(key,load);
                    return load;
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
        return null;
    }
    static void deleteTexture(String key){
        if(textures.containsKey(key)){
            textures.get(key).release();
            textures.remove(key);
        }
    }

    public static void preLoad(String key) {
        if(textures.containsKey(key)){
            return;
        } else {
            String texturePath = TEXTURES_PATH + key +"."+ TEXTURES_TYPE;
            try {
                FileInputStream textureStream = new FileInputStream(texturePath);
                org.newdawn.slick.opengl.Texture load = TextureLoader.getTexture(TEXTURES_TYPE,textureStream);
                textures.put(key,load);
                return;
            } catch (IOException e) {
                System.err.println("Файл не найден: " + texturePath);
                texturePath = TEXTURES_PATH_ERROR +"."+ TEXTURES_TYPE;
                try {
                    FileInputStream textureStream = new FileInputStream(texturePath);
                    org.newdawn.slick.opengl.Texture load = TextureLoader.getTexture(TEXTURES_TYPE,textureStream);
                    textures.put(key,load);
                    return;
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
        return;
    }
}
