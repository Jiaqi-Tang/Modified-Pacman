import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The crumbs that the player eats to gain points.
 * 
 * @Katelyn Lam
 * @version (a version number or a date)
 */
public class Crumb extends Actor
{
    private static String[] images = {"RegCrumb.png","Special.png","Cherries.png","Cake.png"}; //images to represent the Crumb
    private static int[] pointValues = {2,4,100,20}; //defines how many points each Crumb is worth
    private int index; //defines the type of crumb
    private int pointsGained; //how many points will be gained if the Crumb is eaten
    private int[] squareLoc; //the square the Crumb is on, relative to the maze
    
    /**
     * Constructor - sets image of the crumb and number of points it is worth
     * @param index the type of crumb. 0 - regular, 1 - special (makes the blobs edible), 2 - cherries, 3 - cake (freezes the blobs)
     * @param Square the Square of the Maze the Crumb is located
     */
    public Crumb(int index, Square square)
    {
        setImage(images[index]);
        this.index = index;
        initializeProperties();
        squareLoc = square.squareInMazeLoc();
    }
    
    /**
     * Gets the index that identifies the type of <code>Crumb</code> it is.
     * @return int - an integer from  0 - 3 to identify the <code>Crumb</code>
     */
    public int getIndex()
    {
        return index;
    }
        
    /**
     * Changes the type of <code>Crumb</code> and its properties 
     * (image and number of points gained when eaten)
     */
    public void changeCrumb(int newIndex)
    {
        this.setImage(images[index]);
        index = newIndex;
        initializeProperties();
    }
    
    /**
     * Sets image and point values of the <code>Crumb</code>
     */
    private void initializeProperties()
    {
        setImage(images[index]);
        pointsGained = pointValues[index];
    }
    
    /**
     * Adds to the user's score when <code>Crumb</code> is eaten. Called by the <code>Player</code> class.
     */
    public void addScore()
    {
        Maze crumbWorld = (Maze)getWorld();
        //int score = (crumbWorld.getScore());
        //score += pointsGained;
        int score = pointsGained;
        crumbWorld.updateScore(score);
        crumbWorld.removeObject(this);
    }
    


}
