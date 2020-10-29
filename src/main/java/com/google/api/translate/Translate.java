package com.google.api.translate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import com.google.api.GoogleAPI;

public final class Translate extends GoogleAPI
{
	/*private static final String LANG_PARAM = "&langpair=";
	private static final String TEXT_PARAM = "&q=";
	private static final String PIPE_PARAM = "%7C";
	private static final String URL = "http://ajax.googleapis.com/ajax/services/language/translate";
	private static final String PARAMETERS = "v=2.0&langpair=#FROM#%7C#TO#&q=";*/
	private static final String KEY = "trnsl.1.1.20150615T100344Z.0172e45650194242.dcb75fbc48681706683f0236239e04f11bb8ce06";

	/*public static String execute( String text, Language from, Language to ) throws Exception
	{
		validateReferrer();

		URL url = new URL( "http://ajax.googleapis.com/ajax/services/language/translate" );
		String parameters = "v=2.0&langpair=#FROM#%7C#TO#&q=".replaceAll( "#FROM#", from.toString() ).replaceAll( "#TO#", to.toString() ) + URLEncoder.encode( text, "UTF-8" ) + ( key != null ? "&key=" + key : "" );

		JSONObject json = retrieveJSON( url, parameters );

		return getJSONResponse( json );
	}*/

	public static String[] execute( String[] text, Language from, Language to ) throws Exception
	{
		validateReferrer();

		String[] responses = new String[text.length];
		int k = 0;

		URL url = new URL( "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + KEY );

		StringBuilder parametersBuilder = new StringBuilder();
		parametersBuilder.append( "&lang=" ).append( from ).append( '-' ).append( to );

		String paramSkeleton = parametersBuilder.toString();
		parametersBuilder.setLength( 0 );

		for ( int i = 0; i < text.length; i++ )
		{
			if ( text[i] == null )
				continue;

			parametersBuilder.append( "&text=" ).append( URLEncoder.encode( text[i].toString(), "UTF-8" ) );

			if ( parametersBuilder.length() > 9000 || i == text.length - 1 )
			{
				JSONObject jsonObj = fetchJSON( url, paramSkeleton + parametersBuilder.toString() );
				JSONArray responseArray = ( JSONArray ) jsonObj.get( "text" );
				for ( int j = 0; j < responseArray.length(); j++ )
				{
					responses[k++] = responseArray.get( j ).toString();
				}
				parametersBuilder.setLength( 0 );
			}
		}
		return responses;
	}

	/*public static String[] execute( String[] text, Language[] from, Language[] to ) throws Exception
	{
		validateReferrer();

		if ( ( text.length != from.length ) || ( from.length != to.length ) )
		{
			throw new Exception( "[google-api-translate-java] The same number of texts, from and to languages must be supplied." );
		}

		if ( text.length == 1 )
		{
			return new String[]
			{ execute( text[0], from[0], to[0] ) };
		}

		String[] responses = new String[text.length];

		StringBuilder parametersBuilder = new StringBuilder();

		parametersBuilder.append( "v=2.0&langpair=#FROM#%7C#TO#&q=".replaceAll( "#FROM#", from[0].toString() ).replaceAll( "#TO#", to[0].toString() ) + ( key != null ? "&key=" + key : "" ) );
		parametersBuilder.append( URLEncoder.encode( text[0], "UTF-8" ) );

		for ( int i = 1; i < text.length; i++ )
		{
			parametersBuilder.append( "&langpair=" );
			parametersBuilder.append( from[i].toString() );
			parametersBuilder.append( "%7C" );
			parametersBuilder.append( to[i].toString() );
			parametersBuilder.append( "&q=" );
			if ( text[i] == null )
				continue;
			parametersBuilder.append( URLEncoder.encode( text[i].toString(), "UTF-8" ) );
		}

		URL url = new URL( "http://ajax.googleapis.com/ajax/services/language/translate" );

		JSONArray json = retrieveJSON( url, parametersBuilder.toString() ).getJSONArray( "responseData" );

		for ( int i = 0; i < json.length(); i++ )
		{
			JSONObject obj = json.getJSONObject( i );

			responses[i] = getJSONResponse( obj );
		}
		
		return responses;
	}*/

	private static JSONObject fetchJSON( URL url, String parameters ) throws Exception
	{
		try
		{
			HttpsURLConnection uc = ( HttpsURLConnection ) url.openConnection();
			uc.setDoOutput( true );
			uc.setDoInput( true );

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
			throw new Exception( "Error retrieving translation.", ex );
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

	/*private static String getJSONResponse( JSONObject json ) throws Exception
	{
		if ( json.getString( "responseStatus" ).equals( "200" ) )
		{
			String translatedText = json.getJSONObject( "responseData" ).getString( "translatedText" );
			return HTMLEntities.unhtmlentities( translatedText );
		}
		throw new Exception( "Google returned the following error: [" + json.getString( "responseStatus" ) + "] " + json.getString( "responseDetails" ) );
	}*/

	public static String translate( String text, Language from, Language to ) throws Exception
	{
		String result[] = execute( new String[]
		{ text }, from, to );
		return result.length > 0 ? result[0] : null;
	}
}