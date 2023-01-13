import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Lets the player pick the level they want to play! 
 * The default level is level 1 and the other levels are unlocked as the player passes the previous level.
 * 
 * @author Helen Lee
 * @version 2022 Jan
 */
public class PickLevelsWorld extends World
{
    //Initiates variables
    private Button level1;
    private Button level2;
    private Button level3;
    private Button level4;
    private Button level5;
    private Button level6;
    private Button level7;
    private Color turquoiseBlue = new Color(146, 210, 201);
    private Color lightGrey = new Color(214, 211, 203);
    
    private static int highestLevelUnlocked;
    
    private int maxLevel; //maximum level that the user unlocked
    private int highscore;
    private int[] availableInfo;
    private static final String HIGHSCOREFILE = "highscore.txt"; //name of file that stores high score and level
    
    /**
     * Constructor for objects of class PickLevelsWorld.
     * Declares variables and adds the objects. All the buttons for each level is added here.
     * Level Buttons: Turquoise represents unlocked levels and grey represents locked levels.
     * 
     * <p>Author: Helen Lee</p>
     * 
     */
    public PickLevelsWorld()
    {    
        // Create a new world with 1080x800 cells with a cell size of 1x1 pixels.
        super(1080, 800, 1);  
        availableInfo = initLevel();
        maxLevel = availableInfo[0];
        highscore = availableInfo[1];
        
        //String variables for each level
        String levelOne = "Level 1";
        String levelTwo = "Level 2";
        String levelThree = "Level 3";
        String levelFour = "Level 4";
        String levelFive = "Level 5";
        String levelSix = "Level 6";
        String levelSeven = "Level 7";
        
        level1 = new Button (levelOne, levelOne.length() + 4, 50, turquoiseBlue); //since this is the first level and the default level, there are no conditions needed
        if(2<=maxLevel) //if the user has unlocked up to or greater than level 2
        {
            level2 = new Button (levelTwo, levelTwo.length() + 4, 50, turquoiseBlue); //this button is turquoise blue
        }
        else //otherwise the button is locked and it is grey
        {
            level2 = new Button (levelTwo, levelTwo.length() + 4, 50, lightGrey);
        }
        if(3<=maxLevel) //if the user has unlocked up to or greater than level 3
        {
            level3 = new Button (levelThree, levelThree.length() + 4, 50, turquoiseBlue); //this button is turquoise blue
        }
        else //otherwise the button is locked and it is grey
        {
            level3 = new Button (levelThree, levelThree.length() + 4, 50, lightGrey);
        }
        if(4<=maxLevel) //if the user has unlocked up to or greater than level 4
        {
            level4 = new Button (levelFour, levelFour.length() + 4, 50, turquoiseBlue); //this button is turquoise blue
        }
        else //otherwise the button is locked and it is grey
        {
            level4 = new Button (levelFour, levelFour.length() + 4, 50,lightGrey);
        }
        if(5<=maxLevel) //if the user has unlocked up to or greater than level 5
        {
            level5 = new Button (levelFive, levelFive.length() + 4, 50, turquoiseBlue); //this button is turquoise blue
        }
        else //otherwise the button is locked and it is grey
        {
            level5 = new Button (levelFive, levelFive.length() + 4, 50, lightGrey);
        }
        if(6<=maxLevel) //if the user has unlocked up to or greater than level 6
        {
            level6 = new Button (levelSix, levelSix.length() + 4, 50, turquoiseBlue); //this button is turquoise blue
        }
        else //otherwise the button is locked and it is grey
        {
            level6 = new Button (levelSix, levelSix.length() + 4, 50, lightGrey);
        }
        if(7<=maxLevel) //if the user has unlocked up to level 7
        {
            level7 = new Button (levelSeven, levelSeven.length() + 4, 50, turquoiseBlue); //this button is turquoise blue
        }
        else //otherwise the button is locked and it is grey
        {
            level7 = new Button (levelSeven, levelSeven.length() + 4, 50, lightGrey);
        }
        //adds each level button at a corresponding xy coordinate
        addObject(level1, 540, 200); 
        addObject(level2, 540, 270);
        addObject(level3, 540, 340);
        addObject(level4, 540, 410);
        addObject(level5, 540, 480);
        addObject(level6, 540, 550);
        addObject(level7, 540, 620);
    }
    
