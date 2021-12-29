'''
    Andrew Boylan
    Fall 2021

    Setup functions for displaying splash screen, drawing the play area,
    the status area, and the leaderboard.
    
'''

import turtle
import time
import CodeResources.helper_functions as helper
from CodeResources.tileClass import Tile
import random
import math
from CodeResources.resetClass import resetButton
from CodeResources.movesRecordClass import moveRecord
from CodeResources.loadClass import loadButton

def displaySplashScreen():
    '''
        Display the splash screen upon startup.

        Parameters:
            None
        Returns:
            screen (Screen): turtle Screen object            
    '''
    # Create turtle and screen objects
    splash = turtle.Turtle()
    screen = turtle.Screen()
    
    # Set the window size
    screen.setup(1000, 900)

    # Get the path of the background image relative to the script
    img_path = "Resources/background.gif"
    # Set the background
    turtle.bgpic(img_path)

    # Get the path of the splash screen relative to the script
    img_path = 'Resources/splash_screen.gif'
    screen.register_shape(img_path)
    # Display the image...
    splash.shape(img_path)
    
    # ...for 2 seconds
    time.sleep(0.5)
    
    # Remove the image
    splash.hideturtle()

    return screen

def getNameAndMoves(screen):
    '''
        Get the player's name and the number of moves they would like
        to give themself before they lose the game. More moves means an
        easier game.

        Parameters:
            screen (Screen): the turtle Screen
        Returns:
            playerName (str): the player's name
            numMoves (int): the number of moves the player can make before
                they lose the game. specifed by the user.
        
    '''
    # Get the player's name. Keep asking until they enter a valid name.
    playerName = ""
    while playerName is None or playerName == "":
        playerName = screen.textinput("Welcome to puzzle game!",
                              "What is your name?")

    # Get the player's desired number of moves
    # Input must be numeric, between 5 and 200, and can't be
    # blank or None.
    numMoves = 0
    validNumMoves = False
    while validNumMoves is False:
        numMoves = screen.textinput("How many moves would you like?",
                                    "You must choose a number between"+
                                    " 5 and 200 moves.")
        if not numMoves.isnumeric() or numMoves is None\
           or numMoves == "":
            continue
        numMoves = int(numMoves)
        if numMoves < 5 or numMoves > 200:
            continue
        validNumMoves = True
    # truncate the player's name
    playerName = playerName[:13]
      
    return playerName, numMoves
    
def drawUIFrame(screen):
    '''
        Draws the borders for the button area, the leaderboard area and
        the gameboard area

        Parameters:
            screen (Screen): the turtle Screen
        Returns:
            None
            
    '''

    UIartist = helper.createTurtle("UIartist", -450, -400)
    # Draw button area 
    UIartist.pensize(5)
    UIartist.pendown()
    UIartist.fillcolor('#cccccc')
    UIartist.pencolor('#265389')
    UIartist.begin_fill()
    UIartist.setpos(450, -400)
    UIartist.setpos(450, -260)
    UIartist.setpos(-450, -260)
    UIartist.setpos(-450, -400)
    UIartist.end_fill()
    UIartist.penup()

    # Draw leaderboard area
    UIartist.setpos(190, -210)
    UIartist.pendown()
    UIartist.fillcolor('#cccccc')
    UIartist.pencolor('#265389')
    UIartist.begin_fill()
    UIartist.setpos(450, -210)
    UIartist.setpos(450, 400)
    UIartist.setpos(190, 400)
    UIartist.setpos(190, -210)
    UIartist.end_fill()
    UIartist.penup()

    # Draw puzzle area
    UIartist.setpos(-450, -210)
    UIartist.pendown()
    UIartist.fillcolor('#cccccc')
    UIartist.pencolor('#265389')
    UIartist.begin_fill()
    UIartist.setpos(-450, 400)
    UIartist.setpos(170, 400)
    UIartist.setpos(170, -210)
    UIartist.setpos(-450, -210)
    UIartist.end_fill()
    UIartist.penup()

def drawQuitButton(screen):
    '''
        Create the quit button

        Parameters:
            screen (Screen): the turtle Screen
        Returns:
            quitButton (Turtle): the quit button as a Turtle
        
    '''

    # Draw quit button. On click it runs helper.quit()
    quitButton = helper.createTurtle('quitButton', 375, -330)
    img_path = 'Resources/quitbutton.gif'
    screen.register_shape(img_path)
    quitButton.shape(img_path)
    quitButton.showturtle()
    quitButton.onclick(helper.quit)

    return quitButton

