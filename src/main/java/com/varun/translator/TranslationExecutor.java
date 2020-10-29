package com.varun.translator;

import com.varun.translator.messages.MessagesTranslator;

import com.varun.translator.seed.SeedI18NTranslator;
import com.varun.translator.js.JSTranslator;

public class TranslationExecutor
{
	public static void main( String[] args ) throws Exception
	{
		if ( args.length != 4 )
		{
			printHelp();
			return;
		}
		String type = args[0];
		String[] newArgs = new String[args.length - 1];
		for ( int i = 0, j = 1; j < args.length; i++, j++ )
			newArgs[i] = args[j];
		if ( type.equals( Constants.JS ) )
			new JSTranslator().doConversion( newArgs );
		else if ( type.equals( Constants.SEED ) )
			new SeedI18NTranslator().doConversion( newArgs );
		else if ( type.equals( Constants.MESSAGES ) )
			new MessagesTranslator().doConversion( newArgs );
		else
			printHelp();
	}

	public static void printHelp()
	{
		System.out.println( "\nUsage:" );
		System.out.println( "JS translation: java -jar Translator.jar " + Constants.JS + " <directory> <from_language> <to_language>" );
		System.out.println( "Seed data translation: java -jar Translator.jar " + Constants.SEED + " <directory> <from_language> <to_language>" );
		System.out.println( "Server messages translation: java -jar Translator.jar " + Constants.MESSAGES + " <directory> <from_language> <to_language>" );
	}
}