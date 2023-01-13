import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * <p> Fishman is the player of the game. </p>
 * <p> The movement of the Fishman is controlled by the arrow keys of the keyboard. The up arrow key moves the Fishman up, the left arrow key moves the Fishman 
 * left,  the down arrow key moves the Fishman down, and the right arrow key moves the Fishman right. </p>
 * <p> When moving around the map, the Fishman cannot move into wall or enemy homebase squares, they can only move along the Road and Ice squares. 
 * When moving on Ice square, they move twice as fast as their speed on Road squares. </p>
 * <p> When the Fishman contacts a Crumb, the Crumb is eaten. If the Crumb is a special Crumb that freezes the Enemies or makes the Enemies edible, 
 * the Fishman will notify the Enemies. </p>
 * @author Jiaqi Tang, Katelyn Lam
 * @version 2022 Jan
 */
public class Fishman extends Actor
{
    // Declares Variables
    private int speed;
    private int intersectingSquareIndex;
    private boolean missionAccomplished;
    private int ICE_SPEED = 4;
    private int REG_SPEED = 2;
    private int[] direction;
    private int OFFSET = 10;
    private GreenfootImage fishmanOpen, fishmanClose;
    private int count;
    private int[] borders;
    private int targetCrumbNum; //number of crumbs player must consume
    private int numCrumbsEaten; //number of crumbs the player has eaten
    private int freezeCount, edibleCount;
    private boolean hasLost;
    
      //Initializes Variables
    /** Constructor for Fishman. Creates a Fishman and initializes its variables. 
     * 
     * <p>Author: Jiaqi Tang</p>
     */
    public Fishman()
    {
        speed = 0;
        count = 0;
        numCrumbsEaten = 0;
        direction = new int[2];
        direction[0] = 1;
        direction[1] = 0;
        freezeCount = 0;
        edibleCount = 0;
        hasLost = false;
        
        missionAccomplished = false;
        fishmanOpen = new GreenfootImage("Player open.png");
        fishmanClose = new GreenfootImage("Player closed.png");
        setImage(fishmanClose);
    }
    
    /**
     * The act method of the Fishman.
     * Move Fishman around and Check if there are any crums to eat.
     * <p>Author: Jiaqi Tang</p>
     */
    public void act() 
    {
        //if(!missionAccomplished && !hasLost)
        if(!hasLost)
        {
            //Adjusts the image of the Fishman
            if(count == 1){
                count--;
                setImage(fishmanClose);
            }else if (count > 0){
                count--;
            }
            
            //Checks what square the player is heading towards
            intersectingSquareIndex = checkSquare();
            switch(intersectingSquareIndex)
            {
                case 0:
                    speed = 0;
                    break;
                case 1:
                    speed = REG_SPEED;
                    break;
                case 2:
                    speed = ICE_SPEED;
                    break;
                case 3:
                    speed = 0;
            }
            
            //Moves and rotates the player according to the arrow keys
            if(Greenfoot.isKeyDown("left"))
            {
                setRotation(180);
                move(speed);
                direction[0] = -1;
                direction[1] = 0;
                startGame();
            }
            else if(Greenfoot.isKeyDown("right"))
            {
                setRotation(0);
                move(speed);
                direction[0] = 1;
                direction[1] = 0;
                startGame();
            }
            else if(Greenfoot.isKeyDown("up"))
            {
                setRotation(-90);
                move(speed);
                direction[0] = 0;
                direction[1] = -1;
                startGame();
            }
            else if(Greenfoot.isKeyDown("down"))
            {
                setRotation(90);
                move(speed);
                direction[0] = 0;
                direction[1] = 1;
                startGame();
            }
            
            //Tells the Enemies if they are inedible
            if(edibleCount == 1){
                Enemies.inedible();
                edibleCount--;
            }else if(edibleCount > 0){
                edibleCount--;
            }
            
            //Tells Enemies if they should unfreeze
            if(freezeCount == 1){
                Enemies.unfreeze();
                freezeCount--;
            }else if(freezeCount > 0){
                freezeCount--;
            }
            
            //Tells enemies it's location
            Enemies.fishmanLocation(this.getX(), this.getY());

            
            centerPlayer();
            checkBorders();
            checkCrums();
            checkWin();
        }
    } 
    
    /**
     * Checks if the player is touching a crum. If so, eat the crum and change the image of Fishman to it eating image. If a special enemy related crum is eaten, notify the Enemies. 
     * Enemies are edible for 8 seconds and freeze for 5 seconds. 
     * <p>Author: Jiaqi Tang</p>
     */
    private void checkCrums(){
        Crumb c = (Crumb)this.getOneObjectAtOffset(OFFSET*direction[0], OFFSET*direction[1], Crumb.class);
        if(c != null){
            int index = c.getIndex();
            c.addScore();
            //If a special enemy related crum is eaten, notify the enemies. 
            if (index == 1){
                Enemies.edible();
                edibleCount = 480;
            }else if(index == 3){
                Enemies.freeze();
                freezeCount = 300;
            }
            count = 8;
            setImage(fishmanOpen);
            numCrumbsEaten++;
            
        }
    }
    
