from main import SnakeWindow
from pyglet.app import run


if __name__ == '__main__':

    game = SnakeWindow(
        showGrid=False
    )

    run()
