import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Buttons can be clickable or not clickable. 
 * Some of the buttons can be represented with a picture and some can be given parameters to draw a button out.
 * These buttons help facilitate the game and the player can use them to move from one section (of the game) to another.
 * 
 * @author Helen Lee
 * @version 2022 Jan
 */
public class Button extends Actor
{
    //Initializes variables
    private GreenfootImage image, image2; //image object for the buttons
    private boolean imageUsed, imageUpdated; //boolean to check if the image is already used yet
    private Font textFont = new Font ("Courier New", true, false, 45); //sets a font
    public static final double FONT_RATIO = 0.58; //font ratio
    
    /**
     * Constructor for Button that user can use to specify their own image.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param image takes a String reference of an image object to use as the buttons representative image
     */
    public Button (java.lang.String image)
    {
        //declares variables
        this.image = new GreenfootImage(image);
        
        setImage(image); //sets the image
    }
    
    /**
     * Constructor for Button that can set its own image and a replacing image for later
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param image  takes a String reference of an image object to use as the buttons representative image
     * @param image2 takes a String reference of an image object to use as the buttons representative image for later use
     */
    public Button (java.lang.String image, java.lang.String image2)
    {
        //declares variables
        this.image = new GreenfootImage(image);
        this.image2 = new GreenfootImage(image2);
        
        imageUsed = true; //boolean to check which image is currently being used
        
        setImage(image); //sets the image
    }
    
    /**
     * Constructor for Button that can set its width, height and color of the rectangle box.
     * NO option for the user to input an image.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param width  width of the image
     * @param height height of the image
     * @param color  color of the rectangle box
     */
    public Button (int width, int height, Color color)
    {
        //declares variables
        this.image = new GreenfootImage(width, height);
        
        image.setColor(color); //sets the color to the specified color by the user
        image.drawRect(0, 0, width, height); //draws the rectangle
        image.fillRect(0, 0, width, height); //fills the rectangle
        
        setImage(image); //sets the image
    }
    
    /**
     * Constructor for Button that can set a String, width, text size, and box color.
     * The text is default written in black and the font is in Courier New.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param text     the text user wants to use in the box
     * @param width    the width of the box
     * @param textSize the size of the text
     * @param boxColor color of the rectangle box
     * 
     */
    public Button (String text, int width, int textSize, Color boxColor)
    {
        //draws the image of this button according to the parameters 
        this.image = new GreenfootImage(width*textSize/2, textSize);
        
        image.setColor(boxColor);
        image.drawRect(0, 0, width*textSize/2, textSize);
        image.fillRect(0, 0, width*textSize/2, textSize);
        
        image.setColor(Color.BLACK);
        image.setFont(new Font("Courier New", true, false, textSize));
        image.drawString(text, (width*textSize/14), textSize-10);
        
        setImage(image); //sets the image
    }
    
    /**
     * Act - do whatever the Button wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Nothing in the Act method for this Actor.
     */
    public void act()
    {
        
    }
    
    /**
     * Method to run if the Button object is clicked on, which replaces the current image with another image
     * 
     * <p>Author: Helen Lee</p>
     */
    public void clicked()
    {
        if(imageUsed) //if the original image that was set in the constructor is being used, changes it to the second image and sets the boolean to false
        {
            setImage(image2);
            imageUsed = false;
        }
        else //otherwise (that means the current image is the second image), sets the current image back to the original image and sets the boolean to true
        {
            setImage(image);
            imageUsed = true;
        }
    }
    
    /**
     * Method to get whether a switchable image button has been clicked.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @return boolean imageUsed true if the first image listed is used, false if it's the second image
     */
    public boolean ifClicked()
    {
        return imageUsed;
    }
}
