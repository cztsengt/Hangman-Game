/*
    Name: Caleb Tseng-Tham
    Teacher: Ms. Dyke
    Date: June 10, 2016
    Assignment: ICS 3U final project.
		This program is a thematic hangman game. This game has a wordbank of 100 or more words and phrases stored in a separate file. It also saves
		user data after they have finished playing in a separate file as well.

_______________________________________________________________________________________________________________________________________________________________
						  ~~~~  Variable Dictionary  ~~~~
    |Name|            |Type|                    |Description|
_______________________________________________________________________________________________________________________________________________________________
    c               Console
    highScores      String Array        Array which holds the highscore values to display to the user and sort.
    chosenWords     String Array        Array which holds the currently chosen words so that they aren't repeated again.
    lightBrown      Color               Used for graphics to make the colour light brown.
    darkBrown       Color               Used for graphics to make the colour dark brown.
    WORDCOUNT       static final int    Holds the final value of the amount of available words to be used in the game.
    HIGHSCORES      static final int    Holds the final value of the amount of high scores which can be stored(divide this value by 4).
    ALPHABET        static final int    Holds the final value of the amount of characters in the alphabet for keeping track of what letters are chosen.
    levelSelected   boolean             Determine if the user has selected a level to play.
    exitGame        boolean             Determines if the game should exit or not.
    usernameValid   boolean             Determines if the username is valid.
    wordNew         boolean             Determines if the word being selected to use in the game is fresh.
    choice          String              Holds user's choice when given an option.
    levelChoice     String              Holds the user's choice of level.
    cheat           String              Holds the user's option for cheats.
    answer          String              Holds the answer string which the user will try to guess.
    unknownWord     String              Holds the unknown word which is displayed to the user in "_"'s.
    hint            String              Holds the hint for the answer.
    letter          String              Holds the user's selection of letter in-game.
    username        String              Holds the user's username.
    randomNum       int                 Holds the random number used to determine a random word.
    lives           int                 Holds the lives left that a player has.
    score           int                 Holds the score of the player after the game has completed.
    plays           int                 Holds the value of how many times the user has played this game in a given session.
_______________________________________________________________________________________________________________________________________________________________

*/
import java.awt.*;
import javax.swing.JOptionPane;
import java.io.*;
import hsa.Console;

public class CalebISPHangman
{
    Console c;
    String[] highScores = new String [HIGHSCORES];
    String[] chosenWords = new String [WORDCOUNT];
    Color lightBrown = new Color (247, 240, 213);
    Color darkBrown = new Color (98, 72, 60);
    static final int WORDCOUNT = 100;
    static final int HIGHSCORES = 40;
    static final int ALPHABET = 26;
    boolean levelSelected = false;
    boolean exitGame = false;
    boolean usernameValid = true;
    boolean wordNew = true;
    String choice;
    String levelChoice;
    String cheat = " ";
    String answer = " ";
    String unknownWord = " ";
    String hint = " ";
    String letter = " ";
    String username = " ";
    int randomNum;
    int lives = 6;
    int score;
    int plays = 0;


    // Method which clears the screen and displays the title of the program.
    private void title ()
    {
	c.clear ();
	c.setColor (lightBrown);
	c.fillRect (0, 0, 640, 500);
	c.setColor (darkBrown);
	c.fillRect (0, 72, 640, 20);
	c.setColor (Color.black);
	c.setCursor (5, 1);
	c.setFont (new Font ("Impact", Font.PLAIN, 60));
	c.drawString ("Hangman", 210, 70);
	c.setFont (new Font ("Impact", Font.PLAIN, 30));
	c.drawString ("[Biology]", 450, 70);
    }


    /*
     Method used for clearing lines given the row, column and number of lines to clear.
    _________________________________________________________________________________________________________________________________
						  ~~~~  Variable Dictionary  ~~~~
    |Name|            |Type|                    |Description|
    _________________________________________________________________________________________________________________________________
    row                int                      [Parameter passed] Holds the location of the row to clear.
    col                int                      [Parameter passed] Holds the location of the column to clear.
    amount             int                      [Parameter passed] Holds the number lines starting from the given row to clear.
    _________________________________________________________________________________________________________________________________
    */
    private void lineClear (int row, int col, int amount)
    {
	c.setCursor (row, col);
	for (int x = 0 ; x < amount ; x++)
	{
	    c.println ();
	}
	c.setCursor (row, col);
    }


