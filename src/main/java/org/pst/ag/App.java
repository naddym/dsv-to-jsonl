package org.pst.ag;


import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.pst.ag.core.DsvToJsonlConverter;
import org.pst.ag.model.Person;
import org.pst.ag.record.Record;
import org.pst.ag.record.RecordSchema;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Mohammed Nadeem
 * @github https://github.com/naddym
 */
public class App {
    public static void main(String[] args) {

        try {
            // dsv-jsonl.jar -sourceFilePath= -delimiter= includesHeader= -destinationFilePath=
            Map<String, String> cmdArgumentMap = new HashMap<>();
            for (String arg: args) {
                String[] keyValuePair = arg.split("=");
                if (keyValuePair.length == 2) {
                    cmdArgumentMap.put(keyValuePair[0], keyValuePair[1]);
                }
            }

            final String sourceFilePath = cmdArgumentMap.get("-sourceFilePath");
            if (sourceFilePath == null || !Files.exists(Paths.get(sourceFilePath))) {
                throw new IllegalArgumentException("You must provide -sourceFilePath arg or valid file path");
            }

            String delimiter = cmdArgumentMap.get("-delimiter");
            if (delimiter == null) {
                delimiter = ",";
            }

            final boolean includesHeader = cmdArgumentMap.get("includesHeader") != null && cmdArgumentMap.get("includesHeader").toLowerCase().equals("false") ? false : true;

            final String destinationFilePath = cmdArgumentMap.get("-destinationFilePath");
            if (destinationFilePath == null) {
                throw new IllegalArgumentException("You must provide -desinationFilePath arg");
            }

            convertDsvToJsonl(sourceFilePath, delimiter, includesHeader, destinationFilePath);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    public static void convertDsvToJsonl(final String sourceFilePath,
                                         final String delimiter,
                                         final boolean includesHeader,
                                         final String destinationFilePath) throws Exception {
        final OutputStream out = new FileOutputStream(destinationFilePath);
        final String recordSeparator = "\n";

        // build schema for the Person class
        // we can make this generic method for
        // handling any type class
        // TODO:
        Class<?> target = Person.class;
        CsvSchema recordSchema = RecordSchema.buildSchema(target, delimiter.charAt(0));
        CsvMapper csvMapper = new CsvMapper();

        try(DsvToJsonlConverter converter = new DsvToJsonlConverter(out, recordSeparator)) {

            // get records as stream
            Stream<String> records = Files.lines(Paths.get(sourceFilePath));
            // skip if the provided file has header { first line }
            if(includesHeader) {
                records = records.skip(1);
            }

            records.map(s -> {
                try {
                    // cast to Map.class so that other class types can be parsed
                    return (Map<String, String>)csvMapper.readerFor(Map.class)
                            .with(recordSchema)
                            .readValues(s)
                            .next();
                } catch (IOException io) {
                    throw new RuntimeException(io);
                }
            }).forEach( (map) -> {
                    try {
                        // build record for Person.class
                        Record record = new Record(target);
                        for(Map.Entry<String, String> entry: map.entrySet()) {
                            record.addField(entry.getKey(), entry.getValue());
                        }
                        converter.convert(record);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            });
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
