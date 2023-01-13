import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The screen shown when the player either runs out of time to play the level or is eaten by one of the enemy blobs!
 * Prints out "Game Over!" and the player's score.
 * Gives the user another chance to either look at the instructions again or pick another level to play from.
 * 
 * @author Helen Lee 
 * @version 2022 Jan
 */
public class GameOverWorld extends World
{
    //Initializes variables and objects
    private Font welcomeScreenFont = new Font ("Courier New", true, false, 75); //font specifics
    public static final double FONT_RATIO = 0.58; //font ratio
    private GreenfootImage image;
    
    private int score; //to store the player's score
    
    //buttons to bring the player to other worlds
    private Button playButton; 
    private Button helpButton;
    
    private ScoreBar scorebar; //to show the player's score
    
    /**
     * Constructor for objects of class GameOverWorld.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param score player's score to print out for them at the end of the game
     * 
     */
    public GameOverWorld(int score)
    {    
        // Create a new world with 1080x800 cells with a cell size of 1x1 pixels.
        super(1080, 800, 1);  
        
        this.score = score;
        image = drawEndScreen(1080, 800); //draws the "Game Over!" text
        setBackground(image); //sets the background to this text
        
        scorebar = new ScoreBar("Score: " + this.score); //to show the player their score
        addObject(scorebar, 512, 350);
        
        playButton = new Button ("Play (resized medium).png"); //play button so the user can go pick a level
        addObject(playButton, 375, 500);
        helpButton = new Button ("Help (resized medium).png"); //help button to bring the user to the instructions page
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
     * Draws the end screen. 
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param width  width of the image
     * @param height height of the image
     */
    private GreenfootImage drawEndScreen(int width, int height)
    {
        GreenfootImage temp = new GreenfootImage(width, height); //creates a new GreenfootImage
        
        temp.setColor(Color.WHITE); //sets the colour to white
        temp.drawRect(0, 0, width, height); //draws a rectangle for the background
        temp.fillRect(0, 0, width, height); //fills said rectangle
        
        temp.setColor(Color.BLACK); //sets the colour to black
        temp.setFont(welcomeScreenFont); //sets the font to a pre-decided font 
        String gameTitle = "Game Over!"; //sets the string to the name of the game
        
        //figures out the xy coordinates needed to center the text well 
        //inspired by Mr. Cohen's centering code from the Space Shooting Game
        int wordLength = gameTitle.length();
        int wordWidth = (int)(wordLength * FONT_RATIO);
        int x = ((width - wordWidth) / 5) + 100;
        int y = 300;
        
        //draws the title
        temp.drawString(gameTitle, x, y);
        
        return temp; //returns the finished image
    }
}
