'''
    Andrew Boylan
    Fall 2021

    A resetClass object for the reset button.
    
'''

import turtle
import math

class resetButton:
    '''
        Allows the user to reset the puzzle. moveRecord doesn't change.

        Attributes:
            obj_ledger (list): a list containing Tile objects
            pos_ledger (dict): a dictionary with information about the
                position of each Tile
            turtle1 (Turtle): 
            x_init (int): the x-coordinate for the load button to be
                created at
            y_init (int): the y-coordinate for the load button to be created
                at
            screen (Screen): a turtle Screen object
            turtle1 (Turtle): a trutle object ot handle clicks and display
                the load button image
            
    '''
    def __init__(self,
                 obj_ledger,
                 pos_ledger,
                 x_init,
                 y_init,
                 screen):
        '''
        Parameters:
            obj_ledger (list): a list containing Tile objects
            pos_ledger (dict): a dictionary with information about the
                position of each Tile
            x_init (int): the x-coordinate for the load button to be
                created at
            y_init (int): the y-coordinate for the load button to be created
                at
            screen (Screen): a turtle Screen object
        '''
        self.obj_ledger = obj_ledger
        self.pos_ledger = pos_ledger
        self.turtle1 = turtle.Turtle()
        self.turtle1.hideturtle()
        self.turtle1.speed(0)
        self.turtle1.penup()
        self.turtle1.setpos(x_init, y_init)
        self.screen = screen
        img_path = 'Resources/resetbutton.gif'
        self.screen.register_shape(img_path)
        self.turtle1.shape(img_path)
        self.turtle1.showturtle()
        
    def click_handler(self, x, y):
        '''
            Function that runs when the reset button is clicked. Solves
            the puzzle.

            Parameters:
                x, y (int): these get passed in when .onclick() is called
                but we dont need them.
            Returns:
                None
                
        '''
        # CREATE A BLANK OBJ_LEDGER
        temp_obj_ledger = [0 for x in range(len(self.obj_ledger))]
        
        # FILL IT WITH TURTLES FROM OBJ_LEDGER, BUT IN THE CORRECT
        # ORDER SUCH THAT THE PUZZLE BECOMES SOLVED
        for i in range(len(self.obj_ledger)):
            temp_obj_ledger[self.obj_ledger[i].ID] = self.obj_ledger[i]

        pieceSize = temp_obj_ledger[0].size
        numPieces = temp_obj_ledger[0].totalPieces
        x_init = -448 + 30 + (pieceSize/2)
        y_init = 397 - 30 - (pieceSize/2)
        padding_x = 0
        padding_y = 0
        margin = 5

        for i in range(len(temp_obj_ledger)):
            temp_obj_ledger[i].turtle1.setpos(x_init + padding_x,
                                              y_init + padding_y)
            
            # INCREMENTING THE PADDING
            if not (i+1) % math.sqrt(numPieces):
                padding_x = 0
                padding_y -= pieceSize + margin
            else:
                padding_x += pieceSize + margin

            # ADJUST THE POSITION LEDGER
            self.pos_ledger[i] = i     
