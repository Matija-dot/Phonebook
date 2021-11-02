package com.phonebook.util;

import com.phonebook.model.Validation;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidator {
    private boolean isValidPhoneNumber(String phoneNumber) {
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$"
                + "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private boolean isValidName(String name) {
        Pattern pattern = Pattern.compile("^[a-zA-Z_\\x{100}-\\x{17f}]+( [a-zA-Z_\\x{100}-\\x{17f}]+)*$");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private boolean isValidStreetAddress(String streetAddress) {
        if(streetAddress.equals("")) {
            return true;
        } else {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_\\x{100}-\\x{17f}]+( [a-zA-Z0-9_\\x{100}-\\x{17f}]+)*$");
            Matcher matcher = pattern.matcher(streetAddress);
            return matcher.matches();
        }
    }

    private boolean isValidTown(String town) {
        if(town.equals("")) {
            return true;
        } else {
            Pattern pattern = Pattern.compile("^[a-zA-Z_\\x{100}-\\x{17f}]+( [a-zA-Z_\\x{100}-\\x{17f}]+)*$");
            Matcher matcher = pattern.matcher(town);
            return matcher.matches();
        }
    }

    private boolean isValidBirthDate(LocalDate birthDate) {
        if(birthDate == null) {
            return true;
        } else {
            return(!(birthDate.compareTo(LocalDate.now()) > 0));
        }
    }

    public Validation Validate(String phoneNumber, String firstName, String lastName,
                               String streetAddress, String town, LocalDate birthDate) {
        if(isValidPhoneNumber(phoneNumber)) {
            if(isValidName(firstName)) {
                if(isValidName(lastName)) {
                    if(isValidStreetAddress(streetAddress)) {
                        if(isValidTown(town)) {
                            if(isValidBirthDate(birthDate)) {
                                return new Validation(true, "Valid input.");
                            } else {
                                return new Validation(false, "Select a valid birth date.");
                            }
                        } else {
                            return new Validation(false, "Enter valid town or leave blank.");
                        }
                    } else {
                        return new Validation(false, "Enter valid street address or leave blank.");
                    }
                } else {
                    return new Validation(false, "Valid last name is required.");
                }
            } else {
                return new Validation(false, "Valid first name is required.");
            }
        } else {
            return new Validation(false,"Input phone number unrecognizable.");
        }
    }
}
