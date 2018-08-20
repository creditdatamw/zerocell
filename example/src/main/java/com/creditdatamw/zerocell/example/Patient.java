package com.creditdatamw.zerocell.example;

import java.time.LocalDate;

public class Patient {
    private int row;

    private String patientID;

    private String forename;

    private String otherNames;

    private String surname;

    private LocalDate dateOfBirth;

    private LocalDate dateAdmitted;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateAdmitted() {
        return dateAdmitted;
    }

    public void setDateAdmitted(LocalDate dateAdmitted) {
        this.dateAdmitted = dateAdmitted;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "row=" + row +
                ", patientID='" + patientID + '\'' +
                ", forename='" + forename + '\'' +
                ", otherNames='" + otherNames + '\'' +
                ", surname='" + surname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", dateAdmitted=" + dateAdmitted +
                '}';
    }
}