def drawLoadButton(obj_ledger,
                   pos_ledger,
                   screen,
                   playerName,
                   moveRecord,
                   resetButton,
                   thumbnail,
                   isLeaderboard):
    '''
        Create the load button

        Parameters:
            obj_ledger (list): a list containing Tile objects
            pos_ledger (dict): keeps track of the positon of the Tile
                objects on the grid
            screen (Screen): the turtle Screen
            playerName (str): the player's name
            moveRecord (moveRecord): the moveRecord object to write the
                moves made and moves remaining
            resetButton (resetButton): the reset button
            thumbnail (Turtle): the thumbnail of the puzzle
            isLeaderboard (bool): indicates whether or not a leaderboard
                was able to be loaded
    
        Returns:
            loadButton1 (loadButton): the load button as a loadButton
                object
        
    '''
    loadButton1 = loadButton(obj_ledger,
                              pos_ledger,
                              250,
                              -330,
                              screen,
                              playerName,
                              moveRecord,
                             resetButton,
                             thumbnail,
                             isLeaderboard)
    loadButton1.turtle1.onclick(loadButton1.click_handler)
    return loadButton1
    
def drawMoveWritersInit(numMoves):
    '''
        Creates the moveRecord object to keep track of number of moves
        made so far and number of moves remaining and display this
        information to the user

        Parameters:
            numMoves (int): the number of moves the player can make before
                the loss condition is met. defined by the user in another
                function.
        Returns:
            movesRecord1 (moveRecord): the moveRecord object
    '''
    movesRecord1 = moveRecord(numMoves,
                               "LiSong Pro",
                               25)
    return movesRecord1

def drawLeaderBoardInit(screen):
    '''
        Draws the leaderboard based on what is written in the
        leaderboard.txt file

        Parameters:
            screen (Screen): the turtle Screen object
        Returns:
            leaderboard (Turtle): the leaderboard as a Turtle
            isLeaderboard (bool): indicates whether the program was
                able to successfully load the leaderboard from
                leaderboard.txt
                
    '''
    isLeaderboard = True
    # CHECK IF LEADERBOARD CAN BE LOADED
    try:
        with open('leaderboard.txt', mode='r') as f:
            lines = f.read().split('\n')
    except:
        helper.showLeaderboardError(screen)
        helper.writeError("Leaderboard not found.",
                          "setup.py")
        isLeaderboard = False

    # We still need to create a leaderboard Turtle even if we weren't
    # able to load the leaderboard, because other functions need it,
    # so just create a turtle but don't write anything to the screen
    if isLeaderboard == False:
        leaderboard = helper.createTurtle('leaderboard', 212, 107)
        return leaderboard, isLeaderboard

    # If load was successful, then we write out the top 7 scores
    lines = [x.split(',') for x in lines]
    for i in range(len(lines)):
        lines[i][0] = lines[i][0].strip()
        lines[i][1] = lines[i][1].strip()
    leaderboard = helper.createTurtle('leaderboard', 212, 107)
    leaderboard_header = "Leaderboard (Top 7):\n\n"
    leaderboard.write(leaderboard_header, font=("LiSong Pro", 23),
                      align = "left")
    leaderboard.setpos(212, leaderboard.ycor() - 45)
    counter = 0
    for each in lines:
        # Get correct grammar for "moves"
        if each[0] == '1':
            entry = f"{each[1]}: {each[0]} move\n\n"
        else: 
            entry = f"{each[1]}: {each[0]} moves\n\n"
        leaderboard.write(entry, font=("LiSong Pro", 20),
                          align = "left")
        counter += 1
        if counter >= 7:
            break
        leaderboard.setpos(212, leaderboard.ycor() - 45)

    return leaderboard, isLeaderboard

def drawPuzzleThumbnailInit(screen):
    '''
        Draw the initial puzzle thumbnail

        Parameters:
            screen (Screen): the turtle Screen object
        Returns:
            thumbnail (Turtle): the thumbnail as a Turtle object
            
    '''
    img_path = 'Images/mario/mario_thumbnail.gif'
    screen.register_shape(img_path)
    thumbnail = helper.createTurtle('thumbnail', 318, 286)
    thumbnail.shape(img_path)
    thumbnail.showturtle()

    return thumbnail

