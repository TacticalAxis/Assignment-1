package comp611.assignment1;

import java.util.Arrays;

public abstract class Task<E,F> implements Runnable {

    private final E param;

    public Task(E param) {this.param = param;}

    public abstract int getId();

    public abstract void addListener(TaskObserver<F> observer);

    public abstract void removeListener(TaskObserver<F> observer);

    protected abstract void notifyAll(F progress);

//    public abstract void run();

    public static void main(String[] args) {
        // make a two dimensional array
        int[][] array = new int[10][10];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = i * j;
            }
        }

        // print out the array formatted nicely
        for (int i = 0; i < array.length; i++) {
            System.out.println(Arrays.toString(array[i]));
        }

        // write a lambda expression to get all the elements in column x
        for (int i = 0; i < array.length; i++) {
            char[] setToCheck = new char[array[0].length];
            for (int j = 0; j < array[0].length; j++) {
                setToCheck[j] = (char) array[i][j];
            }
            System.out.println(Arrays.toString(setToCheck));
        }
//        int x = 1;
//        Integer[] column = Arrays.stream(array[x]).boxed().toArray(Integer[]::new);
//        System.out.println(Arrays.toString(column));
    }
}