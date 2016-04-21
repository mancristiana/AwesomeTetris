package dk.kea.class2016february.cristianaman.awesometetris;

/**
 * Created by mancr on 07/03/2016.
 */
public class MyKeyEvent
{
    public enum MyKeyEventType
    {
        Down,
        Up
    }
    public MyKeyEventType type;
    public int keyCode;
    public char character;
}
