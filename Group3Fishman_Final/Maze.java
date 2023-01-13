import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/** 
 * Fishman is a game based off of Pacman. The player moves around the maze and eats 
 * “crumbs” to gain points within a certain amount of time. They are chased by enemy blobs. If
 * they collide with an “active” enemy blob or the time runs out, the player will lose. There are 
 * 7 levels and it will grow progressively harder. Each level has a different amount of time that 
 * requires the level to be completed in. The first level is unlocked by default, then the player
 * has to keep playing to unlock the rest of the levels. 
 *
 * Fishman moves using arrow key controls. It has two looks: one is its resting position 
 * (moving, mouth closed) and one is its eating position.
 *
 * The enemy blobs move about somewhat randomly for the first 5 levels. They change image 
 * when bigger crumbs are eaten. It has three looks: one is the regular look, one is the edible 
 * look (depicted with a glitch effect), and one is after it has been eaten (starry eyes). When the 
 * enemies are eaten, they return to the home base and then regenerates to the original form.
 *
 * The maze is represented by a 2D array of <code>Square</code> of varying sizes. 
 * Please see the images in the Group 3 folder for reference. Each <code>Square</code>
 * is represented by either “road”, “ice”, “wall”, or “home.”
 * Player moves 2x speed on ice and cannot move past the wall. Player cannot enter
 * the home square, but blobs can enter the home square to regenerate. Both the maze and 
 * the crumbs are rendered by reading from a text file with characters. The squares contain 
 * crumbs in the center which the player eats to gain points.
 *
 * There are 4 types of crumbs. When the Fishman eats different crumbs, there will be a 
 * different amount of points for each type and some different effects as well. Regular crumbs 
 * are worth 2 points. Bigger green-looking crumbs are worth 4 points and make blobs edible 
 * for 10 seconds. Cherries are worth 100 points and appear randomly every 50pts.
 * Cakes are worth 20 points, appear randomly half to ¾ way in the game, and freezes the 
 * ghost in place.
 *
 * The player’s level and high score is saved in a text file.
 *
 * All graphics have been created by either Helen Lee or Katelyn Lam. Helen’s graphics consist  
 * of the player, enemy blobs, cherries, cakes, and the other interface related graphics (buttons 
 * and instructions page). Katelyn’s graphics consist of the maze graphics (squares: wall, road, 
 * ice, and home) and the other crumb graphics (regular and bigger crumbs). 
 * 
 * The background music was taken from SoundCloud at   
 * https://soundcloud.com/jamie-berry/jamie-berry-super-mario-bros. It is named “Jamie Berry - 
 * Super Marios Bros (Electro-Swing Remix) **FREE DOWNLOAD**” by JamieBerry.
 *
 * 
 * @Katelyn Lam, Helen Lee, Jiaqi Tang
 * @version 2022 Jan
 */
public class Maze extends World
{
    private ArrayList<String> mazeRows; //a line read from text file reprenting a row of the maze
    private int xSquare; //x-coordinate of square. Initialize with x-coordinate of maze[0][0]
    private int ySquare; //y-coordinate of square. Initialize with y-coordinate of maze[0][0]
    private static final int squareWidth = Square.getSquareWidth(); //width of a square

    
    private Square[][] maze; //2D array of Square that represents the maze
    private static final String HIGHSCOREFILE = "highscore.txt"; //name of file that stores high score and level
    
    private RegenerationBar regenBlobBar; //bar that defines home zone of the blob
    private int recordedTime; //gets current time left by the timer
    private static int[] levelTime = {60,90,120,180,180,210,240}; //amount of time it takes to complete the level in s
    private int cakeAppearanceTime; //random time set for when cake appears, half to 3/4 into the game
    private boolean crumbChangeHappened; //state of whether a crumb has been changed from a regular (initially false)
    private Square crumbSquare; //Square where a crumb change has took place
    private int[] mazeBorders;

    
    private ArrayList<int[]> specialCrumbLocations; //locations of the bigger crumbs
    private int score; //current score of the user. Change to file read from high score if it exists, otherwise set to 0
    private int highscore; //user's high score. Reads from a text file if it exists from a previous state, otherwise it is initially 0.
    private static int[][] playerInitPos = {{9,7},{6,14},{12,12},{10,13},{9,10},{20,9},{11,13}}; //initial square the player starts on
    private Fishman player;
    
