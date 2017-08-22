package com.creditdatamw.zerocell.processor;

import com.creditdatamw.zerocell.annotation.Column;
import com.creditdatamw.zerocell.annotation.ZerocellReaderBuilder;
import lombok.Data;

@Data
@ZerocellReaderBuilder
public class Dummy {
    @Column(index = 0, name="FIRST")
    private int first;

    @Column(index = 1, name="SECOND")
    private String second;
}
