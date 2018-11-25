# Security Systems - backend documentation
* [REST endpoints](#rest-endpoints)  
  * [Step A Validation](#step-a-validation)
  * [Step B Validation](#step-b-validation)
  * [Form submit](#form-submit)

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