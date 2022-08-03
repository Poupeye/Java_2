package Less_3;
import java.util.*;
public class Main {

    public static void main(String[] args) {
        String[] strArr = new String[]{"сиськи", "письки", "хер", "дотан", "хз", "сиськи", "заебался", "дотан", "заебался", "сиськи", "сиськи"};
        SumString(strArr);
        PhoneBook book = new PhoneBook();

        book.add("lox","100","1@");
        book.add("lox2","200","2@");
        book.add("lox3","300","3@");
        book.add("lox2","900","9@");
        book.add("lox2","100","5@");

        System.out.println(book.getPerson("lox2"));
        System.out.println(book.getPerson("lox"));

    }



    public static void SumString(String[] arr) {
        Map<String, Integer> mapa = new HashMap<>();
        for (String s : arr) {
            if (mapa.containsKey(s)) {
                int a = mapa.get(s);
                mapa.put(s, a + 1);
            } else {
                mapa.put(s, 1);
            }
        }
        System.out.println(mapa);
        System.out.println(mapa.keySet());
    }
}
