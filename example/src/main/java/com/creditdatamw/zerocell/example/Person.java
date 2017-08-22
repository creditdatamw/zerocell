package com.creditdatamw.zerocell.example;

import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.annotation.RowNumber;
import com.creditdatamw.zerocell.annotation.ZerocellReaderBuilder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Person Class for tests
 */
@Data
@ZerocellReaderBuilder
public class Person {
    @RowNumber
    private int rowNumber;

    @Column(name= "ID", index=0, convertorClass = IdPrefixingConverter.class)
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
    @Column(name = "DATE_OF_BIRTH", index = 4)
    private LocalDate dateOfBirth;

    @NotNull
    @Past
    @Column(name = "DATE_REGISTERED", index = 6)
    private Date dateOfRegistration;

    @Column(name = "FAV_NUMBER", index = 5)
    private int favouriteNumber;
}