    //padding on both sides of the maze
    private static int hPadding = 40;
    private static int vPadding = 50;
    
    private static int level; //initial level
    private boolean isGameStarted;
    private int numCrumbs; //number of crumbs existent in the level.
    
    //generates a random integer within the bounds of the maze to determine a random location
    private Random generatorX;
    private Random generatorY;
    
    private static int[] numEnemies = {1,2,2,2,2,3,3}; //number of enemies in each level
    private ArrayList<Square> homeSquares; //home squares of the enemy
    private ArrayList<Enemies> enemies; //list of all enemies in the level
    
    private GreenfootSound backgroundMusic; //for the BGM
    private boolean musicPlaying; //boolean to check if the music is playing currently
    
    private Button settingsButton; //settings button that opens a menu like option when clicked
    private Button helpButton; //help button that brings user to the instructions page
    private Button musicButton; //music button to turn music on and off
    private Button restartButton; //restart button to restart the game
    private Button menu; //menu to open the menu option
    
    private boolean menuOpen; //boolean to keep track whether the menu has been opened
    
    private CountdownTimer timer; //timer to keep track of the amount of time left in the game
    
    private ScoreBar scorebar; //score bar to show the current score of the player
    private int scorebarX; //x coordinate specifically for the score bar to adjust the centering of the numbers in the game
    private int scorebarY; //y coordinate specifically for the score bar to adjust the centering of the numbers in the game
    
    private ScoreBar highscoreBar; //highscore bar to show the highest score the player has achieved
    private ScoreBar levelBar; //level bar to show the level the player is currently on
    
    /**
     * Sets up the level including the Maze, labels, timer, and buttons.
     * 
     * <p>Author: Katelyn Lam, Helen Lee, Jiaqi Tang</p>
     * 
     * @param level     the level the player wants to play which connects with which type of maze
     * @param highscore the highest score the player has achieved so far
     * 
     */
    public Maze(int level,int highscore)
    {    
        // Create a new world with 1080x800 cells with a cell size of 1x1 pixels.
        super(1080, 800, 1); 
        mazeRows = new ArrayList<String>();
        homeSquares = new ArrayList<Square>();
        enemies = new ArrayList <Enemies>();
        specialCrumbLocations = new ArrayList<int[]>();
        mazeBorders = new int[4];        
        
        this.level = level;
        this.highscore = highscore;
        generatorX = new Random();
        generatorY = new Random();
        player = new Fishman();
        score = 0;
        crumbChangeHappened = false;
        isGameStarted = false;
        
        scorebar = new ScoreBar(Integer.toString(score)); //score bar
        scorebarX = 1080 - scorebar.getImageLength(); //calculating the length of the score bar to figure out how much space is needed and the final x coordinate of the object
        scorebarY = 30; //the y coordinate for the object
        addObject(scorebar, scorebarX, scorebarY); //adds the scorebar to the world
        
        //minute: levelTime[level - 1]/60,
        //seconds: levelTime[level - 1]%60
        //declares and adds timer
        timer = new CountdownTimer (((levelTime[level - 1]/60)*60)+(levelTime[level - 1]%60), 55, true);
        reinitializeGame();
        addObject(timer, 540, 30);
        timer.pauseTimer();
        
        //loads maze into World, and initialize borders for player to ensure it doesn't go out of bounds
        openFile();
        addEnemies(); //places enemies on Maze
        crumbSquare = maze[0][0]; //Square where a crumb change to cherry or cake happens. Will be set to a random Square location.
        initializeBorders();
        player.setBorders(mazeBorders);
        player.setTarget(numCrumbs);

        musicPlaying = true; //sets the boolean to true
        menuOpen = false; //sets the boolean to false (the menu isn't open)
        
        //Music from https://soundcloud.com/jamie-berry/jamie-berry-super-mario-bros 
        //Jamie Berry - Super Marios Bros (Electro-Swing Remix) **FREE DOWNLOAD** by JamieBerry
        backgroundMusic = new GreenfootSound("Jamie Berry - Mario Bros (Electro-Swing Remix).wav"); //the BGM
        backgroundMusic.setVolume(80);
        backgroundMusic.playLoop(); //plays it on loop
        
        //declares and adds button objects
        settingsButton = new Button ("Settings (resized small).png"); 
        addObject(settingsButton, 45, 50);
        helpButton = new Button ("Help (resized small).png");
        addObject(helpButton, 135, 50);
        musicButton = new Button ("Music (resized small).png", "Music stopped (resized small).png");
        restartButton = new Button ("Restart (resized small).png");
        menu = new Button (150, 200, Color.GRAY);
        
        highscoreBar = new ScoreBar("Highscore: " + highscore);
        addObject(highscoreBar, 1080 - highscoreBar.getImageLength(), 760);
        
        levelBar = new ScoreBar("Level " + level);
        addObject(levelBar, 300, 30);
    }
    
