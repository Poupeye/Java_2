package Less_3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;


public class PhoneBook {

    public HashMap<String, ArrayList<Person>> entries = new HashMap<>();

    public void add (String name, String phone, String email){
        if (entries.containsKey(name)) {
            ArrayList<Person> persons = entries.get(name);
            persons.add(new Person(phone,email));
        } else {
            ArrayList<Person> persons = new ArrayList<>();
            persons.add(new Person(phone,email));
            entries.put(name,persons);
        }
    }

    public ArrayList<String> getMails(String name) {
        if (!entries.containsKey(name)) return null;
        return entries.get(name).stream().map(person -> person.email).collect(Collectors.toCollection(ArrayList::new));
    }




}
