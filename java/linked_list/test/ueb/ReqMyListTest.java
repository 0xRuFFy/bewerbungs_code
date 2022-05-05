package ueb;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests für MyList (isEmpty, size, isSorted, containsElement und getElementAt)
 *
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class ReqMyListTest {

    /**
     * Erzeugt eine Liste mit den übergebenen Werten.
     * @param values Werte, die in die Liste eingefügt werden sollen.
     * @return Liste mit den Werten.
     * @author klk
     */
    private MyList createList(char... values) {
        MyList list = new MyList();
        for (char i : values) {
            list.appendElement(i);
        }
        return list;
    }

    @Test
    public void isEmpty() {
        MyList list = createList('a', 'b', 'e');
        assertFalse(list.isEmpty());

        list = createList('a');
        assertFalse(list.isEmpty());

        list = createList();
        assertTrue(list.isEmpty());
    }

    @Test
    public void size() {
        MyList list = createList('a', 'b', 'e');
        assertEquals(3, list.size());

        list = createList('a');
        assertEquals(1, list.size());

        list = createList();
        assertEquals(0, list.size());
    }

    @Test
    public void isSorted() {
        MyList list = createList('a', 'b', 'e');
        assertTrue(list.isSorted());

        list = createList('a', 'b', 'b', 'c');
        assertTrue(list.isSorted());

        list = createList('b', 'a', 'c');
        assertFalse(list.isSorted());
    }

    @Test
    public void containsElement() {
        MyList list = createList('a', 'b', 'e');
        assertTrue(list.containsElement('a'));

        list = createList('a', 'b', 'e');
        assertTrue(list.containsElement('b'));

        list = createList('a', 'b', 'e');
        assertTrue(list.containsElement('e'));

        list = createList('a', 'b', 'e');
        assertFalse(list.containsElement('c'));

        list = createList();
        assertFalse(list.containsElement('a'));
    }

    @Test
    public void getElementAt() {
        MyList list = createList('a', 'b', 'e');
        assertEquals('a', list.getElementAt(0));

        list = createList('a', 'b', 'e');
        assertEquals('b', list.getElementAt(1));

        list = createList('a', 'b', 'e');
        assertEquals('e', list.getElementAt(2));

        list = createList('a', 'b', 'e');
        assertEquals(Element.INVALID_VALUE, list.getElementAt(-1));

        list = createList();
        assertEquals(Element.INVALID_VALUE, list.getElementAt(2));
    }
}