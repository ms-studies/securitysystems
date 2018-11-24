package com.mszalek.securitysystems;

import com.mszalek.securitysystems.models.FormModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormsRepository extends JpaRepository<FormModel, Long> {
    List<FormModel> findAllByPeselAndIdNumber(String pesel, String idNumber);
}
