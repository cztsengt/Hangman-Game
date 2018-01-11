/*
    Name: Caleb Tseng-Tham
    Date: June 10, 2016
    Teacher: Ms Dyke
    Assignment: Hangman ISP Splash Screen animation.

*/
import java.awt.*;
import java.lang.*;
import hsa.Console;

public class SplashScreen implements Runnable
{
    private Console c;

    /*
    Method which displays the splash screen animation on the screen.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	 lightBrown        Class constructor         Used to create the splashscreen.
	 darkBrown         Color                    Colour variable for dark brown.
	 skyBlue           Color                    Colour variable for the sky.
	 grass             Color                    Colour variable for grass.
	 skin              Color                    Colour variable for skin.
	 jeans             Color                    Colour variable for jeans.
	 xText             int Array                Array which holds the x-values of the textbox.
	 yText             int Array                Array which holds the y-values of the textbox.
	 nText             int                      Holds the number of values in the textbox polygon.             
    _____________________________________________________________________________________________________________________________________________________
    */
    public void splash ()
    {
	Color lightBrown = new Color (247, 240, 213);
	Color darkBrown = new Color (98, 72, 60);
	Color skyBlue = new Color (174, 255, 255);
	Color grass = new Color (45, 176, 21);
	Color skin = new Color (236, 211, 179);
	Color jeans = new Color (46, 69, 101);

	int xText[] = {400, 350, 50, 50, 350, 350, 325};
	int yText[] = {250, 200, 200, 40, 40, 200, 200};
	int nText = 7;

	c.setColor (skyBlue);
	c.fillRect (0, 0, 640, 500);
	c.setColor (grass);
	c.fillRect (0, 470, 640, 30); 

	for (int x = 0 ; x <= 400 ; x++)
	{
	    c.setColor (skyBlue);
	    c.fillRect (400, 481 - x, 200, 20); 
	    c.setColor (darkBrown);
	    c.fillRect (560, 500 - x, 20, 370); 
	    c.setColor (lightBrown);
	    c.fillRect (580, 500 - x, 20, 370); 
	    c.setColor (Color.black);
	    c.fillRect (400, 480 - x, 200, 20); 
	    if (x <= 30)
	    {
		c.setColor (grass);
		c.fillRect (400, 470, 200, 30);
	    }
	    c.setColor (grass);
	    c.fillRect (560, 470, 40, 30);
	    delay (10);
	}

	for (int x = 0 ; x <= 80 ; x++)
	{
	    c.setColor (darkBrown);
	    c.fillRect (400, 500 - x, 100, 50); 
	    c.setColor (grass);
	    c.fillRect (400, 470, 100, 30);
	    delay (10);
	}

	for (int x = 0 ; x <= 310 ; x++) 
	{
	    int xPointLeftLg[] = {425, 440, 440, 420};
	    int yPointLeftLg[] = {670 - x, 670 - x, 730 - x, 730 - x};
	    int nPointsLeftLg = 4;

	    int xPointLeftA[] = {440, 440, 410, 410};
	    int yPointLeftA[] = {290 + 310 - x, 300 + 310 - x, 330 + 310 - x, 320 + 310 - x};
	    int nPointsLeftA = 4;

	    int xPointRightLg[] = {460, 475, 480, 460};
	    int yPointRightLg[] = {360 + 310 - x, 360 + 310 - x, 420 + 310 - x, 420 + 310 - x};
	    int nPointsRightLg = 4;

	    int xPointRightA[] = {460, 460, 490, 490};
	    int yPointRightA[] = {290 + 310 - x, 300 + 310 - x, 330 + 310 - x, 320 + 310 - x};
	    int nPointsRightA = 4;


	    if (x >= 50)
	    {
		c.setColor (skyBlue);
		c.fillRect (400, 500 - x, 100, x);
	    }

	    c.setColor (Color.black);
	    c.fillRect (440, 550 - x, 20, 120); 
	    c.setColor (skin);        
	    c.fillOval (400, 500 - x, 100, 100);
	    c.setColor (Color.black);         
	    c.fillPolygon (xPointLeftA, yPointLeftA, nPointsLeftA);
	    c.fillPolygon (xPointRightA, yPointRightA, nPointsRightA);
	    c.setColor (jeans);
	    c.fillPolygon (xPointLeftLg, yPointLeftLg, nPointsLeftLg);
	    c.fillPolygon (xPointRightLg, yPointRightLg, nPointsRightLg);
	    c.setColor (darkBrown);
	    c.fillRect (400, 420, 100, 50);  
	    c.setColor (grass);
	    c.fillRect (400, 470, 100, 30);  
	    delay (10);
	}

	for (int x = 0 ; x < 195 ; x += 2)          
	{
	    c.setColor (darkBrown);
	    c.drawLine (440, 100 + x, 460, 100 + x);
	    c.setColor (lightBrown);
	    c.drawLine (440, 101 + x, 460, 101 + x);
	    if (x >= 90)
	    {
		c.setColor (skin);         
		c.fillOval (400, 190, 100, 100);
	    }
	    delay (20);
	}
	for (int x = 0 ; x <= 100 ; x++)
	{
	    c.setColor (skyBlue);
	    c.fillRect (401 - x, 420, 100, 50); 
	    c.setColor (darkBrown);
	    c.fillRect (400 - x, 420, 100, 50); 
	    delay (20);
	}
	c.setColor (Color.white);      
	c.fillPolygon (xText, yText, nText);
	c.setColor (Color.black);
	c.setFont (new Font ("Impact", Font.PLAIN, 70));
	c.drawString ("Hangman", 60, 100);
	c.setFont (new Font ("Impact", Font.PLAIN, 30));
	c.drawString ("By: Caleb Tseng-Tham", 60, 150);
	delay (6000);
    }

    private void delay (int delayTime)
    {
	try
	{
	    Thread.sleep (delayTime);
	}

	catch (InterruptedException e)
	{
	}
    }

    public SplashScreen (Console con)
    {
	c = con;
    }

    public void run ()
    {
	splash ();
    }
}


