package Less_3;


import com.sun.source.tree.IfTree;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] strArr = new String[]{"сиськи", "письки", "хер", "дотан", "хз", "сиськи", "заебался", "дотан", "заебался", "сиськи", "сиськи"};
        SumString(strArr);
        PhoneBook book = new PhoneBook();


    }










    public static void SumString(String[] arr) {
        Map<String, Integer> mapa = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            if (mapa.containsKey(arr[i])) {
                int a = mapa.get(arr[i]);
                mapa.put(arr[i], a+1);

            } else {
                mapa.put(arr[i], 1);
            }

        }
        System.out.println(mapa);
        System.out.println(mapa.keySet());
    }
}
