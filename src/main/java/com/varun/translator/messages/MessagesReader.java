package com.varun.translator.messages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MessagesReader
{
	public Map<String, String> readTokens( File file ) throws IOException
	{
		Map<String, String> tokenValueMap = new LinkedHashMap<String, String>();

		int lineNo = 0;
		FileReader fr = new FileReader( file );
		BufferedReader br = new BufferedReader( fr );
		while ( true )
		{
			String line = br.readLine();
			if ( line == null )
				break;
			lineNo++;
			line = line.trim();
			if ( line.length() == 0 || line.startsWith( "#" ) )
				continue;
			String[] tokens = line.split( ":" );
			if ( tokens.length < 2 )
			{
				System.out.println( "Ignoring the invalid 'property' line '" + lineNo + "' encountered in the file: " + file.getAbsolutePath() );
				continue;
			}
			if ( tokens.length > 2 )
			{
				for ( int i = 2; i < tokens.length; i++ )
					tokens[1] += tokens[i];
			}
			tokenValueMap.put( tokens[0].trim(), tokens[1].trim() );
		}
		br.close();
		fr.close();
		return tokenValueMap;
	}
}