package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

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

    Typeface font;
    WorldRenderer renderer;

    State state = State.Running;

    public GameScreen(Game game)
    {
        super(game);
        background = game.loadBitmap("background.png");
        resume = game.loadBitmap("resume.png");
        gameOver = game.loadBitmap("gameover.png");
        font = game.loadFont("font.TTF");
        Log.d("GameScreen constructor", "");
        renderer = new WorldRenderer(game);

    }

    @Override
    public void update(float deltaTime)
    {
        // set state based on world
        if (renderer.world.grid.isGameOver()) state = State.GameOver;

        // set state on touch
        if (state == State.Paused && game.getTouchEvents().size() > 0) // if touched when paused
        {
            state = State.Running; // unpause
        }
        if (state == State.GameOver && game.isTouchEventUp()) // if game over
        {
            game.setScreen(new MainMenuScreen(game)); // start new game
            return;
        }
        if (state == State.Running && game.getTouchY(0) < 36 && game.getTouchX(0) > 320 - 36) // if touched when running
        {
            state = state.Paused; // pause
        }


        if (state == State.Running)
        {
            boolean isTapped = game.isTouchEventUp();
            boolean isDropped = game.isTouchEventUp() && game.getTouchY(0) > 400;
            renderer.world.update(deltaTime, game.getAccelerometer()[0], isTapped && !isDropped, isDropped);
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

        game.drawText(font, Integer.toString(Statistics.score),         248, 87, Color.parseColor("#7e3272"), 20);
        game.drawText(font, Integer.toString(Statistics.linesCount),    248, 177, Color.parseColor("#323392"), 20);
        game.drawText(font, Integer.toString(Statistics.level),         248, 267, Color.parseColor("#0074a2"), 20);
        if(Statistics.comboBonus > 1)
        {
            game.drawText(font, "COMBO",  248, 357, Color.parseColor("#1b7f34"), 18);
            game.drawText(font, " x " + Integer.toString(Statistics.comboBonus),    248, 387, Color.parseColor("#c98f09"), 25);
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
