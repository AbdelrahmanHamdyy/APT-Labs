package com.company;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

// MPI
import mpi.*;

public class Main {

    public static void print(int[][] M) {
        System.out.println("-------------------------");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(M[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }

    public static int calcDeterminant(int me, int[][] Matrix) {
        int result = 0;
        int[] calc = new int[4];
        int col = me - 1;
        int r = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != 0 && j != col) {
                    calc[r] = Matrix[i][j];
                    r++;
                }
            }
        }
        result = Matrix[0][col] * ((calc[0] * calc[3]) - (calc[1] * calc[2]));
        if (col == 1)
            result = -1 * result;
        System.out.println("Rank " + me + ": " + result);
        return result;
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        System.out.println("Process: " + me + " | Size = " + size);
        int[][] Matrix = new int[3][3];
        int[] determinant = new int[1];
        if (me == 0) {
            try {
                File file = new File("input.txt");
                Scanner Reader = new Scanner(file);
                int i = 0;
                while (Reader.hasNextLine()) {
                    int j = 0;
                    String row = Reader.nextLine();
                    for (String s : row.split(" ")) {
                        int num = Integer.parseInt(s);
                        Matrix[i][j] = num;
                        j++;
                    }
                    i++;
                }
                Reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("Error!");
                e.printStackTrace();
            }
            print(Matrix);
        }

        for (int i = 0; i < 3; i++)
            MPI.COMM_WORLD.Bcast(Matrix[i], 0, 3, MPI.INT, 0);

        if (me != 0)
            determinant[0] = calcDeterminant(me, Matrix);

        MPI.COMM_WORLD.Reduce(determinant, 0, determinant, 0, 1, MPI.INT, MPI.SUM, 0);

        if (me == 0) {
            System.out.println("-------------------------");
            System.out.println("Determinant = " + determinant[0]);
            if (determinant[0] == 0)
                System.out.println("--> Singular Matrix");
            else
                System.out.println("--> Invertible Matrix");
        }
        MPI.Finalize();
    }
}
