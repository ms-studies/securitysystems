package com.mszalek.securitysystems;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mszalek.securitysystems.models.*;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class GeneralValidator {

    public StepAResult validateStepA(StepA stepA) {
        StepAResult stepAResult = new StepAResult();
        stepAResult.setFirstName(validateFirstName(stepA.getFirstName()));
        stepAResult.setLastName(validateLastName(stepA.getLastName()));
        stepAResult.setEmail(validateEmail(stepA.getEmail()));
        stepAResult.setBirthDate(validateBirthDate(stepA.getBirthDate()));
        stepAResult.setPhoneNumber(validatePhoneNumber(stepA.getPhoneNumber()));
        return stepAResult;
    }

    public StepBResult validateStepB(StepB stepB) {
        StepBResult stepBResult = new StepBResult();
        stepBResult.setPesel(validatePesel(stepB.getPesel()));
        stepBResult.setIdNumber(validateId(stepB.getIdNumber()));
        return stepBResult;
    }

    public FormModelResult validateForm(FormModel formModel) {
        FormModelResult result = new FormModelResult();
        result.setFirstName(validateFirstName(formModel.getFirstName()));
        result.setLastName(validateLastName(formModel.getLastName()));
        result.setEmail(validateEmail(formModel.getEmail()));
        result.setBirthDate(validateBirthDateWithPesel(formModel.getBirthDate(), formModel.getPesel()));
        result.setPhoneNumber(validatePhoneNumber(formModel.getPhoneNumber()));
        result.setPesel(validatePesel(formModel.getPesel()));
        result.setIdNumber(validateId(formModel.getIdNumber()));
        result.setApplication(validateApplication(formModel.getApplication()));
        result.setPassword(validatePassword(formModel.getPassword()));
        return result;
    }

    public FieldResult validateBirthDateWithPesel(String birthDate, String pesel) {
        FieldResult result = validateBirthDate(birthDate);
        if (result.getStatus().equals("ERROR") || validatePesel(pesel).getStatus().equals("ERROR")) {
            return result;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(birthDate);
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int expectedYear = localDate.getYear() % 100;
            int expectedMonth = localDate.getMonthValue();
            if (localDate.getYear() < 1900) {
                expectedMonth += 80;
            } else if (localDate.getYear() > 2000) {
                expectedMonth += 20;
            }
            int expectedDay = localDate.getDayOfMonth();
            boolean doesYearMatch = pesel.substring(0, 2).equals(String.format("%02d", expectedYear));
            boolean doesMonthMatch = pesel.substring(2, 4).equals(String.format("%02d", expectedMonth));
            boolean doesDayMatch = pesel.substring(4, 6).equals(String.format("%02d", expectedDay));
            if (doesDayMatch && doesMonthMatch && doesYearMatch) {
                return new FieldResult("OK", null);
            } else {
                return new FieldResult("ERROR", "Your date of birth is not matching your PESEL number.");
            }
        } catch (ParseException e) {
            //THIS will never happen
            return result;
        }
    }

    public FieldResult validatePesel(String pesel) {
        if (pesel == null || pesel.trim().isEmpty()) {
            return new FieldResult("ERROR", "PESEL number is required.");
        } else if (!pesel.matches("[0-9]{11}")) {
            return new FieldResult("ERROR", "PESEL number must consist of 11 digits only. For example: 12345678901");
        } else if (!isPeselCheckSumOk(pesel)) {
            return new FieldResult("ERROR", "PESEL number is invalid. Check again.");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return new FieldResult("ERROR", "ID number is required.");
        } else if (!id.matches("[A-Z]{3}[0-9]{6}")) {
            return new FieldResult("ERROR", "ID number must consist of 3 letter followed by 6 digits only, for example: ABC123456");
        } else if (!isIdCheckSumOk(id)) {
            return new FieldResult("ERROR", "ID number is invalid. Check again.");
        } else {
            return new FieldResult("OK", null);
        }
    }

    private boolean isIdCheckSumOk(String id) {
        int[] weights = {7, 3, 1, 9, 7, 3, 1, 7, 3};
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += (id.charAt(i) - 'A' + 10) * weights[i];
        }
        for (int i = 3; i < 9; i++) {
            sum += Integer.parseInt("" + id.charAt(i)) * weights[i];
        }
        return sum % 10 == 0;
    }

    private boolean isPeselCheckSumOk(String pesel) {
        int[] weights = {9, 7, 3, 1, 9, 7, 3, 1, 9, 7};
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Integer.parseInt("" + pesel.charAt(i)) * weights[i];
        }
        return sum % 10 == Integer.parseInt("" + pesel.charAt(10));
    }

    public FieldResult validateApplication(String application) {
        if (application == null || application.trim().isEmpty()) {
            return new FieldResult("ERROR", "Application content is required.");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return new FieldResult("ERROR", "Password is required.");
        } else if (new Zxcvbn().measure(password).getScore() < 3) {
            return new FieldResult("ERROR", "Password is too simple.");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return new FieldResult("ERROR", "First name is required.");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            return new FieldResult("ERROR", "Last name is required.");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new FieldResult("ERROR", "Email is required.");
        } else if (EmailValidator.getInstance().isValid(email)) {
            if (email.matches(".+@.+\\..+")) {
                return new FieldResult("OK", null);
            } else {
                return new FieldResult("ERROR", "Email address is invalid. Example email: jannowak@gmail.com");
            }
        } else {
            return new FieldResult("ERROR", "Email address is invalid. Example email: jankowalski@gmail.com");
        }
    }

    public FieldResult validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return new FieldResult("ERROR", "Phone number is required.");
        } else {
            try {
                Phonenumber.PhoneNumber phoneNumber1 = PhoneNumberUtil.getInstance().parse(phoneNumber, "PL");
                if (PhoneNumberUtil.getInstance().isValidNumber(phoneNumber1)) {
                    return new FieldResult("OK", null);
                } else {
                    return new FieldResult("ERROR", "Phone number is invalid. Example number: +48 555666777");
                }
            } catch (NumberParseException e) {
                e.printStackTrace();
                return new FieldResult("ERROR", "Phone number is invalid. Example number: +48 555666777");
            }
        }
    }

    public FieldResult validateBirthDate(String birthDate) {
        if (birthDate == null || birthDate.trim().isEmpty()) {
            return new FieldResult("ERROR", "Date of birth is required.");
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(birthDate);
                if (date.before(new Date())) {
                    return new FieldResult("OK", null);
                } else {
                    return new FieldResult("ERROR", "Date of birth cannot be in the future..");
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return new FieldResult("ERROR", "Invalid date format. Example date: 1990-10-23");
            }

        }
    }
}
