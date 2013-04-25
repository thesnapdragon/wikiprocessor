package wikiprocessor.parser;

import hu.sztaki.sztakipediaparser.wiki.converter.DefaultWikiInterpreter;
import hu.sztaki.sztakipediaparser.wiki.converter.IWikiInterpreter;
import hu.sztaki.sztakipediaparser.wiki.converter.PlainWikiInterpreter;
import hu.sztaki.sztakipediaparser.wiki.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class SimpleParser extends Parser {

	public SimpleParser(IWikiInterpreter interpreter) {
		super(interpreter);
		// TODO Auto-generated constructor stub
	}
	
	public void parse() {
		String[] args = new String[10];
		String inputFileName = args[0];
		File infile = new File(inputFileName);
		String outputFileName = args[1];
		File outfile = new File(outputFileName);
		boolean isIODirectory = infile.isDirectory();
		if ((infile.isDirectory() && outfile.isFile()) || (infile.isFile() && outfile.isDirectory())) {
			printUsage();
			System.out.println("Input and output must be the same type: both files, or directories!");
		}

		String locale = args[2];

		String renderer = "default";
		if (args.length > 3) {
			renderer = args[3];
		}
		String rewrite = "";
		if (args.length > 4) {
			rewrite = args[4];
		}

		// Create interpreter
		IWikiInterpreter wikiInterpreter = null;
		try {
			if ("html".startsWith(renderer)) {
				wikiInterpreter = new DefaultWikiInterpreter(new Locale(locale));
			} else if ("plain".startsWith(renderer)) {
				wikiInterpreter = new PlainWikiInterpreter(new Locale(locale));
			} else {
				wikiInterpreter = new DefaultWikiInterpreter(new Locale(locale));
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create parser
		Parser p = new Parser(wikiInterpreter);

		// set rewrite files
		if ("true".startsWith(rewrite)) {
			p.setRewriteExistingFile(true);
		}

		// process input
		try {
			if (isIODirectory) {
				p.parseDirectory(inputFileName, outputFileName);
			} else {
				p.parseFile(inputFileName, outputFileName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
