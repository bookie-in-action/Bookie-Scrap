package com.bookie.legacy.common.domain.converter;

import com.bookie.legacy.common.domain.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class StatusConverter implements AttributeConverter<Status, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Status status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public Status convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Status.fromCode(dbData);
    }
}
