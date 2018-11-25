package com.mszalek.securitysystems.models;

import lombok.Data;

@Data
public class FormModelResult {
    FieldResult firstName;
    FieldResult lastName;
    FieldResult email;
    FieldResult phoneNumber;
    FieldResult birthDate;
    FieldResult pesel;
    FieldResult idNumber;
    FieldResult application;
    FieldResult password;

    public boolean isOk() {
        if (firstName != null && !firstName.getStatus().equals("OK")) return false;
        if (lastName != null && !lastName.getStatus().equals("OK")) return false;
        if (email != null && !email.getStatus().equals("OK")) return false;
        if (phoneNumber != null && !phoneNumber.getStatus().equals("OK")) return false;
        if (birthDate != null && !birthDate.getStatus().equals("OK")) return false;
        if (pesel != null && !pesel.getStatus().equals("OK")) return false;
        if (idNumber != null && !idNumber.getStatus().equals("OK")) return false;
        if (application != null && !application.getStatus().equals("OK")) return false;
        if (password != null && !password.getStatus().equals("OK")) return false;
        return true;
    }
}
