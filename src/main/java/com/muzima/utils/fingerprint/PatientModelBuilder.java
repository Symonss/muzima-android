package com.muzima.utils.fingerprint;

import com.muzima.api.model.Patient;
import com.muzima.biometric.model.PatientModel;
import com.muzima.biometric.model.PatientModels;

import java.util.ArrayList;
import java.util.List;

public class PatientModelBuilder {
    public PatientModels build(List<Patient> patients) {
        ArrayList<PatientModel> models = new ArrayList<PatientModel>();
        for (Patient patient : patients) {
            if (patient.getFingerprint() != null) {
                PatientModel patientModel = new PatientModel(patient.getUuid(), patient.getFingerprint());
                models.add(patientModel);
            }
        }
        return new PatientModels(models);
    }
}

