
from typing import List

class Node:
    """Node Class
    -
    Class for a singel LinkedList Node
    
    Variables:
    
        - `prevNode` : Node -> reference to the previous Node (None : self -> first Node)
        - `nextNode` : Node -> reference to the next Node (None : self -> last Node)
        - `value`           -> value of the Node
    """
    def __init__(self, prevNode=None, nextNode=None, value=None) -> None:
        self.prevNode : Node = prevNode
        self.nextNode : Node = nextNode
        self.value = value
    
    @property
    def prevNode(self):
        """Reference to the previous Node (None : self -> first Node)"""
        return self.prevNode
    
    @property
    def prevNode(self):
        """reference to the next Node (None : self -> last Node)"""
        return self.nextNode
        
class LinkedList:
    """LinkedList Class
        -
        
        Linked List Modul for Python

        Functionality:

            - List can be initialized with any amount and any type of values
            - the List can be printed with the normal print() function
            - .get(index) : the value of any Node can be read
            - .__delitem__(index) : removes Node from the List at the point
            - .append(value) : appends the linked List with a Node holding a given value
            - .insert(index, value) : adds a new Node in any given spot with any value
            - .clear() : deletes all Nodes
            - .remove(value) : deletes the first Node with the given value
            
        Variables:

            - .length : gives the length of the List
            - .start : head of List
            
        @Author Konstantin Opora
        """
    
    def __init__(self, initialValues : List =None) -> None:
        self.start = None
        self.length = 0 
        if initialValues != None:
            for value in initialValues:
                self.append(value)
    
    def __str__(self) -> str:
        """returns String representation of the List"""
        print('{ ', end='')
        temp = self.start
        while temp != None:
            print(temp.value, ' -> ' if temp.nextNode != None else '', end='')
            temp = temp.nextNode
        del temp
        return '}'
    
    def get(self, index : int):
        """If possible returns the value of the Node at a give index."""
        if 0 <= index <= self.length:
            temp = self.start
            for _ in range(index):
                temp = temp.nextNode
            return temp.value
        else:
            raise IndexError('Index out of range. Given ID: ' +  str(index) + ' | List length: ' + str(self.length))
    
    def __delitem__(self, index):
        """If possible remove the Node at a give index."""
        if index <= self.length:
            self.length -= 1
            if index == 0:
                self.start = self.start.nextNode
                if self.start != None:
                    self.start.prevNode = None
                return
            
            temp = self.start
            for _ in range(index):
                temp = temp.nextNode
            
            temp.prevNode.nextNode = temp.nextNode
            if temp.nextNode != None:
                temp.nextNode.prevNode = temp.prevNode  
            del temp     
        else:
            raise IndexError('Index out of range. Given ID: ' +  str(index) + ' | List length: ' + str(self.length))
        
    def append(self, value) -> None:
        """Appends the List with a Node holding a given value."""
        self.length += 1     
        if self.length == 1:
            self.start = Node(value=value)
            return     
        temp = self.start
        while temp.nextNode != None:
            temp = temp.nextNode         
        temp.nextNode = Node(prevNode=temp, value=value)
        del temp
        
    def insert(self, index : int, value) -> None:
        """Adds a new Node in the given postion with a given value."""
        if self.length == index == 0:
            self.length += 1
            self.start = Node(value=value)
        elif index <= self.length:
            self.length += 1          
            if index == 0:
                self.start.prevNode = Node(nextNode=self.start, value=value)
                self.start = self.start.prevNode
            elif index+1 == self.length:
                self.append(value)
            else:
                temp = self.start
                for _ in range(index):
                    temp = temp.nextNode
                temp.prevNode.nextNode = Node(prevNode=temp.prevNode, nextNode=temp, value=value)
                temp.prevNode = temp.prevNode.nextNode
                del temp
        else:
            raise IndexError('Index out of range. Given ID: ' +  str(index) + ' | List length: ' + str(self.length))
        
    def clear(self) -> None:
        """Removes all Nodes"""
        self.length = 0
        self.start = None
        
    def remove(self, value) -> None:
        """Removes the first occurens of a given value"""
        temp = self.start
        i = 0
        while temp != None:
            if temp.value == value:
                self.__delitem__(i)
                return
            temp = temp.nextNode
            i += 1
        del temp
        
    @property
    def head(self) -> Node:
        """First element of the List"""
        return self.start
    
    @property
    def length(self) -> Node:
        """Length of the List"""
        return self.length

if __name__ == '__main__':
    
    mainKnot = LinkedList()                     # print(mainKnot) : { }
    mainKnot.append('a')                        # print(mainKnot) : { a }
    mainKnot.insert(0, 'a')                     # print(mainKnot) : { a -> a }
    mainKnot.__delitem__(0)                     # print(mainKnot) : { a }
    mainKnot.append('b')                        # print(mainKnot) : { a -> b }
    mainKnot.clear()                            # print(mainKnot) : { }
    mainKnot.__init__(['a', 'b', 'j', 'c'])     # print(mainKnot) : { a -> b -> j -> c }
    mainKnot.remove('j')                        # print(mainKnot) : { a -> b -> c }
    
    print(mainKnot.get(2))                      # 'c' 
    print(mainKnot)                             # { a -> b -> c }