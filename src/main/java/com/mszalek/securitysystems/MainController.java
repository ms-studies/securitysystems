package com.mszalek.securitysystems;

import com.mszalek.securitysystems.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping(path = "/forms")
    public ResponseEntity<List<SimpleFormModel>> getForms(@RequestParam("pesel") String pesel, @RequestParam("idNumber") String idNumber) {
        return new ResponseEntity<>(formService.getFormModels(pesel, idNumber), HttpStatus.OK);
    }

    @GetMapping(path = "/formDetails")
    public ResponseEntity getFormDetails(@RequestParam("formId") String formId,
                                         @RequestParam("password") String password) {
        try {
            return new ResponseEntity<>(formService.getFormDetails(formId, password), HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> res = new HashMap<>();
            res.put("message", e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

}
