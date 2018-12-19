package abm_utils.parameters;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import fileIO.XLSXUtils;

public abstract class Parameters {

	//	private static Map<String, ModelParameter> paramMap;
	//	private static String[] paramNames;

	protected static void initializeFromMap() {
		try {
			throw new Exception("Must implement in each sub class...");
		} catch (Exception e) {
			e.printStackTrace();
		} }

	private static Map<String, String> mapFromLine(List<String> names, List<String> values)
	{
		/* Make sure the lists have the same length. */
		if (names.size() != values.size()) 
			throw new IllegalArgumentException("field name and value lists  not the same length.");
		Map<String, String> out = new HashMap<String, String>();
		for (int i = 0; i < names.size(); i++)
			out.put(names.get(i), values.get(i));
		return out;
	}

	private static Map<String, ModelParameter> readParamsFromMap(Map<String, List<String>> map)
	{
		Map<String, ModelParameter> out = new HashMap<String, ModelParameter>();

		List<String> headers = map.get("headers");

		for (String st : map.keySet())
			if (!st.equals("headers"))
				out.put(st, new ModelParameter(mapFromLine(headers, map.get(st))));

		return out;
	}

	public static Map<String, ModelParameter> readParamsFromXlsx(String filename, int sheetIndex) { return readParamsFromMap(XLSXUtils.XslxToMap(filename, sheetIndex, "name")); }
	public static Map<String, ModelParameter> readParamsFromXlsx(String filename, String sheetName) { return readParamsFromMap(XLSXUtils.XslxToMap(filename, sheetName, "name")); }

	protected static <T> void initializeFromMap( Map<String, ModelParameter> paramMap, Class<T> c)
	{
		if (c == Parameters.class)
			try {
				throw new Exception("Must set the value of c to the class of this parameter set");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		Field[] fields = c.getFields();
		List<String> names = new LinkedList<String>();
		for (Field f : fields) names.add(f.getName());

		try {
			for (String key : paramMap.keySet())
			{
				ModelParameter p = paramMap.get(key);
				Field f;

				if (names.contains(p.getName()))
				{
					f = c.getField(p.getName());

					switch (f.getType().getSimpleName())
					//					switch (p.getStringValue("type"))
					{
					case "Integer": f.set(null, (Integer) p.getInt()); break;
					case "Double" : f.setDouble(null, (Double) f.getDouble(null)); break;
					case "int": f.setInt(null, p.getInt()); break;
					case "double": f.setDouble(null, p.getDouble()); break;
					case "boolean":	f.setBoolean(null, p.getBool()); break;
					case "String": String val = p.getStringValue(); f.set(null, val); break;
					//					case "java.lang.String": String val = p.getStringValue(); f.set(null, val); break;
					default: throw new IllegalArgumentException("parameter type for " + p.getName() + " not found");
					}
				}
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}

		/* Check that all parameters were initialized. */
		for (Field f : fields)
			try {
				if (f.get(null) == null) System.out.println(f.getName() + " is not initialized");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		/* Report any parameters that were in the worldSource file, but that aren't fields in the class. */
		for (String st : paramMap.keySet())
			if (!names.contains(st)) 
				System.out.println(c.getSimpleName() + String.format("%30s", st) + " is in parameter file, but is not a field in the " + c.getSimpleName() + " class.");

		//		setParamNames();
	}

	public static <T> void initializeFromXLSX(String filename, String sheet, Class<T> c) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		Map<String, ModelParameter> paramMap = readParamsFromXlsx(filename, sheet);
		initializeFromMap(paramMap, c);
	}

	protected static <T> void printFields(Class<T> c) throws IllegalArgumentException, IllegalAccessException
	{
		Field[] fields = c.getFields();

		for (Field f : fields)
		{
			if (f.get(null) != null)
				System.out.println("contains field: " + f.getName() + "(" + f.get(null).toString() + ")");
			else
				System.out.println("contains field: " + f.getName() + "(null)");
		}

	}

	public static <T> void initializeFromXLSX(String filename, int sheet, Class<T> c) throws EncryptedDocumentException, InvalidFormatException, IOException
	{

		Map<String, ModelParameter> paramMap = readParamsFromXlsx(filename, sheet);
		initializeFromMap(paramMap, c);
		//		paramMap = readParamsFromXlsx(filename, sheet);
		//		initializeFromMap(c);
	}

	//	private static void setParamNames()
	//	{ 
	//		Set<String> keySet = paramMap.keySet();
	//		paramNames = new String[keySet.size()];
	//
	//		int n = 0;
	//		for (String nm : keySet) { paramNames[n] = nm; n++; }
	//
	//		Arrays.sort(paramNames);
	//	}

	public static <T> String[] getParamNames(String prefix, Class<T> c)
	{
		Field[] fields = c.getFields();
		String[] names = new String[fields.length + 1];

		names[0] = prefix;
		for (int i = 0; i < fields.length; i++)
			names[i + 1] = fields[i].getName();

		return names;
	}

	public static <T> String[] getParamValues(Class<T> c)
	{
		Field[] fields = c.getFields();
		String[] values = new String[fields.length];
		try {
			for (int i = 0; i < fields.length; i++)
				values[i] = fields[i].get(null).toString();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return values;
	}
}
