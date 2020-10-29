package com.varun.translator.js;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class JSWriter
{
	private String toLocal;

	public JSWriter( String toLocal )
	{
		this.toLocal = toLocal;
	}

	public void writeFile( File file, List<JSModule> modulesInFile ) throws IOException
	{
		FileOutputStream outputStream = new FileOutputStream( file, true );

		for ( JSModule module : modulesInFile )
		{
			if ( module.isSkip() )
				continue;

			outputStream.write( new String( "\n" ).getBytes() );
			outputStream.write( new String( module.getJsName() + "_" + toLocal + " = { \n" ).getBytes() );

			int count = 0;
			for ( String key : module.getKeys() )
			{
				byte[] value = module.getTranslatedValues().get( module.getKeys().indexOf( key ) );
				outputStream.write( new String( "\t\"" + key + "\" : \"" ).getBytes() );

				outputStream.write( value );
				outputStream.write( new String( "\"" ).getBytes() );

				if ( ++count < module.getTranslatedValues().size() )
					outputStream.write( new String( "," ).getBytes() );

				outputStream.write( new String( "\n" ).getBytes() );
			}

			outputStream.write( new String( "};\n" ).getBytes() );
		}
		outputStream.flush();
		outputStream.close();

		/*file = new File( "D:\\F\\PS10.0\\I18N\\js-Saran\\Updated\\" + file.getName() );
		file.createNewFile();
		FileOutputStream outputStream = new FileOutputStream( file, true );

		for ( JSModule module : modulesInFile )
		{
			if ( module.isSkip() )
				continue;

			outputStream.write( new String( module.getJsName() + "_en = { \n" ).getBytes() );

			int count = 0;
			for ( String key : module.getKeys() )
			{
				byte[] value = module.getTranslatedValues().get( module.getKeys().indexOf( key ) );
				outputStream.write( new String( key + " : \"" ).getBytes() );

				outputStream.write( value );
				outputStream.write( new String( "\"" ).getBytes() );

				if ( ++count < module.getTranslatedValues().size() )
					outputStream.write( new String( "," ).getBytes() );

				outputStream.write( new String( "\n" ).getBytes() );
			}

			outputStream.write( new String( "};\n" ).getBytes() );
		}
		outputStream.flush();
		outputStream.close();*/
	}
}
