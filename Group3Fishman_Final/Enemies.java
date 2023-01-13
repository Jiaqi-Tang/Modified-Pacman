import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;
/**
 * <p> Enemies are enemies of the Fishman (player). The Fishman will get eaten by the Enemies (and lose) if they come in contact with any Enemies. </p>
 * <p> The Enemies spawn in the enemy homebase at the beginning of every game. Once the game starts, Enemies move out of their homebase and start moving around. 
 * At this point, the Enemies can no longer go back into their home base and can only move on Road and Ice squares (just like the Fishman). </p>
 * <p> If the Fishman eats a special Crumb that freezes the Enemies or makes the Enemies edible the Enemies will act accordingly. If the Enemies are frozen, 
 * they will stop moving around the map as they usually do. If the Enemies are edible, they will no longer eat the Fishman when they contact the Fishman. Instead, 
 * the Enemy that touches the Fishman will get eaten and revive in the homebase. </p>
 * <p> The smartness of the Enemies decides how likely it will move towards the Fishman. The higher the smartness, the more likely the Enemies will chase the Fishman around. 
 * If the Enemies are edible, higher smartness also means that the Enemies are more likely to move away from the Fishman. </p>
 * 
 * @author Jiaqi Tang
 * @version 2022 Jan
 */
public class Enemies extends Actor
{
    //Declares Variables 
    private int smartness = 0; //How smart the enemy is
    private int speed;
    private int MAX_SPEED = 2;
    private int intersectingSquareIndex;
    private int[] direction;
    private int OFFSET_OTHER = 25; //Used for path finding
    private int OFFSET_CENTER = 14; //Used to check if enemy is centered
    private static boolean EDIBLE = false;
    private static boolean FREEZE = false;
    private Random random;
    private static int fishmanX, fishmanY;
    private Square squareThis, squarePrevious;
    private GreenfootImage enemiesReg, enemiesEdible, enemiesEaten;
    private boolean edible, freeze;
    private boolean alive;
    private boolean enemyWin; //state of whether enemy has collided with the player
    private boolean gameStarted;
    private int deathCount;
    private int oX, oY; //Original x and y location
    private int SMART_ZONE = 250; //How close the enemy need to be to the player to be extra smart
    /**
     * Constructor for Enemies. Creates an Enemy and initializes its variables. 
     * @param smartness Enter a number between 0 and 50. 0 is where the Enemy chooses everything randomly and 50 is where the enemy will always move towards the player. Note that 
     * although the 50 smartness enemy will chase the player around all the time, it is also the most predictable. 
     * @param originalLocationX Enter the x location used to add the enemy to the world. 
     * @param originalLocationY Enter the y location used to add the enemy to the world. 
     * <p>Author: Jiaqi Tang</p>
     */
    public Enemies(int smartness, int originalLocationX, int originalLocationY)
    {
        speed = MAX_SPEED;
        this.smartness = smartness;
        direction = new int[2];
        direction[0] = 0;
        direction[1] = -1;
        edible = false;
        freeze = false;
        alive = true;
        enemyWin = false;
        gameStarted = false;
        
        oX = originalLocationX;
        oY = originalLocationY;
        random = new Random();
        enemiesReg = new GreenfootImage("Enemies Reg.png");
        enemiesEdible = new GreenfootImage("Enemies can be eaten.png");
        enemiesEaten = new GreenfootImage("Enemies after eaten.png");
        setImage(enemiesReg);
    }


    
    /**
     * The act method for Enemies. Moves the Enemy around depending on it's conditions. Checks paths everytime the enemy moved to a new square.
     * <p>Author: Jiaqi Tang</p>
     */
    public void act()
    {
        //Checks what square the enemy is on
        updateSquare();
        
        if(!gameStarted)
        {
            Maze mazeWorld = (Maze)getWorld();
            gameStarted = mazeWorld.checkGameStatus();
        }
        
        else
        {
            if(alive && !enemyWin){
                if(!freeze){
                    //If the enemy is on the center of a new square, check for paths
                    if(centered() && (squareThis != squarePrevious)){
                        int index = 0;
                        if(squareThis != null){
                            index = squareThis.getIndex();
                        }
                        //If enemy is in homebase, move out of home base. Else move regularly.
                        if(index == 3){
                            direction[0] = 0;
                            direction[1] = -1;
                        }else{
                            checkPath();
                            squareThis = squarePrevious;
                        }
                    }
                    
                    //Moves Enemy
                    setLocation(this.getX() + speed*direction[0], this.getY() + speed*direction[1]);
                }
                
                //Checks if the Enemy is edible, move accordingly 
                if(EDIBLE && !edible){
                    edible = true;
                    direction[0] = -direction[0];
                    direction[1] = -direction[1];
                    smartness = -smartness;
                    setImage(enemiesEdible);
                }else if(!EDIBLE && edible){
                    edible = false;
                    direction[0] = -direction[0];
                    direction[1] = -direction[1];
                    smartness = -smartness;
                    setImage(enemiesReg);
                }
                
                //Checks if the Enemy is frozen, move accordingly 
                if(FREEZE && !freeze){
                    freeze = true;
                }else if(!FREEZE && freeze){
                    freeze = false;
                }
                
                //Checks if the Enemy is touching the player, move accordingly 
                if(distanceToPlayer(this.getX(), this.getY()) < 25){
                    //If the enemy is edible, revive the enemy
                    if(edible){
                        alive = false;
                        deathCount = 300;
                        setImage(enemiesEaten);
                        setLocation(oX, oY);
                    }else
                    { //Else, kill the player and end game
                        ((Maze)getWorld()).setEndOfRound(false);
                    }
                }
            }else if(deathCount == 1){
                //Makes sure enemy is dead for 5 seconds and then revives
                deathCount--;
                setImage(enemiesReg);
                alive = true;
            }else if(deathCount > 0){
                deathCount--;
            }
        }
    }


    
    /**
     * Tells enemies they are edible.
     * <p>Author: Jiaqi Tang</p>
     */
    public static void edible(){
        EDIBLE = true;
    }
    
