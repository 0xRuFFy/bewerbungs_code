package huffman;

import java.util.PriorityQueue;

/**
 * Ein Wald von Bäumen ({@link Tree}).
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
public class Forest {
    /**
     * Liste aller Bäume aufsteigend nach ihrem Count-Wert sortiert
     */
    private final PriorityQueue<Tree> forest = new PriorityQueue<>(
            (o1, o2) -> (int) (o1.getCount() - o2.getCount())
    );


    /**
     * Fügt den übergebenen Baum in diesen Wald ein.
     *
     * @param tree Der einzufügende Baum, darf nicht null sein
     */
    public void insert(Tree tree) {
        if (tree == null) {
            throw new IllegalArgumentException("tree darf nicht null sein");
        }
        forest.add(tree);
    }

    /**
     * Wandelt den aktuellen Wald, wenn er nicht leer ist, in einen einzelnen Baum um und gibt
     * diesen zurück. Der Wald wird verändert und besteht anschließend nur noch aus einem Baum.
     * <p>
     * Wenn der Wald leer ist, dann wird der leere Baum zurückgegeben.
     *
     * @return Der resultierende Baum
     */
    public Tree toTree() {
        if (forest.size() == 0) {
            return Tree.empty();
        }
        while (forest.size() != 1) {
            // Entfernt die kleinsten beiden Bäume, merged sie
            // und fügt den neuen Baum wieder dem Wald hinzu
            insert(Tree.merge(forest.poll(), forest.poll()));
        }
        return forest.peek();
    }

}
