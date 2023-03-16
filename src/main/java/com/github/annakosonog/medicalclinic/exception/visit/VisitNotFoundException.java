package com.github.annakosonog.medicalclinic.exception.visit;

public class VisitNotFoundException extends VisitException {

    public VisitNotFoundException(){
        super("Visit not found");
    }
}
