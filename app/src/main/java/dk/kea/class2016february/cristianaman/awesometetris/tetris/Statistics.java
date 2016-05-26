package dk.kea.class2016february.cristianaman.awesometetris.tetris;

/**
 * Created by mancr on 26/05/2016.
 */
public class Statistics
{
    public static int level = 1;
    public static int linesCount = 0;
    public static int linesAtOnce = 0;
    public static int comboBonus = 0;
    public static int score = 0;

    public static int levelToLines() {
        return level * (level + 3);
    }
}
