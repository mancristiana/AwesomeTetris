package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import dk.kea.class2016february.cristianaman.awesometetris.Game;
import dk.kea.class2016february.cristianaman.awesometetris.Screen;

/**
 * Created by mancr on 12/04/2016.
 */
public class TetrisGame extends Game
{
    @Override
    public Screen createStartScreen()
    {
        return new MainMenuScreen(this);
    }
}
