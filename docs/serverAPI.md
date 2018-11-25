#Security Systems - backend documentation
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