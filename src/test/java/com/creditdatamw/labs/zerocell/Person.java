package com.creditdatamw.labs.zerocell;

import com.creditdatamw.labs.zerocell.annotation.Column;
import com.creditdatamw.labs.zerocell.annotation.RowNumber;
import com.creditdatamw.labs.zerocell.converter.LocalDateConverter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.sql.Date;
import java.time.LocalDate;

/**
 * PersonExample Class for tests
 */
public class Person {
    @RowNumber
    private int rowNumber;

    @Column(name= "ID", index=0)
    private String id;

    @NotEmpty
    @Column(name = "FIRST_NAME", index = 1)
    private String firstName;

    @NotEmpty
    @Column(name = "MIDDLE_NAME", index = 2)
    private String middleName;

    @NotEmpty
    @Column(name = "LAST_NAME", index = 3)
    private String lastName;

    @NotNull
    @Past
    @Column(name = "DATE_OF_BIRTH", index = 4, convertorClass = LocalDateConverter.class)
    private LocalDate dateOfBirth;

    @NotNull
    @Past
    @Column(name = "DATE_REGISTERED", index = 6)
    private Date dateOfRegistration;

    @Column(name = "FAV_NUMBER", index = 5)
    private int favouriteNumber;

    public Person() { }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
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

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getFavouriteNumber() {
        return favouriteNumber;
    }

    public void setFavouriteNumber(int favouriteNumber) {
        this.favouriteNumber = favouriteNumber;
    }
}