    // Method which generates a random number.
    private int randomNumber ()
    {
	int randomAnswer = 1 + (int) (Math.random () * WORDCOUNT);
	return randomAnswer;
    }


    // Method to clear the high scores file.
    private void clearHighScores ()
    {
	try
	{
	    PrintWriter output = new PrintWriter (new FileWriter ("highscores.hang"));
	    output.println ("CalebHangmanISP");
	    output.close ();
	}

	catch (IOException e)
	{
	}
    }


    /*
    Method which opens the answers file to get a data line containing an answer and a hint.
    _________________________________________________________________________________________________________________________________
						  ~~~~  Variable Dictionary  ~~~~
    |Name|            |Type|                    |Description|
    _________________________________________________________________________________________________________________________________
    input              BufferedReader           An instance of the buffered reader class.
    chosenLine         String                   Used to hold the value of the randomly chosen line from the answer file.
    randomNum          int                      [Parameter passed] Random number which determines which line in the answer file to chose.
    _________________________________________________________________________________________________________________________________
    */
    private String dataLine (int randomNum)
    {
	BufferedReader input;
	String chosenLine = "";
	try
	{
	    input = new BufferedReader (new FileReader ("answers.hang"));
	    if (!input.readLine ().equals ("CalebHangmanISP"))
	    {
		jOptionProblem ("Sorry, the answer file found has an incorrect file header.");
	    }
	    else
	    {
		for (int x = 0 ; x < randomNum ; x++)
		{
		    chosenLine = input.readLine ();
		}
	    }
	}
	catch (FileNotFoundException e)
	{
	    jOptionProblem ("The answers file has not been found.");
	}
	catch (IOException e)
	{
	}
	return chosenLine;
    }


    /*
    Method which extracts the answer string from the data line.
    _________________________________________________________________________________________________________________________________
						  ~~~~  Variable Dictionary  ~~~~
    |Name|            |Type|                    |Description|
    _________________________________________________________________________________________________________________________________
    tempAnswer         String                   Holds the answer which is taken from the line taken from the answer file.
    chosenLine         String                   [Parameter passed] Holds the string which has the full line taken from the answer file.
    _________________________________________________________________________________________________________________________________
    */
    private String getAnswer (String chosenLine)
    {
	String tempAnswer = "";
	for (int x = 0 ; x < chosenLine.length () ; x++)
	{
	    if (chosenLine.charAt (x) != '/')
	    {
		tempAnswer += chosenLine.charAt (x);
	    }
	    else
		break;
	}
	return tempAnswer;
    }


    /*
     Method which extracts the hint string from the answer line.
    _________________________________________________________________________________________________________________________________
						  ~~~~  Variable Dictionary  ~~~~
    |Name|            |Type|                    |Description|
    _________________________________________________________________________________________________________________________________
    tempHint           String                   Holds the hint which is taken from the line taken from the answer file.
    chosenLine         String                   [Parameter passed] Holds the string which has the full line taken from the answer file.
    _________________________________________________________________________________________________________________________________
    */
    private String findHint (String chosenLine)
    {
	String tempHint = "";
	for (int x = 0 ; x < chosenLine.length () ; x++)
	{
	    if (chosenLine.charAt (x) == '/')
	    {
		tempHint = chosenLine.substring (x + 1, chosenLine.length ());
	    }
	}

	return tempHint;
    }


    /*
     Method which sets the unknown variable which is displayed to the user.
    _________________________________________________________________________________________________________________________________
						  ~~~~  Variable Dictionary  ~~~~
    |Name|            |Type|                    |Description|
    _________________________________________________________________________________________________________________________________
    tempWord           String                    Holds the unknown word which is displayed to the user in-game.
    answer             String                    [Parameter passed] Holds the answer string which the user must find to win.
    unknownWord        String                    [Parameter passed] Holds the unknown word which is partially displayed to the user to try to guess.
    _________________________________________________________________________________________________________________________________
    */
    private String setUnknown (String answer, String unknownWord)
    {

	String tempWord = "";
	if (unknownWord.length () != 1)
	{
	    for (int x = 0 ; x < unknownWord.length () ; x = x + 2)
	    {
		if (unknownWord.charAt (x) == '_')
		{
		    if (letter.charAt (0) != answer.charAt (x / 2))
		    {
			tempWord += "_ ";
		    }
		    if (letter.charAt (0) == answer.charAt (x / 2))
		    {
			tempWord += (answer.charAt (x / 2) + " ");
		    }
		}
		else
		{
		    tempWord += (unknownWord.charAt (x) + " ");
		}
	    }
	}
	else
	{
	    for (int x = 0 ; x < answer.length () ; x++)
	    {
		tempWord += "_ ";
	    }
	}
	return tempWord;
    }