    /**
     * Starts music once the <code>Maze</code> opens.
     * Different from <code>startGame()</code> which
     * is called once the player makes a move
     * 
     * <p>Author: Helen Lee</p>
     */
    public void started()
    {
        backgroundMusic.play(); //plays the music
        musicPlaying = true; //sets the boolean to true
        if(!(musicButton.ifClicked()))
        {
            stopped();
        }
    }
    
    /**
     * Stops music if the program isn't running
     * Eg. if the level is over which then switches to <code>GameOverWorld</code>
     * 
     * <p>Author: Helen Lee</p>
     */
    public void stopped()
    {
        backgroundMusic.pause(); //stops the music
        musicPlaying = false; //sets the boolean to false
    }
    
    /**
     * Starts the timer once the player makes a move (arrow key change is detected).
     * Responsible for spawning cherries and cake crumbs.
     * 
     * <p>Author: Katelyn Lam, Helen Lee, Jiaqi Tang</p>
     */
    public void act()
    {
        if(isGameStarted)
        {
            if(timer.timerEndYet())
            {
                setEndOfRound(false);
            }
            else
            {

                if(!crumbChangeHappened)
                {
                    //spawns a random cherry every time the score increases by 50
                    if((score % 50 == 0) && score > 0) 
                    {
                        crumbSquare = changeRandCrumb(2);
                        recordedTime = timer.getTimerInSeconds();
                        crumbChangeHappened = true;
                    }
                    
                    //spawns a cake 1-2 min in the game randomly
                    else if((levelTime[level - 1] - timer.getTimerInSeconds()) == cakeAppearanceTime) 
                    {
                        crumbSquare = changeRandCrumb(3);
                        recordedTime = timer.getTimerInSeconds();
                        crumbChangeHappened = true;
                    }
                }

                if((recordedTime - timer.getTimerInSeconds()) == 10 && crumbChangeHappened)
                {
                    crumbSquare.changeCrumb(0);
                    crumbChangeHappened = false;
                }
            }             
        }
        scorebar.update(Integer.toString(score)); //updates the score bar
        scorebarX = 1080 - scorebar.getImageLength() + 10; //gets the length of the object to calculate the best x coordinate
        scorebarY = 30; //the y coordinate of the object
        if(scorebar.getX() != scorebarX) //if the newly calculated x coordinate does not equal the current x coordinate of the object, then it moves the scorebar to the new location
        {
            scorebar.setLocation(scorebarX, scorebarY);
        }
        if(Greenfoot.mouseClicked(helpButton)) //if the player clicks the help button, brings them to the instructions
        {
            stopped();
            Greenfoot.setWorld(new InstructionsWorld(this, true, musicButton.ifClicked()));
        }
        if(Greenfoot.mouseClicked(settingsButton)) //if the player clicks the settings button, opens a menu option and shows the music button and restart button
        {
            menuOpen = true;
            addObject(menu, 100, 193);
            addObject(musicButton, 100, 150);
            addObject(restartButton, 100, 240);
        }
        if(Greenfoot.mouseClicked(musicButton)) //if the player clicks the music button
        {
            if(musicPlaying) //if the music is already playing, calls the stopped method to stop the music
            {
                musicButton.clicked(); //calls the clicked method in Button to change the picture icon
                stopped();
            }
            else //otherwise, calls the started method to restart the music
            {
                musicButton.clicked(); //calls the clicked method in Button to change the picture icon
                started();
            }
        }
        if(Greenfoot.mouseClicked(restartButton)) //if the player clicks the restart button
        {
            stopped(); //calls the stopped method to stop the music
            Greenfoot.setWorld(new Maze(1, highscore)); //calls a new world to restart the game from level 1
        }
        if(Greenfoot.mouseClicked(this)) //if the player clicks the world
        {
            if(menuOpen) //if the menu is open, removes the menu and the related objects and sets the boolean to false
            {
                removeObject (menu);
                removeObject(musicButton);
                removeObject(restartButton);
                menuOpen = false;
            }
        }
    }
    
