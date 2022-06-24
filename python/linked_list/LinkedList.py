
from typing import Any, Iterable


class Node:
    def __init__(self, data) -> None:
        self.data = data
        self.next: Node = None
    
    def __str__(self) -> str:
        return str(self.data)


class LinkedList:
    def __init__(self, intitalValues: Iterable = None) -> None:
        self.head: Node = None
        self._length: int = 0
        
        if intitalValues != None:
            for val in intitalValues[::-1]:
                self.push(val)
    
    def __str__(self) -> str:
        return "{{ {} }}".format(' -> '.join(
            map(str, list(self))
        ))
    
    def __iter__(self) -> iter:
        data = [None for _ in range(len(self))]
        tmp = self.head
        i = 0
        while tmp:
            data[i] = tmp.data
            tmp = tmp.next
            i += 1
        return iter(data)

    def __copy__(self) -> 'LinkedList':
        return LinkedList(list(self))
    
    def __add__(self, data) -> 'LinkedList':
        new = self.__copy__()
        new.append(data)
        return new
    
    def __len__(self) -> int:
        return self._length
    
    def __getitem__(self, arg: int | slice) -> Any | 'LinkedList':
        if type(arg) == int:
            if arg < 0:
                return self._get(len(self) + arg)
            return self._get(arg)
        elif type(arg) == slice:
            new = self.__copy__()
            if arg.step and arg.step < 0:
                new = new._get_inverted()
            
            if arg.start and arg.stop:
                new = new._get_from_to(arg.start, arg.stop)
            elif arg.start:
                new = new._get_last_n(len(self) - arg.start)
            elif arg.stop:
                new = new._get_first_n(arg.stop)
            
            return new
        else:
            raise IndexError('Invalid Index')
    
    def _get(self, idx: int):
        if idx < 0:
            raise IndexError(f"Index out of range \nGiven: {idx - len(self)} Length: {len(self)}")
        tmp = self.head
        i = idx
        while i > 0:
            tmp = tmp.next
            if tmp == None:
                raise IndexError(f"Index out of range \nGiven: {idx} Length: {len(self)}")
            i -= 1
        return tmp.data
    
    def _get_inverted(self) -> 'LinkedList':
        new = self.__copy__()
        tmp = self.head
        prev = None
        while tmp:
            new.head = Node(tmp.data)
            new.head.next = prev
            prev = new.head
            tmp = tmp.next
        return new
    
    def _get_first_n(self, n) -> 'LinkedList':
        tmp = self.head
        new = LinkedList()
        while n > 0 and tmp:
            new.append(tmp.data)
            tmp = tmp.next
            n -= 1
        return new
    
    def _get_last_n(self, n) -> 'LinkedList':
        tmp = self._get_inverted().head
        new = LinkedList()
        while n > 0 and tmp:
            new.push(tmp.data)
            tmp = tmp.next
            n -= 1
        return new
    
    def _get_from_to(self, f, t) -> 'LinkedList':
        tmp = self._get_last_n(len(self) - f)
        return tmp._get_first_n(t - (len(self) - len(tmp)))
    
    def _get_all_nth(self, n) -> 'LinkedList':
        if n == 0:
            raise ValueError('Cannot step by 0')
        new = LinkedList()
        tmp = self.head
        while tmp:
            new.append(tmp.data)
            for _ in range(n):
                if not tmp:
                    continue
                tmp = tmp.next
        return new
    
    def is_empty(self) -> bool:
        return len(self) == 0
    
    def push(self, data) -> None:
        tmp = self.head
        newHead = Node(data)
        newHead.next = tmp
        self.head = newHead
        self._length += 1
    
    def append(self, data) -> None:
        tmp = self.head
        if self.is_empty():
            self.head = Node(data)
        else:
            while tmp.next:
                tmp = tmp.next
            tmp.next = Node(data)
            
        self._length += 1
    
    def append_all(self, data: Iterable) -> None:
        for val in data:
            self.append(val)
            
    def invert(self) -> None:
        self = self._get_inverted()