    /*
    Method which determines if the user given letter selection is correct or not.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	answer             String                    [Parameter passed] Holds the answer string which the user must find to win.
	letter             String                    [Parameter passed] Holds the letter which the user chose.
    _____________________________________________________________________________________________________________________________________________________
    */
    private boolean correct (String answer, String letter)
    {

	for (int x = 0 ; x < answer.length () ; x++)
	{
	    if (letter.charAt (0) == answer.charAt (x))
	    {
		return true;
	    }
	}
	return false;

    }


    /*
    Method which removes (covers) a letter on the screen during gameplay after it has been selected.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	selChar            int                       Holds the ASCII value of the letter which the user chose (char at location (0)).
	letter             String                    [Parameter passed] Holds the letter which the user chose.
    _____________________________________________________________________________________________________________________________________________________
    */
    private void removeLetter (String letter)
    {
	int selChar = letter.charAt (0);
	c.setColor (Color.black);
	if (selChar >= 'A' && selChar <= 'F')
	{
	    c.fillRoundRect ((selChar - 65) * 45 + 50, 200, 45, 45, 10, 10);
	}
	else if (selChar >= 'G' && selChar <= 'L')
	{
	    c.fillRoundRect ((selChar - 71) * 45 + 50, 245, 45, 45, 10, 10);
	}
	else if (selChar >= 'M' && selChar <= 'R')
	{
	    c.fillRoundRect ((selChar - 77) * 45 + 50, 290, 45, 45, 10, 10);
	}
	else if (selChar >= 'S' && selChar <= 'X')
	{
	    c.fillRoundRect ((selChar - 83) * 45 + 50, 335, 45, 45, 10, 10);
	}
	if (selChar >= 'Y' && selChar <= 'Z')
	{
	    c.fillRoundRect ((selChar - 89) * 45 + 50, 380, 45, 45, 10, 10);
	}

    }


    /*
    Method which draws the hanged man on the gamescreen. The extent of which is determined by the user's current life count.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________

	 skin              Color                     Colour variable for skin.
	 jeans             Color                     Colour variable for jeans.
	 xPointLeftLg      int Array                 Array which holds the x-values for the hanged man's left leg.
	 yPointLeftLg      int Array                 Array which holds the y-values for the hanged man's left leg.
	 nPointsLeftLg     int Array                 Array which holds the number of points for the hanged man's left leg.
	 xPointLeftA       int Array                 Array which holds the x-values for the hanged man's left arm.
	 yPointLeftA       int Array                 Array which holds the y-values for the hanged man's left arm.
	 nPointsLeftA      int Array                 Array which holds the number of points for the hanged man's left arm.
	 xPointRightLg     int Array                 Array which holds the x-values for the hanged man's right leg.
	 yPointRightLg     int Array                 Array which holds the y-values for the hanged man's right leg.
	 nPointsRightLg    int Array                 Array which holds the number of points for the hanged man's right leg.
	 xPointRightA      int Array                 Array which holds the x-values for the hanged man's right arm.
	 yPointRightA      int Array                 Array which holds the y-values for the hanged man's right arm.
	 nPointsRightA     int Array                 Array which holds the number of points for the hanged man's right arm.
	 lives             int                       [Parameter passed] Holds the number of lives.
    _____________________________________________________________________________________________________________________________________________________

    */
    private void drawHangMan (int lives)
    {
	Color skin = new Color (236, 211, 179);
	Color jeans = new Color (46, 69, 101);
	int xPointLeftLg[] = {455, 470, 470, 450};
	int yPointLeftLg[] = {360, 360, 420, 420};
	int nPointsLeftLg = 4;
	int xPointLeftA[] = {470, 470, 440, 440};
	int yPointLeftA[] = {290, 300, 330, 320};
	int nPointsLeftA = 4;
	int xPointRightLg[] = {490, 505, 510, 490};
	int yPointRightLg[] = {360, 360, 420, 420};
	int nPointsRightLg = 4;
	int xPointRightA[] = {490, 490, 520, 520};
	int yPointRightA[] = {290, 300, 330, 320};
	int nPointsRightA = 4;
	c.setColor (Color.black);
	switch (lives)
	{
	    case 0:
	    case 1:
		c.setColor (jeans);
		c.fillPolygon (xPointRightLg, yPointRightLg, nPointsRightLg);
	    case 2:
		c.setColor (jeans);
		c.fillPolygon (xPointLeftLg, yPointLeftLg, nPointsLeftLg);
	    case 3:
		c.setColor (Color.black);
		c.fillPolygon (xPointRightA, yPointRightA, nPointsRightA);
	    case 4:
		c.setColor (Color.black);
		c.fillPolygon (xPointLeftA, yPointLeftA, nPointsLeftA);
	    case 5:
		c.setColor (Color.black);
		c.fillRect (470, 302, 20, 58); // body
	    case 6:
		for (int x = 0 ; x < 132 ; x += 2) // Strings
		{
		    c.setColor (darkBrown);
		    c.drawLine (470, 170 + x, 490, 170 + x);
		    c.setColor (lightBrown);
		    c.drawLine (470, 171 + x, 490, 171 + x);
		}
		c.setColor (skin);
		c.fillOval (430, 190, 100, 100);
	}
    }


