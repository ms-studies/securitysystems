package com.mszalek.securitysystems;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mszalek.securitysystems.models.*;
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
                return new FieldResult("ERROR", "Twoja data urodzenia nie zgadza się z numerem pesel");
            }
        } catch (ParseException e) {
            //THIS will never happen
            return result;
        }
    }

    public FieldResult validatePesel(String pesel) {
        if (pesel == null || pesel.trim().isEmpty()) {
            return new FieldResult("ERROR", "Numer PESEL nie może być pusty. Prosimy wypełnić to pole.");
        } else if (!pesel.matches("[0-9]{11}")) {
            return new FieldResult("ERROR", "Numer PESEL musi się składać z dokładnie 11 cyfr. Na przykład: 12345678901");
        } else if (!isPeselCheckSumOk(pesel)) {
            return new FieldResult("ERROR", "Numer PESEL jest niepoprawny. Sprawdź jeszcze raz.");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return new FieldResult("ERROR", "Numer dowodu osobistego nie może być pusty. Prosimy wypełnić to pole.");
        } else if (!id.matches("[A-Z]{3}[0-9]{6}")) {
            return new FieldResult("ERROR", "Numer dowodu musi sie składać z dokładnie 3 liter i 6 cyfr, przykładowo: ABC123456");
        } else if (!isIdCheckSumOk(id)) {
            return new FieldResult("ERROR", "Numer dowodu jest niepoprawny. Sprawdź jeszcze raz");
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
            return new FieldResult("ERROR", "Treść aplikacji nie może być pusta. Prosimy wypełnić to pole.");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            return new FieldResult("ERROR", "Imię nie może być puste. Prosimy wypełnić to pole.");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            return new FieldResult("ERROR", "Nazwisko nie może być puste. Prosimy wypełnić to pole.");
        } else {
            return new FieldResult("OK", null);
        }
    }

    public FieldResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new FieldResult("ERROR", "Email nie może być pusty. Prosimy wypełnić to pole.");
        } else if (EmailValidator.getInstance().isValid(email)) {
            if (email.matches(".+@.+\\..+")) {
                return new FieldResult("OK", null);
            } else {
                return new FieldResult("ERROR", "Podany adres email jest nieprawidłowy. Przykładowy adres: jannowak@gmail.com");
            }
        } else {
            return new FieldResult("ERROR", "Podany adres email jest nieprawidłowy. Przykładowy adres: jankowalski@gmail.com");
        }
    }

    public FieldResult validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return new FieldResult("ERROR", "Numer telefony nie może być pusty. Prosimy wypełnić to pole.");
        } else {
            try {
                Phonenumber.PhoneNumber phoneNumber1 = PhoneNumberUtil.getInstance().parse(phoneNumber, "PL");
                if (PhoneNumberUtil.getInstance().isValidNumber(phoneNumber1)) {
                    return new FieldResult("OK", null);
                } else {
                    return new FieldResult("ERROR", "Podany numer telefonu jest niepoprawny. Przykładowy numer: +48 555666777");
                }
            } catch (NumberParseException e) {
                e.printStackTrace();
                return new FieldResult("ERROR", "Podany numer telefonu jest niepoprawny. Przykładowy numer: +48 555666777");
            }
        }
    }

    public FieldResult validateBirthDate(String birthDate) {
        if (birthDate == null || birthDate.trim().isEmpty()) {
            return new FieldResult("ERROR", "Data urodzenia nie może być pusta. Prosimy wypełnić to pole.");
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(birthDate);
                if (date.before(new Date())) {
                    return new FieldResult("OK", null);
                } else {
                    return new FieldResult("ERROR", "Data urodzenia nie może być w przyszłości.");
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return new FieldResult("ERROR", "Niepoprawny format daty urodzenia. Przykładowa data: 1990-10-23");
            }

        }
    }
}
