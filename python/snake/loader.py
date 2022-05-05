
""""Handles all imports and some needed utility"""

import json
from typing import Dict, List, Tuple
from random import randint

# specific pyglet imports
from pyglet.window import Window, key
from pyglet.graphics import Batch, OrderedGroup
from pyglet.shapes import Line, Rectangle, BorderedRectangle
from pyglet import app, clock
from pyglet.text import Label


class data(object):
    """data
    -
    Contains some preset and the json data
    """
    # load the json file into a dict
    with open('./data.json') as file:
        data: Dict = json.load(file)
    
    @classmethod
    def update_highscore(cls, score: int) -> None:
        data: Dict = None
        with open('./data.json') as file:
            data = json.load(file)
            if score > data['player']['highscore']:
                data['player']['highscore'] = score
            
                with open('./data.json', 'w') as file:
                    file.write(json.dumps(data))
    
    @classmethod
    def get_highscore(cls) -> int:
        with open('./data.json') as file:
            value = json.load(file)['player']['highscore']
        return value
    
    @classmethod
    def background(cls, batch : Batch, group : OrderedGroup) -> Rectangle:
        """background
        -
        creats the background Color Rectagle
        """
        return Rectangle(
            0, 0, 
            cls.data['window']['width'], 
            cls.data['window']['height'], 
            cls.data['window']['color'],
            batch, group
            )
        
    @classmethod
    def field(cls, batch : Batch, group : OrderedGroup) -> BorderedRectangle:
        """field
        -
        creats the playingfield border
        """
        return BorderedRectangle(
            cls.data['window']['game']['pos'][0],
            cls.data['window']['game']['pos'][1],
            cls.data['window']['game']['width'],
            cls.data['window']['game']['height'],
            4,
            cls.data['window']['game']['color'],
            cls.data['window']['game']['borderColor'],
            batch, group
            )
    
    @classmethod
    def getGrid(cls, batch : Batch, group : OrderedGroup) -> List[Line]:
        """getGrid
        -
        creats the gridlines
        """
        space : int = cls.data['window']['game']['tileSize']
        start : int = space + cls.data['window']['game']['pos'][0]
        end   : int = cls.data['window']['game']['pos'][0] + cls.data['window']['game']['width']
        
        grid : List[Line] = []
        for i in range(start, end, space):
            horizontal = Line(
                i, start-space,i, end, 2,
                cls.data['window']['game']['borderColor'],
                batch, group
            )
            vertical = Line(
                start-space, i, end, i, 2,
                cls.data['window']['game']['borderColor'],
                batch, group
            )
            
            horizontal.opacity = cls.data['window']['game']['gridOpacity']
            vertical.opacity = cls.data['window']['game']['gridOpacity']
            
            grid.append(horizontal)
            grid.append(vertical)
            
        return grid
        