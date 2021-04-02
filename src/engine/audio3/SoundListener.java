package engine.audio3;

import static org.lwjgl.openal.AL10.*;

public class SoundListener {
    public SoundListener() {
        this(0, 0, 0);
    }

    public SoundListener(float posX, float posY, float posZ ) {
        alListener3f(AL_POSITION, posX, posY, posZ);
        alListener3f(AL_VELOCITY, 0, 0, 0);

    }

    public void setSpeed(float posX, float posY, float posZ) {
        alListener3f(AL_VELOCITY, posX, posY, posZ);
    }

    public void setPosition(float posX, float posY, float posZ) {
        alListener3f(AL_POSITION, posX, posY, posZ);
    }


}
