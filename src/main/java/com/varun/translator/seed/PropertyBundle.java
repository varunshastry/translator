package com.varun.translator.seed;

import java.util.ArrayList;
import java.util.List;

public class PropertyBundle
{
	private String fileName;
	private List<String> keys = new ArrayList<String>();
	private List<String> values = new ArrayList<String>();
	private List<byte[]> translatedValues = new ArrayList<byte[]>();

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName( String fileName )
	{
		this.fileName = fileName;
	}

	public List<String> getKeys()
	{
		return keys;
	}

	public void setKeys( List<String> keys )
	{
		this.keys = keys;
	}

	public List<String> getValues()
	{
		return values;
	}

	public void setValues( List<String> values )
	{
		this.values = values;
	}

	public List<byte[]> getTranslatedValues()
	{
		return translatedValues;
	}

	public void setTranslatedValues( List<byte[]> translatedValues )
	{
		this.translatedValues = translatedValues;
	}
}
