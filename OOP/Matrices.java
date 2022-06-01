package com.company;

interface Addable {
    Object Add(Object obj);
}

class Matrix implements Addable {
    private int[][] Numbers = null; // Matrix
    private int M = 0; // Rows
    private int N = 0; // Columns

    // getters
    public int getRows() {
        return this.M;
    }

    public int getCell(int i, int j) {
        return Numbers[i][j];
    }

    public int getCol() {
        return this.N;
    }

    // Erase Numbers
    public void Erase() {
        Numbers = null;
    }

    // Constructor
    public Matrix(int rows, int col) {
        if (rows > 0)
            this.M = rows;
        else
            System.out.println("Number of rows must be greater than 0");
        if (col > 0)
            this.N = col;
        else
            System.out.println("Number of columns must be greater than 0");
        if (M > 0 && N > 0)
            this.Numbers = new int[M][N];
    }

    //Override Add
    public Object Add(Object obj) {
        Matrix Sum = null;
        if ((obj instanceof Matrix)
                && this.M == ((Matrix)obj).M
                    && this.N == ((Matrix)obj).N) {
            if (this.Numbers != null && ((Matrix)obj).Numbers != null) {
                Sum = new Matrix(this.M, this.N);
                for (int i = 0; i < M; i++) {
                    for (int j = 0; j < N; j++) {
                        Sum.Numbers[i][j] = this.Numbers[i][j] + ((Matrix) obj).Numbers[i][j];
                    }
                }
            }
        }
        return Sum;
    }

    // SetNumbers
    public boolean SetNumbers(int[] arr) {
        if ((this.M * this.N) != arr.length) {
            Numbers = null;
            return false;
        }
        int index = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                Numbers[i][j] = arr[index];
                index++;
            }
        }
        return true;
    }

    // Print
    public void Print() {
        if (Numbers != null) {
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(Numbers[i][j] + " ");
                }
                System.out.println();
            }
        }
    }

    // Transpose
    public void Transpose() {
        if (Numbers != null) {
            int[][] TNumbers = new int[N][M];
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    TNumbers[j][i] = Numbers[i][j];
                }
            }
            // swap
            int temp = M;
            M = N;
            N = temp;

            Numbers = TNumbers;
        }
    }
}

class IdentityMatrix extends Matrix {
    // Constructor
    public IdentityMatrix(int r, int c) {
        super(r, c);
    }

    @Override
    public boolean SetNumbers(int[] arr) {
        boolean set = super.SetNumbers(arr);
        boolean valid = true;
        if (!set || (this.getRows() != this.getCol()))
            valid = false;
        else {
            outer_loop:
            for (int i = 0; i < this.getRows(); i++) {
                for (int j = 0; j < this.getCol(); j++) {
                    if (i == j) {
                        if (this.getCell(i, j) != 1) {
                            valid = false;
                            break outer_loop;
                        }
                    } else {
                        if (this.getCell(i, j) != 0) {
                            valid = false;
                            break outer_loop;
                        }
                    }
                }
            }
        }
        if (valid)
            return true;
        this.Erase();
        return false;
    }

    @Override
    public void Transpose() {
        super.Transpose();
        // Original Matrix Unchanged
    }
}

public class Matrices {

    public static void main(String[] args) {
        int[] arr1 = new int[]{4, 3, 7, 1, 5, 2};
        int[] arr2 = new int[]{1, 6, 4, 5, 7, 3};

        Matrix M1 = new Matrix(2, 3);
        if (M1.SetNumbers(arr1))
            System.out.println("1 - Verified");
        else
            System.out.println("1 - Error");
        System.out.println("Matrix 1:");
        M1.Print();
        System.out.println();

        Matrix M2 = new Matrix(2, 3);
        if (M2.SetNumbers(arr2))
            System.out.println("2 - Verified");
        else
            System.out.println("2 - Error");
        System.out.println("Matrix 2:");
        M2.Print();
        System.out.println();

        Object Sum = M1.Add(M2);
        System.out.println("Sum of 1 and 2:");
        if (Sum != null)
            ((Matrix)Sum).Print();
        else
            System.out.println("Cannot add these matrices!");
        System.out.println();

        System.out.println("Matrix 1 Transpose:");
        M1.Transpose();
        M1.Print();
        System.out.println();
        int[] id = new int[]{1, 0, 0, 0, 1, 0, 0, 0, 1};

        IdentityMatrix I = new IdentityMatrix(3,3);
        if (I.SetNumbers(id))
            System.out.println("Identity - Verified");
        else
            System.out.println("Not an Identity Matrix!");
        System.out.println("Identity Matrix:");
        I.Print();
        System.out.println();

        System.out.println("Identity Transpose:");
        I.Transpose();
        I.Print();
    }
}
