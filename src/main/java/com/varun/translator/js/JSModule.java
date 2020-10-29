package com.varun.translator.js;

import java.util.ArrayList;
import java.util.List;

public class JSModule
{
	private String jsName;
	private boolean skip;
	private List<String> keys = new ArrayList<String>();
	private List<String> values = new ArrayList<String>();
	private List<byte[]> translatedValues = new ArrayList<byte[]>();

	public String getJsName()
	{
		return jsName;
	}

	public void setJsName( String jsName )
	{
		this.jsName = jsName;
	}

	public boolean isSkip()
	{
		return skip;
	}

	public void setSkip( boolean skip )
	{
		this.skip = skip;
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
