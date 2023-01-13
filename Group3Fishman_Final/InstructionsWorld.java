import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The screen where the player can find all the necessary instructions for playing the game.
 * Everything from the rules of the game and the how-tos of the game can be found here.
 * Related graphics are shown here to help the player understand the game better.
 * Instructions were created with Paint.
 * 
 * @author Helen Lee
 * @version 2022 Jan
 */
public class InstructionsWorld extends World
{
    //Initializes variables
    private Font instructionsScreenFont = new Font ("Courier New", true, false, 75);
    private Button playButton;
    private Maze maze;
    private boolean gameStarted, musicPlaying;
    
    /**
     * Constructor for objects of class InstructionsWorld.
     * This constructor is used when the game has already started and the player is already in a maze, 
     * but is looking for a reminder of the instructions. This way, the player can save the current progress of their game,
     * go to the instructions, and return to the game like nothing happened. 
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param maze        the current maze the player is in
     * @param gameStarted boolean to see if the game has started yet 
     * 
     */
    public InstructionsWorld(Maze maze, boolean gameStarted, boolean musicPlaying)
    {    
        // Create a new world with 1080x800 cells with a cell size of 1x1 pixels.
        super(1080, 800, 1);   
        
        //Declares variables
        this.maze = maze; //saves everything in the maze world to another maze
        this.gameStarted = gameStarted;
        this.musicPlaying = musicPlaying;
        
        setBackground(new GreenfootImage("Instructions Page.png")); //sets the background to the picture with the instructions in it
        
        playButton = new Button ("Play (resized small).png"); //the play button 
        addObject(playButton, 500, 700);
    }
    
    /**
     * Another constructor for the objects of class InstructionsWorld. 
     * This constructor is used when the game has not started yet.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param gameStarted boolean to see if the game has started yet
     * 
     */
    public InstructionsWorld(boolean gameStarted)
    {
        // Create a new world with 1080x800 cells with a cell size of 1x1 pixels.
        super(1024, 800, 1);   
        
        setBackground(new GreenfootImage("Instructions Page.png")); //sets the background to the picture with the instructions in it
        
        playButton = new Button ("Play (resized small).png"); //the play button
        addObject(playButton, 500, 700);
    }
    
    /**
     * Act Method for InstructionsWorld.
     * 
     * <p>Author: Helen Lee</p>
     */
    public void act()
    {
        if(Greenfoot.mouseClicked(playButton) && gameStarted) 
        //if the player clicked the play button and the game has already started, sets the world to the maze so everything looks like it has only been paused
        {
            if(musicPlaying)
            {
                maze.started();
            }
            Greenfoot.setWorld(maze);
        }
        else if(Greenfoot.mouseClicked(playButton) && !gameStarted)
        //if the player clicked the play button and the game has not started yet, sets the world to the pick levels world 
        {
            Greenfoot.setWorld(new PickLevelsWorld());
        }
    }
}
