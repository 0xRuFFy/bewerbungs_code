package ueb;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PubTests müssen zu Beginn der Abgabe erfolgreich sein, sonst kann kein Testat für diese Aufgabe erworben werden.
 * @author Tobias Schrock (inf104926) und Konstantin Opora (inf104952)
 */
public class PubElementTest {

    /**
     * Erzeugt ein Element mit übergebenem Wert, bei mehreren Werten werden
     * weitere Elemente angehängt.
     *
     * @param value Wert(e) des Elements/der Elemente
     * @return Element mit Wert, bei mehreren Werten mehrere aneinandergehängte
     * Elemente
     */
    private Element createElements(char... value) {
        if (value.length == 0) {
            return null;
        }
        // ein Element anlegen
        Element el = new Element(value[0]);
        Element firstEl = el; //erstes Element merken

        // weitere Elemente anlegen
        for (int i = 1; i < value.length; i++) {
            el.setNext(new Element(value[i]));
            el = el.getNext();
        }
        return firstEl;
    }

    //-----------------------------------------------

    @Test
    public void testConstructor_char_Element() {
        Element el1 = new Element('z', null);
        Element el2 = new Element('y', el1);
        Element el3 = new Element('x', el2);
        assertEquals('x', el3.getValue());
        assertEquals('y', el3.getNext().getValue());
        assertEquals('z', el3.getNext().getNext().getValue());
        assertNull(el3.getNext().getNext().getNext());
    }
    //-----------------------------------------------


    @Test
    public void testAppendElement_ToOneElement() {
        Element el = new Element('a');
        Element result = el.appendElement('b');
        assertEquals('a', result.getValue());
        assertEquals('b', result.getNext().getValue());
        assertNull(result.getNext().getNext());
    }

    @Test
    public void testAppendElement_Twice() {
        Element el = new Element('a');
        Element result = el.appendElement('b').appendElement('c');
        assertEquals('a', result.getValue());
        assertEquals('b', result.getNext().getValue());
        assertEquals('c', result.getNext().getNext().getValue());
        assertNull(result.getNext().getNext().getNext());
    }

    //-----------------------------------------------

    @Test
    public void testInsertElement_AtFront() {
        Element el = createElements('b', 'd');
        Element result = el.insertElement('a');
        assertEquals('a', result.getValue());
        assertEquals('b', result.getNext().getValue());
        assertEquals('d', result.getNext().getNext().getValue());
        assertNull(result.getNext().getNext().getNext());
    }

    @Test
    public void testInsertElement_InMiddle() {
        Element el = createElements('b', 'd');
        Element result = el.insertElement('c');
        assertEquals('b', result.getValue());
        assertEquals('c', result.getNext().getValue());
        assertEquals('d', result.getNext().getNext().getValue());
        assertNull(result.getNext().getNext().getNext());
    }

    @Test
    public void testInsertElement_AtEnd() {
        Element el = createElements('b', 'd');
        Element result = el.insertElement('e');
        assertEquals('b', result.getValue());
        assertEquals('d', result.getNext().getValue());
        assertEquals('e', result.getNext().getNext().getValue());
        assertNull(result.getNext().getNext().getNext());
    }

    //-----------------------------------------------

    @Test
    public void testDeleteElement_AtFront() {
        Element el = createElements('a', 'b', 'c');
        Element result = el.deleteElement('a');
        assertEquals('b', result.getValue());
        assertEquals('c', result.getNext().getValue());
        assertNull(result.getNext().getNext());
    }

    @Test
    public void testDeleteElement_InMiddle() {
        Element el = createElements('a', 'b', 'c');
        Element result = el.deleteElement('b');
        assertEquals('a', result.getValue());
        assertEquals('c', result.getNext().getValue());
        assertNull(result.getNext().getNext());
    }

    @Test
    public void testDeleteElement_AtEnd() {
        Element el = createElements('a', 'b', 'c');
        Element result = el.deleteElement('c');
        assertEquals('a', result.getValue());
        assertEquals('b', result.getNext().getValue());
        assertNull(result.getNext().getNext());
    }

    @Test
    public void testDeleteElement_NotExisting() {
        Element el = createElements('a', 'b', 'c');
        Element result = el.deleteElement('d');
        assertEquals('a', result.getValue());
        assertEquals('b', result.getNext().getValue());
        assertEquals('c', result.getNext().getNext().getValue());
        assertNull(result.getNext().getNext().getNext());
    }


    //-----------------------------------------------


    @Test
    public void testSize() {
        Element el = createElements('a');
        assertEquals(1, el.size());

        el = createElements('a', 'b', 'c');
        assertEquals(3, el.size());
    }


