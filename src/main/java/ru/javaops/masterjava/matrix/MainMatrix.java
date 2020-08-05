package ru.javaops.masterjava.matrix;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * gkislin
 * 03.07.2016
 */
public class MainMatrix {
    private static final int MATRIX_SIZE = 1000;
    private static final int THREAD_NUMBER = 10;
    private static final String FORMAT = "%-45s %6.3f";

    private final static ExecutorService executor = Executors.newFixedThreadPool(MainMatrix.THREAD_NUMBER);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final int[][] matrixA = MatrixUtil.create(MATRIX_SIZE);
        final int[][] matrixB = MatrixUtil.create(MATRIX_SIZE);

        double singleThreadSum = 0.;
        double optimizedSingleThreadSum = 0.;
        double concurrentThreadSum = 0.;
        int count = 1;
        while (count < 6) {
            System.out.println("\nPass " + count);
            long start = System.currentTimeMillis();
            final int[][] matrixC = MatrixUtil.singleThreadMultiply(matrixA, matrixB);
            double duration = (System.currentTimeMillis() - start) / 1000.;
            out(FORMAT, "Single thread time, sec:", duration);
            singleThreadSum += duration;

            start = System.currentTimeMillis();
            final int[][] optimizedMatrixC = MatrixUtil.optimizedSingleThreadMultiply(matrixA, matrixB);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out(FORMAT, "Optimized single thread time, sec:", duration);
            optimizedSingleThreadSum += duration;

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC = MatrixUtil.concurrentMultiply(matrixA, matrixB, executor);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out(FORMAT, "Concurrent thread time, sec:", duration);
            concurrentThreadSum += duration;

            if (!MatrixUtil.compare(matrixC, optimizedMatrixC) || !MatrixUtil.compare(matrixC, concurrentMatrixC)) {
                System.err.println("Comparison failed");
                break;
            }
            count++;
        }
        executor.shutdown();
        System.out.println();

        out(FORMAT, "Average single thread time, sec:", singleThreadSum / 5.);
        out(FORMAT, "Average optimized single thread time, sec:", optimizedSingleThreadSum / 5.);
        out(FORMAT, "Average concurrent thread time, sec:", concurrentThreadSum / 5.);
    }

    private static void out(String format, String message, double ms) {
        System.out.println(String.format(format, message, ms));
    }
}