     /**
     * Renders maze from text file Maze<i>level</i>.txt
     * Each text file consists of rows of characters of equal length to
     * symbolize the maze, with characters "W"- Wall, "R" - Road, "I" - Ice,
     * "H" - Home. "&&&" is placed to show when the maze input is finished.
     * Coordinates of special crumbs are placed as "y x" and "***" is placed at the bottom
     * of the file.
     * <p>Author: Katelyn Lam</p>
     */
    private void openFile()
    {
        Scanner fileReader;
        String fileName = "Maze"+Integer.toString(level)+".txt";
        try
        {
            fileReader = new Scanner(new File(fileName));
            boolean endOfFileReached = false;
            
            //reads each line of the maze from a text file and add to ArrayList, which defines each row of the maze
            while(!endOfFileReached)
            {
                try
                {
                    String read = fileReader.nextLine();
                    if(read.equals("&&&"))
                    {
                        //gets the positions of the special crumbs
                        do
                        {
                            int[] specialCrumbPos = new int[2];
                            specialCrumbPos[0] = fileReader.nextInt();
                            specialCrumbPos[1] = fileReader.nextInt();
                            specialCrumbLocations.add(specialCrumbPos);
                        }
                        while(!fileReader.nextLine().equals("***"));
                        
                    }

                    else
                        mazeRows.add(read);
                    

                }
                catch(NoSuchElementException e)
                {
                    endOfFileReached = true;
                }
            }
            
            //define how much space is on the border of the maze
            hPadding = (getWidth() - mazeRows.get(0).length()* squareWidth)/2 + squareWidth;
            vPadding = (getHeight() - mazeRows.size() * squareWidth)/2 - (squareWidth/2) ;

            xSquare = hPadding;
            ySquare = vPadding;
            
            maze = new Square[mazeRows.size()][mazeRows.get(0).length()]; //creates a 2D array to represent the maze
            char imageSelect;
            int selectIndex;
            int specialCrumbCounter = 0;
            int[] regenBarLocation = new int[2];
            boolean barAdded = false;
           
            //sets image for each Square of the maze
            for(int i = 0; i < maze.length; i++)
            {
                for(int j = 0; j < mazeRows.get(0).length(); j++)
                {
                    imageSelect = mazeRows.get(i).charAt(j);
                    
                    switch(imageSelect)
                    {
                        case 'W':
                            selectIndex = 0;
                            break;
                            
                        case 'R':
                            selectIndex = 1;
                            break;
                            
                        case 'I':
                            selectIndex = 2;
                            break;
                        
                        case 'H':
                            selectIndex = 3;
                            break;
                            
                        default:
                            selectIndex = 1;
                            break;
                    }
                    maze[i][j] = new Square(selectIndex,j,i);
                    if(selectIndex == 3)
                        homeSquares.add(maze[i][j]);
                    addObject(maze[i][j],xSquare,ySquare); //adds Square to the World
                    
                    if(maze[i][j].getIndex() == 1 || maze[i][j].getIndex() == 2)
                    {
                        //adds crumbs to the maze. Add special crumbs to locations indicated in the text file, otherwise add a regular crumb
                        if(!(i == playerInitPos[level - 1][1] && j == playerInitPos[level - 1][0]))
                        {
                            int crumbType = 0;
                            
                            if(specialCrumbCounter < specialCrumbLocations.size())
                            {
                                if(j == specialCrumbLocations.get(specialCrumbCounter)[0] && i == specialCrumbLocations.get(specialCrumbCounter)[1])
                                {
                                    crumbType = 1;
                                    specialCrumbCounter++;
                                }
                            }
    
                            maze[i][j].setCrumbInSquare(new Crumb(crumbType,maze[i][j]));
                            numCrumbs++;
                        }
                    }
                    xSquare += squareWidth;
                }
                ySquare += squareWidth;
                xSquare = hPadding;
            }
            
            regenBlobBar = new RegenerationBar((homeSquares.size() + 1)/2);
            //add regeneration bar
            regenBarLocation[0] = homeSquares.get(0).getX() - squareWidth/2 + (int)(homeSquares.size()/4.0 * (double)squareWidth);
            regenBarLocation[1] = homeSquares.get(0).getY() - 8;
            
            addObject(regenBlobBar,regenBarLocation[0], regenBarLocation[1]);
            addObject(player,maze[playerInitPos[level - 1][1]][playerInitPos[level - 1][0]].getX(), maze[playerInitPos[level - 1][1]][playerInitPos[level - 1][0]].getY());

                
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Cannot find Maze.txt in directory");
        }
    }
    
