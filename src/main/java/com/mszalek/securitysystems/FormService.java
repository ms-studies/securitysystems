package com.mszalek.securitysystems;

import com.google.common.hash.Hashing;
import com.mszalek.securitysystems.models.FormModel;
import com.mszalek.securitysystems.models.StepA;
import com.mszalek.securitysystems.models.StepB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

@Service
public class FormService {
    final int formSubmitDelayInSeconds = 10;

    @Autowired
    FormsRepository repository;

    public FormModel saveForm(FormModel formModel) throws FormSubmitException {
        formModel.setId(null);
        formModel.setCreateTimestamp(System.currentTimeMillis());

        formModel.setFirstName(EscapeUtils.escape(formModel.getFirstName()));
        formModel.setLastName(EscapeUtils.escape(formModel.getLastName()));
        formModel.setApplication(EscapeUtils.escape(formModel.getApplication()));
        formModel.setPassword(Hashing.sha512().hashString(formModel.getPassword(), StandardCharsets.UTF_8).toString());

        checkIfCanSubmitForm(formModel);

        FormModel resultModel = repository.save(formModel);
        resultModel.setPassword(null);
        return resultModel;
    }

    private void checkIfCanSubmitForm(FormModel formModel) throws FormSubmitException {
        List<FormModel> matchingForms = repository.findAllByPeselAndIdNumber(formModel.getPesel(), formModel.getIdNumber());
        matchingForms.sort(Comparator.comparing(FormModel::getCreateTimestamp));
        if (matchingForms.isEmpty()) {
            return;
        }
        FormModel lastForm = matchingForms.get(matchingForms.size() - 1);
        long timediff = (System.currentTimeMillis() - lastForm.getCreateTimestamp()) / 1000;
        if (timediff < formSubmitDelayInSeconds) {
            throw new FormSubmitException("You need to wait " + (formSubmitDelayInSeconds - timediff) + " seconds to submit another form.");
        }
        if (matchingForms.stream().anyMatch((f1) -> f1.getApplication().equals(formModel.getApplication()))) {
            throw new FormSubmitException("You have already submitted such application.");
        }
    }

    public StepA findSuggestion(StepB stepB) {
        List<FormModel> matchingForms = repository.findAllByPeselAndIdNumber(stepB.getPesel(), stepB.getIdNumber());
        matchingForms.sort(Comparator.comparing(FormModel::getCreateTimestamp));
        if (matchingForms.isEmpty()) {
            return null;
        }
        FormModel formModel = matchingForms.get(matchingForms.size() - 1);
        StepA stepA = new StepA();
        stepA.setBirthDate(formModel.getBirthDate());
        stepA.setEmail(formModel.getEmail());
        stepA.setFirstName(formModel.getFirstName());
        stepA.setLastName(formModel.getLastName());
        stepA.setPhoneNumber(formModel.getPhoneNumber());
        return stepA;
    }
}
