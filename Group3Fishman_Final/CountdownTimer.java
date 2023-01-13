import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * CountdownTimer is a Greenfoot Actor that displays the time as it counts down. It displays any time in terms of hours, 
 * minutes, and seconds, best displayed with an hour value of 2 digits or less.
 * <p>
 * Timer begins counting down from the specified amount of time the moment it is added onto the World and the Run button is clicked.
 * It is highly customizable and flexible. Users can specify the number of seconds or the hours/minutes/seconds. User can choose if they want to have a coloured box
 * displayed behind their text or not and they can choose the colour of the box and the colour of the text. The font is always set to "Courier New".
 * 
 * @author Helen Lee
 * @version November 2021 
 */
public class CountdownTimer extends Actor
{
    String time; //the time in a string format
    private static final double FONT_RATIO = 0.58; //took this from Mr. Cohen's Space OOP Demo Script example from last year ICS3U
    private Color color, textColor; //the color of the box (if there is one) and the color of the text
    private int boxHeight, boxWidth, seconds, SECONDS, size, numActs, x, y; //ints for the height of the box, width of the box, number of seconds, a temporary variable for the seconds, size of the text, number of acts, and xy coordinates of the text
    private boolean hasBox, isPaused, timerIsZero; //booleans for whether the object has a box, the timer has been paused, and and whether the timer has hit zero
    private GreenfootImage image; //image
    private Font textFont; //the font of the string
    
    /**
     * The most BASIC constructor that only takes SECONDS for the countdown timer. 
     * It counts down from the number of seconds that is specified.
     * The number of seconds can be as many as the user wants, but the effect is best when the number of seconds is
     * less than 360 000; however, it MUST be an integer.
     * The default box color is black, the default text color is white, it defaults to having a box, and the default text size is 30.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param seconds the number of seconds specified by the user
     */
    public CountdownTimer(int seconds)
    {
        this(Color.BLACK, Color.WHITE, seconds, 30, true);
    }
    
    /**
     * Constructor that only takes SECONDS for the countdown timer. 
     * Boolean lets user choose if they want it to be used for timing anything below an hour or 3600 seconds
     * otherwise it will work the same as the other constructors. The font for this constructor is bolded as well.
     * Eg. 180 seconds = 03:00
     * Everything else is the same as the other constructors except this one has a default of no box
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param seconds  seconds the number of seconds specified by the user
     * @param textSize size of the text
     * @param noHour   boolean if the user wants the timer to be programmed specifically for a time less than 1 hour
     */
    public CountdownTimer(int seconds, int textSize, boolean noHour)
    {
        this(Color.BLACK, Color.BLACK, seconds, textSize, false); //defaults to the other constructors
        if(noHour) //but if this boolean is true, draws the more specific look instead for this object
        {           
            //declares variables like all the other constructors
            time = calculateHoursMinutesSeconds(seconds);
            this.hasBox = hasBox;
            this.color = color;
            this.textColor = textColor;
            size = textSize;
            textFont = new Font("Courier New", true, false, textSize);
            SECONDS = seconds;
            this.seconds = SECONDS;
            timerIsZero = false;
            
            centerText(noHour); //needs a different centering method as the String is a different length from the other constructors
            redraw();
        }
    }
    
    /**
     * The most BASIC constructor that only takes HOURS/MINUTES/SECONDS for the countdown timer. 
     * It works similarly to the one above, but user can input hours, 
     * minutes, and seconds. Then it counts down from the number of hours, minutes, and seconds that is specified by 
     * the user. The number of hours, minutes, and seconds can be as many as the user wants; the numbers can be over 
     * 24 hours, 60 minutes and 60 seconds; however, it MUST be an integer. The font is set to "Courier New".
     * The default box color is black, the default text color is white, it defaults to having a box, and the default text size is 30.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param hours   the number of hours specified by the user
     * @param minutes the number of minutes specified by the user
     * @param seconds the number of seconds specified by the user
     */
    public CountdownTimer(int hours, int minutes, int seconds)
    {
        this(Color.BLACK, Color.WHITE, hours, minutes, seconds, 30, true);
    }
    
    /**
     * Similar to the basic constructor that takes seconds for the countdown timer but lets the user specify the box color and the text color.
     * Everything else is the same.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param color     the color of the rectangle 
     * @param textColor the color of the text, this color should be different from the color of the rectangle for better readability
     * @param seconds   the number of seconds specified by the user
     */
    public CountdownTimer(Color color, Color textColor, int seconds)
    {
        this(color, textColor, seconds, 30, true);
    }
    
