#!/usr/bin/env python3

#* @imports : pyglet
from pyglet.graphics import Batch, OrderedGroup
from pyglet.window import Window, mouse
from pyglet.text import Label
from pyglet.sprite import Sprite
from pyglet import image
from pyglet import app

from typing import List

class Assets:
    """Loads all necessary images that will be used in this application."""
    
    def __init__(self):
        self.path = './assets'
        self.__imgLoader__()
    
    def __imgLoader__(self):
        self.grid_img = image.load(self.path + '/grid.png') 
        self.bg_img   = image.load(self.path + '/background.png')
        self.o_img    = image.load(self.path + '/O.png')
        self.x_img    = image.load(self.path + '/X.png')
        self.playButton_img_1 = image.load(self.path + '/playButton_normal.png')
        self.playButton_img_2 = image.load(self.path + '/playButton_hover.png')

class Text(Label):
    def __init__(self, text, font_size, x, y, batch, group, bold=False, italic=False, color=(255, 255, 255, 255)):
        super().__init__(text=text, font_name='Proxy 5', font_size=font_size, bold=bold, italic=italic, 
                         color=color, x=x, y=y, anchor_x='center', 
                         anchor_y='center', align='center', batch=batch, group=group)
   
class preSprites:
    """Sets up a few presets that are necessary in the application."""
    
    def __init__(self, batch, groups, windowSize) -> None:
        
        self.batch = batch
        self.groups = groups
        self.windowSize = windowSize
        self.assets = Assets()
        
        #* Grid varibels
        self.gridDim = self.assets.grid_img.width
        self.gridPos = ((self.windowSize[0]-self.gridDim)//2, (self.windowSize[1]-self.gridDim)//2)
    
    def Background(self) -> Sprite:
        """Background Sprit preset"""
        return Sprite(self.assets.bg_img, x=0, y=0, batch=self.batch, group=self.groups[0], usage='static')
    
    def Grid(self) -> Sprite:
        """TicTacToe grid Sprit preset"""
        return Sprite(self.assets.grid_img, x=self.gridPos[0], y=self.gridPos[1], batch=self.batch, 
                      group=self.groups[1], usage='static')

    class Figure(Sprite):
        """Sprite class for the player smbols (X and O)
        
        Calculateds the screenposition based on the given field ID.
        """
        
        def __init__(self, preSprites, type_ : str, ID : int, hover : bool = False, score : bool = False) -> None:
            super().__init__(img=preSprites.assets.x_img if type_ == 'X' else preSprites.assets.o_img, 
                             x=preSprites.gridPos[0] + ((preSprites.gridDim//3) * (ID % 3)), 
                             y=preSprites.gridPos[1] + ((preSprites.gridDim//3) * (2 - (ID // 3))),
                             batch=preSprites.batch, group=preSprites.groups[2])
            
            self.preSprites = preSprites
            
            if score:
                self._x = (preSprites.gridPos[0] - preSprites.assets.x_img.width) // 2 + (0 if ID == 0 else 
                                                                                          preSprites.gridDim + preSprites.gridPos[0])
                self._y = preSprites.gridPos[1] + preSprites.gridDim * .8
                self._update_position()
            
            
            if hover:
                self.opacity = 0
            else:
                self.ID = ID
                self.type_ = type_
            
        def onHover(self, ID : int, type_ : str) -> None:
            self._x = ((self.preSprites.windowSize[0]-self.preSprites.gridDim)//2) + ((self.preSprites.gridDim//3) * (ID % 3))
            self._y =((self.preSprites.windowSize[1]-self.preSprites.gridDim)//2) + ((self.preSprites.gridDim//3) * (2 - (ID // 3)))
            self.opacity = 120
            self._update_position()
    
    class PlayButton(Sprite):
        
        def __init__(self, preSprites, batch, group):
            super().__init__(preSprites.assets.playButton_img_1, 
                             x=(preSprites.windowSize[0] - preSprites.assets.playButton_img_1.width)//2, 
                             y=(preSprites.windowSize[1] - preSprites.assets.playButton_img_1.height)//2, 
                             batch=batch, group=group)
            
            self.preSprites = preSprites
            self.on = False
            
        def hover(self):
            if not self.on:
                self.image = self.preSprites.assets.playButton_img_2
                self.on = True
            
        def unHover(self):
            if self.on:
                self.image = self.preSprites.assets.playButton_img_1
                self.on = False
    
    class ScoreBoard:
        
        def __init__(self, preSprites) -> None:
            
            self.scores : List[int] = [0, 0]
            self.sprites = [
                preSprites.Figure(preSprites, type_='X', ID=0, score=True),
                preSprites.Figure(preSprites, type_='O', ID=1, score=True)
            ]
            self.labels = [
                Label('0', 'Evogria', 150, x=self.sprites[n].x + (preSprites.assets.x_img.width//2), 
                      y=self.sprites[n].y - 250, batch=preSprites.batch, 
                      group=preSprites.groups[2], anchor_x='center', align='center') for n in range(2)
            ]
            
            self.update()
            
        def update(self):
            self.labels[0].text = str(self.scores[0])
            self.labels[1].text = str(self.scores[1])
    