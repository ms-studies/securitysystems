package com.mszalek.securitysystems.models;

import lombok.Data;

@Data
public class StepAResult {
    FieldResult firstName;
    FieldResult lastName;
    FieldResult email;
    FieldResult phoneNumber;
    FieldResult birthDate;
    FieldResult pesel;

    public boolean isOk() {
        if (firstName != null && !firstName.getStatus().equals("OK")) return false;
        if (lastName != null && !lastName.getStatus().equals("OK")) return false;
        if (email != null && !email.getStatus().equals("OK")) return false;
        if (phoneNumber != null && !phoneNumber.getStatus().equals("OK")) return false;
        if (birthDate != null && !birthDate.getStatus().equals("OK")) return false;
        if (pesel != null && !pesel.getStatus().equals("OK")) return false;
        return true;
    }
}