    /*
    Method which determines if the player has won the game or not.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	answer              String                   [Parameter passed] Holds the string for the answer.
	unknownWord         String                   [Parameter passed] Holds the string for the unknown word which is displayed to the user.
	gameWon             boolean                  Determines if the player has won or not.
    _____________________________________________________________________________________________________________________________________________________
    */
    private boolean win (String answer, String unknownWord)
    {
	boolean gameWon = true;
	for (int x = 0 ; x < (unknownWord.length ()) ; x = x + 2)
	{
	    if (unknownWord.charAt (x) != answer.charAt (x / 2))
	    {
		gameWon = false;
	    }

	}
	return gameWon;
    }


    /*
    Method which displays an error message to the user.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	problem            String                   [Parameter passed] Holds the error message to be displayed to the user.
    _____________________________________________________________________________________________________________________________________________________
    */
    private void jOptionProblem (String problem)
    {
	JOptionPane.showMessageDialog (null, problem, "ALERT", JOptionPane.ERROR_MESSAGE);
    }


    /*
    Method which displays a message to the user.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	message            String                   [Parameter passed] Holds the message to be displayed to the user.
	reason             String                   [Parameter passed] Holds the reason for the message to be displayed to the user.
    _____________________________________________________________________________________________________________________________________________________
    */
    private void jOptionMessage (String message, String reason)
    {
	JOptionPane.showMessageDialog (null, message, reason, JOptionPane.INFORMATION_MESSAGE);
    }


    /*
    Method which pauses the program and waits for user input.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	pauseMessage       String                   [Parameter passed] Holds the message to be displayed to the user when pause program is running.
    _____________________________________________________________________________________________________________________________________________________
    */
    private void pauseProgram (String pauseMessage)
    {
	c.println ();
	c.println (pauseMessage);
	c.getChar ();
    }


