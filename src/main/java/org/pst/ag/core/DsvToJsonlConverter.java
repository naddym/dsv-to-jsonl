package org.pst.ag.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.commons.lang3.StringUtils;
import org.pst.ag.record.Record;
import org.pst.ag.record.RecordField;
import org.pst.ag.utils.DateFormatterUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

public class DsvToJsonlConverter implements IConverter, Closeable {

    private final JsonFactory factory;
    private final JsonGenerator generator;
    private static final String DEFAULT_OUTPUT_RECORD_SEPARATOR = " ";

    public DsvToJsonlConverter(OutputStream out) throws IOException {
        this(out, DEFAULT_OUTPUT_RECORD_SEPARATOR);
    }

    public DsvToJsonlConverter(OutputStream out, String recordSeparator) throws IOException {
        this.factory = new JsonFactory();
        this.generator = this.factory.createGenerator(out);
        this.generator.setPrettyPrinter(new MinimalPrettyPrinter(recordSeparator));
    }

    @Override
    public void convert(Record record) throws IOException {
        generator.writeStartObject();

        for (RecordField field: record.getFields()) {
            String name = field.getName();
            String value = field.getValue();
            boolean isNumericType = field.isNumericType();
            boolean isDateType = field.isDateType();

            if (StringUtils.isEmpty(value)) {
                continue;
            }

            if (isNumericType) {
                generator.writeNumberField(name, Long.valueOf(value));
            } else {
                if (isDateType) {
                    LocalDate date = LocalDate.parse(value, DateFormatterUtils.INPUT_FORMATTER);
                    value = date.format(DateFormatterUtils.OUTPUT_FORMATTER);
                }
                generator.writeStringField(name, value);
            }
        }

        generator.writeEndObject();
    }

    @Override
    public void close() throws IOException {
        this.generator.close();
    }
}
