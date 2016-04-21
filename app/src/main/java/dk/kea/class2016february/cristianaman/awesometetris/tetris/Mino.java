package dk.kea.class2016february.cristianaman.awesometetris.tetris;

/**
 * Created by mancr on 12/04/2016.
 */
public class Mino
{
    public static final float WIDTH = 20;
    public static final float HEIGHT = 20;
    private BlockType type;
    private float velocity;

    public Mino()
    {
        this(BlockType.Blank);
    }

    public Mino(BlockType type)
    {
        this(type, 1.0f);
    }

    public Mino(BlockType type, float velocity)
    {
        this.type = type;
        this.velocity = velocity;
    }

    public Mino copy()
    {
        return new Mino(type, velocity);
    }

    public void set(BlockType type, float velocity)
    {
        this.type = type;
        this.velocity = velocity;
    }

    public BlockType getType()
    {
        return type;
    }

    public void setType(BlockType type)
    {
        this.type = type;
    }

    public float getVelocity()
    {
        return velocity;
    }

    public void setVelocity(float velocity)
    {
        this.velocity = velocity;
    }
}
