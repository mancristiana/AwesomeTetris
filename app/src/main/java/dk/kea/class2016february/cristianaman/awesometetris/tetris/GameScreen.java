package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import android.graphics.Bitmap;

import dk.kea.class2016february.cristianaman.awesometetris.Game;
import dk.kea.class2016february.cristianaman.awesometetris.Screen;

/**
 * Created by mancr on 12/04/2016.
 */
public class GameScreen extends Screen
{
    Bitmap background;
//    Bitmap resume;
//    Bitmap gameOver;
    World world;
    WorldRenderer renderer;

    public GameScreen(Game game)
    {
        super(game);
        background = game.loadBitmap("background.png");
//        resume = game.loadBitmap("resume.png");
//        gameOver = game.loadBitmap("gameover.png");
        world = new World();
        renderer = new WorldRenderer(game, world);

    }

    @Override
    public void update(float deltaTime)
    {
        int touchX = -1;
        if (game.isTouchDown(0)) touchX = game.getTouchX(0);
        world.update(deltaTime, game.getAccelerometer()[1], touchX);
        game.drawBitmap(background, 0, 0);
        renderer.render();
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }
}
