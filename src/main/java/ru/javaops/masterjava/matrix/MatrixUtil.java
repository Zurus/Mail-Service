package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static ru.javaops.masterjava.matrix.MainMatrix.cdl;

public class MatrixUtil {

    public static final List<?> tasks = new ArrayList<>();

    protected static Runnable fillResultMatrixRox(int[][] matrixA, int[][] matrixB, int[][] matrixC, int i, int j, int matrixSize) {
        return () -> {
            int sum = 0;
            for (int k = 0; k < matrixSize; k++) {
                sum += matrixA[i][k] * matrixB[k][j];
            }
            matrixC[i][j] = sum;
            cdl.countDown();

            //System.out.println(cdl.getCount());
            //System.out.println("******************Поток " + i+ " " + j + " завершил работу!******************");
        };
    }

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                executor.submit(fillResultMatrixRox(matrixA, matrixB, matrixC, i, j, matrixSize));
            }
        }
        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
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
                int diff = Math.abs(matrixA[i][j] - matrixB[i][j]);
                if (diff != 0) {
                    System.out.println(diff);
                    System.out.println(String.format("Битая часть! i = %d , j = %d , A[i][j] = %d, B[i][j] = %d", i, j, matrixA[i][j], matrixB[i][j]));
                    return false;
                }
            }

        }
        return true;
    }
}