    /**
     * gets the player's current score
     * @return int - the score of the player
     */
    
    public int getScore()
    {
        return score;
    }
    
    /**
     * updates the player's score
     * @param score - the score added to the player's current score, updated in <code>Maze</code>
     */
    public void updateScore(int score)
    {
        this.score += score;
    }
    
    /**
     * Sets borders for the maze. Player cannot travel outside of the border.
     * <p>Author: Katelyn Lam</p>
     */
    private void initializeBorders()
    {
        mazeBorders[0] = maze[0][0].getX(); //left border
        mazeBorders[1] = maze[0][maze[0].length-1].getX(); //right border
        mazeBorders[2] = maze[0][0].getY(); //upper border
        mazeBorders[3] = maze[maze.length-1][0].getY(); //lower border
        
    }
    
    /**
     * Starts timer (for 3 min) to start the round. Called by <code>Fishman</code>
     * <p>Author: Katelyn Lam</p>
     */
    public void startGame()
    {
        isGameStarted = true;
        timer.continueTimer();
    }
    
    /**
     * Reinitializes game, by adding remaining score and setting up a new level Called whenever a new level is set up.
     * <p>Author: Katelyn Lam</p>
     */
    public void reinitializeGame()
    {
        numCrumbs = 0;
        timer.pauseTimer();
        removeObject(timer);
        timer = new CountdownTimer (((levelTime[level - 1]/60)*60)+(levelTime[level - 1]%60), 55, true);
        addObject(timer, 540, 30);
        recordedTime = timer.getTimerInSeconds();
        cakeAppearanceTime = levelTime[level-1]/2 + generatorX.nextInt((levelTime[level-1])/6);
        specialCrumbLocations.clear();
        mazeRows.clear();
        levelBar = new ScoreBar("Level " + level); //re-adds the level text
        addObject(levelBar, 300, 30);
    }
    
    /**
     * Stops the level when player wins or loses
     * @param boolean - state of whether player wins or loses
     */
    public void setEndOfRound(boolean playerWin)
    {
        timer.pauseTimer();
        isGameStarted = false;
        
        /*Moves the player to the next level if they win.
         * Updates score and resets board, player position, and timer to be
         * customized for the next level.
         */
        if(playerWin)
        {
            score += timer.getTimerInSeconds();
            removeAllGameObjects();
            level++;
            reinitializeGame();
            openFile();
            addEnemies();
            crumbSquare = maze[0][0];
            initializeBorders();
            player.setBorders(mazeBorders);
            player.setTarget(numCrumbs);
        }
        else
        {
            /*Stops enemies moving around in the Maze.
            Changes to Game Over screen and displays score.*/
            for(Enemies enemy:enemies)
            {
                enemy.setEnemyWin();
            }
            player.setLoss();
            endGame();
            stopped();
            Greenfoot.setWorld(new GameOverWorld(score));
        }
    }
    
