package com.inspiresoftware.lib.dto.geda.assembler.examples.simple;

import com.inspiresoftware.lib.dto.geda.annotations.Dto;
import com.inspiresoftware.lib.dto.geda.annotations.DtoField;
import org.junit.Ignore;

@Ignore
@Dto
public class TestDto17Class {
    public static final String MY_INTEGER_CONVERTER = "MY_INTEGER_CONVERTER";

    @DtoField(value = "myInteger", converter = MY_INTEGER_CONVERTER)
    private Boolean myBoolean;

    public Boolean getMyBoolean() {
        return myBoolean;
    }

    public void setMyBoolean(Boolean myBoolean) {
        this.myBoolean = myBoolean;
    }
}
