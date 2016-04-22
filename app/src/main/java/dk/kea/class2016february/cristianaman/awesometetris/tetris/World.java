package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import android.util.Log;

import java.util.Random;

/**
 * Created by mancr on 12/04/2016.
 */
public class World
{
    public static final float MIN_X = 22;
    public static final float MAX_X = 222;
    public static final float MIN_Y = 20;
    public static final float MAX_Y = 460;

    public TetrisGrid grid;
    private Random random;

    float fallTime = 0;
    float touchTime = 0;

    public World()
    {
        grid = new TetrisGrid();
        random = new Random();
    }

    public void update(float deltaTime, float accelX, float touchX)
    {
        fallTime += deltaTime;
        if (fallTime > 0.5f) // TODO fall time is affected by level
        {
            // FALL
            if (grid.moveIsPossible(0, 1))
            {
                grid.moveTetramino(0, 1);
            } else
            {
                grid.getTetramino().stop();
                grid.createTetramino();
            }

            fallTime = 0;
        }


        touchTime += deltaTime;
        if (touchTime > 0.1f)
        {
            // TEST x
            int offX = random.nextInt(3) - 1;
            Log.d("WORLD", " OFFX = " + offX);
            if (grid.moveIsPossible(offX, 0))
            {
                grid.moveTetramino(offX, 0);
            }

            touchTime = 0;
        }
    }


}