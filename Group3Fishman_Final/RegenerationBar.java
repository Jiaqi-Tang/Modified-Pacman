import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An image in <code>Maze</code> that defines the home base of the blobs. When the blobs come in contact with it
 * they can regenerate. The bar is designed to center itself according to the width of the home base.
 * 
 * @Katelyn Lam
 * @version (a version number or a date)
 */
public class RegenerationBar extends Actor
{
    private GreenfootImage barImage;
    
    //dimensions of image
    private int width;
    private int height;
    
    private static final int SQUAREWIDTH = 15; //width of 1 square of the regenBar
    
    /**
     * Creates a regeneration bar for the blobs that will be placed over their home base.
     * @param numSquares the number of squares (wide) that makes up the home base
     */
    public RegenerationBar(int numSquares)
    {
        barImage = new GreenfootImage("RegenerationBar.png");
        width = (int)(numSquares/3.0 * barImage.getWidth());
        height = barImage.getHeight();
        barImage.scale(width, height);
        setImage(barImage);
    }
    
    /**
     * Gets width of the regeneration bar
     * @return int - width of regeneration bar
     */
    public int getWidth()
    {
           return width;
    }
}
