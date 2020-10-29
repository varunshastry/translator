package com.varun.translator.seed;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SeedWriter
{
	private String toLocal;

	public SeedWriter( String toLocal )
	{
		this.toLocal = toLocal;
	}

	public void writeFile( File dir, PropertyBundle propertyModule ) throws IOException
	{
		if ( propertyModule != null )
		{
			String fileName = propertyModule.getFileName().substring( 0, propertyModule.getFileName().indexOf( SeedI18NTranslator.PROPERTIES_EXT ) );
			File file = new File( dir.getPath() + "\\" + fileName + "_" + toLocal + SeedI18NTranslator.PROPERTIES_EXT );
			file.createNewFile();
			FileOutputStream outputStream = new FileOutputStream( file, true );

			for ( String key : propertyModule.getKeys() )
			{
				byte[] value = propertyModule.getTranslatedValues().get( propertyModule.getKeys().indexOf( key ) );
				outputStream.write( new String( key + " = " ).getBytes() );
				outputStream.write( value );

				outputStream.write( new String( "\n" ).getBytes() );
			}
			outputStream.flush();
			outputStream.close();
		}
	}
}