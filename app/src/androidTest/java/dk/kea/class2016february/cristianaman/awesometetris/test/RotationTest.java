package dk.kea.class2016february.cristianaman.awesometetris.test;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import dk.kea.class2016february.cristianaman.awesometetris.tetris.BlockType;
import dk.kea.class2016february.cristianaman.awesometetris.tetris.Position;
import dk.kea.class2016february.cristianaman.awesometetris.tetris.PositionComparator;
import dk.kea.class2016february.cristianaman.awesometetris.tetris.Tetramino;

import static junit.framework.Assert.assertEquals;

/**
 * Created by mancr on 24/04/2016.
 */
public class RotationTest
{
    @Test
    public void tetrimino_get_rotated_90_Z_BLock()
    {
        // Arrange
        Tetramino tetramino = new Tetramino(BlockType.Z_RED_Block);
        int cornerX = tetramino.getTopCorner().x;
        int cornerY = tetramino.getTopCorner().y;

        // Act
        List<Position> rotatedPositions = tetramino.getRotated();

        // Assert
        Collections.sort(rotatedPositions, new PositionComparator()); // sort desc by x, desc by y

        assertEquals(rotatedPositions.size(), 4);
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 1, cornerY + 2));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 2, cornerY + 1));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 1, cornerY + 1));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 2, cornerY + 0));
    }

    @Test
    public void tetrimino_get_rotated_180_Z_BLock()
    {
        // Arrange
        Tetramino tetramino = new Tetramino(BlockType.Z_RED_Block);
        tetramino.rotate();

        int cornerX = tetramino.getTopCorner().x;
        int cornerY = tetramino.getTopCorner().y;

        // Act
        List<Position> rotatedPositions = tetramino.getRotated();

        // Assert
        Collections.sort(rotatedPositions, new PositionComparator()); // sort desc by x, desc by y

        assertEquals(rotatedPositions.size(), 4);
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 2, cornerY + 2));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 1, cornerY + 2));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 1, cornerY + 1));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 0, cornerY + 1));
    }

    @Test
    public void tetrimino_get_rotated_270_Z_BLock()
    {
        // Arrange
        Tetramino tetramino = new Tetramino(BlockType.Z_RED_Block);
        tetramino.rotate();
        tetramino.rotate();

        int cornerX = tetramino.getTopCorner().x;
        int cornerY = tetramino.getTopCorner().y;

        // Act
        List<Position> rotatedPositions = tetramino.getRotated();

        // Assert
        Collections.sort(rotatedPositions, new PositionComparator()); // sort desc by x, desc by y

        assertEquals(rotatedPositions.size(), 4);
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 2, cornerY + 2));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 1, cornerY + 1));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 0, cornerY + 1));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 1, cornerY + 0));
    }

    @Test
    public void tetrimino_get_rotated_360_Z_BLock()
    {
        // Arrange
        Tetramino tetramino = new Tetramino(BlockType.Z_RED_Block);
        tetramino.rotate();
        tetramino.rotate();
        tetramino.rotate();

        int cornerX = tetramino.getTopCorner().x;
        int cornerY = tetramino.getTopCorner().y;

        // Act
        List<Position> rotatedPositions = tetramino.getRotated();

        // Assert
        Collections.sort(rotatedPositions, new PositionComparator()); // sort desc by x, desc by y

        assertEquals(rotatedPositions.size(), 4);
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 2, cornerY + 1));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 1, cornerY + 1));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 1, cornerY + 0));
        assertEquals(rotatedPositions.get(0), new Position(cornerX + 0, cornerY + 0));
    }
}
