package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import java.util.Comparator;

/**
 * Created by mancr on 22/04/2016.
 */
public class PositionComparator implements Comparator<Position>
{
    private static final int ASC = 1;
    private static final int DESC = -1;
    private int sortOrderY = DESC; // Y is always sorted in descending order (minos at bottom first)
    private int sortOrderX;

    public PositionComparator()
    {
        this(DESC); // DEFAULT X is sorted in descending order (minos at right first)
    }

    public PositionComparator(int offsetX)
    {
        if(offsetX < 0) // if MOVED TO THE LEFT
            sortOrderX = ASC; // minos at left first
        else sortOrderX = DESC;
    }

    @Override
    public int compare(Position one, Position another)
    {
        if (one.y != another.y)
            return sortOrderY * (one.y - another.y); // SORT BY Y if they are not equal
        else
            return sortOrderX * (one.x - another.x); // SORT BY X if equal
    }
}
