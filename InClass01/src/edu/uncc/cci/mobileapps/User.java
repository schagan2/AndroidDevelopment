package edu.uncc.cci.mobileapps;

import java.util.Objects;

public class User {
    String firstName;
    String lastName;
    String age;
    String email;
    String gender;
    String city;
    String state;


    public User(String firstName,
                String lastName,
                String age,
                String email,
                String gender,
                String city,
                String state){
        this.firstName = firstName;
        this.lastName=lastName;
        this.age=age;
        this.email=email;
        this.gender=gender;
        this.city=city;
        this.state=state;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return  Objects.equals(age, user.age) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(gender, user.gender) &&
                Objects.equals(city, user.city) &&
                Objects.equals(state, user.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, age, email, gender, city, state);
    }

    @Override
    public String toString(){
        return  "First Name: "+this.firstName+"\n"+
                "Last Name: "+this.lastName+"\n"+
                "Age: "+this.age+"\n"+
                "Email: "+this.email+"\n"+
                "Gender: "+this.gender+"\n"+
                "City: "+this.city+"\n"+
                "State: "+this.state+"\n";
    }
}
