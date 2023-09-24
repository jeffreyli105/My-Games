from graphics import*
from time import sleep
from random import randint
import winsound
from playsound import playsound
from threading import Thread
win = GraphWin("Boing",800,500) 
win.setBackground("Black")
#All global variables used in more than one function
global screen
global score
global border
global score
global exitSinglePlayer
global paddle
global brickOne
global brickTwo
global brickThree
global brickFour
global brickFive
global brickSix
global brickSeven
global brickEight
global brickNine
global brickTen
global brickEleven
global brickTwelve
global brickThirteen
global brickFourteen
global brickFifteen
reset = False
#Please press the paddles(DON'T HOLD DOWN THE KEYS)

'''
MAKE ABOUT ME PAGE
Bugs:
MultiPlayer:

SinglPlayer:
When ball hits the sides of the paddle, it glitches Fix: hits right side = bounce up right, hits left side = bounce up left

'''
#Functions

#Undraws all objects inside of singleplayer
def undrawAllSinglePlayer():
    #global variables
    global wordScore
    global border
    global score
    global exitSinglePlayer
    global paddle
    global brickOne
    global brickTwo
    global brickThree
    global brickFour
    global brickFive
    global brickSix
    global brickSeven
    global brickEight
    global brickNine
    global brickTen
    global brickEleven
    global brickTwelve
    global brickThirteen
    global brickFourteen
    global brickFifteen
    global ball
    #Undraws everything
    border.undraw()
    score.undraw()
    exitSinglePlayer.undraw()
    paddle.undraw()
    brickOne.undraw()
    brickTwo.undraw()
    brickThree.undraw()
    brickFour.undraw()
    brickFive.undraw()
    brickSix.undraw()
    brickSeven.undraw()
    brickEight.undraw()
    brickNine.undraw()
    brickTen.undraw()
    brickEleven.undraw()
    brickTwelve.undraw()
    brickThirteen.undraw()
    brickFourteen.undraw()
    brickFifteen.undraw()
    ball.undraw()

#Updates the score(singleplayer)
def updateScore():
    #global variables
    global broken
    global score
    #Sets score to be the number of bricks broken and draws it
    score = Text(Point(250,450), str(broken))
    score.setSize(35)
    score.setTextColor("White")
    score.draw(win)
#Creates a new screen(when changing between modes)
def newScreen():
    #Creates a black rectangle that covers the whole screen
    black = Rectangle(Point(0,800), Point(800,0))
    black.setFill("Black")
    black.draw(win)
#Main menu of game
def homeScreen():
    #Global variables
    global scoreP1
    global scoreP2
    #Sets the variable "screen" to be "HOME"(current screen is home screen)
    screen = "HOME"
    #Creates a new screen(in case they are returning from a mode)
    newScreen()
    #Exit button
    exitHome = Image(Point(403,475), "exitButton.gif")
    exitHome.draw(win)
    #Name of game
    title = Image(Point(400,100), "BoingTitle.gif")
    title.draw(win)
    #Rules
    rulesButton = Image(Point(403,220), "Rules.gif")
    rulesButton.draw(win)
    #Singleplayer button
    singlePlayer = Image(Point(403,330), "SinglePlayerButton.gif")
    singlePlayer.draw(win)
    #Multiplayer button
    multiplayer = Image(Point(403, 408), "MultiPlayerButton.gif")
    multiplayer.draw(win)
    #About me button
    aboutMe = Image(Point(403,265), "aboutMeButton.gif")
    aboutMe.draw(win)
    #Main menu music
    winsound.PlaySound("BoingLobbyMusic.wav", winsound.SND_LOOP | winsound.SND_ASYNC)
    while True:
        mouse = win.getMouse()
        #If the mouse clicks the "Rules" button, go to the rules screen
        if (mouse.getX() >= 350 and mouse.getX() <= 456
        and mouse.getY() >= 195 and mouse.getY() <= 240):
            rulesScreen()
        #If the mouse clicks the "About me" button, go to the about me screen
        elif(mouse.getX() >= 300 and mouse.getX() <= 506
        and mouse.getY() >= 240 and mouse.getY() <= 286):
            aboutMeScreen()
        #If the mouse clicks the "Singleplayer button, go to the singleplayer screen
        elif (mouse.getX() >= 257 and mouse.getX() <= 551
        and mouse.getY() >= 290 and mouse.getY() <= 365):
            singlePlayerScreen()
        #If the mouse clicks the "Multiplayer button, go to the multiplayer screen
        elif (mouse.getX() >= 257 and mouse.getX() <= 551
        and mouse.getY() >= 369 and mouse.getY() <= 445):
            scoreP1 = 0
            scoreP2 = 0
            multiPlayerScreen()
        elif(mouse.getX() >= 328 and mouse.getX() <= 479
        and mouse.getY() >= 450 and mouse.getY() <= 498):
            win.close()
