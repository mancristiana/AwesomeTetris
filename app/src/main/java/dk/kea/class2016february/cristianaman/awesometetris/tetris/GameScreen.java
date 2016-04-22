package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import android.graphics.Bitmap;

import java.util.List;

import dk.kea.class2016february.cristianaman.awesometetris.Game;
import dk.kea.class2016february.cristianaman.awesometetris.Screen;
import dk.kea.class2016february.cristianaman.awesometetris.State;
import dk.kea.class2016february.cristianaman.awesometetris.TouchEvent;

/**
 * Created by mancr on 12/04/2016.
 */
public class GameScreen extends Screen
{
    Bitmap background;
    Bitmap resume;
    Bitmap gameOver;

    World world;
    WorldRenderer renderer;

    State state = State.Running;

    public GameScreen(Game game)
    {
        super(game);
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        world = new World();
        renderer = new WorldRenderer(game, world);

    }

    @Override
    public void update(float deltaTime)
    {
        // set state based on world
        if (world.grid.isGameOver()) state = State.GameOver;

        // set state on touch
        if (state == State.Paused && game.getTouchEvents().size() > 0) // if touched when paused
        {
            state = State.Running; // unpause
        }
        if (state == State.GameOver) // if game over
        {
            List<TouchEvent> events = game.getTouchEvents();
            int stop = events.size();
            for (int i = 0; i < stop; i++)
            {
                if (events.get(i).type == TouchEvent.TouchEventType.Up)
                {
                    game.setScreen(new MainMenuScreen(game)); // start new game
                    return;
                }
            }
        }
        if (state == State.Running && game.getTouchY(0) < 36 && game.getTouchX(0) > 320 - 36) // if touched when running
        {
            state = state.Paused; // pause
        }


        if (state == State.Running)
        {
            int touchX = -1;
            if (game.isTouchDown(0)) touchX = game.getTouchX(0);
            world.update(deltaTime, game.getAccelerometer()[0], touchX);
        }

        game.drawBitmap(background, 0, 0);
        renderer.render();

        if (state == State.Paused)
        {
            game.drawBitmap(resume, 160 - resume.getWidth() / 2, 240 - resume.getHeight() / 2);
        }
        if (state == State.GameOver)
        {
            game.drawBitmap(gameOver, 160 - gameOver.getWidth() / 2, 240 - gameOver.getHeight() / 2);
        }
    }

    @Override
    public void pause()
    {
        if (state == State.Running)
        {
            state = State.Paused;
        }
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
