package com.mszalek.securitysystems;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mszalek.securitysystems.models.FieldResult;
import com.mszalek.securitysystems.models.StepA;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

@Service
public class GeneralValidator {

    public Map<String, FieldResult> validateStepA(StepA stepA) {
        Map<String, FieldResult> map = new HashMap<>();
        map.put("firstName", validateFirstName(stepA.getFirstName()));
        map.put("lastName", validateLastName(stepA.getLastName()));
        map.put("email", validateEmail(stepA.getEmail()));
        map.put("birthDate", validateBirthDate(stepA.getBirthDate()));
        map.put("phoneNumber", validatePhoneNumber(stepA.getPhoneNumber()));
        return map;
    }


    public FieldResult validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return new FieldResult("ERROR", "First name cannot be empty");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            return new FieldResult("ERROR", "Last name cannot be empty");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new FieldResult("ERROR", "Email cannot be empty");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return new FieldResult("ERROR", "Phone number cannot be empty");
        } else {
            try {
                Phonenumber.PhoneNumber phoneNumber1 = PhoneNumberUtil.getInstance().parse(phoneNumber, "PL");
                if (PhoneNumberUtil.getInstance().isValidNumber(phoneNumber1)) {
                    return new FieldResult("OK", null);
                } else {
                    return new FieldResult("ERROR", "The number you have provided is invalid");
                }
            } catch (NumberParseException e) {
                e.printStackTrace();
                return new FieldResult("ERROR", "The number you have provided is not even a number");
            }
        }
    }

    public FieldResult validateBirthDate(String birthDate) {
        if (birthDate == null || birthDate.trim().isEmpty()) {
            return new FieldResult("ERROR", "Date of birth cannot be empty");
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(birthDate);
                return new FieldResult("OK", null);
            } catch (ParseException e) {
                e.printStackTrace();
                return new FieldResult("ERROR", "Invalid date format");
            }

        }
    }
}