    /**
     * Centers Fishman to the middle of the lane. Without this code, it is possible for the sides of Fishman to overlap with the walls of the map.
     * <p>Author: Jiaqi Tang</p>
     */
    private void centerPlayer(){
        /*
         * If Fishman is going horizally, move it up if it's bottom is touching a wall/ghost base and move it down if it's top is touching a wall/ghost base. 
         * Similar logic applies for going vertically. 
         */
        if(direction[1] == 0)
            {
                if(this.getOneObjectAtOffset(0, OFFSET, Square.class) != null)
                {
                    Square touchingSquare = (Square)this.getOneObjectAtOffset(0, OFFSET, Square.class);
                    if(touchingSquare.getIndex() == 0 || touchingSquare.getIndex() == 3){
                        this.setLocation(this.getX(), this.getY() - 2);
                    }
                }
                if(this.getOneObjectAtOffset(0, -OFFSET, Square.class) != null)
                {
                    Square touchingSquare = (Square)this.getOneObjectAtOffset(0, -OFFSET, Square.class);
                    if(touchingSquare.getIndex() == 0 || touchingSquare.getIndex() == 3){
                        this.setLocation(this.getX(), this.getY() + 2);
                    }
                }
            }
        if(direction[0] == 0){  
            if(this.getOneObjectAtOffset(OFFSET, 0, Square.class) != null)
            {
                Square touchingSquare = (Square)this.getOneObjectAtOffset(OFFSET, 0, Square.class);
                if(touchingSquare.getIndex() == 0 || touchingSquare.getIndex() == 3){
                    this.setLocation(this.getX() - 2, this.getY());
                }
            }
            if(this.getOneObjectAtOffset(-OFFSET, 0, Square.class) != null)
            {
                Square touchingSquare = (Square)this.getOneObjectAtOffset(-OFFSET, 0, Square.class);
                if(touchingSquare.getIndex() == 0 || touchingSquare.getIndex() == 3){
                    this.setLocation(this.getX() + 2, this.getY());
                }
            }
        }
    }
    
    /**
     * Checks square in front of the player and returns the index of that square
     * @return int - index of <code>Square</code>
     * <p>Author: Katelyn Lam, Jiaqi Tang</p>
     */
    private int checkSquare()
    {
        int index = 0;
        if(this.getOneObjectAtOffset(OFFSET*direction[0], OFFSET*direction[1], Square.class) != null)
        {
            Square touchingSquare = (Square)this.getOneObjectAtOffset(OFFSET*direction[0], OFFSET*direction[1], Square.class);
            return touchingSquare.getIndex();
        }
        return index;
    }
    
    /**
     * Initializes borders of the maze, called by the <code>Maze</code> class.
     * The borders are defined as the furthest horizontal or vertical coordinate [left,right,up,down]
     * @param borders the borders of the maze, defined in the main <code>Maze</code> class
     * <p>Author: Katelyn Lam</p>
     */
    public void setBorders(int[] borders)
    {
        this.borders = borders;
    }

    /**
     * Ensures player is within the borders of the world. If the player is not,
     * set the player at the center of the Square it is on.
     * <p>Author: Katelyn Lam</p>
     */
    public void checkBorders()
    {
        int playerX = this.getX();
        int playerY = this.getY();

        if(playerX < borders[0])
            playerX = borders[0];
            
        else if(playerX > borders[1])
            playerX = borders[1];
        
        if(playerY < borders[2])
            playerY = borders[2];
            
        else if(playerY > borders[3])
            playerY = borders[3];
        
        this.setLocation(playerX,playerY);
    }
        
    /**
     * Starts the game by detecting when the player makes a movement
     */
    private void startGame()
    {
        Maze mazeWorld = (Maze)getWorld();
        if(!mazeWorld.checkGameStatus())
            mazeWorld.startGame();
        
    }
    
    /**
     * Sets the number of crumbs the player has to achieve to win. Called by <code>Maze</code>
     * @param numCrumbs number of crumbs in the maze for the level
     */
    public void setTarget(int numCrumbs)
    {
        targetCrumbNum = numCrumbs;
    }
            
    /**
     * Checks if the player has eaten all the <code>Crumbs</code> in the <code>Maze</code>
     */
    public void checkWin()
    {
        Maze mazeWorld = (Maze)getWorld();
        if(numCrumbsEaten == targetCrumbNum)
        {
            mazeWorld.setEndOfRound(true);
            numCrumbsEaten = 0;
        }
    }
    
    
    public void setWin()
    {
        missionAccomplished = true;
    }
    
    /**
     * Called by <code>Enemies</code> when they collide with the player
     * to end the game
     * 
     * <p>Author: Katelyn Lam</p>
     */
    public void setLoss()
    {
        hasLost = true;
    }
}
