package com.varun.translator.js;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.google.api.translate.Translate;

import com.varun.translator.StringHelper;
import com.varun.translator.Translator;

public class JSTranslator
{
	/*
	 * Usage : JSTranslator <dirPath> <fromLanguage> <toLanguage> Example: JSTranslator C:\\try en_GB fr_FR
	 */

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

		JSReader reader = new JSReader( fromLocal, toLocal );
		Translator translator = new Translator( fromLanguage, toLanguage );
		JSWriter writer = new JSWriter( toLocal );

		List<JSModule> modulesInFile;

		int count = 0;
		File[] listFiles = dir.listFiles( filter );
		for ( File file : listFiles )
		{
			modulesInFile = new ArrayList<JSModule>();
			reader.readFile( file, modulesInFile );
			translator.translate( modulesInFile );
			writer.writeFile( file, modulesInFile );
			System.out.println( "Conversion Completed For : " + file.getName() + ". Completed -- " + ( ++count ) + " / " + listFiles.length );
		}
	}

	FilenameFilter filter = new FilenameFilter()
	{
		public boolean accept( File dir, String name )
		{
			return name.matches( "^[A-Z]+[A-Za-z]*.js" );
		}
	};
}
