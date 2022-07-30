package Less_3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class PhoneBook {

    public Map<String, ArrayList<Person>> entries = new HashMap<>();

    public void add(String name, String phone, String email) {
        if (entries.containsKey(name)) {
            ArrayList<Person> persons = entries.get(name);
            persons.add(new Person(phone, email));
        } else {
            ArrayList<Person> persons = new ArrayList<>();
            persons.add(new Person(phone, email));
            entries.put(name, persons);
        }
    }


    public List<String> getPerson(String name) {
        if (!entries.containsKey(name)) return null;
//        return entries.get(name).stream().map(Person::getEmail).collect(Collectors.toList());
        List<Person> persons = entries.get(name);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < persons.size(); i++) {
            result.add(persons.get(i).getPhone());
            result.add(persons.get(i).getEmail());
        }
        return result;
    }

}
