package com.mszalek.securitysystems;

import com.mszalek.securitysystems.models.FieldResult;
import com.mszalek.securitysystems.models.StepA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ValidatorController {

    @Autowired
    GeneralValidator validator;

    @PostMapping(path = "/validate/stepA")
    public ResponseEntity validateStepA(@RequestBody StepA stepA) {
        Map<String, FieldResult> validationResult = validator.validateStepA(stepA);
        HttpStatus status =
                validationResult.values().stream()
                        .anyMatch((res) -> res.getStatus().equals("ERROR"))
                        ? HttpStatus.BAD_REQUEST : HttpStatus.ACCEPTED;

        return new ResponseEntity<>(validationResult, status);
    }
}
