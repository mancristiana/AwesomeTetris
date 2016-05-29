package dk.kea.class2016february.cristianaman.awesometetris.tetris;

import java.util.Collections;
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
    private Tetramino ghost;

    private boolean gameOver;

    public TetrisGrid()
    {
        random = new Random();
        gameOver = false;
        grid = new Mino[WIDTH][HEIGHT];
        ghost = new Tetramino(Mino.Ghost);
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++)
                grid[i][j] = Mino.Blank;

    }

    public void clear()
    {
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++)
                grid[i][j] = Mino.Blank;
    }

    public Mino[][] get()
    {
        return grid;
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

        return grid[x][y].equals(Mino.Blank) || grid[x][y].equals(Mino.Ghost);
    }

    /**
     * This method adds the positions of a new Tetramino of a generated random type
     * Adds Tetramino to the grid
     */
    public void createTetramino()
    {
        Mino type = Mino.fromInteger(random.nextInt(7));
        tetramino = new Tetramino(type);
        tetramino.addTo(this);
        drawGhost();
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

        grid[newX][newY] = grid[currentX][currentY];
        grid[currentX][currentY] = Mino.Blank;
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
        drawGhost();
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
            grid[position.x][position.y] = Mino.Blank;
        tetramino.rotate();
        for (Position position : tetramino.positions)
            grid[position.x][position.y] = tetramino.getType();
        drawGhost();
    }

    public void removeLines()
    {
        Statistics.linesAtOnce = 0;
        int y = HEIGHT - 1;
        while (y >= 0)
        {
            if (isCompleteLine(y))
            {
                Statistics.linesCount++;
                Statistics.linesAtOnce++;
                removeLine(y);
            } else
            {
                y--;
            }
        }

        if (Statistics.linesAtOnce == 0)
        {
            Statistics.comboBonus = 0;
        } else
        {
            Statistics.comboBonus++;
            Statistics.increaseScore();
            Statistics.increaseLevel();
        }
    }

    public boolean isCompleteLine(int indexY)
    {
        int minoCount = 0;
        for (int x = 0; x < WIDTH; x++)
        {
            if (!isSpaceOn(x, indexY))
                minoCount++;
        }
        return minoCount == WIDTH;
    }

    public void removeLine(int indexY)
    {
        for (int y = indexY; y > 0; y--)
            for (int x = 0; x < WIDTH; x++)
            {
                grid[x][y] = grid[x][y-1];
            }
    }

    public void drawGhost()
    {
        for (int i = 0; i < 4; i++)
        {
            Position ghostP = ghost.positions.get(i);
            Position pieceP = tetramino.positions.get(i);

            // Clear minos on old ghost positions
            if (isSpaceOn(ghostP.x, ghostP.y))
                grid[ghostP.x][ghostP.y] = Mino.Blank;

            // Set new position for ghost which is the current position for the tetramino
            ghostP.x = pieceP.x;
            ghostP.y = pieceP.y;
        }

        // Get the dropped offset for ghost
        int offY = 0;
        while (moveIsPossible(0, offY))
            offY++;
        ghost.move(0, offY - 1);

        // Draw ghost
        for (Position position : ghost.positions)
        {
            if (isSpaceOn(position.x, position.y))
                grid[position.x] [position.y] = Mino.Ghost;
        }

    }

    public void drop()
    {
        int offY = ghost.positions.get(0).y - tetramino.positions.get(0).y;
        moveTetramino(0, offY);
    }
}
