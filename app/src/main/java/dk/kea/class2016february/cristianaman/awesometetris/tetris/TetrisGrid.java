package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by mancr on 19/04/2016.
 */
public class TetrisGrid
{
    public static final int WIDTH = 10;
    public static final int HEIGHT = 22;
    private Mino[][] grid;
    private Random random;
    private Tetramino tetramino;
    private Tetramino ghostpiece;
    private int[] heightList;

    private boolean gameOver;

    public TetrisGrid()
    {
        random = new Random();
        gameOver = false;
        grid = new Mino[WIDTH][HEIGHT];
        heightList = new int[WIDTH];

        for (int i = 0; i < WIDTH; i++)
        {
            for (int j = 0; j < HEIGHT; j++)
                grid[i][j] = new Mino(BlockType.Blank, 0);
            heightList[i] = HEIGHT - 1;
        }
    }

    public void clear()
    {
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++)
                grid[i][j].set(BlockType.Blank, 0);      // NULL MINO HAS TYPE -1 and velocity 0
    }

    public Mino[][] get()
    {
        return grid;
    }

    public boolean hasMino(int x, int y)
    {
        // Check if the position is on the tetris grid
        if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT)
            return false;
        return !isSpaceOn(x, y);
    }

    /**
     * Checks if there is space on position given by params
     *
     * @param x
     * @param y
     * @return false if position is not on the grid
     */
    public boolean isSpaceOn(int x, int y)
    {
        // Check if the position is on the tetris grid
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT)
            return false;

        BlockType type = getMino(x, y).getType();
        return type.equals(BlockType.Blank) || type.equals(BlockType.Ghost);
    }

    public Mino getMino(int x, int y)
    {
        return grid[x][y];
    }

    public void setMino(int x, int y, BlockType type, float velocity)
    {
        grid[x][y].set(type, velocity);
    }

    /**
     * This method adds the positions of a new Tetramino of a generated random type
     * Adds Tetramino to the grid
     */
    public void createTetramino()
    {
        BlockType type = BlockType.fromInteger(random.nextInt(7));
        tetramino = new Tetramino(type);
        tetramino.addTo(this);
        ghostpiece = tetramino.getGhost(heightList);
        ghostpiece.addTo(this);
    }

    public Tetramino getTetramino()
    {
        return tetramino;
    }

    private void moveMino(int currentX, int currentY, int newX, int newY)
    {
        if (currentX == newX && currentY == newY)
            return;
//        // check if there is space on the new position
        if (!isSpaceOn(newX, newY))
            throw new RuntimeException("Minos can't overlap");

        Mino mino = getMino(currentX, currentY);
        setMino(newX, newY, mino.getType(), mino.getVelocity());
        setMino(currentX, currentY, BlockType.Blank, 0);
    }

    /**
     * This method moves a tetramino by given offsets
     *
     * @param offsetX
     * @param offsetY
     * @return true if move was possible, false otherwise
     */
    public void moveTetramino(int offsetX, int offsetY)
    {
//        Log.d("MOVE TETRAMINO", "OFFSET X = " + offsetX + " OFFSET Y = " + offsetY);
        Collections.sort(tetramino.positions, new PositionComparator(offsetX));
//        Log.d("MOVE TETRAMINO LIST: ", tetramino.positions.toString());

        for (Position position : tetramino.positions)
        {
            moveMino(position.x,
                    position.y,
                    position.x + offsetX,
                    position.y + offsetY);
        }
        tetramino.move(offsetX, offsetY);
        updateGhost();
    }

    /**
     * This method checks whether tetramino can be moved on the space specified by offset
     *
     * @param offsetX delta between new x and old x
     * @param offsetY
     * @return
     */
    public boolean moveIsPossible(int offsetX, int offsetY)
    {
        int newX, newY;
        for (Position position : tetramino.positions)
        {
            newX = position.x + offsetX;
            newY = position.y + offsetY;

            // Move is impossible if there is no space
            // and the mino that is occupying the space is not part of the tetrimino
            if (!isSpaceOn(newX, newY) && !tetramino.contains(newX, newY))
                return false;
        }
        return true;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    public void setGameOver(boolean gameOver)
    {
        this.gameOver = gameOver;
    }

    public boolean rotateIsPossible()
    {
        for (Position position : tetramino.getRotated())
        {
            if (!isSpaceOn(position.x, position.y) && !tetramino.contains(position.x, position.y))
                return false;
        }
        return true;
    }

    public void rotateTetramino()
    {
        for (Position position : tetramino.positions)
            setMino(position.x, position.y, BlockType.Blank, 0);
        tetramino.rotate();
        for (Position position : tetramino.positions)
            setMino(position.x, position.y, tetramino.getType(), 1);
    }

    public void removeLines()
    {
        int removed = 0;
        int y = HEIGHT - 1;
        while (y >= 0)
        {
            if (isCompleteLine(y))
            {
                removeLine(y);
                removed++;
            } else
            {
                y--;
            }
        }
        for (int i = 0; i < WIDTH; i++)
            heightList[i] += removed;
    }

    public boolean isCompleteLine(int indexY)
    {
        int minoCount = 0;
        for (int x = 0; x < WIDTH; x++)
        {
            if (hasMino(x, indexY))
                minoCount++;
        }
        return minoCount == WIDTH;
    }

    public void removeLine(int indexY)
    {
        for (int y = indexY; y > 0; y--)
            for (int x = 0; x < WIDTH; x++)
            {
//                grid[x][y] = grid[x][y-1]
                Mino minoAbove = getMino(x, y - 1);
                setMino(x, y, minoAbove.getType(), minoAbove.getVelocity());
            }
    }

    public void updateHeight()
    {
        for (Position position : tetramino.positions)
        {
            if (position.y < heightList[position.x])
                heightList[position.x] = position.y;
        }
    }

    public void updateGhost()
    {
        for (Position position : ghostpiece.positions)
            if (isSpaceOn(position.x, position.y))
                setMino(position.x, position.y, BlockType.Blank, 0);
        ghostpiece = tetramino.getGhost(heightList);
        ghostpiece.addTo(this);
    }
}