    /*
    Main menu method which asks for the user's decision on what to do.
    Also displays the player's current username and chosen level.
    The user cannot play the game without first choosing a level and is given the choice to overwrite their current choices
    if they decide to select another level.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	userDecision       int                   Variable used to hold user's choice during 'JOptionPane.YES_NO_OPTION'
    _____________________________________________________________________________________________________________________________________________________
    */
    public void mainMenu ()
    {
	int userDecision;
	title ();
	choice = " ";
	c.setTextBackgroundColor (lightBrown);
	c.setColor (Color.black);
	c.setFont (new Font ("Impact", Font.PLAIN, 30));
	c.drawString ("Main Menu:", 150, 120);
	c.setFont (new Font ("Arial", Font.PLAIN, 20));
	c.drawString ("1) Play Hangman", 150, 160);
	c.drawString ("2) Level Selection", 150, 190);
	c.drawString ("3) Instructions", 150, 220);
	c.drawString ("4) Highscores", 150, 250);
	c.drawString ("5) Quit", 150, 280);
	c.drawString ("Enter your choice: ", 150, 316);
	if (username != (" "))
	{
	    c.drawString ("Current Username:  " + username, 0, 450);
	}
	else
	{
	    c.drawString ("Current Username: [No username selected]", 0, 450);
	}
	if (levelSelected == false)
	{
	    c.drawString ("Current Level: [No level selected]", 0, 480);
	}
	else
	{
	    c.drawString ("Current Level: " + levelChoice, 0, 480);
	}
	c.setCursor (16, 40);
	choice = c.readLine ();
	if (!choice.equals ("1") && !choice.equals ("2") && !choice.equals ("3") && !choice.equals ("4")
		&& !choice.equals ("5"))
	{
	    jOptionProblem ("Please enter a proper menu choice!");
	    mainMenu ();
	}

	if (choice.equals ("2") && levelSelected == true)
	{
	    userDecision = JOptionPane.showConfirmDialog (null, "You already have a username and level selected. Would you like to overwrite it?", "Alert", JOptionPane.YES_NO_OPTION);
	    if (userDecision == 1)
	    {
		mainMenu ();
	    }
	}
	if (choice.equals ("1") && levelSelected == false)
	{
	    jOptionProblem ("Please select a level before playing.");
	    mainMenu ();
	}
    }


    /*
    Method which displays the game screen, asks for user input, and also runs the main portion of the game.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	gray               Colour                   Colour variable for the colour gray.
	wordBankChar       char                     Holds the values of the char for letters A-Z.
	wordBankString     String                   Holds the values for the letters to be displayed on the screen (as letter options).
	chosenLetter       String Array             Array which holds the user chosen letters.
    _____________________________________________________________________________________________________________________________________________________
    */
    public void display ()
    {
	Color gray = new Color (65, 65, 65);
	lives = 6;
	char wordBankChar = 65;
	String wordBankString;
	String[] chosenLetter = new String [ALPHABET];
	title ();
	c.setFont (new Font ("Arial", Font.PLAIN, 20));
	c.setColor (Color.black);
	c.setColor (darkBrown);
	c.fillRect (590, 170, 20, 250);
	c.setColor (gray);
	c.fillRect (610, 170, 20, 250);
	c.setColor (Color.black);
	c.fillRect (430, 150, 200, 20);

	for (int y = 0 ; y < 180 ; y = y + 45)
	{
	    for (int x = 0 ; x < 270 ; x = x + 45)
	    {
		wordBankString = wordBankChar + "";
		c.drawString (wordBankString, 65 + x, y + 235);
		c.drawRoundRect (x + 50, y + 200, 45, 45, 10, 10);
		wordBankChar++;
	    }
	}

	for (int x = 0 ; x < 90 ; x = x + 45)
	{
	    wordBankString = wordBankChar + "";
	    c.drawRoundRect (50 + x, 380, 45, 45, 10, 10);
	    c.drawString (wordBankString, 65 + x, 415);
	    wordBankChar++;
	}

	for (int x = 0 ; x < 26 ; x++)
	{
	    chosenLetter [x] = " ";
	}

	while (true)
	{
	    unknownWord = setUnknown (answer, unknownWord);
	    c.setFont (new Font ("Impact", Font.PLAIN, 30));
	    c.drawString ("Play Game: ", 10, 65);
	    c.setFont (new Font ("Arial", Font.PLAIN, 20));
	    lineClear (6, 0, 2);
	    c.drawString ("Unknown Word: " + unknownWord, 10, 130);
	    c.drawString ("Chosen level: " + levelChoice, 10, 160);

	    if (levelChoice.equals ("2") && (cheat.equals ("YES") || cheat.equals ("NO")))
	    {
		c.drawString ("Cheats enabled: " + cheat, 10, 190);
		if (cheat.equals ("YES"))
		{
		    c.drawString ("Cheat: " + answer, 220, 190);
		}
	    }
	    else
		c.drawString ("Hint: " + hint, 10, 190);
	    while (true)
	    {
		if (lives == 0 && win (answer, unknownWord) == false)
		{
		    jOptionMessage ("Game over. You lose. Your score has been saved and your username reset. You will now return to the main menu", "LOSE");
		    exitGame = true;
		}
		else if (lives != 0 && win (answer, unknownWord))
		{
		    jOptionMessage ("Congratulations You win! Your score has been saved and your username reset. You will now return to the main menu", "WIN");
		    exitGame = true;
		}
		else
		{
		    while (true)
		    {
			c.drawString ("Lives left: " + lives, 10, 480);
			c.drawString ("Enter choice:", 10, 455);
			c.setCursor (23, 17);
			letter = c.readLine ();
			if (letter.length () != 0)
			{
			    break;
			}
			else
			    jOptionProblem ("Please enter a letter!");
		    }
		    letter = letter.toUpperCase ();
		    if ((letter.charAt (0) < 'A' || letter.charAt (0) > 'Z')
			    && (letter.charAt (0) < 'a' || letter.charAt (0) > 'z'))
		    {
			jOptionProblem ("Please enter a letter inbetween a-z and A-z.");
			letter = " ";
		    }
		    else if (chosenLetter [0].charAt (0) == letter.charAt (0))
		    {
			jOptionMessage ("You have already chosen that letter once.", "Repeated Selection");
			letter = " ";
		    }
		    else if (correct (answer, letter) == false)
		    {
			jOptionProblem ("You are incorrect!");
			chosenLetter [0] = letter.charAt (0) + " ";
			drawHangMan (lives);
			lives--;
			removeLetter (letter);
			letter = " ";
		    }
		    else if (letter.length () != 1)
		    {
			jOptionProblem ("Please enter a single letter.");
			letter = " ";
		    }
		    else
		    {
			removeLetter (letter);
			chosenLetter [0] = letter.charAt (0) + " ";

		    }
		}
		lineClear (23, 0, 2);
		break;
	    }
	    if (exitGame)
	    {
		break;
	    }
	}

	if (levelChoice.equals ("2") && cheat.equals ("YES"))
	{
	    score = lives;
	}
	else if (levelChoice.equals ("1") && cheat.equals ("NO"))
	{
	    score = lives * 2;
	}
	else
	{
	    score = lives * 3;
	}

	saveHighScores ();
	username = " ";
	unknownWord = " ";
	answer = " ";
	hint = " ";
	cheat = "NO";
	score = 0;
	exitGame = false;
	levelSelected = false;
    }


