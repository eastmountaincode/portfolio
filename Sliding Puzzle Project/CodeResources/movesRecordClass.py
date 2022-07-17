'''
    Andrew Boylan
    Fall 2021

    A moveRecord object that contains two turtles whose job it
    is to write the number of moves that have been taken as well as the
    number of moves that remain.
    
'''

import CodeResources.helper_functions as helper

class moveRecord:
    '''
        Keeps track of how many moves have been made and how
        many remain.

        Attributes:
            numMovesRemaining (int): The number of moves until the user
                loses the game. This is given by the user during setup. This
                attribute is decremented as the user makes moves.
            numMovesRemainingPerm (int): The number of moves until the user
                loses the game. When the user switches puzzles by using
                the load button, we want to reset the counter, so we need
                to know what to put it back to. This attribute, unlike
                numMovesRemaining, does not decrement as the user
                makes moves but serves as a permanent record of the
                number of moves the user wants to give themself until
                they lose the game.
            numMovesTotal (int): The number of moves made so far. Increments
                as the user makes moves. Starts at zero.
            fontStyle (str): The style of the font.
            fontSize (str): The size of the font.
            moveTotal (Turtle): The turtle object that writes numMovesTotal
            moveRemaining (Turtle): The turtle object that writes
                numMovesRemaining
                
    '''
    
    def __init__(self,
                 numMovesRemaining,
                 fontStyle,
                 fontSize):
        '''
        Initialize the moveRecord object.

        Parameters:
            numMovesRemaining (int): The number of moves until the user
                loses the game. This is given by the user during setup. This
                attribute is decremented as the user makes moves.
            numMovesRemainingPerm (int): The number of moves until the user
                loses the game. When the user switches puzzles by using
                the load button, we want to reset the counter, so we need
                to know what to put it back to. This attribute, unlike
                numMovesRemaining, does not decrement as the user
                makes moves but serves as a permanent record of the
                number of moves the user wants to give themself until
                they lose the game.
            numMovesTotal (int): The number of moves made so far. Increments
                as the user makes moves. Starts at zero.
            fontStyle (str): The style of the font.
            fontSize (str): The size of the font.
            
        '''
        self.numMovesRemaining = numMovesRemaining
        self.numMovesRemainingPerm = numMovesRemaining
        self.numMovesTotal = 0
        self.fontStyle = fontStyle
        self.fontSize = fontSize
        
        moveTotal = helper.createTurtle('moveTotal', -379, -325)
        moveTotal.write(f"Player moves so far: {self.numMovesTotal}",
                    font=(fontStyle, fontSize, "bold"))
        self.moveTotal = moveTotal
        
        moveRemaining = helper.createTurtle('moveRemaining', -379, -360)
        moveRemaining.write(f"Player moves remaining: "
                            f"{self.numMovesRemaining}",
                          font=(fontStyle, fontSize, "bold"))
        self.moveRemaining = moveRemaining
        
    def updateMovesOnClick(self):
        '''
            Function that runs whenever a valid Tile move is made.
            Updates the move counters.

            Parameters:
                None
            Returns:
                None
                
        '''
        # Write the number of moves made so far
        self.moveTotal.clear()
        self.numMovesTotal += 1
        self.moveTotal.write(f"Player moves so far: {self.numMovesTotal}",
                             font=(self.fontStyle,
                                   self.fontSize, "bold"))

        # Write the number of moves remaining
        self.moveRemaining.clear()
        self.numMovesRemaining -= 1
        self.moveRemaining.write(f"Player moves remaining: "
                            f"{self.numMovesRemaining}",
                          font=(self.fontStyle,
                                self.fontSize, "bold"))
        
