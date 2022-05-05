package huffman;


import coding.Bit;
import coding.BitChain;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * Ein leerer Huffman-Baum.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
class Empty extends Tree {
    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int getByte() {
        throw new IllegalArgumentException();
    }

    @Override
    public long getCount() {
        return 0;
    }

    @Override
    public BitChain[] toCodetable() {
        return new BitChain[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
    }

    @Override
    protected void toGraphviz(StringBuilder sb, String prefix) {
        assert sb != null;
        assert prefix != null;
    }

    @Override
    public void translate(Iterator<Bit> iterator, long numberOfBytes,
                          OutputStream destination) throws IOException {
    }

    @Override
    public boolean isInvariant() {
        return true;
    }
}