def drawPuzzleInit(screen, moveRecord, playerName, isLeaderboard):
    '''
        Draw the initial puzzle, which is always mario.puz

        Parameters:
            screen (Screen): the turtle Screen object
            moveRecord (moveRecord): the moveRecord object
            playerName (str): the player's name
            isLeaderboard (bool): indicates whether or not the leaderboard
                was able to be loaded
        Returns:
            obj_ledger (list): a list containing Tile objects
            pos_ledger (dict): a dictionary that keeps track of the position
                of all the Tile objects on the grid
                
    '''
    txt_path = 'Puzzles/mario.puz'                        
    with open(txt_path, mode='r') as f:
        lines = f.read().split('\n')
    puzzleInfo = helper.makePuzzleInfoDict(lines)
    indexChoices = [x for x in range(puzzleInfo['numPieces'])]
    obj_ledger = []
    init_x = -448 + 30 + (puzzleInfo['pieceSize']/2)
    init_y = 397 - 30 - (puzzleInfo['pieceSize']/2)
    padding_x = 0
    padding_y = 0
    margin = 5
    pos_ledger = {}

    # THIS IS THE FOR LOOP THAT INITIALIZES THE TILE OBJECTS
    for i in range(puzzleInfo['numPieces']):
        randIndex = random.choices(indexChoices)
        indexChoices.remove(randIndex[0])
        # If randIndex is the biggest number, then it's the blankTile
        if randIndex[0] == (puzzleInfo['numPieces'] - 1):
            blankTileIndex = i
        obj_ledger.append(Tile(puzzleInfo['pieceSize'],
                     randIndex[0],
                     puzzleInfo['numPieces'],
                     puzzleInfo['img_loc_list'][randIndex[0]],
                     init_x + padding_x,
                     init_y + padding_y,
                     screen,
                     obj_ledger,
                     margin,
                     pos_ledger,
                     moveRecord,
                     playerName,
                     isLeaderboard))
        
        # SETTING ONCLICK EVENT
        obj_ledger[i].turtle1.onclick(obj_ledger[i].click_handler)

        # INCREMENTING THE PADDING
        if not (i+1) % math.sqrt(puzzleInfo['numPieces']):
            padding_x = 0
            padding_y -= (puzzleInfo['pieceSize'] + margin)
        else:
            padding_x += (puzzleInfo['pieceSize'] + margin)

    # SETTING THE BLANKTILE ATTRIBUTE FOR EACH TILE
    for i in range(puzzleInfo['numPieces']):
        obj_ledger[i].setBlankTileObjLedgerIndex(blankTileIndex)

    # CREATING THE POS_LEDGER V2
    for i in range(puzzleInfo['numPieces']):
        tileID = obj_ledger[i].ID
        floorID = i
        pos_ledger[tileID] = floorID

    return obj_ledger, pos_ledger

def drawResetButton(screen, obj_ledger, pos_ledger):
    '''
        Draw the reset button.

        Parameters:
            screen (Screen): the turtle Screen object
            obj_ledger (list): a list containing Tile objects
            pos_ledger (dict): a dictionary that keeps track of the position
                of all the Tile objects on the grid
        Returns:
            resetButton1 (resetButton): the reset button as a resetButton
                object
            
    '''
    resetButton1 = resetButton(obj_ledger,
                              pos_ledger,
                              125,
                              -330,
                              screen)
    resetButton1.turtle1.onclick(resetButton1.click_handler)
    for i in range(len(obj_ledger)):
        obj_ledger[i].setResetButton(resetButton1)
    return resetButton1
      
def run_setup():
    '''
        The main driver for setup.py

        Parameters:
            None
        Returns:
            None
    '''
    # Display splash screen
    screen = displaySplashScreen()

    # Get player name and number of moves allow for the game
    playerName, numMoves = getNameAndMoves(screen)

    # Draw the background boxes
    drawUIFrame(screen)

    # Create the quit button
    quitButton = drawQuitButton(screen)

    # Create the moveRecord, draw the leaderboard, thumbnail, puzzle,
    # reset button, and load button
    moveRecord1 = drawMoveWritersInit(numMoves)
    leaderboard, isLeaderboard = drawLeaderBoardInit(screen)
    thumbnail = drawPuzzleThumbnailInit(screen)
    obj_ledger, pos_ledger = drawPuzzleInit(screen,
                                            moveRecord1,
                                            playerName,
                                            isLeaderboard)
    resetButton = drawResetButton(screen, obj_ledger, pos_ledger)
    loadButton = drawLoadButton(obj_ledger,
                                pos_ledger,
                                screen,
                                playerName,
                                moveRecord1,
                                resetButton,
                                thumbnail,
                                isLeaderboard)   
    
