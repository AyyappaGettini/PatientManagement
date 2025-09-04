package com.ty.patient_service.exception;

public class PatientNotFoundWithId extends RuntimeException{
    public PatientNotFoundWithId(String message) {
        super(message);
    }
}
