package org.computer.aman.metrics.util.var_scope;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;

public class Main {

	public static final String VERSION = "4.0.0";
	public static final String COPYRIGHT = "(C) 2015-2022 Hirohisa AMAN <aman@computer.org>";
	public static final String HEADER = "path\tline\tkind\tname\ttype\tbegin\tend";

	public static void main(String[] args) 
	{
		// printVersion();
		
		if ( args.length == 0 ) {
			printError("Specify a source file or a directory!");
			//printUsage();
			System.exit(1);
		}

		boolean verboseMode = false;
		boolean dirMode = false;
		
		Path path = null;

		for ( int i = 0; i < args.length; i++ ){
			if ( args[i].startsWith("-") ){
				if ( args[i].equals("-v") ){
					verboseMode = true;
				}
				else{
					printError("Invalid option: " + args[i]);
					//printUsage();
					System.exit(1);
				}
			}
			else {
				if ( i+1 < args.length ) {
					printError("Too many arguments!");
					//printUsage();
					System.exit(1);	
				}
				path = Paths.get(args[i]);
				if ( Files.isDirectory(path) ) {
					dirMode = true;
				}
				if ( !Files.isReadable(path) ) {
					if ( dirMode ){
						printError("Cannot read the directory " + path);
						System.exit(1);
					}
					else{
						printError("Cannot read the file: " + path);	
						System.exit(1);				
					}
				}
			}
		}
		
		if ( path == null ){
			printError("No " + (dirMode ? "directory" : "Java source file") + " is specified!" );
			//printUsage();
			System.exit(1);	
		}
		
		System.out.println(HEADER);
		ArrayList<Path> targetList = extractTargetJavaFiles(path, dirMode, verboseMode);
		for (Iterator<Path> iterator = targetList.iterator(); iterator.hasNext();) {
			Path target = (Path)iterator.next();
			String targetStr = target.toString().replace(File.separator, "/");
			if ( verboseMode ) {
				System.err.println("==== " + targetStr + " ====");
			}
			try {
				VaribleDeclarationVisitor visitor = new VaribleDeclarationVisitor();
				try {
					StaticJavaParser.parse(target).accept(visitor, null);
				}
				catch( ParseProblemException ppe ) {
					if ( verboseMode ) {
						System.err.println("**** " + targetStr + " **** [parse error] Skipped!");						
					}
					continue;
				}
				catch( Error err ) {
					if ( verboseMode ) {
						System.err.println("**** " + targetStr + " **** [parse error] Skipped!");						
					}
					continue;
				}
				ArrayList<Variable> varList = visitor.getVariableList();
				for (Iterator<Variable> itrVar = varList.iterator(); itrVar.hasNext();) {
					Variable variable = (Variable) itrVar.next();
					if ( verboseMode ) {
						System.err.println(variable);						
					}
					System.out.println(targetStr + "\t" + variable.toTSV());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * Extracts the Java source files from the specified path.
	 * 
	 * @param aPath file or directory path
	 * @param dirMode is the specified path is a directory or not  
	 * @param verboseMode is it in the verbose mode or not
	 * @return ArrayList of Java source file paths
	 */
	private static ArrayList<Path> extractTargetJavaFiles(final Path aPath, final boolean dirMode, final boolean verboseMode)
	{
		ArrayList<Path> targetList = new ArrayList<Path>();
		
		if ( dirMode ){
			try ( Stream<Path> stream = Files.walk(aPath) ){
				for (Iterator<Path> iterator = stream.iterator(); iterator.hasNext();) {
					Path p = (Path)iterator.next();
					if ( p.getFileName().toString().endsWith(".java") && Files.isReadable(p) ) {
						targetList.add(p);
					}
				}
			}
			catch( IOException e ) {
				System.err.println(e);
			}
			System.err.println("[target dir] " + aPath);
			System.err.println(" (" + targetList.size() + " java files)");
			if ( verboseMode ) {
				int cnt = 1;
				for (Iterator<Path> iterator = targetList.iterator(); iterator.hasNext();) {
					Path p = (Path)iterator.next();
					System.err.println(" 　　" + cnt + ") " + p);
					cnt++;
				}
			}
		}
		else{
			if ( aPath.toString().endsWith(".java") && Files.isReadable(aPath) ) {
				targetList.add(aPath);
				System.err.println("[target file] " + aPath);
			}
		}
		
		if ( targetList.size() == 0 ) {
			printError("Java source file not found!");
			System.exit(1);	
		}
		
		return targetList;
	}
	
	/**
	 * Prints an error message with the usage.
	 * 
	 * @param aMessage error message
	 */
	private static void printError(final String aMessage)
	{
		System.err.println("*** Error ***");
		System.err.println(aMessage);
		System.err.println();
	}
	
	/**
	 * Prints the usage of this application.
	 */
	private static void printUsage()
	{
		System.err.println("[Usage]");
		System.err.println(" java -jar JavaVariableExtractor [-v] (java-file | java-file-directory)");
		System.err.println("    -v : verbose mode");
	}
	
	/**
	 * Prints the version information of this application.
	 */
	private static void printVersion()
	{
		System.err.println("This is JavaVariableExtractor ver." + VERSION + ".");
		System.err.println(COPYRIGHT);
		System.err.println();
	}

}
