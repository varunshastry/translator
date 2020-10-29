package com.varun.translator.messages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class MessagesWriter
{
	public void write( Map<String, String> translatedTokens, String fileName ) throws IOException
	{
		FileWriter fw = new FileWriter( fileName );
		BufferedWriter bw = new BufferedWriter( fw );
		for ( Map.Entry<String, String> token : translatedTokens.entrySet() )
		{
			bw.write( token.getKey() + " : " + token.getValue() );
			bw.newLine();
		}
		bw.flush();
		fw.flush();
		bw.close();
		fw.close();
	}
}