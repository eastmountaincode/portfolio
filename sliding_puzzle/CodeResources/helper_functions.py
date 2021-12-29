'''
    Andrew Boylan
    Fall 2021

    Various functions for use in other files.

'''
import turtle
import time
import os
import math
from os import listdir
from os.path import isfile, join
from datetime import datetime

def createTurtle(turtleName,
                 xpos = 0,
                 ypos = 0):
    '''
    A quick way to create a turtle object, especially when the
    turtle will be used to display an image.

    Parameters:
        turtleName (str): The name of the turtle. This gets passed into
            the createTurtle() function, but then a turtle object gets
            associated with this string.
            xpos (int): X-coordinate where turtle should be created.
                Default is 0.
            ypos (int): Y-coordinate where turtle should be created.
                Default is 0.
    Returns:
        turtleName (Turtle): Returns a Turtle object.
    
    '''
    turtleName = turtle.Turtle()
    turtleName.hideturtle()
    turtleName.penup()
    turtleName.speed(0)
    turtleName.setpos(xpos, ypos)
    
    return turtleName

def showCorruptedError(screen):
    '''
        Display an error message for a corrupted puzzle.

        Parameters:
            screen (Screen): a turtle Screen object
        Returns:
            None
    '''
    errorMsg = turtle.Turtle()
    img_path = f"{os.getcwd()}/Resources/file_error.gif"
    screen.register_shape(img_path)
    errorMsg.shape(img_path)
    time.sleep(2)
    errorMsg.hideturtle()

def showLeaderboardError(screen):
    '''
        Display an error message for a non-existant leaderboard.

        Parameters:
            screen (Screen): a turtle Screen object
        Returns:
            None
    '''
    errorMsg = turtle.Turtle()
    img_path = f"{os.getcwd()}/Resources/leaderboard_error.gif"
    screen.register_shape(img_path)
    errorMsg.shape(img_path)
    time.sleep(2)
    errorMsg.hideturtle()

def showFileWarning(screen):
    '''
        Display an error message for an excess of .puz files in the
        root directory.

        Parameters:
            screen (Screen): a turtle Screen object
        Returns:
            None
    '''
    errorMsg = turtle.Turtle()
    img_path = f"{os.getcwd()}/Resources/file_warning.gif"
    screen.register_shape(img_path)
    errorMsg.shape(img_path)
    time.sleep(2)
    errorMsg.hideturtle()

def writeError(errorType, location):
    '''
        A function for logging erros into 5001_puzzle.err, which is
        located in the root directory.

        Parameters:
            errorType (str): A description of the error
            location (str): The location of the file where the error
                occured
        Returns:
            None
            
    '''
    txt_path = f"{os.getcwd()}/5001_puzzle.err"
    with open(txt_path, mode = 'r') as f:
        lines = f.readlines()
    now = datetime.now()
    dt_str = now.strftime("%d/%m/%Y %H:%M:%S")
    entry = f"{dt_str}, ERROR: {errorType}, LOCATION: {location}"
    with open(txt_path, mode = "a") as f:
        f.write(entry + "\n")
    with open(txt_path, mode = 'r') as f:
        lines = f.readlines()     
      
def makePuzzleInfoDict(lines):
    '''
        A function for creating a dictionary filed with information
        about a given .puz file. Reads in the information of a .puz
        file as a list of lines.

        Parameters:
            lines (list): A list containing information from a .puz file
        Returns:
            puzzleInfo (dict): A dictionary with information about a .puz
                file.
                
    '''
    puzzleInfo = {}
    puzzleInfo['name'] = lines[0].replace('name: ', "")
    puzzleInfo['numPieces'] = int(lines[1].replace('number: ', ""))
    puzzleInfo['pieceSize'] = int(lines[2].replace('size: ', ""))
    puzzleInfo['img_loc_list'] = [x.split(':')[1].strip() for x in\
                                  lines[4:] if x]
    puzzleInfo['thumbnail'] = lines[3].replace('thumbnail: ', '')
    return puzzleInfo

