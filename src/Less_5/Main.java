package Less_5;
import java.util.Arrays;

public class Main extends Thread {

    static final int SIZE = 10000000;
    static final int H = SIZE / 2;
    static long a = System.currentTimeMillis();
    static float[] arr = new float[SIZE];

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
        thread(arrMethod1);
        thread(arrMethod2);

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
        Runnable r = new Runnable() {
            @Override
            public void run() {
                MathArray(firstHalf);
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                MathArray(secondHalf);
            }
        };
        thread(r);
        thread(r2);

        System.arraycopy(firstHalf, 0, arr, 0, H);
        System.arraycopy(secondHalf, 0, arr, H, H);
        System.out.println(System.currentTimeMillis() - a);
    }

    public static void thread (Runnable r) {
       Thread threadNew = new Thread(r);
       threadNew.start();
    }

    public  static void MathArray (float[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5)
                    * Math.cos(0.4f + i / 2));
        }
    }
}
