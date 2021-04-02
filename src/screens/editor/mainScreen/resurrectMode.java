package screens.editor.mainScreen;

import engine.Actor;
import engine.Mathp;
import engine.Start;
import engine.control.InputMain;
import engine.control.interfaces.Textbox;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.enviroment.RessurectPoint;
import org.newdawn.slick.Color;
import screens.editor.creationWindows.createWindowMagic;

import java.util.List;

public class resurrectMode {

    private static int addPointStage = 0;
    private static int addLeft, addTop, addRight, addBot, addResX, addResY;


    static Textbox textID= new Textbox(128,96,256,32,"interface/text","ИД",Text.CAMBRIA_14,4);
    static Textbox textName = new Textbox(128,96+64,256,32,"interface/text","Имя",Text.CAMBRIA_14,4);
    static Textbox textMapTo = new Textbox(128,96+64*2,256,32,"interface/text","на карту",Text.CAMBRIA_14,4);
    static Textbox textIDTo = new Textbox(128,96+64*3,256,32,"interface/text","требуемая точка перехода",Text.CAMBRIA_14,4);
    static Textbox textMapToFileName = new Textbox(128,96+64*4,256,32,"interface/text","файл карты",Text.CAMBRIA_14,4);


    public static void control() {
        editorScreen.addItem.work();
        if (editorScreen.addItem.isReleased()) {
            addPointStage = 1;
        }
        if (InputMain.isKeyJustReleased(InputMain.RMB)) {
            if (addPointStage > 0) addPointStage--;
            else {
                List<RessurectPoint> ressurections = StartGame.game.ressurections;
                for (int i = 0; i < ressurections.size(); i++) {
                    RessurectPoint el = ressurections.get(i);
                    int left = el.getVariableInteger("left");
                    int top = el.getVariableInteger("top");
                    int right = el.getVariableInteger("right");
                    int bot = el.getVariableInteger("bot");
                    int resX = el.getVariableInteger("locX");
                    int resY = el.getVariableInteger("locY");
                    double curX = InputMain.getCursorX() - Camera.locX + 16;
                    double curY = InputMain.getCursorY() - Camera.locY + 16;
                    if (curX > left && curX < right && curY > top && curY < bot) {
                        StartGame.game.ressurections.remove(el);
                        addPointStage = 3;
                        addLeft = left;
                        addTop = top;
                        addRight = right;
                        addBot = bot;
                        addResY = resY;
                        addResX = resX;
                        textID.setText(el.getVariableString("ID"));
                        textName.setText(el.getVariableString("name"));
                        textMapTo.setText(el.getVariableString("mapTo"));
                        textIDTo.setText(el.getVariableString("transitionTo"));
                        textMapToFileName.setText(el.getVariableString("mapToFileName"));

                    }
                }
            }
        }
        if (InputMain.isKeyJustReleased(InputMain.LMB)) {
            if (addPointStage == 1) {
                double x = InputMain.getCursorX() - Camera.locX + 16;
                double y = InputMain.getCursorY() - Camera.locY + 16;
                x = (int) (x / 32) * 32;
                y = (int) (y / 32) * 32;
                if (Mathp.inRectangle(addLeft - 16, addTop - 16, addLeft + 16, addTop + 16, InputMain.getCursorX() - Camera.locX, InputMain.getCursorY() - Camera.locY)) {
                    addPointStage = 2;
                    addRight = addLeft;
                    addBot = addTop;
                } else {
                    addLeft = (int) x;
                    addTop = (int) y;
                }
            } else if (addPointStage == 2) {
                double x = InputMain.getCursorX() - Camera.locX + 16;
                double y = InputMain.getCursorY() - Camera.locY + 16;
                x = (int) (x / 32) * 32;
                y = (int) (y / 32) * 32;
                if (Mathp.inRectangle(addRight - 16, addBot - 16, addRight + 16, addBot + 16, InputMain.getCursorX() - Camera.locX, InputMain.getCursorY() - Camera.locY)) {
                    addPointStage = 3;
                    addResX = addLeft;
                    addResY = addTop;
                } else {
                    addRight = (int) x;
                    addBot = (int) y;
                }
            } else if (addPointStage == 3) {
                double x = InputMain.getCursorX() - Camera.locX + 16;
                double y = InputMain.getCursorY() - Camera.locY + 16;
                x = (int) (x / 32) * 32;
                y = (int) (y / 32) * 32;
                if (Mathp.inRectangle(addResX - 16, addResY - 16, addResX + 16, addResY + 16, InputMain.getCursorX() - Camera.locX, InputMain.getCursorY() - Camera.locY)) {
                    addPointStage = 0;
                    int addLeft1 = Math.min(addLeft, addRight);
                    int addTop1 = Math.min(addTop, addBot);
                    int addRight1 = Math.max(addLeft, addRight);
                    int addBot1 = Math.max(addTop, addBot);
                    StartGame.game.ressurections.add(new RessurectPoint(addLeft1, addTop1, addRight1, addBot1, addResX, addResY, textMapTo.getText(), textIDTo.getText(), textID.getText(), textName.getText(),textMapToFileName.getText()));
                } else if (x > addLeft && x < addRight && y > addTop && y < addBot) {
                    addResX = (int) x;
                    addResY = (int) y;
                }
            } else {

            }
        }
    }
    public static void draw() {
        editorScreen.addItem.draw();

        textID.work(); textName.work(); textMapTo.work(); textIDTo.work(); textMapToFileName.work();
        textID.draw(); textName.draw(); textMapTo.draw(); textIDTo.draw(); textMapToFileName.draw();

        if(addPointStage == 1){
            double x = addLeft + Camera.locX;
            double y = addTop + Camera.locY;
            Render2D.angleColorDraw("qwert","interface/white",x,y,32,32,0,0,0,1,0.25);
        }
        if(addPointStage == 2){
            double x = addLeft + Camera.locX;
            double y = addTop + Camera.locY;
            Render2D.angleColorDraw("qwert","interface/white",x,y,32,32,0,0,1,0,0.5);
            x = addRight + Camera.locX;
            y = addBot + Camera.locY;
            Render2D.angleColorDraw("qwert","interface/white",x,y,32,32,0,0,0,1,0.5);
        }
        if(addPointStage == 3){
            double x = (addLeft+addRight)/2 + Camera.locX;
            double y = (addTop+addBot)/2 + Camera.locY;
            double sx = (addRight-addLeft);
            double sy = (addBot-addTop);
            Render2D.angleColorDraw("qwert","interface/white",x,y,sx,sy,0,0,1,0,0.5);
            double resX = addResX + Camera.locX;
            double resY = addResY + Camera.locY;
            Render2D.angleColorDraw("qwert1","interface/white",resX,resY,32,32,0,0,1,1,0.5);
        }
    }
}
