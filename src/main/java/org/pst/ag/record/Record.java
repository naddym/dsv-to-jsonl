package org.pst.ag.record;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Record {
    private List<RecordField> fields;
    private final Class<?> clazz;

    public Record(Class<?> clazz) {
        this.clazz = clazz;
        this.fields = new ArrayList<>();
    }

    public void addField(String name, String value) throws NoSuchFieldException {
        RecordField field = RecordField.builder()
                .name(name)
                .value(value)
                .isNumericType(isNumeric(name))
                .isDateType(isDate(name))
                .build();
        this.fields.add(field);
    }

    public List<RecordField> getFields() {
        return new ArrayList<>(this.fields);
    }

    private boolean isDate(final String name) throws NoSuchFieldException {
        return !isNumeric(name) && this.clazz.getDeclaredField(name).getType().getSimpleName().equals("Date");
    }
    private boolean isNumeric(final String name) throws NoSuchFieldException {
        Field field = this.clazz.getDeclaredField(name);
        switch (field.getType().getSimpleName()) {
            case "Integer":
            case "Long":
                return true;
            default:
                return false;
        }
    }
}
