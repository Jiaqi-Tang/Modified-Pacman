import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Represents a square on the maze. Can be road, ice, or wall, or the home square of the Blob.
 * 
 * @Katelyn Lam
 * @version (a version number or a date)
 */
public class Square extends Actor
{
    private static String[] images = {"Wall.png","Road.png","Ice.png","Home.png"}; //images that represent the Square
    private int index; //index that identifies the type of Square
    private int[] mazeLoc = new int[2]; //location of the Square in the 2D maze array in Maze
    private static int squareWidth = 25; //width of the Square, which is the width of the image it is represented by
    private Crumb crumbOnSquare; //initializes Crumb on Square. Can only be called if it is a Road or Ice Square
    
    /**
     * Constructor - sets Square to appropriate image, saves location relative to the Maze, and initializes identifier 
     * @param index Identifier for the type of Square in the Maze. The player can move on the types with index 1 or 2 and
     * not on 0 or 3.
     * @param mazeX index <code>i</code> for the 2D array of <code>Square</code> representing the maze in <code>Maze</code>
     * @param mazeY index <code>j</code> for the 2D array of <code>Square</code> representing the maze in <code>Maze</code>
     */
    public Square(int index, int mazeX, int mazeY)
    {
        setImage(images[index]);
        this.index = index;
        mazeLoc[0] = mazeX;
        mazeLoc[1] = mazeY;
    }
    
    /**
     * Gets the identifier for type of Square
     * @return int - index the Square is represented by: 0 - Wall, 1 - Road, 2 - Ice, 3 - Home base for Blob
     */
    public int getIndex()
    {
        return index;
    }
    
    /**
     * Gets the width of the <code>Square</code>, which is based on the width of the image
     * which it is represented by, in pixels.
     * @return int - width of the image of the <code>Square</code>, should be 25px.
     */
    public static int getSquareWidth()
    {
        return squareWidth;
    }
    
    /**
     * Adds a <code>Crumb</code> to the <code>Square</code> in the <code>Maze</code>
     * @param crumb A <code>Crumb</code> object that can be eaten by the <code>Player</code>
     */
    public void setCrumbInSquare(Crumb crumb)
    {
        World squareWorld = getWorld();
        squareWorld.addObject(crumb,this.getX(),this.getY());
        crumbOnSquare = crumb;
    }
    
    /**
     * Gets the location of the Square in terms of the <code>Maze</code>
     * @return int[] - an array of length 2 indicating the location of the <code>Square</code> 
     * relative to the <code>Maze</code>
     */
    public int[] squareInMazeLoc()
    {
        return mazeLoc;
    }
    
    /**
     * Returns the type of <code>Crumb</code> that is on the <code>Square</code>,
     * by returning its index: 0 - regular, 1 - special (makes ghosts edible), 3 - cherries, 4 - cake.
     * @return int - index that identifies <code>Crumb</code>. 
     * Returns -1 if there is no <code>Crumb</code> on that <code>Square</code>.
     */
    public int getCrumbType()
    {
        if(crumbOnSquare != null)
            return crumbOnSquare.getIndex();
        return -1;
    }
    
    /**
     * Changes the <code>Crumb</code> on the <code>Square</code>. Ensures that the <code>Maze</code> class cannot
     * access the <code>Crumb</code> object on the <code>Square</code> directly.
     * @param index an identifier for the the type of <code>Crumb<code> the <code>Crumb</code> on the <code>Square</code> changes to.
     * Identified by: 0 - regular, 1 - special (makes enemies edible), 2 - cherries, 3 - cake 
     */
    public void changeCrumb(int index)
    {
        if(crumbOnSquare != null)
            crumbOnSquare.changeCrumb(index);
    }
    
    /**
     * Checks if the crumb on the <code>Square</code> is in the <code>Maze</code>
     * @return boolean - <code>true</code> if the <code>Crumb</code> is in the <code>Maze</code>;
     * <code>false</code> otherwise
     */
    public boolean checkCrumbInMaze()
    {
        if(crumbOnSquare != null)
        {
            if(crumbOnSquare.getWorld() == null)
                return false;
            else
                return true;
        }
        return false;
    }
}