#Count down(when entering multiplayer/singleplayer and when someone scores in multiplayer)
def countDown():
    mouse = win.checkMouse()
    #If they click the exit button while it's counting down, they are brought to the main menu
    if(mouse != None):
        if(mouse.getX() >= 325 and mouse.getX() <= 473
        and mouse.getY() >= 4 and mouse.getY() <= 53):
            reset = False
            border.undraw()
            exitMultiPlayer.undraw()
            playerOneScore.undraw()
            playerTwoScore.undraw()
            paddleOne.undraw()
            paddleTwo.undraw()
            ball.undraw()
            homeScreen()
    #Count down code begins here and checking if exit button was clicked while it's counting down
    num = Text(Point(400,110), "5")
    num.setSize(35)
    num.setTextColor("pink")
    num.draw(win)
    time.sleep(1)
    num.undraw()
    for i in range(4, 0, -1):
        mouse = win.checkMouse()
        if(mouse != None):
            if(mouse.getX() >= 325 and mouse.getX() <= 473
            and mouse.getY() >= 4 and mouse.getY() <= 53):
                reset = False
                border.undraw()
                exitMultiPlayer.undraw()
                playerOneScore.undraw()
                playerTwoScore.undraw()
                paddleOne.undraw()
                paddleTwo.undraw()
                ball.undraw()
                homeScreen()
        num = Text(Point(400,110), str(i))
        num.setSize(35)
        num.setTextColor("pink")
        num.draw(win)
        time.sleep(1)
        num.undraw()
#Losing screen for singleplayer
def singlePlayerLoseScreen(broken):
    #Create a new screen, draw the lose screen and exit button
    newScreen()
    screen = Image(Point(400,250), "singlePlayerLoseScreen.gif")
    screen.draw(win)
    
    score = Text(Point(200,350), "Score: " + str(broken))
    score.setSize(35)
    score.setFill("Red")
    score.draw(win)

    exitButton = Image(Point(600,350), "exitButton.gif")
    exitButton.draw(win)
    #If exit button is pressed, go back to home screen
    while True:
        mouse = win.checkMouse()
        if(mouse != None):
            if(mouse.getX() >= 528 and mouse.getX() <= 674
            and mouse.getY() >= 326 and mouse.getY() <= 373):
                homeScreen()
#Win screen for singleplayer
def singlePlayerWinScreen():
    #Create a new screen, draw the win screen, wait 5 seconds and brings user back to main menu
    newScreen()
    screen = Image(Point(400,250), "singlePlayerWinScreen.gif")
    screen.draw(win)
    time.sleep(5)
    homeScreen()
#About me screen
def aboutMeScreen():
    #Creates a new screen and draw the about me screen
    newScreen()
    aboutMe = Image(Point(400,250), "aboutMe.gif")
    aboutMe.draw(win)
    #About me page music
    winsound.PlaySound("rulesMusic.wav", winsound.SND_LOOP | winsound.SND_ASYNC)
    #Exit button
    exitRules = Image(Point(380,470), "exitButton.gif")
    exitRules.draw(win)
    #If exit button is pressed, return back to main menu
    while True:
        mouse = win.getMouse()
        if (mouse.getX() >= 306 and mouse.getX() <= 454
        and mouse.getY() >= 442 and mouse.getY() <= 494):
            winsound.PlaySound("ButtonPressed.wav", winsound.SND_ASYNC)
            homeScreen()