    @Test
    public void testExistsElement_First() {
        Element el = createElements('b', 'c', 'e');
        assertTrue(el.containsElement('b'));
    }

    @Test
    public void testExistsElement_Middle() {
        Element el = createElements('b', 'c', 'e');
        assertTrue(el.containsElement('c'));
    }

    @Test
    public void testExistsElement_Last() {
        Element el = createElements('b', 'c', 'e');
        assertTrue(el.containsElement('e'));
    }

    @Test
    public void testExistsElement_NotExisting() {
        Element el = createElements('b', 'c', 'e');
        assertFalse(el.containsElement('a'));
        assertFalse(el.containsElement('d'));
        assertFalse(el.containsElement('f'));
    }

    @Test
    public void testIsSorted_Gapless() {
        Element el = createElements('b', 'c', 'd');
        assertTrue(el.isSorted());
    }

    @Test
    public void testIsSorted_WithGaps() {
        Element el = createElements('a', 'c', 'e');
        assertTrue(el.isSorted());
    }

    @Test
    public void testIsSorted_DoubleValues() {
        Element el = createElements('b', 'b', 'e');
        assertTrue(el.isSorted());
        el = createElements('b', 'e', 'e');
        assertTrue(el.isSorted());
        el = createElements('b', 'c', 'c', 'e', 'e');
        assertTrue(el.isSorted());
    }

    @Test
    public void testIsSorted_NotSorted() {
        Element el = createElements('a', 'b', 'd', 'c');
        assertFalse(el.isSorted());
        el = createElements('a', 'c', 'b', 'd');
        assertFalse(el.isSorted());
        el = createElements('b', 'c', 'a', 'd');
        assertFalse(el.isSorted());
    }

    //-----------------------------------------------

    @Test
    public void testToString() {
        Element el = createElements('a', 'b', 'c');
        assertEquals("a b c", el.toString());

        el = createElements('b');
        assertEquals("b", el.toString());
    }

    //-----------------------------------------------

    @Test
    public void testGetElementAt() {
        Element el = createElements('a', 'b', 'c');
        assertEquals('a', el.getElementAt(0));
        assertEquals('b', el.getElementAt(1));
        assertEquals('c', el.getElementAt(2));
    }

    @Test
    public void testGetElementAt_InvalidArgument() {
        Element el = createElements('a', 'b', 'c');
        assertEquals(0, el.getElementAt(-1));
        assertEquals(0, el.getElementAt(3));
    }

    //-----------------------------------------------

    @Test
    public void testInsertElementAt_Front() {
        Element el = createElements('a', 'b', 'c');
        Element result = el.insertElementAt('A', 0);
        assertEquals('A', result.getValue());
        assertEquals('a', result.getNext().getValue());
        assertEquals('b', result.getNext().getNext().getValue());
        assertEquals('c', result.getNext().getNext().getNext().getValue());
    }

    @Test
    public void testInsertElementAt_Middle() {
        Element el = createElements('a', 'b', 'c');
        Element result = el.insertElementAt('B', 1);
        assertEquals('a', result.getValue());
        assertEquals('B', result.getNext().getValue());
        assertEquals('b', result.getNext().getNext().getValue());
        assertEquals('c', result.getNext().getNext().getNext().getValue());
    }

    @Test
    public void testInsertElementAt_End() {
        Element el = createElements('a', 'b');
        Element result = el.insertElementAt('C', 2);
        assertEquals('a', result.getValue());
        assertEquals('b', result.getNext().getValue());
        assertEquals('C', result.getNext().getNext().getValue());
        assertNull(result.getNext().getNext().getNext());
    }

    @Test
    public void testInsertElementAt_InvalidIndex() {
        Element el = createElements('b', 'c');
        Element result = el.insertElementAt('A', -1);
        assertEquals(2, result.size());
        assertEquals('b', result.getValue());
        assertEquals('c', result.getNext().getValue());
        assertNull(result.getNext().getNext());

        el = createElements('b', 'c');
        result = el.insertElementAt('A', 3);
        assertEquals(2, result.size());
        assertEquals('b', result.getValue());
        assertEquals('c', result.getNext().getValue());
        assertNull(result.getNext().getNext());
    }

    //-----------------------------------------------

    @Test
    public void testPrependElement() {
        Element el = createElements('a', 'b');
        Element result = el.prependElement('A');
        assertEquals('A', result.getValue());
        assertEquals('a', result.getNext().getValue());
        assertEquals('b', result.getNext().getNext().getValue());
        assertNull(result.getNext().getNext().getNext());
    }

}
