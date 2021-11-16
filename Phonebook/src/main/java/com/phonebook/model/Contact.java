package com.phonebook.model;

import java.time.LocalDate;

public class Contact {
    private final int contactID;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String town;
    private LocalDate birthDate;

    public Contact(int contactID, String phoneNumber, String firstName, String lastName,
                        String streetAddress, String town, LocalDate birthDate) {
        this.contactID = contactID;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.town = town;
        this.birthDate = birthDate;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public int getContactID() {
        return this.contactID;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public String getTown() {
        return this.town;
    }

    public String getFullName() {
        return (this.lastName + ", " + this.firstName);
    }
}
