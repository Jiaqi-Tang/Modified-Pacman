import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The opening screen of the entire game. Lists the name of the game and allows the 
 * player the option of going straight to the game or going to the instructions.
 * 
 * @author Helen Lee
 * @version 2022 Jan
 */
public class WelcomeWorld extends World
{
    //Initializes variables and objects
    private Font welcomeScreenFont = new Font ("Courier New", true, false, 75);
    public static final double FONT_RATIO = 0.58;
    
    private Button playButton;
    private Button helpButton;
    
    /**
     * Constructor for objects of class WelcomeWorld.
     * Draws the welcome screen and contains the buttons needed to go to other screens in the game.
     * 
     * <p>Author: Helen Lee</p>
     */
    public WelcomeWorld()
    {    
        // Create a new world with 1080x800 cells with a cell size of 1x1 pixels.
        super(1080, 800, 1);  
        setBackground(drawTitleScreen(1080, 800)); //sets the background
        
        //declaring and adding objects
        playButton = new Button ("Play (resized medium).png");
        addObject(playButton, 375, 500);
        helpButton = new Button ("Help (resized medium).png");
        addObject(helpButton, 675, 500);
    }
    
    /**
     * Act Method for WelcomeWorld.
     * 
     * <p>Author: Helen Lee </p>
     */
    public void act()
    {
        //if help button is clicked, goes to InstructionsWorld to show the instructions to help the player with the game
        if(Greenfoot.mouseClicked(helpButton))
        {
            Greenfoot.setWorld(new InstructionsWorld(false));
        }
        //if play button is clicked, goes to Maze so the player can begin the game
        if(Greenfoot.mouseClicked(playButton))
        {
            Greenfoot.setWorld(new PickLevelsWorld());
        }
    }
    
    /**
     * Draws the welcome screen. 
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param width  width of the image
     * @param height height of the image
     */
    private GreenfootImage drawTitleScreen(int width, int height)
    {
        GreenfootImage temp = new GreenfootImage(width, height); //creates a new GreenfootImage
        
        temp.setColor(Color.WHITE); //sets the colour to white
        temp.drawRect(0, 0, width, height); //draws a rectangle for the background
        temp.fillRect(0, 0, width, height); //fills said rectangle
        
        temp.setColor(Color.BLACK); //sets the colour to black
        temp.setFont(welcomeScreenFont); //sets the font to a pre-decided font 
        String gameTitle = "F I S H M A N"; //sets the string to the name of the game
        
        //figures out the xy coordinates needed to center the text well 
        //inspired by Mr. Cohen's centering code from the Space Shooting Game
        int wordLength = gameTitle.length();
        int wordWidth = (int)(wordLength * FONT_RATIO);
        int x = ((width - wordWidth) / 5) + 25;
        int y = 350;
        
        //draws the title
        temp.drawString(gameTitle, x, y);
        
        return temp; //returns the finished image
    }
}
