package dk.kea.class2016february.cristianaman.awesometetris;

/**
 * Created by mancr on 17/02/2016.
 */
public abstract class Screen
{
    protected final Game game;

    public Screen(Game game)
    {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
}