    /*
    Method which allows the user to select a username, level, and if they would like to activate hints/cheats.
    This method is also where the player's randomly chosen word is chosen, this method prevents any duplicate words from being chosen in a given session
    (one session is everytime the program runs). If the user happens to run through all words/phrases availiable the program will reset this so that they
    can continue playing.
    */
    public void levelSelect ()
    {
	title ();
	c.setFont (new Font ("Impact", Font.PLAIN, 30));
	c.drawString ("Pre-Game Level Selection:", 10, 120);
	do
	{
	    c.setFont (new Font ("Arial", Font.PLAIN, 20));
	    c.drawString ("Enter username(maximum 19 characters):", 10, 156);
	    c.setCursor (8, 49);
	    username = c.readLine ();
	    if (username.length () > 19 || username.equals (""))
	    {
		jOptionProblem ("Your username must be less than or equal to 19 characters, and at least 1 character long.");
		lineClear (8, 1, 2);
		username = "";
		usernameValid = false;
	    }
	    else
		usernameValid = true;

	}
	while (usernameValid == false);
	c.setFont (new Font ("Impact", Font.PLAIN, 30));
	c.drawString ("Please select your level.", 10, 200);
	c.setFont (new Font ("Arial", Font.PLAIN, 20));
	c.drawString ("Level 1 (Hints)", 10, 230);
	c.drawString ("Level 2 (Cheat Option/No Hint)", 10, 260);
	while (true)
	{
	    c.drawString ("Please choose a level: ", 10, 295);
	    c.setCursor (15, 27);
	    levelChoice = c.readLine ();
	    if (!levelChoice.equals ("1") && !levelChoice.equals ("2"))
	    {
		jOptionProblem ("Please enter a 1 or 2!");
		lineClear (15, 27, 1);
	    }
	    else if (levelChoice.equals ("2"))
	    {
		while (true)
		{
		    c.setFont (new Font ("Impact", Font.PLAIN, 30));
		    c.drawString ("Enable Cheats?", 10, 345);
		    c.setFont (new Font ("Arial", Font.PLAIN, 20));
		    c.drawString ("Would you like to enable cheats? Please enter 'Yes' or 'No':", 10, 375);
		    c.setCursor (19, 68);
		    cheat = c.readLine ().toUpperCase ();
		    if (!cheat.equals ("YES") && !cheat.equals ("NO"))
		    {
			jOptionProblem ("Please enter a 'Yes' or 'No'!");
			lineClear (19, 0, 2);
		    }
		    else
			break;
		}
		break;
	    }
	    else
		break;
	}
	while (true)
	{
	    randomNum = randomNumber ();
	    answer = getAnswer (dataLine (randomNum));
	    if (plays > 0)
	    {
		for (int x = 0 ; x < plays ; x++)
		{
		    if (plays == WORDCOUNT)
		    {
			for (int y = 0 ; y < WORDCOUNT ; y++)
			{
			    chosenWords [y] = " ";
			    jOptionMessage ("You have already gone through all words/phrases. Word bank reset.", "Words and Phrases Cycled");
			    plays = 0;
			    wordNew = true;
			    break;
			}
		    }
		    if (answer.equals (chosenWords [x]))
		    {
			wordNew = false;
			break;
		    }

		}
	    }
	    if (!answer.equals (" ") && wordNew == true)
	    {
		chosenWords [plays] = answer;
		plays++;
		hint = findHint (dataLine (randomNum));
		levelSelected = true;
		wordNew = true;
		break;
	    }
	    wordNew = true;
	}
	pauseProgram ("Press any key to return to the main menu.");
    }