    /**
     * Tells enemies they are frozen.
     * <p>Author: Jiaqi Tang</p>
     */
    public static void freeze(){
        FREEZE = true;
    }
    
    /**
     * Tells enemies they are not edible.
     * <p>Author: Jiaqi Tang</p>
     */
    public static void inedible(){
        EDIBLE = false;
    }
    
    /**
     * Tells enemies they are not frozen.
     * <p>Author: Jiaqi Tang</p>
     */
    public static void unfreeze(){
        FREEZE = false;
    }
    
    /**
     * Updates what square the Enemy is on.
     * <p>Author: Jiaqi Tang</p>
     */
    public void updateSquare(){
        squareThis = (Square)this.getOneObjectAtOffset(0, 0, Square.class);
    }
    
    /**
     * Checks if the Enemy is at the center of a square.
     * <p>Author: Jiaqi Tang</p>
     */
    public boolean centered(){
        Square squareFront = (Square)this.getOneObjectAtOffset(OFFSET_CENTER, 0, Square.class);
        Square squareLeft = (Square)this.getOneObjectAtOffset(0, -OFFSET_CENTER, Square.class);
        Square squareBack = (Square)this.getOneObjectAtOffset(-OFFSET_CENTER, 0, Square.class);
        Square squareRight = (Square)this.getOneObjectAtOffset(0, OFFSET_CENTER, Square.class);
        
        if((squareFront != squareThis) && (squareLeft != squareThis) && (squareBack != squareThis) && (squareRight != squareThis)){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Accesses the Fishman's location. 
     * <p>Author: Jiaqi Tang</p>
     */
    public static void fishmanLocation(int x, int y){
        fishmanX = x;
        fishmanY = y;
    }
    
    /**
     * Checks to see what paths are available. If there are multiple paths to choose, choose randomly based on the smartness of the Enemy, as well as the player and enemy location. 
     * <p>Author: Jiaqi Tang</p>
     */
    private void checkPath(){
        int front = 0;
        int left = 0;
        int back = 0;
        int right = 0;
        
        //Accesses the 4 squares on the 4 sides of the Enemy
        Square squareFront = (Square)this.getOneObjectAtOffset(OFFSET_OTHER*direction[0], OFFSET_OTHER*direction[1], Square.class);
        if(squareFront != null){
            front = squareFront.getIndex();
        }
        Square squareLeft = (Square)this.getOneObjectAtOffset(OFFSET_OTHER*direction[1], -OFFSET_OTHER*direction[0], Square.class);
        if(squareLeft != null){
            left = squareLeft.getIndex();
        }
        Square squareBack = (Square)this.getOneObjectAtOffset(-OFFSET_OTHER*direction[0], -OFFSET_OTHER*direction[1], Square.class);
        if(squareBack != null){
            back = squareBack.getIndex();
        }
        Square squareRight = (Square)this.getOneObjectAtOffset(-OFFSET_OTHER*direction[1], OFFSET_OTHER*direction[0], Square.class);
        if(squareRight != null){
            right = squareRight.getIndex();
        }
        
        int prob1 = random.nextInt(100);
        int prob2 = random.nextInt(100);
        int x = direction[0];
        int y = direction[1];
        
        //Calculates the distance of the enemy to the player if it moves in that direction
        int distanceLeft = distanceToPlayer(this.getX() + y*OFFSET_OTHER, this.getY() - x*OFFSET_OTHER);
        int distanceRight = distanceToPlayer(this.getX() - y*OFFSET_OTHER, this.getY() + x*OFFSET_OTHER);
        int distanceFront = distanceToPlayer(this.getX() + x*OFFSET_OTHER, this.getY() + y*OFFSET_OTHER);
        int distanceBack = distanceToPlayer(this.getX() - x*OFFSET_OTHER, this.getY() - y*OFFSET_OTHER);
        
        //Changes direction based on which paths are available. If there are multiple paths available, choose randomly based on smartness of the Enemy
        //If the enemy is close to the player (in SMART_ZONE), it has the option to move back. 
        if((front != 0 && front != 3) && (left == 0 || left == 3 || squareLeft == null) && (right == 0 || right == 3 || squareRight == null))
        {   //When front is the only path not blocked
            if((distanceToPlayer(this.getX(), this.getY()) < SMART_ZONE) && (distanceFront > distanceBack) && (back != 3)){
                //If in SMART_ZONE and moving back is a better option, have probability of moving back
                if(prob2 < smartness){
                    direction[0] = -x;
                    direction[1] = -y;
                }
            }
        }else if((front == 0 || front == 3 || squareFront == null) && (left == 0 || left == 3  || squareLeft == null) && (right == 0 || right == 3 || squareRight == null))
        {    //When everything is block so the the enemy is force to to back
            direction[0] = -x;
            direction[1] = -y;
        }else if((front == 0  || front == 3 || squareFront == null) && (left != 0 && left != 3) && (right == 0  || right == 3 || squareRight == null))
        {   //When left is the only thing not blocked
            //Turn left
            direction[0] = y;
            direction[1] = -x;
            if((distanceToPlayer(this.getX(), this.getY()) < SMART_ZONE) && (distanceLeft > distanceBack) && (back != 3)){
                //If in SMART_ZONE and moving back is a better option, have probability of moving back
                if(prob2 < smartness){
                    direction[0] = -x;
                    direction[1] = -y;
                }
            }
        }else if((front == 0  || front == 3 || squareFront == null) && (left == 0 || left == 3  || squareLeft == null) && (right != 0 && right != 3))
        {  //When right is the only path not blocked
            //Turn right
            direction[0] = -y;
            direction[1] = x;
            if((distanceToPlayer(this.getX(), this.getY()) < SMART_ZONE) && (distanceRight > distanceBack) && (back != 3)){
                //If in SMART_ZONE and moving back is a better option, have probability of moving back
                if(prob2 < smartness){
                    direction[0] = -x;
                    direction[1] = -y;
                }
            }
        }else if((front == 0  || front == 3 || squareFront == null) && (left != 0 && left != 3) && (right != 0 && right != 3))
        {  //When left and right are not blocked
            if(distanceLeft > distanceRight)
            { //Has higher probability to move right if right is a better choice
                if(prob1 < (50 + smartness)){
                    direction[0] = -y;
                    direction[1] = x;
                }else{
                    direction[0] = y;
                    direction[1] = -x;
                }
            }else
            {  //Else have higher probability to move left
                if(prob1 < (50 - smartness)){
                    direction[0] = -y;
                    direction[1] = x;
                }else{
                    direction[0] = y;
                    direction[1] = -x;
                }
            }
            //If moving back is best choice, the enemy might do so
            if((distanceToPlayer(this.getX(), this.getY()) < SMART_ZONE) && (distanceBack < distanceRight) && (distanceBack < distanceLeft) && (back != 3)){
                if(prob2 < smartness){
                    direction[0] = -x;
                    direction[1] = -y;
                }
            }
        }else if((front != 0 && front != 3) && (left == 0 || left == 3  || squareLeft == null) && (right != 0 && right != 3))
        {  //When front and right are not blocked
            if(distanceFront > distanceRight){
                if(prob1 < (50 + smartness)){
                    direction[0] = -y;
                    direction[1] = x;
                }
            }else{
                if(prob1 < (50 - smartness)){
                    direction[0] = -y;
                    direction[1] = x;
                }
            }
            //If moving back is best choice, the enemy might do so
            if((distanceToPlayer(this.getX(), this.getY()) < SMART_ZONE) && (distanceBack < distanceRight) && (distanceBack < distanceFront) && (back != 3)){
                if(prob2 < smartness){
                    direction[0] = -x;
                    direction[1] = -y;
                }
            }
        }else if((front != 0 && front != 3) && (left != 0 && left != 3) && (right == 0  || right == 3 || squareRight == null))
        {  //When front and left are not blocked
            if(distanceFront > distanceLeft){
                if(prob1 < (50 + smartness)){
                    direction[0] = y;
                    direction[1] = -x;
                }
            }else{
                if(prob1 < (50 - smartness)){
                    direction[0] = y;
                    direction[1] = -x;
                }
            }
            //If moving back is best choice, the enemy might do so
            if((distanceToPlayer(this.getX(), this.getY()) < SMART_ZONE) && (distanceBack < distanceLeft) && (distanceBack < distanceFront) && (back != 3)){
                if(prob2 < smartness){
                    direction[0] = -x;
                    direction[1] = -y;
                }
            }
        }else if((front != 0 && front != 3) && (left != 0 && left != 3) && (right != 0 && right != 3))
        { //When front, left, right are all not blocked
            if((distanceFront < distanceLeft) && (distanceFront < distanceRight))
            { //When front is the mest option, have higher chance of moving front
                if(prob1 < (33 + smartness)){
                    direction[0] = x;
                    direction[1] = y;
                }else if(prob1 < (67 - smartness/2)){
                    direction[0] = -y;
                    direction[1] = x;
                }else{
                    direction[0] = y;
                    direction[1] = -x;
                }
            }else if(distanceLeft < distanceRight)
            { //Else if left is a better option, have a higher chance of moing left
                if(prob1 < (33 + smartness)){
                    direction[0] = y;
                    direction[1] = -x;
                }else if(prob1 < (67 - smartness/2)){
                    direction[0] = -y;
                    direction[1] = x;
                }else{
                    direction[0] = x;
                    direction[1] = y;
                }
            }else
            {  //Else have higher chance of moving right
                if(prob1 < (33 + smartness)){
                    direction[0] = -y;
                    direction[1] = x;
                }else if(prob1 < (67 - smartness/2)){
                    direction[0] = x;
                    direction[1] = y;
                }else{
                    direction[0] = y;
                    direction[1] = -x;
                }
            }
            //If moving back is best choice, the enemy might do so
            if((distanceToPlayer(this.getX(), this.getY()) < SMART_ZONE) && (distanceBack < distanceLeft) && (distanceBack < distanceFront) && (distanceBack < distanceRight) 
            && (back != 3)){
                if(prob2 < smartness){
                    direction[0] = -x;
                    direction[1] = -y;
                }
            }
        }
    }
    
    /**
     * Checks how close the Enemy is to the Fishman
     * <p>Author: Jiaqi Tang</p>
     */
    private int distanceToPlayer(int x, int y){
        int distance = (int) Math.sqrt((x-fishmanX)*(x-fishmanX) + (y-fishmanY)*(y-fishmanY));
        return distance;
    }
    
    /**
     * Ends game when enemy collides with player
     */
    
    public void setEnemyWin()
    {
        enemyWin = true;
    }
}
