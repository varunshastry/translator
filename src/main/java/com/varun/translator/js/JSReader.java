package com.varun.translator.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;

import com.varun.translator.StringHelper;

public class JSReader
{
	private String fromLocal;
	private String toLocal;

	public JSReader( String fromLocal, String toLocal )
	{
		this.fromLocal = fromLocal;
		this.toLocal = toLocal;
	}

	public void readFile( File file, List<JSModule> modulesInFile ) throws IOException
	{
		FileInputStream fis = new FileInputStream( file );
		InputStreamReader ins = new InputStreamReader( fis );
		BufferedReader reader = new BufferedReader( ins );
		String line;
		while ( ( line = reader.readLine() ) != null )
		{
			if ( line.contains( "=" ) && line.contains( fromLocal ) )
			{
				JSModule jsModule = new JSModule();
				jsModule.setJsName( StringHelper.getJSName( line, fromLocal ) );
				initializeKeyValues( reader, jsModule );
				modulesInFile.add( jsModule );
			}
			else if ( line.contains( "=" ) )
			{
				String jsName = StringHelper.getJSName( line, toLocal );
				if ( jsName == null )
					continue;

				jsName = jsName.split( "=" )[0].trim();
				for ( JSModule module : modulesInFile )
				{
					if ( module.getJsName().trim().equals( jsName ) )
					{
						module.setSkip( true );
					}
				}
			}
		}
		reader.close();
		ins.close();
		fis.close();
	}

	Pattern compile = Pattern.compile( "^([ \t\n\r\f]*\".*\"[ \t\n\r\f]*:)|(.*:)" );

	private void initializeKeyValues( BufferedReader reader, JSModule jsModule ) throws IOException
	{
		while ( true )
		{
			/*line = line.replaceAll( ": *\"", ":" );
			line = line.replaceAll( "\"[ \t\n\r\f]*,*[ \t\n\r\f]*$", "" );*/

			String line = reader.readLine();
			line = line.trim();
			if ( line.length() == 0 )
				continue;

			if ( line.startsWith( "}" ) )
				break;

			int endIndxOfVar = line.indexOf( '}' );

			if ( endIndxOfVar > 0 )
				line = line.substring( 0, endIndxOfVar );

			String[] parts = extractKeyValue( line );

			if ( parts[0].equals( "" ) )
				continue;

			if ( !jsModule.getKeys().contains( parts[0] ) )
			{
				jsModule.getKeys().add( parts[0] );
				jsModule.getValues().add( parts[1] );

			}
			if ( endIndxOfVar > 0 )
				break;
		}
	}

	private String[] extractKeyValue( String line )
	{
		String[] keyValuePair = new String[]
		{ "", "" };

		String keyAndRestOfLine = line.substring( 1 );
		int firstDoubleQuoteIndx = keyAndRestOfLine.indexOf( '"' );
		String key = keyAndRestOfLine.substring( 0, firstDoubleQuoteIndx );
		String restOfLineWithoutSemiColon = keyAndRestOfLine.substring( firstDoubleQuoteIndx + 1 ).trim().substring( 1 ).trim();
		String value = restOfLineWithoutSemiColon.substring( 1, restOfLineWithoutSemiColon.lastIndexOf( '"' ) );

		keyValuePair[0] = key;
		keyValuePair[1] = value;

		/*Matcher matcher = compile.matcher( line );
		if ( matcher.find() )
		{
			String group = matcher.group();
			keyValuePair[0] = group.substring( 0, group.length() - 1 );
			int regionEnd = matcher.end();
			keyValuePair[1] = line.substring( regionEnd );
		}*/
		return keyValuePair;
	}
}
