#!/usr/bin/env python3

from typing import List

class Field:
    
    def __init__(self) -> None:
        
        self.field  = ['' for _ in range(9)]
        self.player = 'X'
        self.done = False
        self.draw = False
        self.winner = ''
        self.winnerSpots = []
     
    def reset(self) -> None:
        self.field  = ['' for _ in range(9)]
        self.player = 'X'
        self.done = False
        self.draw = False
        self.winner = ''
        self.winnerSpots = []
    
    def isDone(self, window) -> None:
        for spr in window.spriteField:
            if spr.ID in self.winnerSpots:
                spr.color = (27, 222, 114)
        
        window.scoreBoard.scores[0 if self.winner == 'X' else 1] += 1
        window.scoreBoard.update()
        
    def place(self, window, ID : int) -> None:
        """ Places, if possible, the symbole of the current player (X or O)
        in the given ID spot (0 - 8).
        
        Also places the same symbol in the game Window given.
        """
        if self.field[ID] == '':
            self.field[ID] = self.player
            window.spriteField.append(window.preSprites.Figure(window.preSprites, self.player, ID))
            
            check = self.checkForDone()
            if check == 1:
                self.done = True
                self.winner = self.player
            elif check == 2:
                self.done = True
                self.draw == True
            
            if self.done:
                self.isDone(window)
                
            self.player = 'O' if self.player == 'X' else 'X'
            
    
    def checkForDone(self) -> int: #@out: 0 -> not Over | 1 -> Over with winner | 2 -> draw
        """ Checks if the current game is done or not.
        
        The Function returns a 0 if the game is not done, a 1 if ist done and a 2 if it is a draw.
        """
        #* diagonals
        if '' != self.field[0] == self.field[4] == self.field[8]:
            self.winnerSpots = [0, 4, 8]
            return 1
        
        if '' != self.field[2] == self.field[4] == self.field[6]:
            self.winnerSpots = [2, 4, 6]  
            return 1

        
        #* rows and collums
        for i in range(3):
                if '' != self.field[0+(3*i)] == self.field[1+(3*i)] == self.field[2+(3*i)]:
                    self.winnerSpots = [0+(3*i), 1+(3*i), 2+(3*i)]
                    return 1
                
                if '' != self.field[i+0] == self.field[i+3] == self.field[i+6]:
                    self.winnerSpots = [i+0, i+3, i+6]  
                    return 1
        
        #* draw        
        if self.field.count('') == 0:
            return 2
        
        #* not Over
        return 0