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

    public static void increaseScore() {
        Statistics.score += (int) Math.pow(3, Statistics.linesAtOnce) * Statistics.level * Statistics.comboBonus;
    }

    public static void increaseLevel()
    {
        if (Statistics.linesCount > level * (level + 3))
            Statistics.level++;
    }
}
