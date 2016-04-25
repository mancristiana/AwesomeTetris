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
    boolean isMoved;

    public World()
    {
        grid = new TetrisGrid();
        grid.createTetramino();
        random = new Random();
    }

    public void update(float deltaTime, float accelX, boolean isTapped)
    {
        isMoved = false;
        fallTime += deltaTime;
        if (fallTime > 0.5f) // TODO fall time is affected by level
        {
            // FALL
            if (grid.moveIsPossible(0, 1))
            {
                grid.moveTetramino(0, 1);
                isMoved = true;
            } else
            {
                grid.getTetramino().stop(grid);
                grid.createTetramino();
                isMoved = true;
            }

            fallTime = 0;
        }

        int offX = -(int) (accelX * 10 * deltaTime);
        Log.d("WORLD", " OFFX = " + offX);
        if (grid.moveIsPossible(offX, 0))
        {
            grid.moveTetramino(offX, 0);
        }

        if(isTapped && grid.rotateIsPossible())
        {
            grid.rotateTetramino();
        }
    }
}