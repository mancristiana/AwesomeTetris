package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mancr on 12/04/2016.
 */
public class Tetramino
{
    List<Position> positions;
    private Position topCorner;
    private int sideOffset;
    private static final int initialX = 4;
    private static final int initialY = 0;
    private Mino type;

    public Tetramino(Mino type)
    {
        positions = new ArrayList<>();
        topCorner = new Position(initialX - 1, initialY);
        this.type = type;
        constructPositionList(type);
    }

    /**
     * This method CREATES A TETRAMINO by constructing the position list for each Mino
     * based on the given type
     *
     * @param type
     */
    private void constructPositionList(Mino type)
    {
        positions.clear();
        switch (type)
        {
            case Z_RED_Block: // Z Block
                positions.add(new Position(initialX - 1, initialY));
                positions.add(new Position(initialX, initialY));
                positions.add(new Position(initialX, initialY + 1));
                positions.add(new Position(initialX + 1, initialY + 1));
                break;
            case L_ORANGE_Block: // L Block
                positions.add(new Position(initialX - 1, initialY + 1));
                positions.add(new Position(initialX, initialY + 1));
                positions.add(new Position(initialX + 1, initialY + 1));
                positions.add(new Position(initialX + 1, initialY));
                break;
            case Square_YELLOW_Block: // Square Block
                positions.add(new Position(initialX, initialY));
                positions.add(new Position(initialX + 1, initialY));
                positions.add(new Position(initialX, initialY + 1));
                positions.add(new Position(initialX + 1, initialY + 1));
                break;
            case S_GREEN_Block: // S Block
                positions.add(new Position(initialX, initialY));
                positions.add(new Position(initialX + 1, initialY));
                positions.add(new Position(initialX - 1, initialY + 1));
                positions.add(new Position(initialX, initialY + 1));
                break;
            case J_SKY_Block: // Reversed L Block
                positions.add(new Position(initialX - 1, initialY));
                positions.add(new Position(initialX - 1, initialY + 1));
                positions.add(new Position(initialX, initialY + 1));
                positions.add(new Position(initialX + 1, initialY + 1));
                break;
            case T_BLUE_Block: // T Block
                positions.add(new Position(initialX, initialY));
                positions.add(new Position(initialX - 1, initialY + 1));
                positions.add(new Position(initialX, initialY + 1));
                positions.add(new Position(initialX + 1, initialY + 1));
                break;
            case I_PINK_Block: // I Block
                positions.add(new Position(initialX - 1, initialY));
                positions.add(new Position(initialX, initialY));
                positions.add(new Position(initialX + 1, initialY));
                positions.add(new Position(initialX + 2, initialY));
                topCorner.y--;
                break;
            case Ghost:
                positions.add(new Position(0, 0));
                positions.add(new Position(0, 0));
                positions.add(new Position(0, 0));
                positions.add(new Position(0, 0));
                break;
        }
    }

    public void addTo(TetrisGrid grid)
    {
        for (Position position : positions)
        {
            if (!grid.isSpaceOn(position.x, position.y))
            {
                if(!type.equals(Mino.Ghost))
                    grid.setGameOver(true);
            }
            else
                grid.get()[position.x][position.y] = type;
        }
    }

    public void move(int offsetX, int offsetY)
    {
        topCorner.x += offsetX;
        topCorner.y += offsetY;
        for (Position position : positions)
        {
            position.x += offsetX;
            position.y += offsetY;
        }
    }

     /**
     * This method checks whether the mino on the given position is part of this tetrimino
     *
     * @param x coordinate x of the given mino
     * @param y coordinate y of the given mino
     * @return true if mino is part of this tetrmino
     */
    public boolean contains(int x, int y)
    {
        for (Position position : positions)
            if (position.x == x && position.y == y)
                return true;
        return false;
    }

    public void rotate()
    {
        positions = getRotated();
        topCorner.x += sideOffset;
    }

    public List<Position> getRotated()
    {
        List<Position> rotatedPositions = new ArrayList<>();


        for (Position position : positions)
        {
            rotatedPositions.add(findNewPosition(position));
        }
        return rotatedPositions;
    }

    public Position findNewPosition(Position p)
    {
        int size = 3;
        if (type.equals(Mino.I_PINK_Block)) size = 4;
        else if (type.equals(Mino.Square_YELLOW_Block))
            return p;

        if (topCorner.x + size > TetrisGrid.WIDTH)
        {
            sideOffset = TetrisGrid.WIDTH - topCorner.x - size;
        } else if (topCorner.x < 0)
        {
            sideOffset = -topCorner.x;
        } else
        {
            sideOffset = 0;
        }


        int beforeX = 0;
        for (int afterY = size - 1; afterY >= 0; afterY--)
        {
            int beforeY = 0;
            for (int afterX = 0; afterX < size; afterX++)
            {
                if (beforeX == p.x - topCorner.x && beforeY == p.y - topCorner.y)
                {
                    return new Position(topCorner.x + afterX + sideOffset, topCorner.y + afterY);
                }
                beforeY++;
            }
            beforeX++;
        }

        return p;
    }

    public Position getTopCorner()
    {
        return topCorner;
    }

    public void setTopCorner(Position topCorner)
    {
        this.topCorner = topCorner;
    }

    public Mino getType()
    {
        return type;
    }

    public void setType(Mino type)
    {
        this.type = type;
    }
}
