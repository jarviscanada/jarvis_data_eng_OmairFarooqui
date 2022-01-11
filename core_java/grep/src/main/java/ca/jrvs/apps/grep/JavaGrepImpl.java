package ca.jrvs.apps.grep;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.sun.org.slf4j.internal.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

public class JavaGrepImpl implements JavaGrep {

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String g_regex;
    private String g_rootPath;
    private String g_outFile;

    @Override
    public void process() throws IOException{
        List<String> matchedLines = new ArrayList<String>();
        List<File> lf = listfiles(g_rootPath);
        //for loops and if
        for (File f : lf){
            List<String> lines = readLines(f);
            for (String line : lines){
                if (containsPattern(line)==true){
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listfiles(String rootDir){
        File dir = new File(rootDir);
        File fileList[] = dir.listFiles();
        List<File> listOfFiles = new ArrayList<File>();

        for (File file : fileList){
            listOfFiles.add(file);
        }

        return listOfFiles;
    }

    @Override
    public List<String> readLines(File inputFile){
        Scanner sc = new Scanner(inputFile);
        List<String> listOfLines = new ArrayList<String>();
        while(sc.hasNextLine()){
            listOfLines.add(sc.nextLine());
        }
        return listOfLines;
    }

    @Override
    public boolean containsPattern(String line){
        String regex = getRegex();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        while(matcher.find()){
            return true;
        }
        return false;

    }

    @Override
    public void writeToFile(List<String> lines) throws IOException{
        String outFile = getOutFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, true));

        for (String l : lines){
            writer.append(l + "\n");
        }
        writer.close();
    }

    @Override
    public String getRootPath(){
        return g_rootPath;
    }

    @Override
    public void setRootPath(String rootPath){
        g_rootPath = rootPath;
    }

    @Override
    public String getRegex(){
        return g_regex;
    }

    @Override
    public void setRegex(String regex){
        g_regex = regex;
    }

    @Override
    public String getOutFile(){
        return g_outFile;
    }

    @Override
    public void setOutFile(String outFile){
        g_outFile = outFile;
    }

    public static void main(String[] args) throws IOException {

        if(args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        BasicConfigurator.configure();

        JavaGrepImpl javaGrepImpl = new JavaGrepImpl();
        javaGrepImpl.setRegex(args[0]);
        javaGrepImpl.setRootPath(args[1]);
        javaGrepImpl.setOutFile(args[2]);

        try{
            javaGrepImpl.process();
        }catch (Exception ex){
            javaGrepImpl.logger.error("Error: Unable to process", ex);
        }
    }
}