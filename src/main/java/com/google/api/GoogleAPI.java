package com.google.api;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class GoogleAPI
{
	protected static final String ENCODING = "UTF-8";
	protected static String referrer;
	protected static String key;

	public static void setHttpReferrer( String pReferrer )
	{
		referrer = pReferrer;
	}

	public static void setKey( String pKey )
	{
		key = pKey;
	}

	public static void validateReferrer() throws Exception
	{
		if ( ( referrer == null ) || ( referrer.length() == 0 ) )
			throw new Exception( "[google-api-translate-java] Referrer is not set. Call setHttpReferrer()." );
	}

	protected static JSONObject retrieveJSON( URL url ) throws Exception
	{
		try
		{
			HttpURLConnection uc = ( HttpURLConnection ) url.openConnection();
			uc.setRequestProperty( "referer", referrer );
			uc.setRequestMethod( "GET" );
			uc.setDoOutput( true );
			try
			{
				String result = inputStreamToString( uc.getInputStream() );

				JSONObject localJSONObject = new JSONObject( result );
				return localJSONObject;
			}
			finally
			{
				uc.getInputStream().close();
				if ( uc.getErrorStream() != null )
					uc.getErrorStream().close();
			}
		}
		catch ( Exception ex )
		{
			throw new Exception( "[google-api-translate-java] Error retrieving translation.", ex );
		}
	}

	protected static JSONObject retrieveJSON( URL url, String parameters ) throws Exception
	{
		try
		{
			HttpURLConnection uc = ( HttpURLConnection ) url.openConnection();
			uc.setRequestProperty( "referer", referrer );
			uc.setRequestMethod( "POST" );
			uc.setDoOutput( true );

			PrintWriter pw = new PrintWriter( uc.getOutputStream() );
			pw.write( parameters );
			pw.flush();
			try
			{
				String result = inputStreamToString( uc.getInputStream() );

				JSONObject localJSONObject = new JSONObject( result );
				return localJSONObject;
			}
			finally
			{
				uc.getInputStream().close();
				if ( uc.getErrorStream() != null )
				{
					uc.getErrorStream().close();
				}
				pw.close();
			}
		}
		catch ( Exception ex )
		{
			throw new Exception( "[google-api-translate-java] Error retrieving translation.", ex );
		}
	}

	private static String inputStreamToString( InputStream inputStream ) throws Exception
	{
		StringBuilder outputBuilder = new StringBuilder();
		try
		{
			if ( inputStream != null )
			{
				BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream, "UTF-8" ) );
				String string;
				while ( ( string = reader.readLine() ) != null )
				{
					outputBuilder.append( string ).append( '\n' );
				}
			}
		}
		catch ( Exception ex )
		{
			throw new Exception( "[google-api-translate-java] Error reading translation stream.", ex );
		}

		return outputBuilder.toString();
	}
}