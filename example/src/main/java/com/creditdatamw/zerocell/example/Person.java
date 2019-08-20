package com.creditdatamw.zerocell.example;

import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.annotation.RowNumber;
import com.creditdatamw.zerocell.annotation.ZerocellReaderBuilder;

import java.time.LocalDate;

/**
 * Person Class for tests
 */
@ZerocellReaderBuilder("PersonExcelReader")
public class Person {
    @RowNumber
    private int row;

    @Column(name= "ID", index=0, converterClass = IdPrefixingConverter.class)
    private String id;

    @Column(name = "FIRST_NAME", index = 1)
    private String firstName;

    @Column(name = "MIDDLE_NAME", index = 2)
    private String middleName;

    @Column(name = "LAST_NAME", index = 3)
    private String lastName;

    @Column(name = "DATE_OF_BIRTH", index = 4)
    private LocalDate dateOfBirth;

    @Column(name = "DATE_REGISTERED", index = 6)
    private LocalDate dateOfRegistration;

    @Column(name = "FAV_NUMBER", index = 5)
    private int favouriteNumber;

    public Person() {
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public int getFavouriteNumber() {
        return favouriteNumber;
    }

    public void setFavouriteNumber(int favouriteNumber) {
        this.favouriteNumber = favouriteNumber;
    }
}
