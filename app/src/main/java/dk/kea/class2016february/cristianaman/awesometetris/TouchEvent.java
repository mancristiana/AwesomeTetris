package dk.kea.class2016february.cristianaman.awesometetris;

/**
 * Created by mancr on 29/02/2016.
 */
public class TouchEvent
{
    public enum TouchEventType
    {
        Down,
        Up,
        Dragged
    }
    public TouchEventType type;
    public int x;
    public int y;
    public int pointer;



}
