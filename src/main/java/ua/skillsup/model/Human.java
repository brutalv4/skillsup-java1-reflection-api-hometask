package ua.skillsup.model;

import ua.skillsup.annotation.CustomDateFormat;
import ua.skillsup.annotation.JsonValue;

import java.time.LocalDate;
import java.util.Calendar;

public class Human {
    private String firstName;
    private String lastName;
    @JsonValue(name = "fun")
    private String hobby;
    @CustomDateFormat(format = "dd-MM-yyyy")
    private LocalDate dateBirth;

    public Human(String firstName, String lastName, String hobby, LocalDate dateBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hobby = hobby;
        this.dateBirth = dateBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getHobby() {
        return hobby;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    @Override
    public String toString() {
        return "Human{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateBirth=" + dateBirth +
                '}';
    }
}
