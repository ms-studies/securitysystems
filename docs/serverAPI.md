# Security Systems - backend documentation
* [API REST endpoints](#rest-endpoints)  
  * [Step A Validation](#step-a-validation)
  * [Step B Validation](#step-b-validation)
  * [Form submit](#form-submit)
  * [List of forms]("list-of-forms)
  * [Form details]("form-details)
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
    "email": "thiss@email..com"
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
        "msg": "Last name is required."
    },
    "email": {
        "status": "ERROR",
        "msg": "Email address is invalid. Example email: jankowalski@gmail.com"
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
        "msg": "PESEL number must consist of 11 digits only. For example: 12345678901"
    },
    "idNumber": {
        "status": "ERROR",
        "msg": "ID number is invalid. Check again."
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
    "application": "  ",
    "password": "Some password"
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
        "status": "OK",
        "msg": null
    },
    "pesel": {
        "status": "OK",
        "msg": null
    },
    "idNumber": {
        "status": "OK",
        "msg": null
    },
    "application": {
        "status": "ERROR",
        "msg": "Application content is required."
    },
    "password": {
        "status": "ERROR",
        "msg": "Password is too simple."
    },
    "duplicateError": null,
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
    "application": "&lt;xss&gt;Some application",
    "password": null,
    "createTimestamp": 1543154560763
}
```

Example response with valid fields but with duplicate message:  
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
        "status": "OK",
        "msg": null
    },
    "pesel": {
        "status": "OK",
        "msg": null
    },
    "idNumber": {
        "status": "OK",
        "msg": null
    },
    "application": {
        "status": "OK",
        "msg": null
    },
    "password": {
        "status": "OK",
        "msg": null
    },
    "duplicateError": "You have already submitted such application.",
    "ok": false
}
```

### List of forms
Type: GET  
Path: /forms
##### **Parameters:** (required)  
* Name: pesel  
Type: query parameter (string)

* Name: idNumber  
Type: query parameter (string)

Example request:
`GET localhost:8080/forms?pesel=90080517455&idNumber=SAK299208`

##### **Response:**  
Example response:  
Status: 200 (OK)  
Body:
```json
[
    {
        "id": 1,
        "createTimeStamp": 1543159371313
    }
]
```
If there is no matching forms, the empty list is returned


### Form details
Type: GET  
Path: /formDetails
##### **Parameters:** (required)  
* Name: formId  
Type: query parameter (string)

* Name: password  
Type: query parameter (string)

Example request:
`GET localhost:8080/formDetails?formId=1&password=Some password123-1`

##### **Response:**  
Example response:  
Status: 200 (OK)  
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
    "application": "&lt;xss&gt;Some application",
    "password": null,
    "createTimestamp": 1543159371313
}
```

Example response if there is no matching form id:
Status: 400 (BAD REQUEST)
```json
{
    "message": "No matching form."
}
```


Example response if there is no matching form id:
Status: 400 (BAD REQUEST)
```json
{
    "message": "Invalid password"
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
* Password
  * Must exist
  * Must not be empty (trimmed)
  * Must be considered Strong or Very Strong according to [Zxcbn4j](https://github.com/nulab/zxcvbn4j) library metrics
## Duplicate prevention
* User data suggestion - When user validates stepB, if he/she has already submitted form with same stepB, then the validation will return a suggestion with the last used user data.
* After user has submitted the form, he/she can submit next form with the same pesel and id number after X (e.g. 5 minute) time frame.
* A combination of PESEL number, ID number and Application must be unique. It means a person cannot submit the same aplication twice.
  
## SQL injection prevention
Backend application uses [JPARepository](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html) interface from Spring Boot JPA, which uses [EntityManager](https://docs.oracle.com/javaee/7/api/javax/persistence/EntityManager.html) from JavaEE. EntityManager's methods (excluding native queries which are not used by us) are generating parametrized SQL queries which are SQLInjection-safe.
>Unfortunately, we were not able to find official way of proving that, using EntityManager gives us 100% safety against SQL Injection...  
>Our conclusion is based on two factors:
>* EntityManager's safeness is considered "common knowledge" in source in the internet.
>* On "Network Database Systems" course dr Smolinski and dr Karbowańczyk claimed that using entity manager makes the application 99% safe against SQL Injection (and it was tested during the course), so we wouldn't dare to contest that.
## XSS prevention
According to [OWASP Cross Site Scripting prevention cheatsheet](https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#XSS_Prevention_Rules_Summary), since we are displaying our data in HTML Body, we are using [HTML Entity Encoding](https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#RULE_.231_-_HTML_Escape_Before_Inserting_Untrusted_Data_into_HTML_Element_Content).
That means we are doing following mapping on backend side before storing form in database:
```
& --> &amp;
< --> &lt;
> --> &gt;
" --> &quot;
' --> &#x27;
/ --> &#x2F;
```
The mapping is applied only to fields that are not validated against regex: first name, last name, application.