#Rules page
def rulesScreen():
    #Create a new screen and draw the rules screen
    newScreen()
    screen = Image(Point(400,250), "BoingGameRules.gif")
    screen.draw(win)
    #Rules page music
    winsound.PlaySound("rulesMusic.wav", winsound.SND_LOOP | winsound.SND_ASYNC)
    #Exit button
    exitRules = Image(Point(380,470), "exitButton.gif")
    exitRules.draw(win)
    #If exit button is pressed, return back to main menu
    while True:
        mouse = win.getMouse()
        if (mouse.getX() >= 306 and mouse.getX() <= 454
        and mouse.getY() >= 442 and mouse.getY() <= 494):
            winsound.PlaySound("ButtonPressed.wav", winsound.SND_ASYNC)
            homeScreen()

#Singleplayer screen
def singlePlayerScreen():
    #Global variables
    global score
    global broken
    global wordScore
    global border
    global score
    global exitSinglePlayer
    global paddle
    global brickOne
    global brickTwo
    global brickThree
    global brickFour
    global brickFive
    global brickSix
    global brickSeven
    global brickEight
    global brickNine
    global brickTen
    global brickEleven
    global brickTwelve
    global brickThirteen
    global brickFourteen
    global brickFifteen
    global ball
    newScreen()
    winsound.PlaySound("singleplayerMusic.wav", winsound.SND_LOOP | winsound.SND_ASYNC)
    #Initializing variables
    broken = 0
    border = Rectangle(Point(0,0), Point(799,399))
    border.setFill("Black")
    border.setOutline("White")
    border.draw(win)

    wordScore = Text(Point(150,450), "Score: ")
    wordScore.setSize(35)
    wordScore.setTextColor("White")
    wordScore.draw(win)
    
    score = Text(Point(250,450), str(broken))
    score.setSize(35)
    score.setTextColor("White")
    score.draw(win)

    exitSinglePlayer = Image(Point(550,450), "exitButton.gif")
    exitSinglePlayer.draw(win)

    paddle = Rectangle(Point(355,370), Point(455,380))
    paddle.setFill("Blue")
    paddle.draw(win)

    hitBox = Rectangle(Point(350,365), Point(460,385))
    hitBox.setOutline("Black")
    hitBox.draw(win)

    brickOne = Rectangle(Point(10,10), Point(158,60))
    brickOne.setFill("Red")
    brickOne.draw(win)
    brickTwo = Rectangle(Point(168,10), Point(316,60))
    brickTwo.setFill("Red")
    brickTwo.draw(win)
    brickThree = Rectangle(Point(326,10), Point(474,60))
    brickThree.setFill("Red")
    brickThree.draw(win)
    brickFour = Rectangle(Point(484,10), Point(632,60))
    brickFour.setFill("Red")
    brickFour.draw(win)
    brickFive = Rectangle(Point(642,10), Point(790,60))
    brickFive.setFill("Red")
    brickFive.draw(win)
    
    brickSix = Rectangle(Point(10,70), Point(158,120))
    brickSix.setFill("Red")
    brickSix.draw(win)
    brickSeven = Rectangle(Point(168,70), Point(316,120))
    brickSeven.setFill("Red")
    brickSeven.draw(win)
    brickEight = Rectangle(Point(326,70), Point(474,120))
    brickEight.setFill("Red")
    brickEight.draw(win)
    brickNine = Rectangle(Point(484,70), Point(632,120))
    brickNine.setFill("Red")
    brickNine.draw(win)
    brickTen = Rectangle(Point(642,70), Point(790,120))
    brickTen.setFill("Red")
    brickTen.draw(win)
    
    brickEleven = Rectangle(Point(10,130), Point(158,180))
    brickEleven.setFill("Red")
    brickEleven.draw(win)
    brickTwelve = Rectangle(Point(168,130), Point(316,180))
    brickTwelve.setFill("Red")
    brickTwelve.draw(win)
    brickThirteen = Rectangle(Point(326,130), Point(474,180))
    brickThirteen.setFill("Red")
    brickThirteen.draw(win)
    brickFourteen = Rectangle(Point(484,130), Point(632,180))
    brickFourteen.setFill("Red")
    brickFourteen.draw(win)
    brickFifteen = Rectangle(Point(642,130), Point(790,180))
    brickFifteen.setFill("Red")
    brickFifteen.draw(win)

    brokeOne = False
    brokeTwo = False
    brokeThree = False
    brokeFour = False
    brokeFive = False
    brokeSix = False
    brokeSeven = False
    brokeEight = False
    brokeNine = False
    brokeTen = False
    brokeEleven = False
    brokeTwelve = False
    brokeThirteen = False
    brokeFourteen = False
    brokeFifteen = False
    
    ball = Circle(Point(405,250),10)
    ball.setFill("Yellow")
    ball.draw(win)
    directionX = 5
    directionY = 5
    countDown()
    while True:
        key = win.checkKey()
        #Move the ball using the variables "directionX" and "directionY"
        ball.move (1 * directionX, 1 * directionY)
        #If ball hits the top, change direction to go down
        if ((ball.getCenter ().getY () + 10 >= hitBox.getP1().getY()
          and ball.getCenter().getX() >= hitBox.getP1().getX() and
              ball.getCenter().getX() <= hitBox.getP2().getX())
              or ball.getCenter().getY() + 10 <= 10):
            directionY *= -1
        #Code to check if the ball hits any of the bricks
        elif(brokeOne == False and (ball.getCenter().getY() - 10 <= brickOne.getP2().getY()
           and (ball.getCenter().getX() >= brickOne.getP1().getX()
            and ball.getCenter().getX() <= brickOne.getP2().getX()))):
            brickOne.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeOne = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()
        elif(brokeTwo == False and (ball.getCenter().getY() - 10 <= brickTwo.getP2().getY()
           and (ball.getCenter().getX() >= brickTwo.getP1().getX()
            and ball.getCenter().getX() <= brickTwo.getP2().getX()))):
            brickTwo.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeTwo = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeThree == False and (ball.getCenter().getY() - 10 <= brickThree.getP2().getY()
           and (ball.getCenter().getX() >= brickThree.getP1().getX()
            and ball.getCenter().getX() <= brickThree.getP2().getX()))):
            brickThree.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeThree = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeFour == False and (ball.getCenter().getY() - 10 <= brickFour.getP2().getY()
           and (ball.getCenter().getX() >= brickFour.getP1().getX()
            and ball.getCenter().getX() <= brickFour.getP2().getX()))):
            brickFour.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeFour = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeFive == False and (ball.getCenter().getY() - 10 <= brickFive.getP2().getY()
           and (ball.getCenter().getX() >= brickFive.getP1().getX()
            and ball.getCenter().getX() <= brickFive.getP2().getX()))):
            brickFive.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeFive = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeSix == False and (ball.getCenter().getY() - 10 <= brickSix.getP2().getY()
           and (ball.getCenter().getX() >= brickSix.getP1().getX()
            and ball.getCenter().getX() <= brickSix.getP2().getX()))):
            brickSix.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeSix = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeSeven == False and (ball.getCenter().getY() - 10 <= brickSeven.getP2().getY()
           and (ball.getCenter().getX() >= brickSeven.getP1().getX()
            and ball.getCenter().getX() <= brickSeven.getP2().getX()))):
            brickSeven.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeSeven = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeEight == False and (ball.getCenter().getY() - 10 <= brickEight.getP2().getY()
           and (ball.getCenter().getX() >= brickEight.getP1().getX()
            and ball.getCenter().getX() <= brickEight.getP2().getX()))):
            brickEight.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeEight = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeNine == False and (ball.getCenter().getY() - 10 <= brickNine.getP2().getY()
           and (ball.getCenter().getX() >= brickNine.getP1().getX()
            and ball.getCenter().getX() <= brickNine.getP2().getX()))):
            brickNine.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeNine = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeTen == False and (ball.getCenter().getY() - 10 <= brickTen.getP2().getY()
           and (ball.getCenter().getX() >= brickTen.getP1().getX()
            and ball.getCenter().getX() <= brickTen.getP2().getX()))):
            brickTen.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeTen = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeEleven == False and (ball.getCenter().getY() - 10 <= brickEleven.getP2().getY()
           and (ball.getCenter().getX() >= brickEleven.getP1().getX()
            and ball.getCenter().getX() <= brickEleven.getP2().getX()))):
            brickEleven.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeEleven = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeTwelve == False and (ball.getCenter().getY() - 10 <= brickTwelve.getP2().getY()
           and (ball.getCenter().getX() >= brickTwelve.getP1().getX()
            and ball.getCenter().getX() <= brickTwelve.getP2().getX()))):
            brickTwelve.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeTwelve = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeThirteen == False and (ball.getCenter().getY() - 10 <= brickThirteen.getP2().getY()
           and (ball.getCenter().getX() >= brickThirteen.getP1().getX()
            and ball.getCenter().getX() <= brickThirteen.getP2().getX()))):
            brickThirteen.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeThirteen = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeFourteen == False and (ball.getCenter().getY() - 10 <= brickFourteen.getP2().getY()
           and (ball.getCenter().getX() >= brickFourteen.getP1().getX()
            and ball.getCenter().getX() <= brickFourteen.getP2().getX()))):
            brickFourteen.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeFourteen = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()

        elif(brokeFifteen == False and (ball.getCenter().getY() - 10 <= brickFifteen.getP2().getY()
           and (ball.getCenter().getX() >= brickFifteen.getP1().getX()
            and ball.getCenter().getX() <= brickFifteen.getP2().getX()))):
            brickFifteen.undraw()
            broken += 1
            score.undraw()
            updateScore()
            brokeFifteen = True
            directionY *= -1
            if(broken == 15):
                undrawAllSinglePlayer()
                singlePlayerWinScreen()
        #If the ball hits the ground, undraw everything from the singleplayer screen and go to the singlePlayerLoseScreen function
        elif(ball.getCenter().getY() + 10 >= 399):
            undrawAllSinglePlayer()
            singlePlayerLoseScreen(broken)
        #If ball hits the sides of the wall, bounce off of them by changing their x direction to be the opposite one they had when the ball was approaching the wall
        if(ball.getCenter().getX() + 10 >= 790 or ball.getCenter().getX()
             - 10 <= 10):
            directionX *= -1
        #Cause the ball to slow down
        time.sleep (0.01)
        #Controls for the paddle
        if(key == "Left"):
            if(paddle.getP1().getX() >= 10):
                paddle.move(-50,0)
                hitBox.move(-50,0)
        elif(key == "Right"):
            if(paddle.getP2().getX() <= 800):
                paddle.move(50,0)
                hitBox.move(50,0)
        #If exit button is pressed, go back to home screen
        else:
            mouse = win.checkMouse()
            if(mouse != None):
                if(mouse.getX() >= 481 and mouse.getX() <= 611
                and mouse.getY() >= 425 and mouse.getY() <= 478):
                    homeScreen()
