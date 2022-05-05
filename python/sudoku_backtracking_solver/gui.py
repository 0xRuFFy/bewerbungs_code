import pyglet
import main

#* base Grid
START = [n for n in main.board]

class Solver:
    def __init__(self):
        self.index = 0
        self.board = [n for n in START]
        self.spots = [i for i, n in enumerate(self.board) if n == 0]
        self.done = False
        self.numbers = []
        self.initNumbers()
        main.solveIt(main.board)
        self.correct = main.board

    def initNumbers(self):
        for i, n in enumerate(START):
            x = ((i % 9)+.5)  * (window_size/9)
            y = window_size - ((i // 9)+.5) * (window_size/9)
            self.numbers.append(pyglet.text.Label(str(n) if n != 0 else '', x=x, y=y, color=(0, 0, 0, 255), 
                                                  font_name='Cascadia Code',font_size=20, 
                                                  anchor_x='center', anchor_y='center', 
                                                  batch=batch, group=number_layer))
    
    def solverStep(self, dt=0):
        if not self.done:
            if self.board[self.spots[self.index]] < 9:
                self.board[self.spots[self.index]] += 1
                self.numbers[self.spots[self.index]].text = str(self.board[self.spots[self.index]])
                self.numbers[self.spots[self.index]].color = (40, 237, 112, 255) if self.board[self.spots[self.index]] == self.correct[self.spots[self.index]] else (237, 70, 40, 255)
                if main.isDone(self.board):
                    self.done = True
                    return
                if main.isCorrect(self.board):
                    self.index += 1
            elif self.index > 0:
                self.board[self.spots[self.index]] = 0
                self.numbers[self.spots[self.index]].text = ''
                self.index -= 1
            else:
                self.done = True
                return
        
window_size = 675

window = pyglet.window.Window(window_size, window_size)

batch = pyglet.graphics.Batch()

background   = pyglet.graphics.OrderedGroup(0)
line_layer   = pyglet.graphics.OrderedGroup(1)
number_layer = pyglet.graphics.OrderedGroup(2)

bg = pyglet.shapes.Rectangle(0, 0, width=window_size, height=window_size, 
                             color=(255, 255, 255), batch=batch, group=background)
lines = []

for i in range(9):
    lines.append(pyglet.shapes.BorderedRectangle(((i % 3)) * window_size/3, window_size-((i // 3)+1) * window_size/3,
                                               width=window_size/3, height=window_size/3, border=4, color=(255, 255, 255), 
                                               border_color=(0, 0, 0), batch=batch, group=line_layer))
    lines.append(pyglet.shapes.Line(0, window_size/9 * i, window_size, window_size/9 * i, 
                                    width=2, color=(0, 0, 0), batch=batch, group=line_layer))
    lines.append(pyglet.shapes.Line(window_size/9 * i, 0, window_size/9 * i, window_size, 
                                    width=2, color=(0, 0, 0), batch=batch, group=line_layer)) 

solver = Solver()

pyglet.clock.schedule_interval(solver.solverStep, 1/60)

@window.event
def on_draw():

    window.clear()
    batch.draw()
    
    
pyglet.app.run()