    /**
     * Removes maze, players, and enemies in the Maze
     * <p>Author: Katelyn Lam</p>
     */
    private void removeAllGameObjects()
    {
        //removes regeneration bar
        removeObject(regenBlobBar);
        //removes the maze
        for(int i = 0; i < maze.length; i++)
        {
            for(int j = 0; j < maze[i].length; j++)
            {
                removeObject(maze[i][j]);
            }
        }
        
        //removes player
        removeObject(player);
        
        //removes enemies
        for(Enemies enemy:enemies)
        {
            removeObject(enemy);
        }
        enemies.clear();
        homeSquares.clear();
        removeObject(levelBar); //removes the level text
    }
    
    /**
     * Saves user's preferences when they lose, including the last level they were playing and their high score
     * <p>Author: Katelyn Lam</p>
     */
    private void endGame()
    {
            try
            {
                FileWriter writer = new FileWriter(HIGHSCOREFILE);
                int writeScore;
                int newLevel;
                
                if(score > highscore)
                    writeScore = score;
                else
                    writeScore = highscore;
                    
                if(level > PickLevelsWorld.highestUnlockedLevel())
                    newLevel = level;
                else
                    newLevel = PickLevelsWorld.highestUnlockedLevel();
                writer.write(newLevel+" "+writeScore);
                writer.close();
            }
            catch(IOException e)
            {
                System.out.println("ERROR: Cannot save high score.");
                e.printStackTrace();
            }
        
        
    }

    /**
     * Gets the status of the round, of whether it has started or not
     * @return boolean - <code>false</code> if the game is not actively in play, <code>true</code>
     * if it is in progress
     * <p>Author: Katelyn Lam</p>
     */
    public boolean checkGameStatus()
    {
        return isGameStarted;
    }
    
    /**
     * Generates a random location on the <code>Maze</code> where the <code>Crumb</code> will change from regular to cherry/cake
     * @param changeTo type of <code>Crumb</code> a regular <code>Crumb</code> could change to (either 2 - cherry or 3 - cake)
     * @return Square - the <code>Square</code> where the <code>Crumb</code> change took place
     * <p>Author: Katelyn Lam </p>
     */
    public Square changeRandCrumb(int changeTo)
    {
        int randX = 0;
        int randY = 0;
        int xBound = maze[0].length;
        int yBound = maze.length;
        Square selectedSquare;
        
        do
        {
            randX = generatorX.nextInt(xBound);
            randY = generatorY.nextInt(yBound);
            selectedSquare = maze[randY][randX];
        }
        while(selectedSquare.getCrumbType() != 0 || !selectedSquare.checkCrumbInMaze());
        
        selectedSquare.changeCrumb(changeTo);
        return selectedSquare;
    }
    
    /**
     * Adds enemies to home region in world on the row further from the regeneration bar
     * <p>Author: Katelyn Lam, Jiaqi Tang</p>
     */
    private void addEnemies()
    {
        Square enemyLoc; //location of enemies in terms of Square in maze
        int initSquareIndex = homeSquares.size()/2 + 1;
        
        //Set enemies smartness base on the level
        int smartness = 0;
        if(level < 3){
            smartness = 10;
        }
        else if (level < 5){
            smartness = 20;
        }else if (level == 5){
            smartness = 15;
        }else if (level == 6){
            smartness = 30;
        }else if (level > 6){
            smartness = 50;
        }
        
        //sets enemies on home squares
        for(int i = 0; i < numEnemies[level - 1]; i++)
        {
            if(level < 6){
                enemyLoc = homeSquares.get(initSquareIndex);
                enemies.add(new Enemies(smartness, enemyLoc.getX(), enemyLoc.getY()));
                addObject(enemies.get(i),enemyLoc.getX(), enemyLoc.getY());
                initSquareIndex++;
            }else
            { //If level is 6 or above, make 1 enemy extremely smart and the others OK smart
                enemyLoc = homeSquares.get(initSquareIndex);
                if(i == 0){
                    enemies.add(new Enemies(smartness, enemyLoc.getX(), enemyLoc.getY()));
                }else{
                    enemies.add(new Enemies(smartness - 15, enemyLoc.getX(), enemyLoc.getY()));
                }
                addObject(enemies.get(i),enemyLoc.getX(), enemyLoc.getY());
                initSquareIndex++;
            }
        }
        Enemies.inedible();
    }
}
