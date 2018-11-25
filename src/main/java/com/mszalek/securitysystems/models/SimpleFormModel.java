package com.mszalek.securitysystems.models;

import lombok.Data;

@Data
public class SimpleFormModel {
    private Long id;
    private Long createTimeStamp;

    public SimpleFormModel(FormModel model) {
        this.id = model.getId();
        this.createTimeStamp = model.getCreateTimestamp();
    }
}
