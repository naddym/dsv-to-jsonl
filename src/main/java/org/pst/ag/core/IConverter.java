package org.pst.ag.core;

import org.pst.ag.record.Record;

import java.io.IOException;

public interface IConverter {

    void convert(Record record) throws IOException;
}
