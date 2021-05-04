package edu.uncc.cci.mobileapps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/* InClass Assignment 1 MainPart1, MainPart1.java, Name: Sindhura Chaganti, Name: Aakanksha Chauhan, Group: A12 */

public class MainPart1 {
    /*
     * Question 1:
    * - In this question you will use the Data.users array that includes
    * a list of users. Formatted as : firstname,lastname,age,email,gender,city,state
    * - Create a User class that should parse all the parameters for each user.
    * - Insert each of the users in a list.
    * - Print out the TOP 10 oldest users.
    * */

    public static void main(String[] args) {
        ArrayList users = new ArrayList();
        for (String str : Data.users) {
            String[] user = str.split(",");
            if(user.length == 7){
              users.add(new User(user[0],user[1],user[2],user[3],user[4],user[5],user[6]));
            }
        }
        //example on how to access the Data.users array.
        Collections.sort(users, new Comparator<User>() {

            public int compare(User t, User t1) {
                return (Integer.parseInt(t1.getAge())-(Integer.parseInt(t.getAge()))) ;

            }
        });

        //Printing top 10 oldest users
        List userx = (List) users.stream().limit(10).collect(Collectors.toList());
        System.out.println(userx);

    }
    }
