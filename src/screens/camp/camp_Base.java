package screens.camp;

import engine.Start;
import engine.control.interfaces.Button;
import engine.render.Render2D;
import game.StartGame;
import game.actor.player.NPC;
import screens.Controls;
import screens.ingame.GameScreen;

import static screens.Controls.campScreens;

public class camp_Base {
    static float wide = 600;
    static float high = 516;
    static Button inv = new Button(Render2D.getWindowWidth()/2-wide/2+32,Render2D.getWindowHeight()/2-high/2-40,64,64,"interface/butCampInv","interface/campChange");
    static Button skl = new Button(Render2D.getWindowWidth()/2-wide/2+32+64,Render2D.getWindowHeight()/2-high/2-40,64,64,"interface/butCampSkills","interface/campChange");
    static Button spl = new Button(Render2D.getWindowWidth()/2-wide/2+32+64*2,Render2D.getWindowHeight()/2-high/2-40,64,64,"interface/butCampSpells","interface/campChange");
    static Button jrl = new Button(Render2D.getWindowWidth()/2-wide/2+32+64*3,Render2D.getWindowHeight()/2-high/2-40,64,64,"interface/butCampJournal","interface/campChange");
    static Button trs = new Button(Render2D.getWindowWidth()/2-wide/2+32+64*4,Render2D.getWindowHeight()/2-high/2-40,64,64,"interface/butCampTransitions","interface/campChange");

    static Button rem = new Button(Render2D.getWindowWidth()/2+wide/2-32-64,Render2D.getWindowHeight()/2-high/2-40,64,64,"interface/butCampRemoveMerc","interface/campChange");

    static Button exi = new Button(Render2D.getWindowWidth()/2+wide/2-32,Render2D.getWindowHeight()/2-high/2-40,64,64,"interface/butCancel","interface/cancel");

    static Button[] changes = {inv,skl,spl,jrl,trs};
    static void render(int screen){
        Render2D.angleColorDraw("campBack","interface/white", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2,Render2D.getWindowWidth(),Render2D.getWindowHeight(),0,0,0,0,0.75);
        Render2D.angleDraw("campBorder1","interface/borderBack", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2-high/2,wide,4,0);
        Render2D.angleDraw("campBorder2","interface/borderBack", Render2D.getWindowWidth()/2,Render2D.getWindowHeight()/2+high/2,wide,4,0);
        Render2D.angleDraw("campBorder3","interface/borderBack", Render2D.getWindowWidth()/2+wide/2,Render2D.getWindowHeight()/2,high,4,90);
        Render2D.angleDraw("campBorder4","interface/borderBack", Render2D.getWindowWidth()/2-wide/2,Render2D.getWindowHeight()/2,high,4,90);
        if(StartGame.game.playerInRessurectionPoint()) {

            for (int i = 0; i < changes.length; i++) {
                changes[i].work();
                changes[i].draw();
                if (changes[i].isReleased())
                    Start.setScreen(campScreens[i]);
                if (i == screen)
                    Render2D.angleColorDraw("campChooser", "interface/splFiltersChosen", Render2D.getWindowWidth() / 2 - wide / 2 + 32 + 64 * i, Render2D.getWindowHeight() / 2 - high / 2 - 40, 64, 64, 0, 1, 1, 1, 0.5);
            }
            if(StartGame.game.getChoosenHero() > 0) {
                rem.work();
                rem.draw();
                if (rem.isReleased()) {
                    NPC npc = new NPC(StartGame.game.getControled().getVariableString("backNPC"),
                            StartGame.game.getControled().getVariableDouble("locX"),
                            StartGame.game.getControled().getVariableDouble("locY"));
                    StartGame.game.getNpcList().add(npc);
                    StartGame.game.controlGroup.remove(StartGame.game.getChoosenHero());
                    StartGame.game.setChoosenHero(0);
                }
            }
        }
        exi.work();exi.draw();
        if(exi.isReleased()){
            Start.setScreen(new GameScreen());
        }

    }
}
