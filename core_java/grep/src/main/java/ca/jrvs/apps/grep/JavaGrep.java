package main.java.ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.*;

public interface JavaGrep {

    /**
     * Workflow of application
     * @throws IOException
     */

    void process() throws IOException;

    /**
     * Traverse a directory and return all files
     * @param rootDir (input directory)
     * @return files under root directory
     */
    List<File> listfiles(String rootDir);

    /**
     * Read through a file and return all lines in that file
     * @param inputFile
     * @return lines
     */
    List<String> readLines(File inputFile);

    /**
     * check if input line contains regex pattern
     * @param line
     * @return true if match otherwise false
     */
    boolean containsPattern(String line);

    /**
     * write lines to output files
     * @param lines
     * @throws IOException
     */
    void writeToFile(List<String> lines) throws IOException;

    String getRootPath();

    void setRootPath(String rootPath);

    String getRegex();

    void setRegex(String regex);

    String getOutFile();

    void setOutFile(String outFile);

}