    /**
     * Similar to the basic constructor that takes hours/minutes/seconds for the countdown timer but lets the user specify the box color and the text color.
     * Everything else is the same.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param color     the color of the rectangle 
     * @param textColor the color of the text, this color should be different from the color of the rectangle for better readability
     * @param hours     the number of hours specified by the user
     * @param minutes   the number of minutes specified by the user
     * @param seconds   the number of seconds specified by the user
     */
    public CountdownTimer(Color color,Color textColor, int hours, int minutes, int seconds)
    {
        this(color, textColor, hours, minutes, seconds, 30, true);
    }
    
    /**
     * Similar to the basic constructor that takes seconds for the countdown timer but allows user to specify the size of the text as well. 
     * Everything is the same as the one above.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param color     the color of the rectangle 
     * @param textColor the color of the text, this color should be different from the color of the rectangle for better readability
     * @param seconds   the number of seconds specified by the user
     * @param textSize  the size of the text, which will determine the size of the rectangle (if user wants one)
     */
    public CountdownTimer(Color color, Color textColor, int seconds, int textSize)
    {
        this(color, textColor, seconds, textSize, true);
    }
    
    /**
     * Similar to the basic constructor that takes hours/minutes/seconds for the countdown timer but allows user to specify the size of the text as well. 
     * Everything is the same as the one above.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param color     the color of the rectangle 
     * @param textColor the color of the text, this color should be different from the color of the rectangle for better readability
     * @param hours     the number of hours specified by the user
     * @param minutes   the number of minutes specified by the user
     * @param seconds   the number of seconds specified by the user
     * @param textSize  the size of the text, which will determine the size of the rectangle (if user wants one)
     */
    public CountdownTimer(Color color, Color textColor, int hours, int minutes, int seconds, int textSize)
    {
        this(color, textColor, hours, minutes, seconds, textSize, true);
    }
    
    /**
     * Similar to the basic constructor that takes SECONDS for the countdown timer but more flexible. 
     * User can choose if they want a coloured box behind their text or not. The countdown text is displayed with the color and size as determined by the user. If the user 
     * chooses to have a coloured box, the width and height of the box is determined by the size of the text. Everything else is the same.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param color     the color of the rectangle 
     * @param textColor the color of the text, this color should be different from the color of the rectangle for better readability
     * @param seconds   the number of seconds specified by the user
     * @param textSize  the size of the text, which will determine the size of the rectangle (if user wants one)
     * @param hasBox    is true if user wants box, false for no box
     */
    public CountdownTimer(Color color, Color textColor, int seconds, int textSize, boolean hasBox)
    {
        this.color = color;
        SECONDS = seconds;
        this.seconds = SECONDS;
        this.textColor = textColor;
        size = textSize;
        numActs = 0;
        this.hasBox = hasBox;
        isPaused = false;
        textFont = new Font("Courier New", size);
        timerIsZero = false;
        
        time = calculateHoursMinutesSeconds(seconds); //calculates the hours, minutes, and seconds and puts it into a string
        
        centerText(); //calculates the xy values the string needs to be drawn to "center" the text 
        
        image = new GreenfootImage(boxWidth, boxHeight);
        image = drawTimer(color, textColor, boxWidth, boxHeight);
        setImage(image);
    }
    
    /**
     * The constructor that takes HOURS/SECONDS/MINUTES for a countdown timer but more flexible.
     * It lets the user choose if they want a box behind their timer. The countdown text is displayed with the color and size as determined by the user. If the user 
     * chooses to have a coloured box, the width and height of the box is determined by the size of the text. Everything else is the same.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param color     the color of the rectangle 
     * @param textColor the color of the text, this color should be different from the color of the rectangle for better readability
     * @param hours     the number of hours specified by the user
     * @param minutes   the number of minutes specified by the user
     * @param seconds   the number of seconds specified by the user
     * @param textSize  the size of the text, which will determine the size of the rectangle (if user wants one)
     * @param hasBox    is true if user wants box, false for no box
     */
    public CountdownTimer(Color color, Color textColor, int hours, int minutes, int seconds, int textSize, boolean hasBox)
    {
        this.color = color;
        this.textColor = textColor;
        size = textSize;
        numActs = 0;
        this.hasBox = hasBox;
        textFont = new Font("Courier New", size);
        isPaused = false; 
        this.seconds = convertToSeconds(hours, minutes, seconds);
        SECONDS = this.seconds;
        timerIsZero = false;
        
        time = calculateHoursMinutesSeconds(this.seconds); //calculates the hours, minutes, and seconds and puts it into a string
        
        centerText(); //calculates the hours, minutes, and seconds and puts it into a string
        
        image = new GreenfootImage(boxWidth, boxHeight);
        image = drawTimer(color, textColor, boxWidth, boxHeight);
        setImage(image);
    }