#Multiplayer screen
def multiPlayerScreen():
    #Global variables
    global reset
    global scoreP1
    global scoreP2
    global playerOneScore
    global playerTwoScore
    global border
    global exitMultiPlayer
    global ball
    global paddleOne
    global paddleTwo
    #Create a new screen and play the multiplayer music
    newScreen()
    winsound.PlaySound("multiplayerMusic.wav", winsound.SND_LOOP | winsound.SND_ASYNC)
    #If game did not reset(user just entered mode), initialize and draw everything in multiplayer
    if(reset == False):
##        print("reset is false")
        scoreP1 = 0
        scoreP2 = 0
        border = Rectangle(Point(0,90), Point(799,499))
        border.setFill("Black")
        border.setOutline("White")
        border.draw(win)

##        exitText = Text(Point(400,40), "Press B to exit")
##        exitText.setTextColor("Sky Blue")
##        exitText.setSize(35)
##        exitText.draw(win)
        exitMultiPlayer = Image(Point(400,30), "exitButton.gif")
        exitMultiPlayer.draw(win)
        
        playerOneScore = Text(Point(200,50), str(scoreP1))
        playerOneScore.setSize(35)
        playerOneScore.setTextColor("White")
        playerOneScore.draw(win)

        playerTwoScore = Text(Point(600,50), str(scoreP2))
        playerTwoScore.setSize(35)
        playerTwoScore.setTextColor("White")
        playerTwoScore.draw(win)
    
        paddleOne = Rectangle(Point(20,250), Point(30,300))
        paddleOne.setFill("Red")
        paddleOne.draw(win)
        
        paddleTwo = Rectangle(Point(769,250), Point(779,300))
        paddleTwo.setFill("Blue")
        paddleTwo.draw(win)
    
        ball = Circle(Point(400,300), 10)
        ball.setFill("Yellow")
        ball.draw(win)
        countDown()
    #If the game resetted because one of the users scored a point, only reset the paddles, ball, and score(to be the new score)
    else:
