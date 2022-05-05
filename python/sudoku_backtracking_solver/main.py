from puzzles import Puzzles
from typing import List

# choose a sudoku from Puzzles class
board = Puzzles.medium
# board = Puzzles.extrem

def printer(board : List[int]):
    """prints a formate version of the board on stdout

    Args:
        board (List[int]): the sudoku
    """
    for i in range(9):
        print(board[0+9*i], board[1+9*i], board[2+9*i], ' ', board[3+9*i], board[4+9*i], board[5+9*i], ' ', board[6+9*i], board[7+9*i], board[8+9*i])
        if i % 3 == 2:
            print('')

def rows(field : List[int]) -> bool:
    """checks if all rows of the field are valid

    Args:
        field (List[int]): the sudoku

    Returns:
        bool: True -> all rows are valid else -> False
    """
    for i in range(9):
        seen = []
        for j in range(9):
            current = field[i*9 + j]
            if current != 0:
                if current in seen: return False
                seen.append(current)
    return True

def collums(field : List[int]) -> bool:
    """checks if all collums of the field are valid

    Args:
        field (List[int]): the sudoku

    Returns:
        bool: True -> all collums are valid else -> False
    """
    for i in range(9):
        seen = []
        for j in range(9):
            current = field[j*9 + i]
            if current != 0:
                if current in seen: return False
                seen.append(current)
    return True

def boxes(field : List[int]) -> bool:
    """checks if all 3x3 boxes of the field are valid

    Args:
        field (List[int]): the sudoku

    Returns:
        bool: True -> all 3x3 boxes are valid else -> False
    """
    for i in range(9):
        seen = []
        for j in range(3):
            for k in range(3):
                #* 3*(*%3)   -> current Box Collum
                #* (i//3)*27 -> current Box Row
                #* j*9       -> goto next Row in Box
                #* k         -> goto next value in the current Row
                current = field[(3*(i%3) + (i//3)*27) + j*9 + k]
                if current != 0:
                    if current in seen: return False
                    seen.append(current)
    return True

def isCorrect(field : List[int]) -> bool:
    """checks if the sudoku is valid

    Args:
        field (List[int]): the sudoku
    Returns:
        bool: True -> the sudoku is valid else -> False
    """
    return rows(field) and collums(field) and boxes(field)

def isDone(field : List[int]) -> bool:
    """checks if all spots in the sudoku are filled and if it is still valid

    Args:
        field (List[int]): the sudoku

    Returns:
        bool: True -> the sudoku is valid and completet else -> False
    """
    return isCorrect(field) and field.count(0) == 0

def solveRe(field : List[int], spots : List[int], id : int):
    """ Solves a given Sudoku grid using Backtracking with a recursive approach

    Args:
        field (List[int]): the Sudoku grid to solve
        spots (List[int]): list of open spots
        id (int): index for current spot in spots
    """
    if field[spots[id]] < 9:
        field[spots[id]] += 1
        if isDone(field):
            printer(board)
        elif isCorrect(field):
            solveRe(field, spots, id+1)
        else:
            solveRe(field, spots, id)
    elif id > 0:
        field[spots[id]] = 0
        solveRe(field, spots, id-1)
    else:
        print('Not Possible!')

def solveIt(field : List[int]):
    """Solves a given Sudoku grid using Backtracking in an iterative approach

    Args:
        field (List[int]): the Sudoku grid to solve
    """
    spots = [i for i, n in enumerate(field) if n == 0]
    id = 0

    while True:
        if field[spots[id]] < 9:
            field[spots[id]] += 1
            if isDone(field):
                printer(board)
                break
            if isCorrect(field):
                id += 1
        elif id > 0:
            field[spots[id]] = 0
            id -= 1
        else:
            print('Not Possible!')
            break
        
if __name__ == "__main__":
    #* problem with max recursion depth of python can occur
    # solveRe(board, [i for i, n in enumerate(board) if n == 0], 0)

    #* Iterative methode -> no recursion depth problems
    solveIt(board)
