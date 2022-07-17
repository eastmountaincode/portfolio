'''
    Andrew Boylan
    Fall 2021

    Class for Tile objects, which are the individual puzzle pieces.
'''
import turtle
import os
import CodeResources.helper_functions as helper
import math
import time

class Tile:
    '''
        The individual game tile. The most important object in the game.
        Much of the game's behavior is stored in the Tiles.

        Attributes:
            size (int): the size of the puzzle piece.
            ID (int): the Tile's tileID. In a solved puzzle, the tileIDs
                are sequential from top left, over to the end of the row,
                then starting at the left end of the next row below, and so
                on
            totalPieces (int): the total number of pieces in the given puzzle
            turtle1 (Turtle): the Tile's turtle, used for moving and handling
                click events
            margin (int): distance between Tiles
            image_loc (str): the location of the image for a given Tile
            screen (Screen): the turtle Screen object
            obj_ledger (list): a list containing Tile objects
            blankTile (Tile): the blank tile. every Tile needs this because
                if a valid click is made, the clicked Tile needs to move
                AND the blank tile needs to move
            pos_ledger (dict): a dictionary with information about the
                position of each Tile
            resetButton (resetButton): the reset button
            moveRecord (moveRecord): the moveRecord object
            playerName (str): the player's name
            isLeaderboard (bool): indicates whether or not the leaderboard
                was able to be loaded


    '''
    def __init__(self,
                 size,
                 ID,
                 totalPieces,
                 image_loc,
                 xpos_init,
                 ypos_init,
                 screen,
                 obj_ledger,
                 margin,
                 pos_ledger,
                 moveRecord,
                 playerName,
                 isLeaderboard,
                 blankTile = None,
                 resetButton = None):
        '''
        Initialize the Tile
        
        Parameters:
            size (int): size of the image
            ID (int): Tile's tileID
            totalPieces (int): number of pieces in the puzzle
            image_loc (str): location of the image for the Tile
            xpos_init (int): initial x-coordinate
            ypos_init (int): initial y-coordinate
            screen (Screen): the turtle Screen object
            obj_ledger (list): a list containing Tile objects
            margin (int): distance between Tiles
            pos_ledger (dict): a dictionary with information about the
                position of each Tile
            moveRecord (moveRecord): the moveRecord object
            playerName (str): the player's name
            isLeaderboard (bool): indicates whether or not the leaderboard
                was able to be loaded
            blankTile (Tile): the blank tile. every Tile needs this because
                if a valid click is made, the clicked Tile needs to move
                AND the blank tile needs to move
            resetButton (resetButton): the reset button
        '''
        self.size = size
        self.ID = ID
        self.totalPieces = totalPieces
        self.turtle1 = turtle.Turtle()
        self.turtle1.hideturtle()
        self.turtle1.speed(0)
        self.turtle1.penup()
        self.turtle1.setpos(xpos_init, ypos_init)
        self.margin = margin
        self.image_loc = image_loc
        self.screen = screen
        self.obj_ledger = obj_ledger
        self.screen.register_shape(f'./{image_loc}')
        self.turtle1.shape(f'./{image_loc}')
        self.turtle1.showturtle()
        self.blankTile = blankTile
        self.pos_ledger = pos_ledger
        self.resetButton = resetButton
        self.moveRecord = moveRecord
        self.playerName = playerName
        self.isLeaderboard = isLeaderboard     

    def moveUp(self):
        '''
            Move the tile up

            Parameters:
                None
            Returns:
                None
                
        '''
        self.turtle1.speed(4)
        self.turtle1.setpos(self.turtle1.xcor(),
                           self.turtle1.ycor() + self.size + self.margin)
        self.turtle1.speed(0)
    def moveDown(self):
        '''
            Move the tile down

            Parameters:
                None
            Returns:
                None
                
        '''
        self.turtle1.speed(4)
        self.turtle1.setpos(self.turtle1.xcor(),
                           self.turtle1.ycor() - self.size - self.margin)
        self.turtle1.speed(0)
    def moveRight(self):
        '''
            Move the tile right

            Parameters:
                None
            Returns:
                None
                
        '''
        self.turtle1.speed(4)
        self.turtle1.setpos(self.turtle1.xcor() + self.size + self.margin,
                           self.turtle1.ycor())
        self.turtle1.speed(0)
    def moveLeft(self):
        '''
            Move the tile left

            Parameters:
                None
            Returns:
                None
                
        '''
        self.turtle1.speed(4)
        self.turtle1.setpos(self.turtle1.xcor() - self.size - self.margin,
                           self.turtle1.ycor())
        self.turtle1.speed(0)

    def doNothing(self, x, y):
        '''
            Dummy function. Does nothing
            
            Parameters:
                x, y (int): x and y are dummy variables. .onclick(quit)
                automatically passes in the coordinates of the mouse
                when the click occured, but we don't need them.
            Returns:
                None
                
        '''
        pi = 3.14
        
    def click_handler(self, x, y):
        '''
            The main game behavior. Function runs whenever a Tile is clicked.

            Parameters:
                None
            Returns:
                None
                
        '''
        movement = False
        btIndex = self.totalPieces - 1
        vert = int(math.sqrt(self.totalPieces))

        # Prevent other click events from happening
        # while the current one is still being processed.
        # click_handler off.
        for i in range(len(self.obj_ledger)):
            self.obj_ledger[i].turtle1.onclick(self.doNothing)

        clickedFloorID = self.pos_ledger[self.ID]
        blankFloorID = self.pos_ledger[self.totalPieces - 1]

        # CASE: BLANK TILE IS ON THE RIGHT
        # Check if blank tile is on the right
        # If the floorID of the tile we clicked is to the
        # right of the blank tile's floorID AND
        # the tiles floorID is not on the rightmost edge...

        # Check blank neighborness AND edge validity      
        eqCheckR, edgeCheckR = helper.checkBlankRight(clickedFloorID,
                               blankFloorID,
                               self.totalPieces)
        
        if eqCheckR and edgeCheckR:
            # Then move to the right.
            # ACTUAL MOVEMENT
            self.moveRight()
            self.obj_ledger[self.blankTile].moveLeft()

            # update the position ledger
            # The key of pos_ledger is the tileID
            # The value of pos_ledger is the floorID
            # The tile we clicked moves left, so floorID is plus 1
            self.pos_ledger[self.ID] = self.pos_ledger[self.ID] + 1
            # The blank tile moves
            self.pos_ledger[btIndex] = self.pos_ledger[btIndex] - 1
            
            movement = True

        # CASE: BLANK TILE IS ON THE LEFT
        # Check if blank tile is on the LEFT
        # If the floorID of the tile we clicked is to the left
        # of the blank tile's floorID AND
        # the clicked tiles floorID is not on the leftmost edge...

        # Check blank neighborness AND edge validity      
        eqCheckL, edgeCheckL = helper.checkBlankLeft(clickedFloorID,
                               blankFloorID,
                               self.totalPieces)
        
        if eqCheckL and edgeCheckL and not movement:

            # ACTUAL MOVEMENT
            self.moveLeft()
            self.obj_ledger[self.blankTile].moveRight()

            # update the position ledger
            # The tile we clicked moves Left, so floorID is minus 1
            self.pos_ledger[self.ID] = self.pos_ledger[self.ID] - 1
            # The blank tile moves right, so floorID is plus 1
            self.pos_ledger[btIndex] = self.pos_ledger[btIndex] + 1
            
            movement = True
        
        # CASE: BLANK TILE IS ABOVE
        # Check if blank tile is ABOVE
        # If the floorID of the tile we clicked is below
        # the blank tile's floorID...

        # Check blank neighborness AND edge validity
        eqCheckUp = helper.checkBlankUp(clickedFloorID,
                                        blankFloorID,
                                        self.totalPieces)
        
        if eqCheckUp and not movement:

            # ACTUAL MOVEMENT
            self.moveUp()
            self.obj_ledger[self.blankTile].moveDown()

            # update the position ledger
            # The tile we clicked moves Up, so floorID is minus 4
            self.pos_ledger[self.ID] = self.pos_ledger[self.ID] - vert
            # The blank tile moves down, so floorID is plus 4
            self.pos_ledger[btIndex] = self.pos_ledger[btIndex] + vert

            movement = True

        # CASE: BLANK TILE IS DOWN
        # Check if blank tile is BELOW
        # If the floorID of the tile we clicked is above
        # the blank tile's floorID...

        # Check blank neighborness
        eqCheckD = helper.checkBlankDown(clickedFloorID,
                                         blankFloorID,
                                         self.totalPieces)

        if eqCheckD and not movement:

            # ACTUAL MOVEMENT
            self.moveDown()
            self.obj_ledger[self.blankTile].moveUp()

            # update the position ledger
            # The tile we clicked moves down, so floorID is plus 4
            self.pos_ledger[self.ID] = self.pos_ledger[self.ID] + vert
            # The blank tile moves up, so floorID is minus 4
            self.pos_ledger[btIndex] = self.pos_ledger[btIndex] - vert

            movement = True

        #############################################################

        # If we've gotten to this point, then the clicked tile
        # isn't neighboring the blank tile. Turn the click_handler
        # back on.
        for i in range(len(self.obj_ledger)):
            arg = self.obj_ledger[i].click_handler
            self.obj_ledger[i].turtle1.onclick(arg)
            
        #If movement, update player moves so far,
        # check for win condition, and check lose condition
        if movement:
            # UPDATE MOVES
            self.moveRecord.updateMovesOnClick()

            # CHECK FOR WIN
            if helper.checkWin(self.pos_ledger):
                # if there is a leaderboard...
                if self.isLeaderboard:
                    txt_path = f"{os.getcwd()}/leaderboard.txt"
                    with open(txt_path, mode='r') as f:
                        lines = f.read().split('\n')
                    lines = [x.split(',') for x in lines]
                    for i in range(len(lines)):
                        lines[i][0] = lines[i][0].strip()
                        lines[i][1] = lines[i][1].strip()

                    # PUT THE USER'S SCORE IN CORRECT POSITION
                    userScore = self.moveRecord.numMovesTotal
                    
                    for i in range(len(lines)):
                        if i == 0:
                            if userScore < int(lines[0][0]):
                                lines.insert(0, [f'{userScore}',
                                                 self.playerName])
                                break

                        elif i > 0 and i < (len(lines) - 1):
                            if userScore >= int(lines[i-1][0]) and\
                               userScore < int(lines[i][0]):
                                lines.insert(i, [f'{userScore}',
                                                 self.playerName])      
                                break
                        elif i == (len(lines) - 1):
                            if userScore < int(lines[i][0]):
                                lines.insert(i, [f'{userScore}',
                                                 self.playerName])                                        
                                break
                            else:
                                lines.append([f'{userScore}',
                                              self.playerName])                     
                                break

                    # Write to leaderboard regardless of whether resetUsed
                    with open(txt_path, mode = "w") as f:
                        for i in range(len(lines)):
                            if i < (len(lines) - 1):
                                f.write(f"{lines[i][0]}, {lines[i][1]}" +\
                                        "\n")
                            elif i == (len(lines) - 1):
                                f.write(f"{lines[i][0]}, {lines[i][1]}")

                # Show win and credits and quit
                img_path = f"{os.getcwd()}/Resources/winner.gif"                        
                self.screen.register_shape(img_path)
                winmsg = turtle.Turtle()
                winmsg.shape(img_path)
                time.sleep(2)
                winmsg.hideturtle()
                turtle.clearscreen()
                turtle.bye()

            # CHECK FOR LOSS
            if self.moveRecord.numMovesRemaining == 0:
                img_path = f"{os.getcwd()}/Resources/Lose.gif"                         
                self.screen.register_shape(img_path)
                winmsg = turtle.Turtle()
                winmsg.shape(img_path)
                time.sleep(2)
                winmsg.hideturtle()
                turtle.clearscreen()
                turtle.bye()
                     
    def setBlankTileObjLedgerIndex(self, blankTileInput):
        '''
            Give a Tile a blankTile attribute

            Parameters:
                blankTileInput (Tile): the blank tile Tile object
            Returns:
                None
                
        '''
        self.blankTile = blankTileInput

    def setResetButton(self, resetButton):
        '''
            Give a Tile a resetButton attribute

            Parameters:
                resetButton (resetButton): the resetButton
            Returns:
                None
                
        '''
        self.resetButton = resetButton