    /*
    Method which saves the player's data after they have finished a game.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	comparator         int                      Holds the integer value of the number to compare the player's score with.
	arrayIndex         int                      Holds the location of the array.
	data               String Array             Array which holds the highscores.
    _____________________________________________________________________________________________________________________________________________________
    */
    public void saveHighScores ()
    {
	int comparator;
	int arrayIndex = 0;
	String[] data = new String [HIGHSCORES + 2];
	String inputData;
	title ();
	for (int x = 0 ; x < HIGHSCORES ; x++)
	{
	    data [x] = "-1";
	}
	try
	{
	    BufferedReader file = new BufferedReader (new FileReader ("highscores.hang"));
	    inputData = file.readLine ();
	    if (inputData != null)
	    {
		if (inputData.equals ("CalebHangmanISP"))
		{
		    while (true)
		    {
			for (int x = 0 ; x < HIGHSCORES ; x++)
			{
			    data [x] = file.readLine ();
			    if (data [x] == null)
				break;
			}
			break;
		    }
		}
		else
		{
		    jOptionProblem ("Invalid Header. Unable to read the file. Creating a new highscores file.");
		}
	    }
	    else
	    {
		jOptionProblem ("Highscores file is empty. Creating a new highscores file.");
	    }
	}
	catch (IOException e)
	{
	}
	for (int x = 1 ; x < HIGHSCORES ; x = x + 4)
	{
	    try
	    {
		comparator = Integer.parseInt (data [x]);
		if (score > comparator)
		{
		    arrayIndex = x - 1;
		    break;
		}
	    }
	    catch (NumberFormatException e)
	    {
	    }
	}
	for (int x = HIGHSCORES - 4 ; x >= arrayIndex ; x--)
	{
	    data [x + 4] = data [x];
	}
	if (cheat.equals ("YES"))
	{
	    data [arrayIndex + 3] = ("YES");
	}
	else
	{
	    data [arrayIndex + 3] = ("NO");
	}
	data [arrayIndex + 2] = levelChoice;
	data [arrayIndex + 1] = Integer.toString (score);
	data [arrayIndex] = username;
	try
	{
	    PrintWriter output = new PrintWriter (new FileWriter ("highscores.hang"));
	    output.println ("CalebHangmanISP");
	    for (int x = 0 ; x < HIGHSCORES ; x++)
	    {
		if (data [x] != null)
		{
		    output.println (data [x]);
		}
	    }
	    output.close ();
	}
	catch (IOException e)
	{
	}
    }


