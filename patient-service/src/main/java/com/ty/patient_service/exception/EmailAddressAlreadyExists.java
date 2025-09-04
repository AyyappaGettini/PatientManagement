package com.ty.patient_service.exception;

public class EmailAddressAlreadyExists extends RuntimeException{
    public EmailAddressAlreadyExists(String email){
        super(email);
    }
}
