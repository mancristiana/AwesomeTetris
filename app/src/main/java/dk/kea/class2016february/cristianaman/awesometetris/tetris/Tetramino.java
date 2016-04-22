package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mancr on 12/04/2016.
 */
public class Tetramino
{
    List<Position> positions = new ArrayList<>();
    private static final int initialX = 4;
    private static final int initialY = 0;
    private static final int initialVelocity = 1;
    private TetrisGrid grid;

    public Tetramino(TetrisGrid grid, BlockType type)
    {
        this.grid = grid;
        constructPositionList(type);
        // Add tetramino to the grid
        for (Position position : positions)
        {
            grid.setMino(position.x, position.y, type, initialVelocity);
        }
    }

    /**
     * This method CREATES A TETRAMINO by constructing the position list for each Mino
     * based on the given type
     *
     * @param type
     */
    private void constructPositionList(BlockType type)
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
                break;
        }
    }

    public void stop()
    {
        for (Position position : positions)
        {
            grid.get()[position.x][position.y].setVelocity(0);
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
}
