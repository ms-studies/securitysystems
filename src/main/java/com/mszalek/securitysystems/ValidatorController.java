package com.mszalek.securitysystems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidatorController {

    @Autowired
    GeneralValidator validator;

    @GetMapping(path = "/validate/stepA")
    public ResponseEntity validateStepA() {
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
}
