package com.varun.translator.seed;

import java.io.File;
import java.io.FilenameFilter;

import com.google.api.translate.Translate;

import com.varun.translator.StringHelper;
import com.varun.translator.Translator;

public class SeedI18NTranslator
{
	public static final String PROPERTIES_EXT = ".properties";

	/*
	 * Usage : SeedI18NTranslator <dirPath> <fromLanguage> <toLanguage> 
	 * SeedI18NTranslator C:\\try en_GB fr_FR
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

		SeedReader reader = new SeedReader( fromLocal );
		Translator translator = new Translator( fromLanguage, toLanguage );
		SeedWriter writer = new SeedWriter( toLocal );

		int count = 0;
		File[] listFiles = dir.listFiles( filter );
		for ( File file : listFiles )
		{
			if ( file.getName().contains( fromLocal ) )
			{
				PropertyBundle bundle = new PropertyBundle();
				reader.readFile( file, bundle );
				translator.translateBundle( bundle, 128 );
				writer.writeFile( dir, bundle );
				System.out.println( "Conversion Completed For : " + file.getName() + ". Completed -- " + ( ++count ) + " / " + listFiles.length );
			}
		}
	}

	FilenameFilter filter = new FilenameFilter()
	{
		public boolean accept( File dir, String name )
		{
			return name.matches( "^[A-Z]+[a-zA-Z_0-9]*" + PROPERTIES_EXT );
		}
	};
}