##        print("reset is true")
        
        playerOneScore = Text(Point(200,50), str(scoreP1))
        playerOneScore.setSize(35)
        playerOneScore.setTextColor("White")
        playerOneScore.draw(win)

        playerTwoScore = Text(Point(600,50), str(scoreP2))
        playerTwoScore.setSize(35)
        playerTwoScore.setTextColor("White")
        playerTwoScore.draw(win)
        
        paddleOne = Rectangle(Point(20,250), Point(30,300))
        paddleOne.setFill("Red")
        paddleOne.draw(win)
        
        paddleTwo = Rectangle(Point(769,250), Point(779,300))
        paddleTwo.setFill("Blue")
        paddleTwo.draw(win)

        ball = Circle(Point(400,300), 10)
        ball.setFill("Yellow")
        ball.draw(win)
        
        countDown()
    directionX = 5
    directionY = 5
    
    while True:  
        key = win.checkKey()
        #Move the ball using the variables "directionX" and "directionY"
        ball.move (1 * directionX, 1 * directionY)
        #If the ball hits the right side, the left player receives a point. If they have 5 points, they win
        if (ball.getCenter ().getX () + 10 >= 799):
            scoreP1 += 1
            if(scoreP1 == 5):
                reset = False
                redVictory = Image(Point(400,250), "RedWins.gif")
                border.undraw()
                exitMultiPlayer.undraw()
                playerOneScore.undraw()
                playerTwoScore.undraw()
                paddleOne.undraw()
                paddleTwo.undraw()
                ball.undraw()
                redVictory.draw(win)
                time.sleep(5)
                homeScreen()
            reset = True
            paddleOne.undraw()
            paddleTwo.undraw()
            ball.undraw()
            playerOneScore.undraw()
            playerTwoScore.undraw()

            playerOneScore = Text(Point(200,50), str(scoreP1))
            playerOneScore.setSize(35)
            playerOneScore.setTextColor("White")
            playerOneScore.draw(win)

            playerTwoScore = Text(Point(600,50), str(scoreP2))
            playerTwoScore.setSize(35)
            playerTwoScore.setTextColor("White")
            playerTwoScore.draw(win)
            paddleOne = Rectangle(Point(20,250), Point(30,300))
            paddleOne.setFill("Red")
            paddleOne.draw(win)
        
            paddleTwo = Rectangle(Point(769,250), Point(779,300))
            paddleTwo.setFill("Blue")
            paddleTwo.draw(win)

            ball = Circle(Point(400,300), 10)
            ball.setFill("Yellow")
            ball.draw(win)
            directionX *= -1
            directionY *= -1
            countDown()
