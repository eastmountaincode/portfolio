'''
    Andrew Boylan
    Fall 2021

    A class for the load button.

'''

import os
from os import listdir
from os.path import isfile, join
import random
from CodeResources.tileClass import Tile
import math
import CodeResources.helper_functions as helper
from CodeResources.resetClass import resetButton

class loadButton:
    '''
    Loads new puzzles.

    Attributes:
        obj_ledger (list): a list containing Tile objects
        pos_ledger (dict): a dictionary with information about the
            position of each Tile
        x_init (int): the x-coordinate for the load button to be
            created at
        y_init (int): the y-coordinate for the load button to be created
            at
        screen (Screen): a turtle Screen object
        playerName (str): the name of the play for use in writing
            to the leaderboard upon win
        moveRecord (moveRecord): a moveRecord object containing
            turtles that write the number of moves that have been
            taken and then number of moves that remain
        resetButton (resetButton): a resetButton object that rearranges
            the Tiles so that the puzzle becomes solved
        thumbnail (Turtle): a Turtle object that displays the
            thumbnail
        isLeaderboard (bool): a boolean that indicates whether the
            program was able to load a leaderboard or not.
        turtle1 (Turtle): A turtle object to handle clicks and display
            the load button image.
        
    '''
    def __init__(self,
                 obj_ledger,
                 pos_ledger,
                 x_init,
                 y_init,
                 screen,
                 playerName,
                 moveRecord,
                 resetButton,
                 thumbnail,
                 isLeaderboard):
        '''
            Initialize the loadButton object.

            Parameters:
                obj_ledger (list): a list containing Tile objects
                pos_ledger (dict): a dictionary with information about the
                    position of each Tile
                x_init (int): the x-coordinate for the load button to be
                    created at
                y_init (int): the y-coordinate for the load button to be
                    created at
                screen (Screen): a turtle Screen object
                playerName (str): the name of the play for use in writing
                    to the leaderboard upon win
                moveRecord (moveRecord): a moveRecord object containing
                    turtles that write the number of moves that have been
                    taken and then number of moves that remain
                resetButton (resetButton): a resetButton object that
                    rearranges the Tiles so that the puzzle becomes solved
                thumbnail (Turtle): a Turtle object that displays the
                    thumbnail
                isLeaderboard (bool): a boolean that indicates whether the
                    program was able to load a leaderboard or not.
            
        '''
        self.obj_ledger = obj_ledger
        self.pos_ledger = pos_ledger
        self.screen = screen
        self.playerName = playerName
        self.moveRecord = moveRecord
        self.resetButton = resetButton
        self.thumbnail = thumbnail
        self.isLeaderboard = isLeaderboard
        
        self.turtle1 = helper.createTurtle("turtle1", x_init, y_init)

        img_path = 'Resources/loadbutton.gif'
        self.screen.register_shape(img_path)
        self.turtle1.shape(img_path)
        self.turtle1.showturtle()

    def click_handler(self, x, y):
        '''
            Function that runs when the loadButton is clicked on. Loads
            a new puzzle.

            Parameters:
                x, y (int): these get passed in when .onclick() is called
                but we dont need them.
            Returns:
                None
                
        '''
        # Get all the puzzle options to display in the textbox
        # Get the user's choice and check if it is valid
        puzzleFolder = f"{os.path.abspath(os.getcwd())}/Puzzles"
        onlyPuzFiles = [f for f in listdir(puzzleFolder) if\
                     isfile(os.path.join(puzzleFolder, f)) and\
                        f[-3::] == "puz"]
        
        # If there are more than 10 puzzle files, show a warning
        if len(onlyPuzFiles) > 10:
            helper.showFileWarning(self.screen)
        onlyPuzFiles = onlyPuzFiles[:10]
        
        # Get a string with puzzle options to display to the user
        puzzleOptions = "" 
        for i in range(len(onlyPuzFiles)):
            if i < (len(onlyPuzFiles) - 1):
                puzzleOptions += f"{onlyPuzFiles[i]}\n"
            else:
                puzzleOptions += f"{onlyPuzFiles[i]}"

        # Get puzzle choice from user. Show an error if the user
        # chooses a puzzle that isn't available. 
        puzzleChoice = self.screen.textinput("Which puzzle?",
                                    "Choose a puzzle to load:\n\n" + \
                                    puzzleOptions)
        if puzzleChoice is not None:
            puzzleChoice = puzzleChoice.lower()
        validChoice = False
        while validChoice == False:
            if puzzleChoice in onlyPuzFiles:
                validChoice = True
                break
            if puzzleChoice is None:
                break
            else:
                helper.showCorruptedError(self.screen)
                helper.writeError("Puzzle choice not found.",
                                  "loadClass.py")
                break
      
        # If the user's choice is valid...
        if validChoice:
            
            # FIRST, USE PUZZLEINFO TO MAKE SURE THE PUZ FILE
            # ISN"T CORRUPTED
            txt_path = f'{os.getcwd()}/Puzzles/{puzzleChoice}'
            with open(txt_path, mode='r') as f:
                lines = f.read().split('\n')
            puzzleInfo = helper.makePuzzleInfoDict(lines)

            # Use corrupted sentinel value, that we can tell if
            # the puzzle has multiple types of corruption, and if so,
            # what they are.
            corrupted = False
            # CHECK IF NUM PIECES IS CORRECT
            if puzzleInfo['numPieces'] not in [4, 9, 16]:
                helper.writeError("Puzzle corrupted. Incorrect number"+
                                  " of pieces",
                                  "loadClass.py")
                corrupted = True
            
            # TRY TO LOAD THE THUMBNAIL
            arg = puzzleInfo['thumbnail']
            img_path = f"{os.getcwd()}/{arg}"
            try:
                self.screen.register_shape(img_path)
            except:
                helper.writeError("Puzzle corrupted. Unable to get"+
                                  " thumbnail from the specified location",
                                  "loadClass.py")
                corrupted = True

            # TRY TO LOAD EACH TILE
            for i in range(len(puzzleInfo['img_loc_list'])):
                arg = puzzleInfo['img_loc_list'][i]
                img_path = f"{os.getcwd()}/{arg}"
                try:
                    self.screen.register_shape(img_path)
                except:
                    helper.writeError("Puzzle corrupted. Unable to get"+
                                      " tile from the specified location",
                                      "loadClass.py")
                    corrupted = True
                    break

            # CHECK FOR CORRECT PUZZLE SIZE
            pieceSize = puzzleInfo['pieceSize']
            if pieceSize < 50 or pieceSize > 110:
                helper.writeError("Puzzle corrupted. The piece size is"+
                                  "either too big or too small",
                                  "loadClass.py")
                corrupted = True

            if corrupted == True:
                helper.showCorruptedError(self.screen)
                return

            # If we've gotten to this point, we can assume that the
            # puzzle file is not corrupted.

            # CHANGE THE THUMBNAIL
            arg = puzzleInfo['thumbnail']
            img_path = f"{os.getcwd()}/{arg}"
            self.screen.register_shape(img_path)
            self.thumbnail.clear()
            self.thumbnail.shape(img_path)
     
            # Hide all the existing Tiles
            for i in range(len(self.obj_ledger)):
                self.obj_ledger[i].turtle1.hideturtle()

            indexChoices = [x for x in range(puzzleInfo['numPieces'])]
            init_x = -448 + 30 + (puzzleInfo['pieceSize']/2)
            init_y = 397 - 30 - (puzzleInfo['pieceSize']/2)
            self.pos_ledger = {}
            self.obj_ledger = []
            padding_x = 0
            padding_y = 0
            margin = 5
           
            # THIS IS THE FOR LOOP THAT INITIALIZES THE TILE OBJECTS
            for i in range(puzzleInfo['numPieces']):
                randIndex = random.choices(indexChoices)
                indexChoices.remove(randIndex[0])
                # If randIndex is the biggest number, then
                # it's the blankTile
                if randIndex[0] == (puzzleInfo['numPieces'] - 1):
                    blankTileIndex = i
                self.obj_ledger.append(Tile(puzzleInfo['pieceSize'],
                             randIndex[0],
                             puzzleInfo['numPieces'],
                             puzzleInfo['img_loc_list'][randIndex[0]],
                             init_x + padding_x,
                             init_y + padding_y,
                             self.screen,
                             self.obj_ledger,
                             margin,
                             self.pos_ledger,
                             self.moveRecord,
                             self.playerName,
                             self.isLeaderboard))
                
                # SETTING ONCLICK EVENT
                arg = self.obj_ledger[i].click_handler
                self.obj_ledger[i].turtle1.onclick(arg)

                # INCREMENTING THE PADDING
                if not (i+1) % math.sqrt(puzzleInfo['numPieces']):
                    padding_x = 0
                    padding_y -= (puzzleInfo['pieceSize'] + margin)
                else:
                    padding_x += (puzzleInfo['pieceSize'] + margin)

            # SETTING THE BLANKTILE ATTRIBUTE FOR EACH TILE
            for i in range(puzzleInfo['numPieces']):
                self.obj_ledger[i].setBlankTileObjLedgerIndex(blankTileIndex)

            # DRAW A NEW RESET BUTTON
            self.resetButton = resetButton(self.obj_ledger,
                                           self.pos_ledger,
                                          125,
                                          -330,
                                          self.screen)       

            # SET THE RESET BUTTON ATTRIBUTE FOR EACH TILE
            for i in range(len(self.obj_ledger)):
                # Give each tile in the obj_ledger a reset button
                self.obj_ledger[i].setResetButton(self.resetButton)
                arg = self.obj_ledger[i].resetButton.click_handler
                self.obj_ledger[i].resetButton.turtle1.onclick(arg)

            # CREATING THE POS_LEDGER V2
            for i in range(puzzleInfo['numPieces']):
                tileID = self.obj_ledger[i].ID
                floorID = i
                self.pos_ledger[tileID] = floorID

            # REDRAW THE MOVE RECORD
            self.moveRecord.numMovesTotal = 0
            arg = self.moveRecord.numMovesRemainingPerm
            self.moveRecord.numMovesRemaining = arg
            self.moveRecord.moveTotal.clear()
            self.moveRecord.moveTotal.write("Player moves so far: "+
                                            f"{self.moveRecord.numMovesTotal}",
                                            font=(self.moveRecord.fontStyle,
                                                  self.moveRecord.fontSize,
                                                  "bold"))
            self.moveRecord.moveRemaining.clear()
            self.moveRecord.moveRemaining.write(f"Player moves remaining: "
                            f"{self.moveRecord.numMovesRemaining}",
                          font=(self.moveRecord.fontStyle,
                                self.moveRecord.fontSize, "bold"))
