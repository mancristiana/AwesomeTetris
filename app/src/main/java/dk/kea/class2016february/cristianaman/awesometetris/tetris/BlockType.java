package dk.kea.class2016february.cristianaman.awesometetris.tetris;

/**
 * Created by mancr on 21/04/2016.
 */
public enum BlockType
{
    Z_RED_Block,
    L_ORANGE_Block,
    Square_YELLOW_Block,
    S_GREEN_Block,
    J_SKY_Block,
    T_BLUE_Block,
    I_PINK_Block,
    Ghost,
    Blank;

    public static BlockType fromInteger(int x) {
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
                return J_SKY_Block;
            case 5:
                return T_BLUE_Block;
            case 6:
                return I_PINK_Block;
            case 7:
                return Ghost;
        }
        return Blank;
    }
}
