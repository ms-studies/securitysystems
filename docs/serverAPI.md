# Security Systems - backend documentation
* [API REST endpoints](#rest-endpoints)  
  * [Step A Validation](#step-a-validation)
  * [Step B Validation](#step-b-validation)
  * [Form submit](#form-submit)
* [Field validations](#field-validations)
* [Duplicate prevention](#duplicate-prevention)
* [SQL Injection prevention](#sql-injection-prevention)
* [XSS prevention](#xss-prevention)

## REST endpoints
### Step A Validation
Type: POST  
Path: /validate/stepA
##### **Parameter:** (required)  
Name: stepA  
Type: body parameter  
Example value:
```json
{
    "phoneNumber": "506 444 224",
    "firstName": "Jan",
    "lastName": "",
    "birthDate": "1990-12-10",
    "email": "this@.s.omeemail.com"
}
```
##### **Response:**  
Example response with invalid fields:  
Status: 400 (BAD REQUEST)  
Body:
```json
{
    "firstName": {
        "status": "OK",
        "msg": null
    },
    "lastName": {
        "status": "ERROR",
        "msg": "Nazwisko nie może być puste. Prosimy wypełnić to pole."
    },
    "email": {
        "status": "ERROR",
        "msg": "Podany adres email jest nieprawidłowy. Przykładowy adres: jankowalski@gmail.com"
    },
    "phoneNumber": {
        "status": "OK",
        "msg": null
    },
    "birthDate": {
        "status": "OK",
        "msg": null
    },
    "ok": false
}
```

Example response with valid fields:  
Status: 202 (ACCEPTED)  
Body:
```json
{
    "firstName": {
        "status": "OK",
        "msg": null
    },
    "lastName": {
        "status": "OK",
        "msg": null
    },
    "email": {
        "status": "OK",
        "msg": null
    },
    "phoneNumber": {
        "status": "OK",
        "msg": null
    },
    "birthDate": {
        "status": "OK",
        "msg": null
    },
    "ok": true
}
```

### Step B Validation
Type: POST  
Path: /validate/stepB
##### **Parameter:** (required)  
Name: stepB  
Type: body parameter  
Example value:
```json
{
    "pesel":"900805217455",
    "idNumber":"SAK299108"
}
```
##### **Response:**  
Example response with invalid fields:  
Status: 400 (BAD REQUEST)  
Body:
```json
{
    "pesel": {
        "status": "ERROR",
        "msg": "Numer PESEL musi się składać z dokładnie 11 cyfr. Na przykład: 12345678901"
    },
    "idNumber": {
        "status": "ERROR",
        "msg": "Numer dowodu jest niepoprawny. Sprawdź jeszcze raz"
    },
    "suggestion": null,
    "ok": false
}
```

Example response with valid fields:  
Status: 202 (ACCEPTED)  
Body:
```json
{
    "pesel": {
        "status": "OK",
        "msg": null
    },
    "idNumber": {
        "status": "OK",
        "msg": null
    },
    "suggestion": null,
    "ok": true
}
```

Example response with valid fields and suggestion:  
Status: 202 (ACCEPTED)  
Body:
```json
{
    "pesel": {
        "status": "OK",
        "msg": null
    },
    "idNumber": {
        "status": "OK",
        "msg": null
    },
    "suggestion": {
        "firstName": "Marcin",
        "lastName": "Szałek",
        "email": "my@email.com",
        "phoneNumber": "+48505505505",
        "birthDate": "1990-08-05"
    },
    "ok": true
}
```
### Form submit
Type: POST  
Path: /submit
##### **Parameter:** (required)  
Name: formModel  
Type: body parameter  
Example value:
```json
{
    "firstName": "Marcin",
    "lastName": "Szałek",
    "phoneNumber": "+48505505505",
    "pesel": "90080517455",
    "idNumber": "SAK299208",
    "birthDate": "1990-08-05",
    "email": "my@email.com",
    "application": "Some text"
}
```
##### **Response:**  
Example response with invalid fields:  
Status: 400 (BAD REQUEST)  
Body:
```json
{
    "firstName": {
        "status": "OK",
        "msg": null
    },
    "lastName": {
        "status": "OK",
        "msg": null
    },
    "email": {
        "status": "OK",
        "msg": null
    },
    "phoneNumber": {
        "status": "OK",
        "msg": null
    },
    "birthDate": {
        "status": "ERROR",
        "msg": "Twoja data urodzenia nie zgadza się z numerem pesel"
    },
    "pesel": {
        "status": "OK",
        "msg": null
    },
    "idNumber": {
        "status": "ERROR",
        "msg": "Numer dowodu musi sie składać z dokładnie 3 liter i 6 cyfr, przykładowo: ABC123456"
    },
    "application": {
        "status": "OK",
        "msg": null
    },
    "ok": false
}
```

Example response with valid fields:  
Status: 201 (CREATED)  
Body:
```json
{
    "id": 1,
    "firstName": "Marcin",
    "lastName": "Szałek",
    "phoneNumber": "+48505505505",
    "email": "my@email.com",
    "birthDate": "1990-08-05",
    "idNumber": "SAK299208",
    "pesel": "90080517455",
    "application": "Some XSS text",
    "createTimestamp": 1543137294050
}
```

## Field validations
In validate steps requests as well as when submitting the form, every field is validated separately with following methods:
* FirstName
  * Must exist
  * Must not be empty (trimmed)
* LastName
  * Must exist
  * Must not be empty (trimmed)
* BirthDate
  * Must exist
  * Must not be empty (trimmed)
  * Must be formatted in pattern "yyyy-MM-dd" (checked by exception handling while parsing)
  * Must not be from future (checked comparing with date on the server)
  * _On form submit:_ Must be matching date in the pesel number
* Email
  * Must exist
  * Must not be empty (trimmed)
  * Must pass [EmailValidator.validate()](https://commons.apache.org/proper/commons-validator/apidocs/index.html?org/apache/commons/validator/EmailValidator.html) from Apache Commons
    * We are using this library since it is the most recommended one
    * It is not 100% guaranteed to catch all invalid emails. From official docs: `This implementation is not guaranteed to catch all possible errors in an email address.`
    * It performs regex checks on whole email
    * It performs regex checks on user part
    * It performs regex checks on domain part
  * Must pass `.+@.+\..+` regex check that ensures a gTLD, since EmailValidator is accepting dotless domains like `xx@yyy`, which were prohibited by [ICANN](https://www.icann.org/news/announcement-2013-08-30-en).
* PhoneNumber
  * Must exist
  * Must not be empty (trimmed)
  * Must be able to [parse](https://github.com/googlei18n/libphonenumber/blob/master/java/libphonenumber/src/com/google/i18n/phonenumbers/PhoneNumberUtil.java#L3018) to a possible Polish PhoneNumber using Google's [libphonenumber](https://github.com/googlei18n/libphonenumber) library.
  * Must pass [isValid](https://github.com/googlei18n/libphonenumber/blob/master/java/libphonenumber/src/com/google/i18n/phonenumbers/PhoneNumberUtil.java#2250) method from Google's [libphonenumber](https://github.com/googlei18n/libphonenumber) library.
* Pesel
  * Must exist
  * Must not be empty (trimmed)
  * Must contain of 11 digits only (regex: `[0-9]{11}`)
  * Must have a valid checksum (based on [this](https://pl.wikipedia.org/wiki/PESEL#Cyfra_kontrolna_i_sprawdzanie_poprawno%C5%9Bci_numeru) article)
* IdNumber
  * Must exist
  * Must not be empty (trimmed)
  * Must contain of 3 letters followed by 6 digits only (regex: `[A-Z]{3}[0-9]{6}`)
  * Must have a valid checksum (based on [this](https://pl.wikipedia.org/wiki/Dow%C3%B3d_osobisty_w_Polsce#Wz%C3%B3r,_wymiana_i_wa%C5%BCno%C5%9B%C4%87_dowodu) article)
* Application
  * Must exist
  * Must not be empty (trimmed)
  
## Duplicate prevention
//TODO
## SQL injection prevention
//TODO
## XSS prevention
//TODO