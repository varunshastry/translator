package com.varun.translator;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;

import com.varun.translator.js.JSModule;
import com.varun.translator.seed.PropertyBundle;

public class Translator
{
	final int MAX_JSON_LENGTH = 128;

	private String fromLanguage;
	private String toLanguage;

	public Translator( String fromLanguage, String toLanguage )
	{
		this.fromLanguage = fromLanguage;
		this.toLanguage = toLanguage;
	}

	public void translate( List<JSModule> modulesInFile ) throws Exception
	{
		for ( JSModule module : modulesInFile )
		{
			if ( module.isSkip() )
				continue;

			translateModule( module, MAX_JSON_LENGTH );
		}
	}

	public void translateModule( JSModule module, int batchSize ) throws Exception
	{
		String[] array = module.getValues().toArray( new String[0] );
		List<String> convertedValues = new ArrayList<String>();

		int index = 0;
		try
		{
			for ( ; index < array.length; index += batchSize )
			{
				int destLength = Math.min( array.length, index + batchSize );
				String[] items = Translate.execute( Arrays.copyOfRange( array, index, destLength ), Language.fromString( fromLanguage ), Language.fromString( toLanguage ) );
				convertedValues.addAll( Arrays.asList( items ) );
			}
		}
		catch ( JSONException ex )
		{
			ex.printStackTrace( System.err );
			System.exit( 1 );
		}

		for ( String value : convertedValues )
		{
			value = getEditedText( value ); // Required to replace % 1 to %1
			value = value.replace( "\"", "\\\"" ); // Required for french
			value = value.replace( "\\ n", "\\n" ); // Fixing gooogle's problem in our code.
			byte[] byteValue = value.getBytes( "UTF8" );
			module.getTranslatedValues().add( byteValue );
		}

	}

	public void translateBundle( PropertyBundle bundle, int batchSize ) throws Exception
	{
		String[] array = bundle.getValues().toArray( new String[0] );
		List<String> convertedValues = new ArrayList<String>();

		int index = 0;
		try
		{
			for ( ; index < array.length; index += batchSize )
			{
				int destLength = Math.min( array.length, index + batchSize );
				String[] items = Translate.execute( Arrays.copyOfRange( array, index, destLength ), Language.fromString( fromLanguage ), Language.fromString( toLanguage ) );
				convertedValues.addAll( Arrays.asList( items ) );
			}
		}
		catch ( JSONException ex )
		{
			ex.printStackTrace( System.err );
			System.exit( 1 );
		}

		for ( String value : convertedValues )
		{
			value = getEditedText( value ); // Required to replace % 1 to %1
			value = value.replace( "\"", "\\\"" ); // Required for french
			value = value.replace( "\\ n", "\\n" ); // Fixing gooogle's problem in our code.
			byte[] byteValue = value.getBytes( "UTF8" );
			bundle.getTranslatedValues().add( byteValue );
		}
	}

	public Map<String, String> getTranslatedTokens( Map<String, String> tokens ) throws Exception
	{
		Map<String, String> translatedTokens = new LinkedHashMap<String, String>();
		String[] translatedValues = Translate.execute( ( String[] ) tokens.values().toArray(), Language.fromString( fromLanguage ), Language.fromString( toLanguage ) );
		int indx = 0;
		for ( String key : tokens.keySet() )
		{
			String value = translatedValues[indx];
			translatedTokens.put( key, value );
		}
		return translatedTokens;
	}

	private static String getEditedText( String aText )
	{
		StringBuffer result = new StringBuffer();
		Matcher matcher = fINITIAL_A.matcher( aText );
		while ( matcher.find() )
		{
			matcher.appendReplacement( result, "%" + matcher.group( 0 ).replace( " ", "" ).replace( "%", "" ) );
		}
		matcher.appendTail( result );
		return result.toString();
	}

	private static final Pattern fINITIAL_A = Pattern.compile( "%[ \t\n\r\f]*(\\d){1}|(\\d){1}[ \t\n\r\f]*%", Pattern.CASE_INSENSITIVE );
}
