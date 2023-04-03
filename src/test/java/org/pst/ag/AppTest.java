package org.pst.ag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.pst.ag.model.Person;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

/**
 * Unit test for Convert DSV to JSONL App.
 */
public class AppTest {

    @Test(expected = Exception.class)
    public void shouldFailWhenInvalidSourceFilePathGiven() throws Exception {
        // given
        final String sourceFilePath = "src/test/resources/input/no_file.txt";
        final String delimiter = ",";
        final boolean includesHeader = true;
        final String destinationFilePath = "src/test/resources/output/1.txt";

        // when
        App.convertDsvToJsonl(sourceFilePath, delimiter, includesHeader, destinationFilePath);
    }

    @Test
    public void shouldSuccessWhenValidSourceFilePathAndCommaDelimiterGiven() throws Exception {
        // given
        final String sourceFilePath = "src/test/resources/input/1.txt";
        final String delimiter = ",";
        final boolean includesHeader = true;
        final String destinationFilePath = "src/test/resources/output/1.txt";

        // when
        App.convertDsvToJsonl(sourceFilePath, delimiter, includesHeader, destinationFilePath);

        List<Object> people = mapToObject(destinationFilePath, Person.class);

        // then
        assertTrue( people.size() == 3);
        assertEquals("Amadeus", ((Person)people.get(0)).getMiddleName());
    }

    @Test
    public void shouldSuccessWhenValidSourceFilePathAndPipeDelimiterGiven() throws Exception {
        // given
        final String sourceFilePath = "src/test/resources/input/2.txt";
        final String delimiter = "|";
        final boolean includesHeader = true;
        final String destinationFilePath = "src/test/resources/output/2.txt";

        // when
        App.convertDsvToJsonl(sourceFilePath, delimiter, includesHeader, destinationFilePath);

        List<Object> people = mapToObject(destinationFilePath, Person.class);

        // then
        assertTrue( people.size() == 3);
        assertEquals("Amadeus", ((Person)people.get(0)).getMiddleName());
    }

    @Test
    public void shouldSuccessWhenValidSourceFilePathAndIncludesHeaderSetToFalseGiven() throws Exception {
        // given
        final String sourceFilePath = "src/test/resources/input/3_without_header.txt";
        final String delimiter = ",";
        final boolean includesHeader = false;
        final String destinationFilePath = "src/test/resources/output/3_without_header.txt";

        // when
        App.convertDsvToJsonl(sourceFilePath, delimiter, includesHeader, destinationFilePath);

        List<Object> people = mapToObject(destinationFilePath, Person.class);

        // then
        assertTrue( people.size() == 3);
        assertEquals("Amadeus", ((Person)people.get(0)).getMiddleName());
    }

    /**
     *
     * @param path
     * @param clazz
     * @return list of objects to support other models apart from Person object
     * @throws Exception
     */
    private List<Object> mapToObject(final String path, Class<?> clazz) throws Exception {
        try {
            String output = new String(Files.readAllBytes(Paths.get(path)));

            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(output);

            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> result = new ArrayList<>();

            while (jsonParser.nextToken() != null) {
                Object obj = objectMapper.readValue(jsonParser, clazz);
                result.add(obj);
            }

            return result;
        } finally {
            // remove file
            try {
                new File(path).delete();
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
