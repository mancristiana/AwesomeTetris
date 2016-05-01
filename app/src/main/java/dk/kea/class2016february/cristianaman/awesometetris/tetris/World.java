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
    float moveTime = 0;

    public World()
    {
        Log.d("World constructor", "");
        grid = new TetrisGrid();
        grid.createTetramino();
        random = new Random();
    }

    public void update(float deltaTime, float accelX, boolean isTapped, boolean isDropped)
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
                grid.removeLines();
                grid.createTetramino();
            }

            fallTime = 0;
        }

        moveTime += deltaTime;
        int offX = (int) (accelX * 100 * deltaTime);
        if (offX > 0) offX = -1;
        else if (offX < 0) offX = 1;
        moveTime += (Math.abs(accelX) < 1.5) ? 0 : Math.abs(Math.abs(accelX / 2) - 1.5f);

        if (offX != 0)
//            Log.d("WORLD", " OFFX = " + offX);
            Log.d("ACCEL", accelX + "");
        if (grid.moveIsPossible(offX, 0) && moveTime > 10)
        {
            grid.moveTetramino(offX, 0);
            moveTime = 0;
        }

        if (isTapped && grid.rotateIsPossible())
        {
            grid.rotateTetramino();
        }

        if (isDropped)
        {
            grid.drop();
        }

    }
}