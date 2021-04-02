package screens.ingame;

import engine.Start;
import engine.control.InputMain;
import engine.render.Camera;
import engine.render.Render2D;
import engine.render.Text;
import game.StartGame;
import game.actor.player.RolePlay;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import static screens.Controls.openESCMenu;

public class GameOverScreen extends GameScreen {

    double alpha = 0.0;
    double alphaAdd = 0.004;
    @Override
    public void input() {
        if (InputMain.isKeyJustReleased(openESCMenu)) {
            Start.setScreen(new MainMenu());
            return;
        }
        if (alpha > 1) {

            if (InputMain.isKeyJustReleased(Keyboard.KEY_SPACE)) {
                StartGame.game.allResurect();
                alphaAdd *= -2;
                int targetCameraX = (int) (-StartGame.game.getControled().getVariableTrunked("locX")+ Render2D.getWindowWidth()/2);
                int targetCameraY = (int) (-StartGame.game.getControled().getVariableTrunked("locY")+ Render2D.getWindowHeight()/2);

                Camera.setLocation(targetCameraX,targetCameraY);
                alpha = 2;
            }
        }
    }

    @Override
    public void logic() {
        super.logic();
        alpha += alphaAdd;
        if(alpha < 1 && alphaAdd < 0){
            Start.setScreen(new GameScreen());
        }
        if(alpha > 2) alpha = 2;
    }

    @Override
    public void render(){
        super.render();
        if(alphaAdd > 0)
        Render2D.angleColorDraw("campBack1","interface/white", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2,Render2D.getWindowWidth(),Render2D.getWindowHeight(),0,1,1,1,alpha);
        Render2D.angleColorDraw("campBack","interface/DeathScreen", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2,Render2D.getWindowWidth(),Render2D.getWindowHeight(),0,1,1,1,alpha-1);
        if(alpha >= 1 && alphaAdd > 0){
            Text.drawString("Ваш отряд потерпел поражение",10,41,Text.CAMBRIA_34, Color.red);
            Text.drawString("Ваш отряд потерпел поражение",11,40,Text.CAMBRIA_34, Color.white);

            Text.drawString("Нажмите ESC, чтобы выйти в меню",10,191,Text.CAMBRIA_34, Color.red);
            Text.drawString("Нажмите ESC, чтобы выйти в меню",11,190,Text.CAMBRIA_34, Color.white);

            Text.drawString("Нажмите SPACE, чтобы продолжить игру",10,261,Text.CAMBRIA_34, Color.red);
            Text.drawString("Нажмите SPACE, чтобы продолжить игру",11,260,Text.CAMBRIA_34, Color.white);
        }
    }
}
