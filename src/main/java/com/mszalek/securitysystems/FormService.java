package com.mszalek.securitysystems;

import com.mszalek.securitysystems.models.FormModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormService {
    @Autowired
    FormsRepository repository;

    public FormModel saveForm(FormModel formModel) {
        formModel.setId(null);
        formModel.setCreateTimestamp(System.currentTimeMillis());

        formModel.setFirstName(EscapeUtils.escape(formModel.getFirstName()));
        formModel.setLastName(EscapeUtils.escape(formModel.getLastName()));
        formModel.setApplication(EscapeUtils.escape(formModel.getApplication()));

        FormModel resultModel = repository.save(formModel);
        return resultModel;
    }
}
