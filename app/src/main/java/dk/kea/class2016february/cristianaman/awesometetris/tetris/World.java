package dk.kea.class2016february.cristianaman.awesometetris.tetris;

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

    float fallTime = 0;
    float createTime = 0;

    public World()
    {
        grid = new TetrisGrid();
    }

    public void update(float deltaTime, float accelX, float touchX)
    {
        fallTime += deltaTime;
        if (fallTime > 0.5f)
        {
            if (grid.hasCollision())
            {
                grid.getTetramino().stop();
                grid.createTetramino();
            }
            else
            {
                grid.moveTetramino(0, 1);
            }

            fallTime = 0;
        }
    }


}