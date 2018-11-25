package com.mszalek.securitysystems;

import com.mszalek.securitysystems.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    @Autowired
    GeneralValidator validator;

    @Autowired
    FormsRepository repository;

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
            StepA suggestion = findSuggestion(stepB);
            result.setSuggestion(suggestion);
        }
        return new ResponseEntity<>(result, result.isOk() ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/submit")
    public ResponseEntity submitForm(@RequestBody FormModel formModel) {
        Map<String, FieldResult> validationResult = validator.validateForm(formModel);
        HttpStatus status = validationResultToStatus(validationResult);
        if (status == HttpStatus.ACCEPTED) {
            FormModel resultModel = formService.saveForm(formModel);
            return new ResponseEntity<>(resultModel, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(validationResult, status);
    }

    private StepA findSuggestion(StepB stepB) {
        List<FormModel> matchingForms = repository.findAllByPeselAndIdNumber(stepB.getPesel(), stepB.getIdNumber());
        matchingForms.sort(Comparator.comparing(FormModel::getCreateTimestamp));
        if (matchingForms.isEmpty()) {
            return null;
        }
        FormModel formModel = matchingForms.get(0);
        StepA stepA = new StepA();
        stepA.setBirthDate(formModel.getBirthDate());
        stepA.setEmail(formModel.getEmail());
        stepA.setFirstName(formModel.getFirstName());
        stepA.setLastName(formModel.getLastName());
        stepA.setPhoneNumber(formModel.getPhoneNumber());
        return stepA;
    }

    private HttpStatus validationResultToStatus(Map<String, FieldResult> validationResult) {
        return validationResult.values().stream()
                .anyMatch((res) -> res.getStatus().equals("ERROR"))
                ? HttpStatus.BAD_REQUEST
                : HttpStatus.ACCEPTED;
    }
}