    /*
    Reads the highscores file and displays it to the user.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	highScoreRow       String                   Holds lines of data from the highscores file.
	highScoreChoice    char                     Char which gives the user an option to clear the highscores file.
    _____________________________________________________________________________________________________________________________________________________
    */
    public void highScores ()
    {
	String highScoreRow;
	char highScoreChoice;
	title ();
	c.setCursor (7, 1);
	c.print ("[Name]", 20);
	c.print ("[Score]", 20);
	c.print ("[Level]", 20);
	c.println ("[Cheats]");
	try
	{
	    BufferedReader input = new BufferedReader (new FileReader ("highscores.hang"));
	    highScoreRow = input.readLine ();
	    if (highScoreRow.equals ("CalebHangmanISP"))
	    {
		highScoreRow = input.readLine ();
		if (highScoreRow != null)
		{
		    c.print (highScoreRow, 20);
		    for (int x = 0 ; x < HIGHSCORES - 1 ; x++)
		    {
			highScoreRow = input.readLine ();
			if ((highScoreRow != null) && (!highScoreRow.equals ("-1")))
			{
			    c.print (highScoreRow, 20);
			}
		    }
		    c.setCursor (20, 1);
		    c.print ("Press x to clear the highscores file. Press any other key to continue: ");
		    highScoreChoice = c.getChar ();
		    if (highScoreChoice == 'x')
		    {
			//jOptionMessage ("You have cleared the High Scores file.", "High Scores Cleared.");
			clearHighScores ();
		    }
		}
		else
		{
		    c.setCursor (20, 1);
		    pauseProgram ("There are no High Scores to display. Press any key to continue: ");
		}
	    }
	    else
	    {
		jOptionProblem ("Invalid highscore file. Returning to the main menu.");
	    }
	}

	catch (FileNotFoundException e)
	{
	    jOptionProblem ("The High Score File was not found.");
	}

	catch (IOException e)
	{
	}
    }


    // Method which displays instructions to the user.
    public void instructions ()
    {
	title ();
	c.setCursor (6, 1);
	c.println ("Instructions:");
	c.println ("Enter a letter from A-Z and try to guess the unknown word which is shown in '_'.");
	c.println ("Every incorrect letter will cause you to lose 1 life and add a body part to the hanged man.");
	c.println ("After 6 tries (when the hanged man is completed), you will lose.");
	c.println ("<> Cheats = 1 point per life.");
	c.println ("<> Hints = 2 points per life.");
	c.println ("<> No cheats and no hints = 3 points per life.");
	c.println ("If you have cheats enabled your score will be reduced by 50%.");
	c.println ("Numbers and puncuation will not be included in the words and phrases.");
	c.println ("Please note, that you can not enter an empty space.");
	c.println ("Phrases have '_'s built into the word, so you do not need to enter a character ");
	c.println ("for spaces.");
	c.println ("The username which you select must be less than 19 characters.");
	pauseProgram ("Press any key to return to the main menu.");
    }


    // Displays a goodbye message and closes the program.
    public void goodBye ()
    {
	title ();
	c.println ("Thank you for using this program.");
	c.println ("This is a game by: Caleb Tseng-Tham.");
	c.println ("Goodbye.");
	pauseProgram ("Press any key to close the program.");
	c.close ();
    }


    /*
    Constructor for the splash screen animation.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	 b                Class constructor         Used to create the splashscreen.
    _____________________________________________________________________________________________________________________________________________________
    */
    public void splashScreen ()
    {
	SplashScreen b = new SplashScreen (c);
	b.run ();
    }


    /*
    Console constructor
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	 c                Class constructor         Used to create the Console.
    _____________________________________________________________________________________________________________________________________________________
    */
    public CalebISPHangman ()
    {
	c = new Console ();
    }


    /*
    Main method, which controls the flow of the program and other methods.
    ____________________________________________________________________________________________________________________________________________________
						      ~~~~  Variable Dictionary  ~~~~
	|Name|            |Type|                    |Description|
    _____________________________________________________________________________________________________________________________________________________
	 d                Class constructor         Used to create another instance of the CalebISPHangman class.
    _____________________________________________________________________________________________________________________________________________________
    */
    public static void main (String[] args)
    {
	CalebISPHangman d = new CalebISPHangman ();
	d.splashScreen ();
	do
	{
	    d.mainMenu ();
	    if (!d.choice.equals ("5"))
	    {
		if (d.choice.equals ("1"))
		{
		    d.display ();
		}
		else if (d.choice.equals ("2"))
		    d.levelSelect ();
		else if (d.choice.equals ("3"))
		    d.instructions ();
		else
		    d.highScores ();
	    }
	}

	while (!d.choice.equals ("5"));
	d.goodBye ();
    }
}
