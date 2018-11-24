package com.mszalek.securitysystems;

import com.mszalek.securitysystems.models.FieldResult;
import com.mszalek.securitysystems.models.StepA;
import com.mszalek.securitysystems.models.StepB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return new ResponseEntity<>(validationResult, validationResultToStatus(validationResult));

    }

    @PostMapping(path = "/validate/stepB")
    public ResponseEntity validateStepB(@RequestBody StepB stepB) {
        Map<String, FieldResult> validationResult = validator.validateStepB(stepB);
        return new ResponseEntity<>(validationResult, validationResultToStatus(validationResult));
    }

    private HttpStatus validationResultToStatus(Map<String, FieldResult> validationResult) {
        return validationResult.values().stream()
                .anyMatch((res) -> res.getStatus().equals("ERROR"))
                ? HttpStatus.BAD_REQUEST
                : HttpStatus.ACCEPTED;
    }
}