    /**
     * The Act method here controls the timer itself, whether the timer text will change. 
     * This way all the math is done here and the user just has to add the object when they use it.
     * 
     * <p>Author: Helen Lee</p>
     */
    public void act()
    {
        // Add your action code here.
        if(!isPaused) //checks if the timer has been paused, if not
        {
            numActs++; //number of acts increased, every 60 acts equals 1 second
            if(numActs%60 == 0) //if 1 seconds has passed
            {
                if(seconds>=0) //if the number of seconds is greater than zero
                {
                    time = calculateHoursMinutesSeconds(seconds); //calculates the hours, minutes, and seconds and puts it into a string
                    seconds--; //number of seconds is decreased
                }
            }
        }
        if(seconds <= 0)
        {
            timerIsZero = true; //if the number of seconds is less than zero, boolean equals true
        }
        redraw(); //updates the timer text
    }
    
    /**
     * Use: PAUSE the timer.
     * 
     * <p>Author: Helen Lee</p>
     */
    public void pauseTimer()
    {
        isPaused = true;
    }
    
    /**
     * Use: allows the user to CONTINUE the timer after they pause it.
     * 
     * <p>Author: Helen Lee</p>
     */
    public void continueTimer()
    {
        isPaused = false;
    }
    
    /**
     * Use: can INCREASE the total time by the specified amount of seconds.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param secondsAdded the number of seconds the user wants the current time to be increased by
     */
    public void addSeconds(int secondsAdded)
    {
        seconds += secondsAdded;
        timerIsZero = false;
    }
    
    /**
     * Use: can DECREASE the total time by the specified amount of seconds.
     * If the number of seconds is 0 or less than 0, the number of seconds just automatically go to 0.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param secondsDecreased the number of seconds the user wants the current time to be decreased by
     */
    public void decreaseSeconds(int secondsDecreased)
    {
        seconds -= secondsDecreased;
        if(seconds<=0)
        {
            //if the number of seconds is 0 or less than 0, the number of seconds just automatically go to zero.
            seconds = 0; 
            timerIsZero = true;
        }
    }
    
    /**
     * Use: tells the user whether the timer has ended yet.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @return timerIsZero true if the timer has ended, false if not
     */
    public boolean timerEndYet()
    {
        return timerIsZero;   
    }
    
    /**
     * Use: RESET the timer to the original time. If the user has not used the setTimer method before using this
     * method, the values will be the same as the original values from the constructor. Otherwise, the values will be the same
     * as the values they used to set the Timer previously.
     * 
     * <p>Author: Helen Lee</p>
     */
    public void resetTimer()
    {
        seconds = SECONDS;
        timerIsZero = false;
    }
    
    /**
     * Use: get the current values of the timer, so they can access the number of hours, minutes, and seconds the timer is 
     * currently at. It is returned as a String, in the format of: hours:minutes:seconds. 
     * For example, hours = 1, minutes = 2, seconds = 30, the String returned would be "1:02:30".
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @return time the current time of the timer
     */
    public String getTimer()
    {
        return time;
    }
    
    /**
     * Use: get the current values of the timer, so they can access the number of seconds the timer is 
     * currently at. It is returned as an int. The user can use the number to do whatever calculations they need.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @return seconds the current number of seconds the timer is at
     */
    public int getTimerInSeconds()
    {
        return seconds;
    }
    
    /**
     * Use: set the color of the rectangle.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param newColor the new color for the rectangle
     */
    public void setColor(Color newColor)
    {
        this.color = newColor;
    }
    
    /** 
     * Use: set the color of the text. 
     * Should try to have a different color from the color of the rectangle for better readability.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param newTextColor the new color for the text
     */
    public void setTextColor(Color newTextColor)
    {
        this.textColor = newTextColor;
    }
    
