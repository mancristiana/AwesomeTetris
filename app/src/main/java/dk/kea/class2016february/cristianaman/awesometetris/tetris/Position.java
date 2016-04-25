package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import android.media.audiofx.Equalizer;

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


    // DEFAULT COMPARATOR sorts by Y DESC then X DESC
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;

    }

    @Override
    public String toString()
    {
        return "Pos{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
