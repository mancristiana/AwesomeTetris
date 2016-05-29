package dk.kea.class2016february.cristianaman.awesometetris.tetris;

/**
 * Created by mancr on 21/04/2016.
 */
public enum Mino
{
    Z_RED_Block,
    L_ORANGE_Block,
    Square_YELLOW_Block,
    S_GREEN_Block,
    I_SKY_Block,
    J_BLUE_Block,
    T_PINK_Block,
    Ghost,
    Blank;

    public static final float WIDTH = 20;
    public static final float HEIGHT = 20;

    public static Mino fromInteger(int x) {
        switch(x) {
            case 0:
                return Z_RED_Block;
            case 1:
                return L_ORANGE_Block;
            case 2:
                return Square_YELLOW_Block;
            case 3:
                return S_GREEN_Block;
            case 4:
                return I_SKY_Block;
            case 5:
                return J_BLUE_Block;
            case 6:
                return T_PINK_Block;
            case 7:
                return Ghost;
        }
        return Blank;
    }
}
