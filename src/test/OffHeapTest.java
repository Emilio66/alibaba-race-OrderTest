package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zhaoz on 2016/7/27.
 */
public class OffHeapTest {

    public static void main(String[] args) throws InterruptedException, IOException {
        Thread.sleep(10000);
        long start1 = System.nanoTime();
        ByteBuffer heapBuffer = ByteBuffer.allocate(512 << 20);
        long end1 = System.nanoTime();
        System.out.println("JVM Heap allocating 1G takes: "+(end1 - start1) +" ns");

        start1 = System.nanoTime();
        for(int i=0; i < 512; i++)
            heapBuffer.put(new byte[1 << 20]);
        end1 = System.nanoTime();
        System.out.println("JVM Heap writing 512 takes: "+(end1 - start1) +" ns" +
                ", isDirect: "+heapBuffer.isDirect());

        long start2 = System.nanoTime();
        ByteBuffer offHeapBuffer = ByteBuffer.allocateDirect(1 << 30 -1);
        long end2 = System.nanoTime();
        System.out.println("Off Heap allocating 1G takes: "+(end2 - start2) +" ns"+
                ", isDirect: "+offHeapBuffer.isDirect());

        start2 = System.nanoTime();
        for(int i=0; i < 512; i++)
            offHeapBuffer.put(new byte[1 << 20]);
        end2 = System.nanoTime();
        System.out.println("Off Heap writing 512 takes: "+(end2 - start2) +" ns");

        long start3 = System.nanoTime();
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File("a.txt"), "rw");
        MappedByteBuffer mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE,
                0, 1 << 30);
        long end3 = System.nanoTime();
        System.out.println("Mapped Heap allocating 1G takes: "+(end3 - start3) +" ns" +
                ", isDirect: "+mappedByteBuffer.isDirect());

        start3 = System.nanoTime();
        for(int i=0; i < 512; i++)
            mappedByteBuffer.put(new byte[1 << 20]);
        end3 = System.nanoTime();
        System.out.println("Mapped Heap writing 512M takes: "+(end3 - start3)  +" ns");
        Thread.sleep(350000);
    }
}