##            multiPlayerScreen()
        #If the ball hits the left side, the right player receives a point. If they have 5 points, they win
        elif(ball.getCenter ().getX () - 10 <= 0):
            scoreP2 += 1
            if(scoreP2 == 5):
                reset = False
                blueVictory = Image(Point(400,250), "BlueWins.gif")
                border.undraw()
                exitMultiPlayer.undraw()
                playerOneScore.undraw()
                playerTwoScore.undraw()
                paddleOne.undraw()
                paddleTwo.undraw()
                ball.undraw()
                blueVictory.draw(win)
                time.sleep(5)
                homeScreen()
            reset = True
            paddleOne.undraw()
            paddleTwo.undraw()
            ball.undraw()
            playerOneScore.undraw()
            playerTwoScore.undraw()

            playerOneScore = Text(Point(200,50), str(scoreP1))
            playerOneScore.setSize(35)
            playerOneScore.setTextColor("White")
            playerOneScore.draw(win)

            playerTwoScore = Text(Point(600,50), str(scoreP2))
            playerTwoScore.setSize(35)
            playerTwoScore.setTextColor("White")
            playerTwoScore.draw(win)
            
            paddleOne = Rectangle(Point(20,250), Point(30,300))
            paddleOne.setFill("Red")
            paddleOne.draw(win)
        
            paddleTwo = Rectangle(Point(769,250), Point(779,300))
            paddleTwo.setFill("Blue")
            paddleTwo.draw(win)

            ball = Circle(Point(400,300), 10)
            ball.setFill("Yellow")
            ball.draw(win)
            directionX *= -1
            directionY *= -1
            countDown()
        #If the ball hits the top or bottom of the borders, change the y direction to be the opposite
        elif (ball.getCenter ().getY () + 10 >= 499 or
            ball.getCenter ().getY () -10 <= 90):
            directionY *= -1
        #If the ball hits paddleOne...
        if(ball.getCenter().getX() - 10 <= paddleOne.getP2().getX()
            and ball.getCenter().getY() >= paddleOne.getP1().getY()
           and ball.getCenter().getY() <= paddleOne.getP2().getY()):
            #If the ball hits the bottom of that paddle, bounce the ball downwards and right
            if(ball.getCenter().getY() <= paddleOne.getP2().getY()
               and ball.getCenter().getY() >= paddleOne.getP2().getY() - 15):
                directionX = 5
                directionY = 5
            #If the ball hits the top of that paddle, bounce the ball, upwards and right
            elif(ball.getCenter().getY() >= paddleOne.getP1().getY()
                 and ball.getCenter().getY() <= paddleOne.getP1().getY() + 15):
                directionX = 5
                directionY = -5
            #If the ball hits the paddle regularly, change its x direction to be the opposite
            else:
                directionX *= -1
        #If the ball hits paddleTwo...
        elif(ball.getCenter().getX() + 10 >= paddleTwo.getP1().getX()
            and ball.getCenter().getY() >= paddleTwo.getP1().getY()
           and ball.getCenter().getY() <= paddleTwo.getP2().getY()):
            #If the ball hits the bottom of that paddle, bounce the ball downwards and left
            if(ball.getCenter().getY() <= paddleTwo.getP2().getY()
               and ball.getCenter().getY() >= paddleTwo.getP2().getY() - 15):
                directionX = -5
                directionY = 5
            #If the ball hits the top of that paddle, bounce the ball upwards and left
            elif(ball.getCenter().getY() >= paddleTwo.getP1().getY()
                 and ball.getCenter().getY() <= paddleTwo.getP1().getY() + 15):
                directionX = -5
                directionY = -5
            #If the ball hits the paddle regularly, change its x direction to be the opposite
            else:
                directionX *= -1
        #Slows down the ball and paddles
        time.sleep (0.01)
        #Key binds for the movement of the paddles
        if(key == "w"):
            if(paddleOne.getP1().getY() > 90): 
                paddleOne.move(0,-40)
        elif(key == "s"):
            if(paddleOne.getP2().getY() <= 485):
                paddleOne.move(0,40)
        elif(key == "Up"):
            if(paddleTwo.getP1().getY() > 90):
                paddleTwo.move(0,-40)
        elif(key == "Down"):
            if(paddleTwo.getP2().getY() <= 485):
                paddleTwo.move(0,40)
        #If the user clicks the exit button, undraw everything and go back to the home screen
        else:
            mouse = win.checkMouse()
            if(mouse != None):
                if(mouse.getX() >= 325 and mouse.getX() <= 473
                and mouse.getY() >= 4 and mouse.getY() <= 53):
                    reset = False
                    scoreP1 = 0
                    scoreP2 = 0
                    border.undraw()
                    exitMultiPlayer.undraw()
                    playerOneScore.undraw()
                    playerTwoScore.undraw()
                    paddleOne.undraw()
                    paddleTwo.undraw()
                    ball.undraw()
                    homeScreen()
  
#Main code(start at home screen)
homeScreen()
    
    
