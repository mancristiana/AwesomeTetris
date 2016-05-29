package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import android.graphics.Bitmap;

import dk.kea.class2016february.cristianaman.awesometetris.Game;

/**
 * Created by mancr on 12/04/2016.
 */
public class WorldRenderer
{
    Game game;
    World world;
    Bitmap tetriminiosImage;

    public WorldRenderer(Game game)
    {
        this.game = game;
        this.world = new World();
        this.tetriminiosImage = game.loadBitmap("tetriminios.png");
    }

    public void render()
    {
        for (int i = 0; i < world.grid.WIDTH; i++)
            for (int j = 0; j < world.grid.HEIGHT; j++)
            {
                Mino mino = world.grid.get()[i][j];
                if (mino != Mino.Blank)
                    game.drawBitmap(tetriminiosImage,
                            (int) (i * Mino.WIDTH + World.MIN_X),
                            (int) (j * Mino.HEIGHT + World.MIN_Y),
                            0, (int) (mino.ordinal() * Mino.HEIGHT),
                            (int) Mino.WIDTH, (int) Mino.HEIGHT);
            }
    }
}
