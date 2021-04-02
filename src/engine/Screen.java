package engine;

import engine.render.Render2D;

public abstract class Screen extends Render2D {
    public void workScreen(){
        input();
        logic();
        render();
    }
    public void preLoad(){}
    public abstract void input();
    public abstract void logic();
    public abstract void render();
}
