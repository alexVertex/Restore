package engine.audio3;


import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.OggData;
import org.newdawn.slick.openal.OggDecoder;
import org.newdawn.slick.util.ResourceLoader;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.*;

import static org.lwjgl.openal.AL10.*;

public class SoundBuffer {
    private final int bufferId;
    public SoundBuffer(String file) {
        this.bufferId = alGenBuffers();
        OggDecoder decoder = new OggDecoder();
        InputStream in = ResourceLoader.getResourceAsStream(file);
        OggData ogg = null;
        try {
            ogg = decoder.getData(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AL10.alBufferData(this.bufferId, ogg.channels > 1 ? 4355 : 4353, ogg.data, ogg.rate);
    }

    public int getBufferId() {
        return this.bufferId;
    }

    public void cleanup() {
        alDeleteBuffers(this.bufferId);
    }
}
