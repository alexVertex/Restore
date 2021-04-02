package game.actor.game;

import engine.Mathp;
import game.StartGame;

public class Region {
   public double[][] ambientLights = {
            {0.24,0.36,0.61},
            {0.24,0.36,0.61},
            {0.36,0.47,0.64},
            {0.67,0.51,0.46},
            {1,0.55,0.41},
            {1,0.78,0.51},
            {1,0.97,0.81},
            {1,1,1},
            {1,1,1},
            {1,1,1},
            {1,1,1},
            {1,1,1},
            {1,1,1},
            {1,1,1},
            {1,1,1},
            {1,1,1},
            {1,1,1},
            {1,1,1},
            {1,0.97,0.81},
            {1,0.80,0.53},
            {1,0.47,0.32},
            {0.65,0.5,0.45},
            {0.36,0.47,0.64},
            {0.24,0.36,0.61}
    };

    public double[] visionMults = {
            0.5,
            0.5,
            0.5,
            0.5,
            0.75,
            0.75,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            0.75,
            0.75,
            0.5,
            0.5,
            0.5,
            0.5
    };

    public  String[] battleMusic = {"atwar","battle","combat","death"};
    public  String[] exploreMusic = {"gipath","glory","nature"};

    public double[] getAmbientLight(){
        double time = StartGame.game.time;
        double timeMax = StartGame.game.timeMax;
        double timePerHour = timeMax/24.0;
        int timeBase = (int) (time/timePerHour);
        time -= timeBase*timePerHour;
        double timeOtn = time/timePerHour;
        if(timeBase == ambientLights.length){
            timeBase = 0;
        }
        int timeNext = timeBase+1;

        if(timeNext == ambientLights.length){
            timeNext = 0;
        }
        return new double[]{
                ambientLights[timeBase][0]+(timeOtn*(ambientLights[timeNext][0]-ambientLights[timeBase][0])),
                ambientLights[timeBase][1]+(timeOtn*(ambientLights[timeNext][1]-ambientLights[timeBase][1])),
                ambientLights[timeBase][2]+(timeOtn*(ambientLights[timeNext][2]-ambientLights[timeBase][2]))};
    }

    public double getVisionMult(){
        double time = StartGame.game.time;
        double timeMax = StartGame.game.timeMax;
        double timePerHour = timeMax/23.0;
        int timeBase = (int) (time/timePerHour);
        return visionMults[timeBase];
    }

    public String getMusic(boolean battle){
        double random = Mathp.random();
        if(battle){
            int index = (int) (random * battleMusic.length);
            return battleMusic[index];
        } else {
            int index = (int) (random * exploreMusic.length);
            return exploreMusic[index];
        }
    }
}
