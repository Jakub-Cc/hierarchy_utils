package common;

import org.apache.commons.cli.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public abstract class CmdLineParser {
    private CommandLineParser parser;
    private CommandLine cmd;
    private Options options;
    private HelpFormatter helpText;

    public CmdLineParser() {
        parser = new BasicParser();
        options = new Options();
        helpText = new HelpFormatter();
        createOptions();
    }

    protected abstract void createOptions();
    protected abstract void parseParameters(Parameters paramsToSet);

    public void parse(String[] args, Parameters paramsToSet)
    {
        try {
            cmd = parser.parse(options, args);
        }
        catch( ParseException exp ) {
            System.err.println(exp.getMessage());
            System.exit(1);
        }
        if(cmd.hasOption('h') || cmd.hasOption("help") || args.length == 0)
        {
            viewHelp();
            System.exit(0);
        }
        else
        {
            parseParameters(paramsToSet);
        }
    }

    protected static Color parseColor(String optionValue) {
        Color returnValue = null;
        switch(optionValue)
        {
            case "green":
                returnValue = Color.green;
                break;
            case "black":
                returnValue = Color.black;
                break;
            case "blue":
                returnValue = Color.blue;
                break;
            case "lightBlue":
                returnValue = new Color(0, 191, 255);
                break;
            case "yellow":
                returnValue = Color.yellow;
                break;
            case "cyan":
                returnValue = Color.cyan;
                break;
            case "lightGray" :
                returnValue = Color.lightGray;
                break;
            case "gray":
                returnValue = Color.gray;
                break;
            case "darkGray":
                returnValue = Color.darkGray;
                break;
            case "magenta":
                returnValue = Color.magenta;
                break;
            case "orange":
                returnValue = Color.orange;
                break;
            case "pink":
                returnValue = Color.pink;
                break;
            case "red":
                returnValue = Color.red;
                break;
            case "white":
                returnValue = Color.white;
                break;
            default:
                System.err.println("Cannor parse color: " + optionValue + "."
                        + " It should be one of {green, black, blue, lightBlue, yellow,"
                        + " cyan, lightGray, gray, darkGray, magenta, orange,"
                        + " pink, red, white}.");
                System.exit(1);
        }
        return returnValue;
    }

    protected static int parsePositiveIntegerParameter(String parsedOptionValue, String invalidArgMsg) {
        int parsedValue = -1;
        try
        {
            parsedValue = Integer.valueOf(parsedOptionValue);
            if(parsedValue <= 0)
            {
                throw new NumberFormatException();
            }
        }
        catch(NumberFormatException e)
        {
            System.err.println("'" + parsedOptionValue + "' " + invalidArgMsg
                    + " " + e.getMessage());
            System.exit(-1);
        }
        return parsedValue;
    }

    protected static int parseIntegerParameter(String parsedOptionValue, String invalidArgMsg) {
        int parsedValue = -1;
        try
        {
            parsedValue = Integer.valueOf(parsedOptionValue);
        }
        catch(NumberFormatException e)
        {
            System.err.println("'" + parsedOptionValue + "' " + invalidArgMsg
                    + " " + e.getMessage());
            System.exit(-1);
        }
        return parsedValue;
    }

    protected static double parsePositiveDoubleParameter(String parsedOptionValue, String invalidArgMsg) {
        double parsedValue = -1;
        try
        {
            parsedValue = Double.valueOf(parsedOptionValue);
            if(parsedValue <= 0.0)
            {
                throw new NumberFormatException();
            }
        }
        catch(NumberFormatException e)
        {
            System.err.println("'" + parsedOptionValue + "' " + invalidArgMsg
                    + " " + e.getMessage());
            System.exit(-1);
        }
        return parsedValue;
    }

    protected static double parseDoubleParameter(String parsedOptionValue, String invalidArgMsg) {
        double parsedValue = -1;
        try
        {
            parsedValue = Double.valueOf(parsedOptionValue);
        }
        catch(NumberFormatException e)
        {
            System.err.println("'" + parsedOptionValue + "' " + invalidArgMsg
                    + " " + e.getMessage());
            System.exit(-1);
        }
        return parsedValue;
    }

    protected void viewHelp()
    {
        helpText.printHelp( "java -jar [jar_name].jar", options );
    }

    protected Path parseInputFile()
    {
        File inputFile = null;
        if(cmd.hasOption('i'))
        {
            inputFile = new File(cmd.getOptionValue('i'));
            if(!inputFile.exists() || inputFile.isDirectory())
            {
                System.err.println("Input file shoud be existing file!");
                System.exit(1);
            }
        }
        else
        {
            System.err.println("No input file specified! Use -i option.");
            System.exit(1);
        }
        return inputFile.toPath();
    }

    protected Path parseOutputFile()
    {
        File outputFolder = null;
        if(cmd.hasOption('o'))
        {
            String outputFolderName = cmd.getOptionValue('o');
            outputFolder = new File(outputFolderName);
            if(outputFolder.isFile())
            {
                System.err.println(outputFolderName + " should be an path to directory!");
                System.exit(1);
            }
            if(!outputFolder.exists())
            {
                System.out.println(outputFolderName + " doesn't exist, creating folder.");
                try {
                    Files.createDirectories(outputFolder.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            System.err.println("No output file specified! Use -o option.");
            System.exit(1);
        }
        return outputFolder.toPath();
    }
}

