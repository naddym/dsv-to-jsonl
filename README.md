To utilize Apache Maven, follow these steps:

1. Execute mvn install to compile and run the test cases.
2. After the compilation, a jar file will be generated in the "target" folder. You may either copy the jar file or navigate to the folder and proceed to the next step.
3. Use the following command to test your input:
    
    mnv exec:java -Dexec.args='-sourceFilePath="your_input_file_path" -delimiter="," -includesHeader=true -destinationFilePath="your_output_file_path"'

Please note that these instructions assume you have Apache Maven installed and set up properly.