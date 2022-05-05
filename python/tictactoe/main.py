#!/usr/bin/env python3

from loader import *
from field import Field

#! Constants
screenResolution = (1920, 1080)
windowSize = (1300, 900)


class GameWindow(Window):
    def __init__(self, game : Field) -> None:
        """Initializes the game window"""
        super(GameWindow, self).__init__(windowSize[0], windowSize[1], caption='Tic Tac Toe')
        self.set_location((screenResolution[0] - self.width)//2, (screenResolution[1] - self.height)//2)
        
        self.game = game
        self.state : int = 0
        
        #* Batches
        self.gameBatch     = Batch()
        self.mainMenuBatch = Batch()
        
        self.switch = {
            0 : self.mainMenuBatch, 
            1 : self.gameBatch
                       }
        
        #* OrderdGroups
        self.groups = (OrderedGroup(0), OrderedGroup(1), OrderedGroup(2))
        
        #* Sprites
        self.preSprites = preSprites(self.gameBatch, self.groups, windowSize)
        self.set_icon(self.preSprites.assets.grid_img)
        
        self.bg_1       = self.preSprites.Background()
        self.bg_2       = self.preSprites.Background()
        self.bg_2.batch = self.mainMenuBatch
        self.grid       = self.preSprites.Grid()
        self.scoreBoard = self.preSprites.ScoreBoard(self.preSprites)
        self.playButton = self.preSprites.PlayButton(self.preSprites, self.mainMenuBatch, self.groups[2])
        
        self.spriteField = []
        self.hover = [self.preSprites.Figure(self.preSprites, type_='X', ID=0, hover=True),
                      self.preSprites.Figure(self.preSprites, type_='O', ID=0, hover=True)]
        
        #* Text
        self.headLine = Text('Tic-Tac-Toe', 75, windowSize[0]//2, (windowSize[1]//5)*4, self.mainMenuBatch, self.groups[1])
        self.subHeadLine = Text('A Python Project', 25, windowSize[0]//2, (windowSize[1]//5)*4 - 75,
                             self.mainMenuBatch, self.groups[1], color=(27, 222, 114, 255), bold=True)
    
    def on_mouse_motion(self, x, y, dx, dy) -> None:
        if self.state == 1:
            if not self.game.done and self.onGrid(x, y):
                x__ = (x - self.preSprites.gridPos[0]) // 240
                y__ = (2 - (y - self.preSprites.gridPos[1]) // 240)
                
                if self.game.field[x__ + y__*3] == '':
                    self.hover[0 if self.game.player == 'X' else 1].onHover(x__ + y__*3, self.game.player)
                    # self.set_mouse_visible(False)
                else:
                    self.hover[0].opacity = 0
                    self.hover[1].opacity = 0
                    # self.set_mouse_visible(True)
            else:
                self.hover[0].opacity = 0
                self.hover[1].opacity = 0
                # self.set_mouse_visible(True)
        elif self.state == 0:
            if self.onPlayButton(x, y):
                self.playButton.hover()
            else:
                self.playButton.unHover()
               
    def on_mouse_press(self, x, y, button, modifiers) -> None:
        """Handles mouse clicks"""
        if self.game.done and (button == mouse.LEFT or button == mouse.RIGHT):
            self.game.reset()
            self.spriteField = []
        elif self.state == 1 and button == mouse.LEFT:
            if not self.game.done and self.onGrid(x, y):
                x__ = (x - self.preSprites.gridPos[0]) // 240
                y__ = (2 - (y - self.preSprites.gridPos[1]) // 240)
             
                self.game.place(self, x__ + y__*3)
                
        elif self.state == 0 and self.playButton.on and button == mouse.LEFT:
            self.state = 1

            
                      
    def onGrid(self, x, y) -> bool:
        """Checks if an given x and y position is on the game grid"""
        return (x > self.preSprites.gridPos[0] and x < self.preSprites.gridPos[0] + self.preSprites.gridDim and
                y > self.preSprites.gridPos[1] and y < self.preSprites.gridPos[1] + self.preSprites.gridDim)
    
    def onPlayButton(self, x, y):
        """Checks if an given x and y position is on the play button"""
        return (x > self.playButton.x and x < self.playButton.x + self.preSprites.assets.playButton_img_1.width and
                y > self.playButton.y and y < self.playButton.y + self.preSprites.assets.playButton_img_1.height)    
    
    def on_draw(self):
        self.clear()
        
        self.switch[self.state].draw()
        


if __name__ == '__main__':
    
    game = Field()
    
    win = GameWindow(game)
    
    app.run()