package edu.uncc.cci.mobileapps;

import java.util.*;

/* InClass Assignment 1 MainPart2, MainPart2.java, Name: Sindhura Chaganti, Name: Aakanksha Chauhan, Group: A12 */

public class MainPart2 {
    /*
     * Question 2:
     * - In this question you will use the Data.users array that includes
     * a list of users. Formatted as : firstname,lastname,age,email,gender,city,state
     * - Create a User class that should parse all the parameters for each user.
     * - The goal is to count the number of users living each state.
     * - Print out the list of State, Count order in ascending order by count.
     * */

    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList();

        //example on how to access the Data.users array.
        for (String str : Data.users) {
            String[] user = str.split(",");
            if(user.length == 7){
                users.add(new User(user[0],user[1],user[2],user[3],user[4],user[5],user[6]));
            }
        }

        HashMap<String, Integer> countPeople = new HashMap();
        for(User userCount: users){
            Integer count = (Integer) countPeople.get(userCount.getState());
            countPeople.put(userCount.getState(), (count == null) ? 1 : count + 1);
        }
      //Create a hash map to print the number of users living in each state
        Map<String, Integer> countState = sortByValue(countPeople);
        for (Map.Entry<String, Integer> people : countState.entrySet()){
            System.out.println("State: "+people.getKey()+", Count: "+people.getValue());
        }
    }
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> countStateUnsorted)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(countStateUnsorted.entrySet());

        // Sort the list in ascending order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap where users from each state are counted
        HashMap<String, Integer> countStateSorted = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> countStatePeople : list) {
            countStateSorted.put(countStatePeople.getKey(), countStatePeople.getValue());
        }
        return countStateSorted;
    }
}


