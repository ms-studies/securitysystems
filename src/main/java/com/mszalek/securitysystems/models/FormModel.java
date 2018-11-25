package com.mszalek.securitysystems.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode
public class FormModel {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String birthDate;
    private String idNumber;
    private String pesel;
    private String application;
    private String password;
    private Long createTimestamp;
}
