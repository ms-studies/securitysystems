package com.mszalek.securitysystems;

import com.google.common.hash.Hashing;
import com.mszalek.securitysystems.models.FormModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

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
        formModel.setPassword(Hashing.sha512().hashString(formModel.getPassword(), StandardCharsets.UTF_8).toString());


        FormModel resultModel = repository.save(formModel);
        resultModel.setPassword(null);
        return resultModel;
    }
}
