package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import android.graphics.Bitmap;

import dk.kea.class2016february.cristianaman.awesometetris.Game;
import dk.kea.class2016february.cristianaman.awesometetris.Screen;

/**
 * Created by mancr on 12/04/2016.
 */
public class MainMenuScreen extends Screen
{
    Bitmap mainMenuImage;
    public MainMenuScreen(Game game)
    {
        super(game);
        mainMenuImage = game.loadBitmap("mainscreen.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if(game.isTouchDown(0))
        {
            game.setScreen(new GameScreen(game));
            return;
        }
        game.drawBitmap(mainMenuImage, 0, 0);
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
