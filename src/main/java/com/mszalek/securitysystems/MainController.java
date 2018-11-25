package com.mszalek.securitysystems;

import com.mszalek.securitysystems.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    GeneralValidator validator;

    @Autowired
    FormService formService;

    @PostMapping(path = "/validate/stepA")
    public ResponseEntity<StepAResult> validateStepA(@RequestBody StepA stepA) {
        StepAResult result = validator.validateStepA(stepA);
        return new ResponseEntity<>(result, result.isOk() ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST);

    }

    @PostMapping(path = "/validate/stepB")
    public ResponseEntity<StepBResult> validateStepB(@RequestBody StepB stepB) {
        StepBResult result = validator.validateStepB(stepB);
        if (result.isOk()) {
            StepA suggestion = formService.findSuggestion(stepB);
            result.setSuggestion(suggestion);
        }
        return new ResponseEntity<>(result, result.isOk() ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/submit")
    public ResponseEntity submitForm(@RequestBody FormModel formModel) {
        FormModelResult result = validator.validateForm(formModel);
        if (result.isOk()) {
            try {
                FormModel resultModel = formService.saveForm(formModel);
                return new ResponseEntity<>(resultModel, HttpStatus.CREATED);

            } catch (FormSubmitException e) {
                result.setDuplicateError(e.getMessage());
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}
