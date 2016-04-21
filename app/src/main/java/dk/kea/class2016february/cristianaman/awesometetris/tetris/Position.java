package dk.kea.class2016february.cristianaman.awesometetris.tetris;

/**
 * Created by mancr on 21/04/2016.
 */
public class Position implements Comparable<Position>
{
    public int x;
    public int y;

    public Position()
    {
    }

    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Position another)
    {
        if ((y > another.y) || (y == another.y && x > another.x))
        {
            return -1;
        } else if ((y < another.y) || (y == another.y && x < another.x))
        {
            return 1;
        }
        return 0;
    }
}