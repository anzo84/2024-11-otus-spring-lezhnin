package ru.otus.hw.persistence.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleAliasConverter implements AttributeConverter<RoleAlias, String> {

    @Override
    public String convertToDatabaseColumn(RoleAlias attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getRoleName();
    }

    @Override
    public RoleAlias convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return RoleAlias.fromDbValue(dbData);
    }
}