package dk.kea.class2016february.cristianaman.awesometetris;

/**
 * Created by mancr on 07/03/2016.
 */
public class KeyEventPool extends Pool<MyKeyEvent>
{

    @Override
    protected MyKeyEvent newItem()
    {
        return new MyKeyEvent();
    }
}
