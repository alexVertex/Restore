package engine.render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Model {
    private  int draw_count;

    private  int v_id;
    private  int t_id;
    private  int c_id;

    private  int i_id;

    public Model(float[] verticles,float[] tex_coords,float[] colors, int[] indicies){
        draw_count = indicies.length;



        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,v_id);
        glBufferData(GL_ARRAY_BUFFER,createBuffer(verticles),GL_DYNAMIC_DRAW);

        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,t_id);
        glBufferData(GL_ARRAY_BUFFER,createBuffer(tex_coords),GL_DYNAMIC_DRAW);

        c_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,c_id);
        glBufferData(GL_ARRAY_BUFFER,createBuffer(colors),GL_DYNAMIC_DRAW);

        i_id = glGenBuffers();

        IntBuffer buffer = BufferUtils.createIntBuffer(indicies.length);
        buffer.put(indicies);
        buffer.flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,i_id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,buffer,GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);

        glBindBuffer(GL_ARRAY_BUFFER,0);

    }
    public void setPosition(float[] verticles){
        glBindBuffer(GL_ARRAY_BUFFER,v_id);
        glBufferData(GL_ARRAY_BUFFER,createBuffer(verticles),GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);

    }
    public void setTextureCoords(float[] textureCoords){
        glBindBuffer(GL_ARRAY_BUFFER,t_id);
        glBufferData(GL_ARRAY_BUFFER,createBuffer(textureCoords),GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }
    public void setColors(float[] colors){
        glBindBuffer(GL_ARRAY_BUFFER,c_id);
        glBufferData(GL_ARRAY_BUFFER,createBuffer(colors),GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }
    public void render(){
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER,v_id);
        glVertexPointer(3,GL_FLOAT,0,0);

        glBindBuffer(GL_ARRAY_BUFFER,c_id);
        glColorPointer(4,GL_FLOAT,0,0);

        glBindBuffer(GL_ARRAY_BUFFER,t_id);
        glTexCoordPointer(2,GL_FLOAT,0,0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,i_id);
        glDrawElements(GL_TRIANGLES,draw_count,GL_UNSIGNED_INT,0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);


        glBindBuffer(GL_ARRAY_BUFFER,0);

        glDisableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);

    }

    private FloatBuffer createBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static HashMap<String, Model> models = new HashMap<>();
    public static Model getModel(String name){
        return models.getOrDefault(name,null);
    }
    public static void addModel(String name, Model model){
        models.put(name,model);
    }
}
