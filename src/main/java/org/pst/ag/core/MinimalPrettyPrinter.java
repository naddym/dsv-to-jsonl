package org.pst.ag.core;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;

import java.io.IOException;
import java.io.Serializable;

public class MinimalPrettyPrinter implements PrettyPrinter, Serializable {
    
    private static final long serialVersionUID = 1L;
    protected String rootValueSeparator;
    protected Separators separators;

    public MinimalPrettyPrinter(String rootValueSeparator) {
        this.rootValueSeparator = rootValueSeparator;
        this.separators = DEFAULT_SEPARATORS;
    }

    public void writeRootValueSeparator(JsonGenerator g) throws IOException {
        if (this.rootValueSeparator != null) {
            g.writeRaw(this.rootValueSeparator);
        }

    }

    public void writeStartObject(JsonGenerator g) throws IOException {
        g.writeRaw('{');
    }

    public void beforeObjectEntries(JsonGenerator g) throws IOException {
    }

    public void writeObjectFieldValueSeparator(JsonGenerator g) throws IOException {
        g.writeRaw(this.separators.getObjectFieldValueSeparator());
    }

    public void writeObjectEntrySeparator(JsonGenerator g) throws IOException {
        g.writeRaw(this.separators.getObjectEntrySeparator());
    }

    public void writeEndObject(JsonGenerator g, int nrOfEntries) throws IOException {
        g.writeRaw('}');
    }

    public void writeStartArray(JsonGenerator g) throws IOException {
        g.writeRaw('[');
    }

    public void beforeArrayValues(JsonGenerator g) throws IOException {
    }

    public void writeArrayValueSeparator(JsonGenerator g) throws IOException {
        g.writeRaw(this.separators.getArrayValueSeparator());
    }

    public void writeEndArray(JsonGenerator g, int nrOfValues) throws IOException {
        g.writeRaw(']');
    }
}