    /**
     * update Method: update the timer object with a new color, text color, amount of seconds, size, and whether or not there is a rectangle box.
     * Similar to the constructor, but lets the user change all of the values at one time without having to create an entirely new object.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param color     the color of the rectangle 
     * @param textColor the color of the text, this color should be different from the color of the rectangle for better readability
     * @param seconds   the number of seconds specified by the user
     * @param textSize  the size of the text, which will determine the size of the rectangle (if user wants one)
     * @param hasBox    is true if user wants box, false for no box
     */
    public void update(Color color, Color textColor, int seconds, int textSize, boolean hasBox)
    {
        time = calculateHoursMinutesSeconds(seconds);
        this.hasBox = hasBox;
        this.color = color;
        this.textColor = textColor;
        size = textSize;
        textFont = new Font("Courier New", textSize);
        SECONDS = seconds;
        this.seconds = SECONDS;
        timerIsZero = false;
        
        centerText();
        redraw();
    }
    
    /**
     * update Method: update the timer object with a new color, text color, amount of hours, number of minutes, number of seconds, size, and whether or not there is a rectangle box.
     * Everything is the same as the one above, but taking hours, minutes, and seconds as parameters instead.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param color     the color of the rectangle 
     * @param textColor the color of the text, this color should be different from the color of the rectangle for better readability
     * @param hours     the number of hours specified by the user
     * @param minutes   the number of minutes specified by the user 
     * @param seconds   the number of seconds specified by the user
     * @param textSize  the size of the text, which will determine the size of the rectangle (if user wants one)
     * @param hasBox    is true if user wants box, false for no box
     */
    public void update(Color color, Color textColor, int hours, int minutes, int seconds, int textSize, boolean hasBox)
    {
        this.seconds = convertToSeconds(hours, minutes, seconds);
        SECONDS = this.seconds;
        time = calculateHoursMinutesSeconds(this.seconds);
        this.hasBox = hasBox;
        this.color = color;
        this.textColor = textColor;
        size = textSize;
        textFont = new Font("Courier New", textSize);
        timerIsZero = false;
        
        centerText();
        redraw();
    }
    
    /**
     * update Method: update the timer object with a new color and text color specified by the user. 
     * Similar to the reset color methods but allows the user to reset both the box color
     * and the text color at the same time.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param color the new color for the rectangle box
     * @param textColor the new color for the text
     */
    public void update(Color color, Color textColor)
    {
        this.update(color, textColor, seconds, size, hasBox);
    }
    
    /**
     * update Method: update the timer to any second(s) they would like to specify. Effectively, user is resetting the time to whatever
     * value the specify. This sets it so that whatever values they input here become the permanent values that are used if 
     * they reset the timer.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param seconds the number of seconds for the timer
     */
    public void update(int seconds)
    {
        this.update(color, textColor, seconds, size, hasBox);
    }
    
    /**
     * update Method: update the timer to any hour(s), minute(s), and/or second(s) they would like to specify.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param hours   the number of hours specified by the user for the timer
     * @param minutes the number of minutes specified by the user for the timer
     * @param seconds the number of seconds specified by the user for the timer
     */
    public void update(int hours, int minutes, int seconds)
    {
        this.update(color, textColor, hours, minutes, seconds, size, hasBox);
    }
    
    /**
     * update Method: update the timer's font size and whether or not it has a box behind its text.
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param textSize the size of the text
     * @param hasBox   true if there is a box behind the text, false if not
     */
    public void update(int textSize, boolean hasBox)
    {
        this.update(color, textColor, seconds, textSize, hasBox);
    }
    
    /*
     * Redraws the image of the timer itself
     * 
     * <p>Author: Helen Lee</p>
     */
    private void redraw()
    {
        image = drawTimer(this.color, this.textColor, boxWidth, boxHeight);
        setImage(image);
    }
    
    /*
     * Centers the text. Inspired by Mr. Cohen's Space OOP Demo Script example from last year ICS3U
     * Takes the length of the text, the width which is a formula that Mr. Cohen calculated.
     * The width of the box is dependent on the length of the text and the height of the box is dependent on the size of text.
     * The xy values for the drawing of the string on the box are specifically calculated values for this specific method of drawing a timer
     * 
     * <p>Author: Helen Lee</p>
     */
    private void centerText()
    {
        //borrowed from Mr. Cohen's Space OOP Demo Script example from last year ICS3U
        //for the centering of the text
        //10:00
        int wordLength = time.length(); // how many letters there are
        int wordWidth = (int) (wordLength * size * FONT_RATIO); //how many pixels
        
        boxWidth = wordWidth+6;
        boxHeight = size;
        
        x = 1;
        y = boxHeight - (size/5);
    }
    
