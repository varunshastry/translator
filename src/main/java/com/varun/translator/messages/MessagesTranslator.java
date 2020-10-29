package com.varun.translator.messages;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

import com.google.api.translate.Translate;

import com.varun.translator.StringHelper;
import com.varun.translator.Translator;

public class MessagesTranslator
{
	public void doConversion( String[] args ) throws Exception
	{
		String a = StringHelper.generateString();
		Translate.setHttpReferrer( a );

		String fromLocal = args[1];
		String toLocal = args[2];
		String fromLanguage = fromLocal.split( "_" )[0];
		String toLanguage = toLocal.split( "_" )[0];

		File dir = new File( args[0] );
		if ( !dir.isDirectory() )
			return;

		MessagesReader reader = new MessagesReader();
		Translator translator = new Translator( fromLanguage, toLanguage );
		MessagesWriter writer = new MessagesWriter();

		int count = 0;
		File[] listFiles = dir.listFiles( filter );
		for ( File file : listFiles )
		{
			Map<String, String> tokens = reader.readTokens( file );
			Map<String, String> translatedTokens = translator.getTranslatedTokens( tokens );
			String fileName = file.getName();
			int lastIndxOfDot = fileName.lastIndexOf( '.' );
			String translatedFileName = fileName.substring( 0, lastIndxOfDot ) + '_' + toLanguage + fileName.substring( lastIndxOfDot );
			writer.write( translatedTokens, dir.getAbsolutePath() + "\\" + translatedFileName );
			count++;
			System.out.println( translatedFileName + " file generated for " + fileName + ". Conversion complete for " + count + " / " + listFiles.length + " files." );
		}
	}

	FilenameFilter filter = new FilenameFilter()
	{
		public boolean accept( File dir, String name )
		{
			return name.matches( "[A-Za-z0-9_]*essages[A-Za-z0-9_]*.properties" );
		}
	};
}