    /**
     * Act Method for PickLevelsWorld.
     * 
     * <p>Author: Helen Lee</p>
     */
    public void act()
    {
        //checks for mouse information and acts accordingly
        //user can choose their level by clicking on the buttons, but they are only allowed to pick a level that they have already unlocked
        if(Greenfoot.mouseClicked(level1)) 
        {
            Greenfoot.setWorld(new Maze(1,highscore));
        }
        if(Greenfoot.mouseClicked(level2) && 2<=maxLevel)
        {
            Greenfoot.setWorld(new Maze(2,highscore));
        }
        if(Greenfoot.mouseClicked(level3) && 3<=maxLevel)
        {
            Greenfoot.setWorld(new Maze(3,highscore));
        }
        if(Greenfoot.mouseClicked(level4) && 4<=maxLevel)
        {
            Greenfoot.setWorld(new Maze(4, highscore));
        }
        if(Greenfoot.mouseClicked(level5) && 5<=maxLevel)
        {
            Greenfoot.setWorld(new Maze(5, highscore));
        }
        if(Greenfoot.mouseClicked(level6) && 6<=maxLevel)
        {
            Greenfoot.setWorld(new Maze(6, highscore));
        }
        if(Greenfoot.mouseClicked(level7) && 7<=maxLevel)
        {
            Greenfoot.setWorld(new Maze(7, highscore));
        }
        //pressing enter is the shortcut to get into the highest level the user has unlocked so far
        if(Greenfoot.isKeyDown("enter")) 
        {
            if(maxLevel == 1)
            {
                Greenfoot.setWorld(new Maze(1,highscore));
            }
            else if(maxLevel == 2)
            {
                Greenfoot.setWorld(new Maze(2,highscore));
            }
            else if(maxLevel == 3)
            {
                Greenfoot.setWorld(new Maze(3,highscore));
            }
            else if(maxLevel == 4)
            {
                Greenfoot.setWorld(new Maze(4,highscore));
            }
            else if(maxLevel == 5)
            {
                Greenfoot.setWorld(new Maze(5,highscore));
            }
            else if(maxLevel == 6)
            {
                Greenfoot.setWorld(new Maze(6,highscore));
            }
            else if(maxLevel == 7)
            {
                Greenfoot.setWorld(new Maze(7,highscore));
            }
        }
    }
    
    /**
     * Initializes the level (which is the last level
     * the user unlocked) and high score
     * @return int[] - array of the form {level, highscore}
     * <p>Author: Katelyn Lam</p>
     */
    private static int[] initLevel()
    {
        int[] levelInfo = {1, 0};
        int numInts = 0;
        String fileName;
        Scanner fileReader;
        String info;
        boolean endOfFileReached = false;
 
        //looks for the file that stores level and highscore (highscore.txt)
        try
        {
            fileReader = new Scanner(new File(HIGHSCOREFILE));
            
            while(!endOfFileReached)
            {
                /*for every line, the first integer is the level and 
                second is the high score*/
                try
                {
                    info = fileReader.nextLine();
                    int index = 0;
                    
                    while(!(info.substring(index,index+1)).equals(" "))
                    {
                        index++;
                    }
                    levelInfo[0] = Integer.parseInt(info.substring(0,index));
                    levelInfo[1] = Integer.parseInt(info.substring(index + 1, info.length()));
    
                }
                 catch(NoSuchElementException e)
                 {
                     endOfFileReached = true;
                  }
              }
           
        }
        catch(FileNotFoundException e)
        {
            return levelInfo;
        }
            return levelInfo;
    }
    
    /**
     * Returns the highest level unlocked. 
     */
    public static int highestUnlockedLevel()
    {
        return highestLevelUnlocked;
    }
}
