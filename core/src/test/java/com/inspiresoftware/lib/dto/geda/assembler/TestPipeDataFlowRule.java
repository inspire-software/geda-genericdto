package com.inspiresoftware.lib.dto.geda.assembler;

import com.inspiresoftware.lib.dto.geda.assembler.extension.PipeDataFlowRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPipeDataFlowRule implements PipeDataFlowRule {
    private List<String> shouldBeSkipFields;
    private Map<String, String> defaultValues;

    public TestPipeDataFlowRule() {
        shouldBeSkipFields = new ArrayList<>();
        defaultValues = new HashMap<>();
    }

    @Override
    public boolean skipPipeDataFlow(String dtoFieldName) {
        return shouldBeSkipFields != null && shouldBeSkipFields.contains(dtoFieldName);
    }

    @Override
    public Object getDefaultValue(String dtoFieldName) {
        return defaultValues.get(dtoFieldName);
    }

    public void addShouldBeSkipField(String fieldName) {
        shouldBeSkipFields.add(fieldName);
    }

    public void addDefaultValue(String fieldName, String value) {
        defaultValues.put(fieldName, value);
    }
}
