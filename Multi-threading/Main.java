package com.company;
import java.util.Scanner;

class Multiply implements Runnable {
    private int[][] A;
    private int[][] B;
    private int[][] C;

    Multiply(int[][] M1, int[][] M2, int[][] R) {
        this.A = M1;
        this.B = M2;
        this.C = R;
    }
    @Override
    public void run() {
        if(Thread.currentThread().getName().equals("1"))
        {
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < B[i].length / 2; j++) {
                    int sum = 0;
                    for (int k = 0; k < A[i].length; k++) {
                        sum += (A[i][k] * B[k][j]);
                    }
                    C[i][j] = sum;
                    System.out.println("Thread " + Thread.currentThread().getName() + " --> " +
                                        "[" + i + "]" + "[" + j + "] = " + sum);
                }
            }
        }
        else
        {
            for (int i = 0; i < A.length; i++) {
                for (int j = B[i].length / 2; j < B[i].length; j++) {
                    int sum = 0;
                    for (int k = 0; k < A[i].length; k++) {
                        sum += (A[i][k] * B[k][j]);
                    }
                    C[i][j - (B[i].length / 2)] = sum;
                    System.out.println("Thread " + Thread.currentThread().getName() + " --> " +
                            "[" + i + "]" + "[" + j + "] = " + sum);
                }
            }
        }
    }

    public void Output() {
        for (int i = 0; i < C.length; i++) {
            for (int j = 0; j < C[i].length; j++) {
                System.out.print(C[i][j] + " ");
            }
            System.out.println();
        }
    }
}

public class Main {

    public static void print(int[][] M) {
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[i].length; j++) {
                System.out.print(M[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void combine(int[][] M1, int[][] M2, int[][] Result) {
        for (int i = 0; i < M1.length; i++) {
            for (int j = 0; j < M1[0].length; j++) {
                Result[i][j] = M1[i][j];
            }
        }
        int index = M1[0].length;
        for (int i = 0; i < M2.length; i++) {
            for (int j = index; j < M2[0].length + index; j++) {
                Result[i][j] = M2[i][j - index];
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Get Input
        Scanner Input = new Scanner(System.in);
        int r1, c1, r2, c2;
        do {
            System.out.print("R1: ");
            r1 = Input.nextInt();
            System.out.print("C1: ");
            c1 = Input.nextInt();
            System.out.print("R2: ");
            r2 = Input.nextInt();
            System.out.print("C2: ");
            c2 = Input.nextInt();
            if (c1 != r2 || c1 == 0 || r1 == 0 || c2 == 0 || r2 == 0)
                System.out.println("Invalid! Re-enter rows & columns valid for multiplication->");
        } while (c1 != r2  || c1 == 0 || r1 == 0 || c2 == 0 || r2 == 0);

        int[][] A = new int[r1][c1];
        int[][] B = new int[r2][c2];
        System.out.println("Enter the values of A:");
        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c1; j++) {
                int row = Input.nextInt();
                A[i][j] = row;
            }
        }
        System.out.println("Enter the values of B:");
        for (int i = 0; i < r2; i++) {
            for (int j = 0; j < c2; j++) {
                int row = Input.nextInt();
                B[i][j] = row;
            }
        }

        /* int[][] A = new int[][]{ {1, 2, 5}, {4, 5, 3} }; */
        /* int[][] B = new int[][]{ {2, 2, 8}, {6, 1, 9}, {4, 3, 0} }; */

        int l = B[0].length / 2;
        int[][] C1 = new int[A.length][l];
        int[][] C2;
        if (B[0].length % 2 == 0)
            C2 = new int[A.length][l];
        else
            C2 = new int[A.length][l + 1];

        Multiply M1 = new Multiply(A, B, C1);
        Multiply M2 = new Multiply(A, B, C2);

        Thread T1 = new Thread(M1);
        Thread T2 = new Thread(M2);

        System.out.println("Matrix A:");
        print(A);
        System.out.println("Matrix B:");
        print(B);

        T1.setName("1");
        T2.setName("2");

        System.out.println("------------------------");

        T1.start();
        T2.start();

        T1.join();
        T2.join();

        System.out.println("------------------------");

        System.out.println("Thread 1:");
        M1.Output();

        System.out.println("Thread 2:");
        M2.Output();

        System.out.println("Result:");
        int[][] C = new int[A.length][B[0].length];
        combine(C1, C2, C);
        print(C);
    }
}

