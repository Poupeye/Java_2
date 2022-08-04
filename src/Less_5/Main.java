package Less_5;

import java.util.Arrays;

public class Main extends Thread {

    static final int SIZE = 10000000;
    static final int H = SIZE / 2;
    static long a = System.currentTimeMillis();
    static float[] arr = new float[SIZE];

    public static void main(String[] args) {
        thread(new Runnable() {
            @Override
            public void run() {
                Array();
            }
        });

        thread(new Runnable() {
            @Override
            public void run() {
                ArrayDouble();
            }
        });
    }

    public static void Array() {
        Arrays.fill(arr, 1);
        MathArray(arr);
        System.out.println(System.currentTimeMillis() - a);
    }

    public static void ArrayDouble() {
        Arrays.fill(arr, 1);
        float[] firstHalf = new float[H];
        float[] secondHalf = new float[H];
        System.arraycopy(arr, 0, firstHalf, 0, H);
        System.arraycopy(arr, H, secondHalf, 0, H);
        thread(new Runnable() {
            @Override
            public void run() {
                MathArray(firstHalf);
            }
        });

        thread(new Runnable() {
            @Override
            public void run() {
                MathArray(secondHalf);
            }
        });


        System.arraycopy(firstHalf, 0, arr, 0, H);
        System.arraycopy(secondHalf, 0, arr, H, H);
        System.out.println(System.currentTimeMillis() - a);
    }

    public static void thread(Runnable r) {
        Thread threadNew = new Thread(r);
        threadNew.start();
    }

    public static void MathArray(float[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5)
                    * Math.cos(0.4f + i / 2));
        }
    }
}
