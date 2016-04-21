package dk.kea.class2016february.cristianaman.awesometetris;

import android.media.SoundPool;

/**
 * Created by mancr on 14/03/2016.
 */
public class Sound
{
    int soundId;
    SoundPool soundPool;

    public Sound(SoundPool soundPool, int soundId)
    {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    public void play(float volume)
    {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    public void dispose()
    {
        soundPool.unload(soundId);
    }
}
