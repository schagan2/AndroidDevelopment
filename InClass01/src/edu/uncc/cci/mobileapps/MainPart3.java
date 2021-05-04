package edu.uncc.cci.mobileapps;

import java.util.*;
import java.util.stream.Collectors;

/* InClass Assignment 1 MainPart3, MainPart3.java, Name: Sindhura Chaganti, Name: Aakanksha Chauhan, Group: A12 */
public class MainPart3 {
    /*
     * Question 3:
     * - This is a simple programming question that focuses on finding the
     * longest increasing subarray. Given the array A = {1,2,3,2,5,2,4,6,7} the
     * longest increasing subarray is {2,4,6,7}, note that the values have to be
     * contiguous.
     * */
    public static Comparator<String> stateComparator = new Comparator<String>() {
        public int compare(String u1, String  u2) {
            String[] str1 = u1.split(",");
            String[] str2 = u2.split(",");
            String c1 = str1[6];
            String c2 = str2[6];
            return c2.compareTo(c1);
        }};

    public static void main(String[] args) {
//
//        ArrayList users = new ArrayList();
//        ArrayList otherUsers = new ArrayList();
////        Set<User> users = new HashSet<User>();
////        Set<User> otherUsers = new HashSet<User>();
//
//        for (String str1 : Data.users) {
//            String[] user = str1.split(",");
//            if(user.length == 7){
//                users.add(new User(user[0],user[1],user[2],user[3],user[4],user[5],user[6]));
//            }
//        }
//
//        for (String str2 : Data.otherUsers) {
//            String[] otherUser = str2.split(",");
//            if(otherUser.length == 7){
//                otherUsers.add(new User(otherUser[0],otherUser[1],otherUser[2],otherUser[3],otherUser[4],otherUser[5],otherUser[6]));
//            }
//        }
//
//        //  Printing out the users that exist in both Data.users and Data.otherUsers
//
//
//      //  System.out.println(otherUsers.stream().filter(users::contains).collect(Collectors.toSet()));
//        otherUsers.retainAll(users);
//        Collections.sort(otherUsers, new Comparator<User>() {
//
//            //Printing the list of users sorted by state in descending order
//            public int compare(User t, User t1) {
//                return (t1.getState().compareTo(t.getState()));
//
//            }
//        });
//        System.out.println(otherUsers);
//    }

    HashSet<String> set = new HashSet<>();
        Collections.addAll(set, Data.users);
    List<String> list = new ArrayList<>();
    //Compare users from both Data.users and Data.otherUsers and then add the common users to the list
        for(String s: Data.otherUsers){
        if(set.contains(s))
            list.add(s);
    }
    // Sort the users by state and then print them in descending order.
        Collections.sort(list, stateComparator);
        for(int i=0;i<list.size();i++)
    {
        System.out.println(list.get(i).toString());
    }
}}