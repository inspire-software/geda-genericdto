package com.inspiresoftware.lib.dto.geda.assembler.extension;

/**
 * Additional rules for data processing (assembling)
 */
public interface PipeDataFlowRule {
    /**
     * Skips assembling (data flow) dto <-> entity (form dto to entity and from entity to dto) or not.
     *
     * @param dtoFieldName
     * @return
     */
    boolean skipPipeDataFlow(String dtoFieldName);

    /**
     * Returns 'default value' by dto field name.
     *
     * @param dtoFieldName
     * @return
     */
    Object getDefaultValue(String dtoFieldName);
}
