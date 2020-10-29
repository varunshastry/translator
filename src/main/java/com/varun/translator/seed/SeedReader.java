package com.varun.translator.seed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.varun.translator.StringHelper;

public class SeedReader
{
	private String fromLocal;

	public SeedReader( String fromLocal )
	{
		this.fromLocal = fromLocal;
	}

	public void readFile( File file, PropertyBundle jsModule ) throws IOException
	{
		FileInputStream fis = new FileInputStream( file );
		InputStreamReader ins = new InputStreamReader( fis );
		BufferedReader reader = new BufferedReader( ins );

		jsModule.setFileName( StringHelper.getJSName( file.getName(), fromLocal ) );
		initializeKeyValues( reader, jsModule );
		reader.close();
		ins.close();
		fis.close();
	}

	Pattern compile = Pattern.compile( "^([ \t\n\r\f]*\".*\"[ \t\n\r\f]*=)|(.*=)" );

	private void initializeKeyValues( BufferedReader reader, PropertyBundle propertyModule ) throws IOException
	{
		String line = reader.readLine();

		if ( line == null )
			return;

		while ( line != null )
		{
			line = line.replaceAll( "= *\"", "=" );
			//			line = line.replaceAll( "\"[ \t\n\r\f]*,*[ \t\n\r\f]*$", "" );

			String[] parts = extractKeyValue( line );

			line = reader.readLine();
			if ( parts[0].equals( "" ) )
				continue;

			if ( !propertyModule.getKeys().contains( parts[0] ) )
			{
				propertyModule.getKeys().add( parts[0] );
				propertyModule.getValues().add( parts[1] );
			}

		}
	}

	private String[] extractKeyValue( String line )
	{
		String[] keyValuePair = new String[]
		{ "", "" };

		Matcher matcher = compile.matcher( line );
		if ( matcher.find() )
		{
			String group = matcher.group();
			keyValuePair[0] = group.substring( 0, group.length() - 1 );
			int regionEnd = matcher.end();
			keyValuePair[1] = line.substring( regionEnd );
		}
		return keyValuePair;
	}
}
