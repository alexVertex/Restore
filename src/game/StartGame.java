package game;

import engine.Start;
import game.actor.game.Game;
import screens.editor.storyEditor.editRegion;
import screens.ingame.LoadingScreen;
import screens.ingame.MainMenu;

public class StartGame {
    public static Game game = new Game();
    public static void init(){
        Start.setScreen(new LoadingScreen());
    }
}
