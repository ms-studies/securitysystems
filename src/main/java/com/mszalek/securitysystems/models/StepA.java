package com.mszalek.securitysystems.models;

import lombok.Data;

@Data
public class StepA {
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    //YYYY-MM-DD
    String birthDate;
    String pesel;
}
