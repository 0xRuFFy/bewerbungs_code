
from os import system
from loader import *
import snake

class SnakeWindow(Window):
    
    WIDTH     : int = data.data['window']['width'] #@var window width
    HEIGHT    : int = data.data['window']['height'] #@var window height
    FRAMERATE : float = 1 / data.data['window']['frameRate'] #@var game Framerate
    
    def __init__(self, showGrid : bool = True) -> None:
        """Window class for a Snake game
        -
        :Tasks:
            `updating` : updates the moving Parts
            `drawing`  : draws all assets
            `inputs`   : handelts all userinputs
            
        `AUTHOR`    : Konstantin Opora \n
        """
        super(SnakeWindow, self).__init__(self.WIDTH, self.HEIGHT, caption='Snake')
        
        self.internalClock = 0 #@var timer for snake moves      
        self.batch      : Batch = Batch() #@var batch to draw all assets
        self.groups     : List[OrderedGroup] = [OrderedGroup(i) for i in range(7)] #@var all OrderedGroups
        self.bg         : Rectangle = data.background(self.batch, self.groups[0]) #@var the background color
        self.playField  : BorderedRectangle = data.field(self.batch, self.groups[1]) #@var space for the game
        if showGrid: 
            self.grid   : List[Line] = data.getGrid(self.batch, self.groups[2]) #@var optional gridlines
            
        self.snake = snake.Snake(self.batch, self.groups[3]) #@var the snake
        
        self.create_stats()
        
        clock.schedule_interval(self.update, self.FRAMERATE) #schedule the update
        
        self.newGameLabel = Label(
                text='', 
                batch=self.batch, group=self.groups[6], 
                x=self.WIDTH/2, y=self.HEIGHT/3*2,
                font_size=30, align='center', anchor_x='center',
                multiline=True, width=self.WIDTH, color=(255, 255, 255, 0)
                )
    
    def start_new_game(self) -> None:
        self.snake = snake.Snake(self.batch, self.groups[3])
        del(self.cover)
        self.newGameLabel.color = (255, 255, 255, 0)
    
    def game_end(self, create: bool = True) -> None:
        if create:
            self.cover = data.background(self.batch, self.groups[5])
            self.cover.opacity = 200
            
            self.newGameLabel.color = (255, 255, 255, 255)
            self.newGameLabel.text = f'Your Score was: {self.snake.score}\n\n\n\n\n\n\n\nPRESS "ENTER" TO START A NEW GAME'
        else:
            self.start_new_game()
    
    def update(self, dt=0) -> None:
        """update Function
        -
        Is scheduled with clock.schedule_interval
        
        Handelts the internal clock and the snake movements
        """
        self.internalClock += dt # add delta to clock
        if self.snake.alive and self.internalClock >= 1/8:
            self.internalClock = 0 # reset clock
            self.snake.move() # make snake move
        elif not self.snake.alive:
            data.update_highscore(self.snake.score)
            self.game_end()
            
            
        self.update_stats()
    
    def create_stats(self) -> None:
        """Creats all Labels that show the stats of the game
        """
        self.scoreLbl = Label(
            text=f'Score: {self.snake.score}', 
            batch=self.batch, group=self.groups[4], 
            x=self.WIDTH*0.6, y=self.HEIGHT//2,
            font_size=20
            )
        self.efficiencyLbl = Label(
            text=f'Efficiency: {0}', 
            batch=self.batch, group=self.groups[4], 
            x=self.WIDTH*0.6, y=self.HEIGHT//2 - 25,
            font_size=20
            )
        self.inputsLbl = Label(
            text=f'KeyInputs: {self.snake.inputCount}', 
            batch=self.batch, group=self.groups[4], 
            x=self.WIDTH*0.6, y=self.HEIGHT//2 - 50,
            font_size=20
            )
        self.inputsEfficiencyLbl = Label(
            text=f'Input efficiency: {0}', 
            batch=self.batch, group=self.groups[4], 
            x=self.WIDTH*0.6, y=self.HEIGHT//2 - 75,
            font_size=20
            )
        self.highscoreLbl = Label(
            text=f'Highscore: {data.get_highscore()}', 
            batch=self.batch, group=self.groups[4], 
            x=self.WIDTH*0.6, y=self.HEIGHT//2 - 100,
            font_size=20
            )
        
    def update_stats(self):
        """updates the text of the stat Labels
        """
        self.scoreLbl.text = f'Score: {self.snake.score}'
        self.efficiencyLbl.text = f'Efficiency: {0 if self.snake.moveCount == 0 else round(self.snake.score / self.snake.moveCount, 3)}'
        self.inputsLbl.text = f'KeyInputs: {self.snake.inputCount}'
        self.inputsEfficiencyLbl.text = f'Input efficiency: {0 if self.snake.inputCount == 0 else round(self.snake.score / self.snake.inputCount, 3)}'
        self.highscoreLbl.text = f'Highscore: {data.get_highscore()}' 
        
    def on_draw(self):
        """on_draw Function
        -
        Clears the screen and redraws the batch
        """
        self.clear()
        self.batch.draw()
        
    def on_key_press(self, symbol, modifiers):
        """on_key_press Function
        -
        Handels all keypresses\n
        moves will be added into a 2 move long queue
        """
        if self.snake.alive:
            if symbol == key.W:
                self.snake.addMove(data.data['moves']['up'])
            if symbol == key.S:
                self.snake.addMove(data.data['moves']['down'])
            if symbol == key.A:
                self.snake.addMove(data.data['moves']['left'])
            if symbol == key.D:
                self.snake.addMove(data.data['moves']['right'])
        else:
            if symbol == key.ENTER:
                self.game_end(False)
            