package org.pst.ag.record;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.lang.reflect.Field;

public class RecordSchema {

    public static CsvSchema buildSchema(Class<?> clazz, final char delimiter) {
        CsvSchema.Builder builder = CsvSchema.builder();
        Field[] classFields = clazz.getDeclaredFields();
        for (int i = 0; i < classFields.length; i++) {
            final Field field = classFields[i];
            final String fieldName = field.getName();
            builder.addColumn(fieldName, getDeclaredType(field));
        }

        builder.setColumnSeparator(delimiter)
                .setQuoteChar('"')
                .setEscapeChar('\\');

        return builder.build();
    }

    private static CsvSchema.ColumnType getDeclaredType(Field field) {
        switch (field.getType().getSimpleName()) {
            case "Integer":
            case "Long":
                return CsvSchema.ColumnType.NUMBER;
            default:
                return CsvSchema.ColumnType.STRING;
        }
    }
}