    /**
     * Centers the text. Inspired by Mr. Cohen's Space OOP Demo Script example from last year ICS3U
     * Takes the length of the text, the width which is a formula that Mr. Cohen calculated.
     * The width of the box is dependent on the length of the text and the height of the box is dependent on the size of text.
     * The xy values for the drawing of the string on the box are specifically calculated values for this specific method of drawing a timer for only 5 characters
     * Eg. 03:00
     * 
     * <p>Author: Helen Lee</p>
     * 
     * @param justMinutes boolean for which method user wants 
     */
    private void centerText(boolean justMinutes)
    {
        int wordLength = time.length(); // how many letters there are
        int wordWidth = (int) (wordLength * size * 0.60); //how many pixels
        
        boxWidth = wordWidth+6;
        boxHeight = size;
        
        x = 1;
        y = boxHeight - (size/5);
    }
    
    /*
     * Converts the hours, minutes, and seconds that the user specified into seconds. 
     * Just in case the user inputs >60 minutes and/or >60 seconds, this method can calculate the number of seconds so 
     * that the text will be correct.
     * 
     * <p>Author: Helen Lee</p>
     */
    private int convertToSeconds(int hours, int minutes, int seconds)
    {
        int temp = (hours*3600)+(minutes*60)+seconds;
        return temp;
    }
    
    /*
     * Converts the number of seconds into a string that puts the seconds into a time format.
     * Eg. 100 seconds --> 00:01:40
     * 
     * <p>Author: Helen Lee</p>
     */
    private String calculateHoursMinutesSeconds(int seconds)
    {
        int hours, minutes, newSeconds;
        String temp = "";
        
        //takes the specific number of seconds given and calculates the correct number of hours, minutes, and seconds
        hours = seconds/3600;
        minutes = (seconds-(hours*3600))/60;
        newSeconds = seconds-((hours*3600)+(minutes*60));
        
        //ensures the correct formatting of the text in specific situations 
        if(hours == 0 && minutes<10 && newSeconds<10)
        {
            temp = "0" + minutes + ":0" + newSeconds;
        }
        else if(hours == 0 && newSeconds<10)
        {
            temp = minutes + ":0" + newSeconds;
        }
        else if(hours == 0 && minutes<10)
        {
            temp = "0" + minutes + ":" + newSeconds;
        }
        else if(hours == 0)
        {
            temp = minutes + ":" + newSeconds;
        }
        else if(hours<10 && minutes<10 && newSeconds<10)
        {
            temp = "0" + hours + ":0" + minutes + ":0" + newSeconds;
        }
        else if(hours<10 && minutes<10)
        {
            temp = "0" + hours + ":0" + minutes + ":" + newSeconds;
        }
        else if(hours<10 && newSeconds<10)
        {
            temp = "0" + hours + ":" + minutes + ":0" + newSeconds;
        }
        else if(minutes<10 && newSeconds<10)
        {
            temp = hours + ":0" + minutes + ":0" + newSeconds;
        }
        else if(hours<10)
        {
            temp = "0" + hours + ":" + minutes + ":" + newSeconds;
        }
        else if(minutes<10)
        {
            temp = hours + ":0" + minutes + ":" + newSeconds;
        }
        else if(newSeconds<10)
        {
            temp = hours + ":" + minutes + ":0" + newSeconds;
        }
        else
        {
            temp = hours + ":" + minutes + ":" + newSeconds;
        }
        return temp;
    }
    
    /*
     * Draws the entire timer (the box and the text) based on the given colors and dimensions.
     * 
     * <p>Author: Helen Lee</p>
     */
    private GreenfootImage drawTimer(Color color, Color textColor, int boxWidth, int boxHeight)
    {
        GreenfootImage temp = new GreenfootImage(boxWidth, boxHeight);
        if(hasBox)
        {
            temp.setColor(color);
            temp.drawRect(0, 0, boxWidth, boxHeight);
            temp.fillRect(0, 0, boxWidth, boxHeight);
        }
        temp.setColor(textColor);
        temp.setFont(textFont);
        temp.drawString(time, x, y);
        return temp;
    }
}
