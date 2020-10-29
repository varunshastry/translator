package com.varun.translator;

import java.util.Random;

public class StringHelper
{
	public static String generateString()
	{
		int length = 7;
		String characters = "abcdefghijklmnopqrstuvwxyz";
		Random rng = new Random();
		char[] text = new char[length];
		for ( int i = 0; i < length; i++ )
		{
			text[i] = characters.charAt( rng.nextInt( characters.length() ) );
		}
		return new String( text );
	}

	public static String getJSName( String line, String fromLocal )
	{
		String[] split = line.split( " =" );
		for ( String part : split )
		{
			if ( part.contains( fromLocal ) )
				return part.replace( "_" + fromLocal, "" );
		}
		return null;
	}
}
