package com.mszalek.securitysystems.models;

import lombok.Data;

@Data
public class StepBResult {
    FieldResult pesel;
    FieldResult idNumber;
    StepA suggestion;

    public boolean isOk() {
        if (pesel != null && !pesel.getStatus().equals("OK")) return false;
        if (idNumber != null && !idNumber.getStatus().equals("OK")) return false;
        return true;
    }
}
