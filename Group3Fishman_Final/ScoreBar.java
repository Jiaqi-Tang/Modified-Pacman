import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * ScoreBar is a visible representation of the player's score as the game progresses. 
 * 
 * @author Helen Lee
 * @version 2022 Jan
 */
public class ScoreBar extends Actor
{
    //Initializes variables
    private String score; //integer score as a string
    private GreenfootImage image; //image
    
    /**
     * Constructor for ScoreBar. Takes the score in a String and prints it out for the player.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param score String representation of the score
     */
    public ScoreBar(String score)
    {
        //Declares variables
        this.score = score;
        image = new GreenfootImage(this.score, 65, Color.BLACK, Color.WHITE); //uses the Greenfoot method to draw the score
        setImage(image); //sets the image
    }
    
    /**
     * Act - do whatever the ScoreBar wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Nothing happens in this method for ScoreBar
     */
    public void act()
    {
        
    }
    
    /**
     * Gets the length of the image
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @return int - length of the image
     */
    public int getImageLength()
    {
        return image.getWidth();
    }
    
    /**
     * Updates the score and redraws the image
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param score new score to be used in the image
     */
    public void update(String score)
    {
        this.score = score;
        redraw(this.score);
    }
    
    /**
     * Draws the image
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param score String representation of the score
     */
    private void redraw(String score)
    {
        image = new GreenfootImage(score, 65, Color.BLACK, Color.WHITE);
        setImage(image);
    }
}