def quit(x=0, y=0):
    '''
        Quit the program.

        Parameters:
            x, y (int): x and y are dummy variables. .onclick(quit)
            automatically passes in the coordinates of the mouse
            when the click occured, but we don't need them.
        Returns:
            None
    '''
    wn = turtle.Screen()
    quitNotif = createTurtle('quitNotif')
    quitChoice = wn.textinput("Are you sure you want to quit?",
                              "Enter Y or Yes to quit.")
    if quitChoice is not None:
        quitChoice = quitChoice.lower()
    if quitChoice == 'y' or quitChoice == 'yes':
        img_path = f"{os.getcwd()}/Resources/quitmsg.gif"
        wn.register_shape(img_path)
        quitNotif.shape(img_path)
        quitNotif.showturtle()
        time.sleep(3)
        turtle.clearscreen()
        turtle.bye()

def checkBlankRight(clickedTile, blankTile, totalPieces):
    '''
        Check if the blank tile is to the right of the clicked tile

        Parameters:
            clickedTile (int): floorID of the clicked tile
            blankTile (int): floorID of the blank tile
            totalPiece (int): total # of pieces in the puzzle

        Returns:
            equalityCheckRight (bool): returns True if the floorID
                of the blank tile is one greater than the floorID
                of the clicked tile
            edgeCheckRight (bool): returns True if the clicked tile
                is not in one of the locations on the grid that if
                it moved to the right, it would go outside the grid.
        
    '''
    equalityCheckRight = False
    edgeCheckRight = False
    if clickedTile + 1 == blankTile:
        equalityCheckRight = True
    
        sqrtOfPuzz = int(math.sqrt(totalPieces))
        invalid = [(sqrtOfPuzz * i) - 1 for i in range(1, sqrtOfPuzz)]
        if clickedTile not in invalid:
            edgeCheckRight = True
    return equalityCheckRight, edgeCheckRight

def checkBlankLeft(clickedTile, blankTile, totalPieces):
    '''
        Check if the blank tile is to the left of the clicked tile

        Parameters:
            clickedTile (int): floorID of the clicked tile
            blankTile (int): floorID of the blank tile
            totalPiece (int): total # of pieces in the puzzle

        Returns:
            equalityCheckLeft (bool): returns True if the floorID
                of the blank tile is one less than the floorID
                of the clicked tile
            edgeCheckLeft (bool): returns True if the clicked tile
                is not in one of the locations on the grid that if
                it moved to the left, it would go outside the grid.
        
    '''
    equalityCheckLeft = False
    edgeCheckLeft = False
    if clickedTile - 1 == blankTile:
        equalityCheckLeft = True
    
        sqrtOfPuzz = int(math.sqrt(totalPieces))
        invalid = [(sqrtOfPuzz * i) for i in range(1, sqrtOfPuzz)]
        if clickedTile not in invalid:
            edgeCheckLeft = True
    return equalityCheckLeft, edgeCheckLeft

def checkBlankUp(clickedTile, blankTile, totalPieces):
    '''
        Check if the blank tile is above the clicked tile.

        Parameters:
            clickedTile (int): floorID of the clicked tile
            blankTile (int): floorID of the blank tile
            totalPiece (int): total # of pieces in the puzzle

        Returns:
            equalityCheckUp (bool): returns True if the floorID of the
                blank tile is 4 greater than the floorID of the clicked
                tile
    '''
    equalityCheckUp = False
    sqrtOfPuzz = int(math.sqrt(totalPieces))
    if clickedTile - sqrtOfPuzz == blankTile:
        equalityCheckUp = True
    return equalityCheckUp

def checkBlankDown(clickedTile, blankTile, totalPieces):
    '''
        Check if the blank tile is below the clicked tile.

        Parameters:
            clickedTile (int): floorID of the clicked tile
            blankTile (int): floorID of the blank tile
            totalPiece (int): total # of pieces in the puzzle

        Returns:
            equalityCheckDown (bool): returns True if the floorID of the
                blank tile is 4 less than the floorID of the clicked
                tile
    '''
    equalityCheckDown = False
    sqrtOfPuzz = int(math.sqrt(totalPieces))
    if clickedTile + sqrtOfPuzz == blankTile:
        equalityCheckDown = True
    return equalityCheckDown

def checkWin(posLedger):
    '''
        Check for win condition

        Parameters:
            posLedger (dict): a dictionary recording where each
                tile is on the grid. Keys are tileIDs, and values
                are floorIDs.
        Returns:
            (bool) True if every tileID has a floorID with a value equal to
                its tileID. For example, in a solved puzzle, the top
                left tile has a tileID of 0 and a floorID of 0.

    '''
    for k, v in posLedger.items():
        if k != v:
            return False
    return True
