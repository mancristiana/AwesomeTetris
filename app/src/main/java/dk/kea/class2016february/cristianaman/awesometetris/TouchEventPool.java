package dk.kea.class2016february.cristianaman.awesometetris;

/**
 * Created by mancr on 29/02/2016.
 *
 * This class keeps all unhandled events, it increases its size if there is not enough space
 */
public class TouchEventPool extends Pool<TouchEvent>
{
    @Override
    protected TouchEvent newItem()
    {
        return new TouchEvent();
    }
}
