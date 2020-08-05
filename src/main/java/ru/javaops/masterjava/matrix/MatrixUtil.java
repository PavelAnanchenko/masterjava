package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] optimizedSingleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[] matrixBColumn = new int[matrixSize];

        for (int j = 0; j < matrixSize; j++) {
            arraysMultiplier(matrixA, matrixB, matrixSize, matrixC, j, matrixBColumn);
        }
        return matrixC;
    }

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) {
        final AtomicInteger threadCounter = new AtomicInteger(0);
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int z = 0; z < matrixSize; z++) {
            final int j = z;
            executor.execute(() -> {
                final int[] matrixBColumn = new int[matrixSize];
                arraysMultiplier(matrixA, matrixB, matrixSize, matrixC, j, matrixBColumn);
                threadCounter.incrementAndGet();
            });
        }
        int countDown = 20;
        try {
            while (threadCounter.get() != matrixSize || countDown > 0) {
                Thread.sleep(10);
                countDown--;
            }
        } catch (InterruptedException e) {
            e.getMessage();
        }
        return matrixC;
    }


    private static void arraysMultiplier(int[][] matrixA, int[][] matrixB, int matrixSize, int[][] matrixC, int j, int[] matrixBColumn) {
        for (int k = 0; k < matrixSize; k++) {
            matrixBColumn[k] = matrixB[k][j];
        }

        for (int i = 0; i < matrixSize; i++) {
            int sum = 0;
            int[] matrixARow = matrixA[i];
            for (int k = 0; k < matrixSize; k++) {
                sum += matrixARow[k] * matrixBColumn[k];
            }
            matrixC[i][j] = sum;
        }
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}