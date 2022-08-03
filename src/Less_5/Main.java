package Less_5;

import java.util.Arrays;

public class Main extends Thread {

    static final int size = 10000000;
    static final int h = size / 2;
    static long a = System.currentTimeMillis();

    public static void main(String[] args) {
        Runnable arrMethod1 = new Runnable() {
            @Override
            public void run() {
                Array();
            }
        };
        Runnable arrMethod2 = new Runnable() {
            @Override
            public void run() {
                ArrayDouble();
            }
        };
        Thread thread1 = new Thread(arrMethod1);
        thread1.start();
        Thread thread2 = new Thread(arrMethod2);
        thread2.start();;
    }


    public static void Array() {
        float[] arr = new float[size];
        Arrays.fill(arr, 1);
        for (int i = 0; i < size; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5)
                    * Math.cos(0.4f + i / 2));
        }
        System.out.println(System.currentTimeMillis() - a);
    }

    public static void ArrayDouble() {
        float[] arr = new float[size];
        Arrays.fill(arr, 1);
        float[] a1 = new float[h];
        float[] a2 = new float[h];
        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0, h);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < h; i++) {
                    a1[i] = (float) (a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5)
                            * Math.cos(0.4f + i / 2));
                }
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < h; i++) {
                    a2[i] = (float) (a2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5)
                            * Math.cos(0.4f + i / 2));
                }
            }
        };
        Thread t1 = new Thread(r);
        t1.start();
        Thread t2 = new Thread(r2);
        t2.start();;

        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, h, h);

        System.out.println(System.currentTimeMillis() - a);


    }
}
