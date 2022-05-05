package coding;

import huffman.Forest;
import huffman.Statistics;
import huffman.Tree;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Stellt Methoden für das Kodieren und Dekodieren von Dateien mit Hilfe der Huffman-Kodierung zur
 * Verfügung.
 *
 * @author mhe, Konstantin Opora inf104952, Lennard Kirchner inf104888
 */
public class HuffmanCoding {
    /**
     * Anzahl der zu erzeugenden Blöcke bei Nutzung der main-Methode
     */
    public static final int DEFAULT_MAIN_BLOCK_COUNT = 4;

    /**
     * Kodiert die übergebene Datei mit einem Block.
     *
     * @param srcPath Pfad zur Quelldatei, darf nicht null sein
     * @param dstPath Pfad zur Zieldatei, darf nicht null oder gleich srcPath sein
     * @return Anzahl der Bits der Kodierung (ohne Padding-Bits)
     * @throws IOException      Fehler beim Lesen oder Schreiben der Dateien
     * @throws RuntimeException Fehler beim Lesen oder Schreiben der Dateien
     */
    public static long encode(String srcPath, String dstPath) throws IOException {
        return encode(srcPath, dstPath, 1);
    }

    /**
     * Kodiert die übergebene Quelldatei in die übergebene Zieldatei. Dabei werden so viele Blöcke
     * erzeugt, wie vom Aufrufer angegeben.
     *
     * @param srcPath    Pfad zur Quelldatei, darf nicht null sein
     * @param dstPath    Pfad zur Zieldatei, darf nicht null oder gleich srcPath sein
     * @param blockCount Anzahl der zu erzeugenden Blöcke, muss &ge; 1 sein
     * @return Anzahl der Bits der Kodierung (ohne Padding-Bits)
     * @throws IOException      Fehler beim Lesen oder Schreiben der Dateien
     * @throws RuntimeException Fehler beim Lesen oder Schreiben der Dateien
     */
    public static long encode(String srcPath, String dstPath, int blockCount) throws IOException {
        if (srcPath == null) {
            throw new IllegalArgumentException("srcPath darf nicht null sein");
        }
        if (dstPath == null) {
            throw new IllegalArgumentException("dstPath darf nicht null sein");
        }
        if (srcPath.equals(dstPath)) {
            throw new IllegalArgumentException("dstPath darf nicht gleich srcPath sein");
        }
        if (blockCount < 1) {
            throw new IllegalArgumentException("blockCount muss >= 1 sein");
        }

        long resultBits = 0; // Anzahl geschriebener Bits

        // Einlesen der Quelldatei
        byte[] srcbytes = Files.readAllBytes(Paths.get(srcPath));

        // Analyse der Quelldatei
        Statistics statistics = Statistics.analyse(srcbytes);

        // Wald erzeugen
        Forest forest = statistics.toForest();

        // Erstellen eines Huffmanbaums
        Tree tree = forest.toTree();

        // Erzeugen der Kodierungstabelle
        BitChain[] kodierungsTabelle = tree.toCodetable();

        //Statistik in Outputstream übertragen
        DataOutputStream output = new DataOutputStream(Files.newOutputStream(Paths.get(dstPath)));
        statistics.writeTo(output);


        // Blockgröße in Output schreiben
        output.writeInt(blockCount);

        // Arrays von Tasks und Threads damit jeder Block als eigener Thread gelöst wird
        EncoderTask[] tasks = new EncoderTask[blockCount];
        Thread[] threads = new Thread[blockCount];

        int startIdx = 0;
        for (int i = 0; i < blockCount; i++) {
            int numOfBytes = srcbytes.length / blockCount
                    + (i < srcbytes.length % blockCount ? 1 : 0);

            tasks[i] = new EncoderTask(kodierungsTabelle, srcbytes, startIdx, numOfBytes);
            threads[i] = new Thread(tasks[i]);

            threads[i].start();

            startIdx += numOfBytes;
        }


        for (int i = 0; i < blockCount; i++) {
            try {
                // Zusammenführen der Threads
                threads[i].join();
                // Schreiben der Längen jedes Blockes
                output.writeInt(tasks[i].getResult().length);
            } catch (InterruptedException e) {
                throw new AssertionError("should not happen", e);
            }
        }

        // Schreiben der Blöcke
        for (EncoderTask task : tasks) {
            output.write(task.getResult());
            resultBits += task.getBitsInResult();
        }

        output.flush();

        return resultBits;
    }

