package com.keove.parserlibrary;

import java.util.ArrayList;


public class ComplexElementProperty {
	
	public ComplexElementProperty(){}
	
	
	
	public Class c;
	public String TagName;
	public ArrayList<AttributeProperty> attrlist;
	public ArrayList<TagProperty> taglist;
	public ArrayList<ComplexElementProperty> complexlist;
	public Boolean isArrayOffC;
	
	public void SetSingleAsArray(ComplexElementProperty cep)
	{
		ArrayList<ComplexElementProperty> ceps = new ArrayList<ComplexElementProperty>();
		ceps.add(cep);
		complexlist = ceps;
	}

}
