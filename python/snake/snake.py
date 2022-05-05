
from loader import *
        
class Apple(BorderedRectangle):
    
    def __init__(self, x, y, batch=None, group=None):
        super().__init__(
            x, y, 
            data.data['window']['game']['tileSize'], 
            data.data['window']['game']['tileSize'],
            data.data['window']['game']['apple']['border'], 
            data.data['window']['game']['apple']['color'],
            data.data['window']['game']['apple']['borderColor'],
            batch, group)
        
    def update(self, x, y):
        self._x = x
        self._y = y
        self._update_position()

class SnakePart(BorderedRectangle):
    """SnakePart
    -
    Creates on part of the Snake
    """
    
    def __init__(self, x, y, batch, group):
        super().__init__(
            x, y,
            data.data['window']['game']['tileSize'],
            data.data['window']['game']['tileSize'],
            border=data.data['window']['game']['snake']['border'],
            color=data.data['window']['game']['snake']['color'],
            border_color=data.data['window']['game']['snake']['borderColor'],
            batch=batch, group=group)

class Snake(object):
    """Snake
    -
    Creates the entier Snake
    """
    
    offset   : Tuple[int] = data.data['window']['game']['pos'] #@var playfield position
    length   : int = data.data['window']['game']['snake']['startLength'] #@var body size
    partSize : int = data.data['window']['game']['tileSize'] #@var snake bodypart size
    xPos     : int = data.data['window']['game']['width'] // partSize // 2 - 1 #@var id of x position
    yPos     : int = xPos #@var id of y position
    
    def __init__(self, batch : Batch, group : OrderedGroup) -> None:
        
        self.body : List[SnakePart] = [
            SnakePart(
                self.xPos * self.partSize + self.offset[0], 
                (self.yPos - i) * self.partSize + self.offset[1],
                batch, group
                ) for i in range(self.length)
        ]

        self.moveQ  : List[List[int]] = []
        self.dir    : List[int] = [0, 1]
        self.alive  : bool = True
        
        #* Stats
        self.score      : int = 0
        self.moveCount  : int = 0
        self.inputCount : int = 0
        
        self.tileCountX  : int = data.data['window']['game']['width']  // self.partSize
        self.tileCountY  : int = data.data['window']['game']['height'] // self.partSize
        
        self.apple = Apple(
            (randint(0, self.tileCountX-1) * self.partSize) + self.offset[0], 
            (randint(0, self.tileCountY-1) * self.partSize) + self.offset[1], 
            batch=batch, group=group)
    
    def addMove(self, move : List[int]) -> None:
        if len(self.moveQ) <= 2:
            self.moveQ.append(move)
            self.inputCount += 1
        
    def move(self) -> None:
        self.moveCount += 1
        if len(self.moveQ) > 0:
            self.dir = self.moveQ[0]
            self.moveQ = self.moveQ[1:]
        
        locations = [[p._x, p._y] for p in self.body]
            
        self.body[0]._x += self.dir[0] * self.partSize
        self.body[0]._y += self.dir[1] * self.partSize
        
        for i in range(1, len(self.body)):
            self.body[i]._x = locations[i-1][0]
            self.body[i]._y = locations[i-1][1]
            
        self.alive = not self.deathCheck()
        if self.alive:
            for part in self.body:
                part._update_position()
                
        if self.checkApple():
                self.body.append(SnakePart(
                    locations[len(locations)-1][0],
                    locations[len(locations)-1][1],
                    batch=self.body[0]._batch, group=self.body[0]._group))
                
    def deathCheck(self) -> bool:
        
        for i in range(1, len(self.body)):
            if self.body[0]._x == self.body[i]._x and self.body[0]._y == self.body[i]._y:
                return True
        
        leftWall    : int = data.data['window']['game']['pos'][0]
        rightWall   : int = data.data['window']['game']['pos'][0] + data.data['window']['game']['width']
        bottomWall  : int = data.data['window']['game']['pos'][1]
        topWall     : int = data.data['window']['game']['pos'][1] + data.data['window']['game']['height']
                
        inWall = (
            leftWall > self.body[0]._x or 
            rightWall <= self.body[0]._x or 
            bottomWall > self.body[0]._y or
            topWall <= self.body[0]._y
            )
        
        return inWall
    
    def checkApple(self):
        valid = False
        if self.body[0]._x == self.apple._x and self.body[0]._y == self.apple._y:
            while not valid:
                self.apple.update((randint(0, self.tileCountX-1) * self.partSize) + self.offset[0], 
                                  (randint(0, self.tileCountY-1) * self.partSize) + self.offset[1])
                for part in self.body:
                    valid = part._x != self.apple._x or part._y != self.apple._y
                    if not valid:
                        break
            
            self.length += 1
            self.score += 1

        return valid
