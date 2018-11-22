package com.mszalek.securitysystems;

import org.springframework.stereotype.Service;

@Service
public class GeneralValidator {

    public String validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return "First name cannot be empty";
        } else {
            return null;
        }
    }

    public String validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            return "Last name cannot be empty";
        } else {
            return null;
        }
    }

    public String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Last name cannot be empty";
        } else {
            return null;
        }
    }
}