    /**
     * Dekodiert die übergebene Quelldatei in die übergebene Zieldatei.
     *
     * @param srcPath Pfad zur Quelldatei, darf nicht null sein
     * @param dstPath Pfad zur Zieldatei, darf nicht null oder gleich srcPath sein
     * @throws IOException      Fehler beim Lesen oder Schreiben der Dateien
     * @throws RuntimeException Fehler beim Lesen oder Schreiben der Dateien
     */
    public static void decode(String srcPath, String dstPath) throws IOException {
        if (srcPath == null) {
            throw new IllegalArgumentException("srcPath darf nicht null sein");
        }
        if (dstPath == null) {
            throw new IllegalArgumentException("dstPath darf nicht null sein");
        }
        if (srcPath.equals(dstPath)) {
            throw new IllegalArgumentException("dstPath darf nicht gleich srcPath sein");
        }
        DataInputStream input = new DataInputStream(Files.newInputStream(Paths.get(srcPath)));
        Statistics statistics = Statistics.readFrom(input);

        // Wald erzeugen
        Forest forest = statistics.toForest();

        // Erstellen eines Huffmanbaums
        Tree tree = forest.toTree();

        int srcSize = (int) statistics.sum(); // Anzahl aller Zeichen

        //Auslesen Anzahl der Blöcke
        int numOfBlocks = input.readInt();

        // Array mit Blockgrößen
        int[] blockSizes = new int[numOfBlocks];

        // Blockgrößen auslesen
        for (int i = 0; i < numOfBlocks; i++) {
            blockSizes[i] = input.readInt();
        }

        // Arrays von Tasks und Threads damit jeder Block als eigener Thread gelöst wird
        DecoderTask[] tasks = new DecoderTask[numOfBlocks];
        Thread[] threads = new Thread[numOfBlocks];


        for (int i = 0; i < numOfBlocks; i++) {
            // Auslesen der Blöcke
            byte[] blockBytes = new byte[blockSizes[i]];

            assert input.read(blockBytes, 0, blockBytes.length) == blockBytes.length;

            ByteArrayInputStream inputStream = new ByteArrayInputStream(blockBytes);

            //Anzahl der zu decodierenden Zeichen
            int numOfBytes = srcSize / numOfBlocks + (i < srcSize % numOfBlocks ? 1 : 0);

            tasks[i] = new DecoderTask(tree, new BitIterator(inputStream), numOfBytes);
            threads[i] = new Thread(tasks[i]);

            threads[i].start();
        }

        byte[] result = new byte[srcSize];  // Ergebnis
        int startIdx = 0;

        for (int i = 0; i < tasks.length; i++) {
            try {
                // Zusammenführen der Threads
                threads[i].join();
                // Zusammensetzen des Ergebnisses
                byte[] bi = tasks[i].getResult();
                System.arraycopy(bi, 0, result, startIdx, bi.length);
                startIdx += bi.length;

            } catch (InterruptedException e) {
                throw new AssertionError("should not happen", e);
            }
        }

        // Schreiben in File
        Files.write(Paths.get(dstPath), result);
    }


    /**
     * Gibt den Hilfetext auf dem übergebenen PrintStream aus.
     *
     * @param s zu nutzender PrintStream
     * @pre s != null;
     */
    private static void printUsage(PrintStream s) {
        assert s != null;

        s.println("Huffman coding");
        s.println("Usage:");
        s.println("-e SRC DST");
        s.println("   Encode file SRC to DST");
        s.println("-d SRC DST");
        s.println("   Decode file SRC to DST");
    }

    /**
     * Hauptprogramm
     *
     * @param args Programmargumente
     * @throws IOException Fehler beim Lesen oder Schreiben der Dateien
     */
    public static void main(String[] args) throws IOException {
        final int numberOfArgs = 3;
        if (args.length == numberOfArgs) {
            switch (args[0]) {
                case "-e":
                    encode(args[1], args[2], DEFAULT_MAIN_BLOCK_COUNT);
                    break;
                case "-d":
                    decode(args[1], args[2]);
                    break;
                default:
                    printUsage(System.err);
                    break;
            }
        } else {
            printUsage(System.err);
        }
    }
}
