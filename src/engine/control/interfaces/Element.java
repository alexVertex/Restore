package engine.control.interfaces;


import engine.render.Render2D;

public abstract class Element extends Render2D {
    //Элемент интерфейса
    //Представляет собой прямоугольную область на экране, с особыми функциями
    protected double locx, locy,sizex,sizey;
    // Состояние элемента интерфейса
    protected int DISABLED = 0; //Отключён
    protected int ENABLED = 1;//Включён
    protected int HOVERED = 2;//На элемент наведена мышь
    protected int PRESSED = 3;//Элемент зажат
    protected int RELEASED = 4;//Элемент отпущен
    protected final double MAX = 4.0;



    protected int status = 1; //0 - неактивная 1 - нажимаемая 2 - навели на кнопку 3 - нажали на кнопку 4 - отпустили кнопку
    private double signal = 0;//Сигнал, которых испускает элемент игтерфейса при активации

    private String texture;

    public abstract void draw();//Отрисовка элемента интерфейса
    public abstract void work();//Обработка элемента интерфейса

    public boolean isReleased(){
        return status == RELEASED;
    }
    public double getStatus() {
        return status;
    }
    public double getSignal() {
        return signal;
    }

    public String getTexture() {
        return texture;
    }

    public Element setTexture(String texture) {
        this.texture = texture;
        return this;
    }

    public void setSignal(double signal) {
        this.signal = signal;
    }
}
