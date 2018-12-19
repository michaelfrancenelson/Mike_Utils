package abm_utils.parameters;

import java.awt.Color;
import java.util.Comparator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

public class ModelParameter implements Comparator<ModelParameter> {

	public static double NA_DOUBLE = Double.MIN_VALUE;
	public static int NA_INT = Integer.MIN_VALUE;
	public static boolean NA_BOOLEAN = false;
	public static String NA_STRING = "";
	public static Color NA_COLOR = new Color(0, 0, 0, 0); 

	private Map<String, String> properties;

	private String st = "";
	private void setSt(String field) 
	{ 
		st = properties.get(field).trim(); 
		if (st == null) st = NA_STRING; 
	}
	private boolean isNA() { return st == NA_STRING; }
	/** TODO: add ability for fuzzy matching. */
	private static boolean hasName(Map<String, String> in) { return in.containsKey("name"); }
	/** TODO: add ability for fuzzy matching. */
	private static boolean hasValue(Map<String, String> in) { return in.containsKey("value"); }

	/** The map must at least contain keys "name" and "value". */
	public ModelParameter (Map<String, String> in)
	{
		/* Make sure the map contains at least the "name" and "value" keys. */
		if (! (hasName(in) && hasValue(in))) throw new IllegalArgumentException("Parameter must include at least the fields 'name' and 'value'.");
		properties = in;
	}

	public ModelParameter(String name, String value, String min, String max, String type)
	{
		properties.put("name", name);
		properties.put("value", value);
		properties.put("min", min);
		properties.put("max", max);
		properties.put("type", type);
	}

	public int compare(ModelParameter c1, ModelParameter c2) { return c1.properties.get("name").compareTo(c2.properties.get("name")); }

	public String getName() { return getStringValue("name"); }
	public String getStringValue() { return getStringValue("value"); }
	public String getStringValue(String field) 
	{ 
		setSt(field);
		return st;
	}

	public int getInt() { return getInt("value"); }
	public int getInt(String field) 
	{ 
		setSt(field);
		if (isNA()) return NA_INT;
		int out;
		try {out = Integer.parseInt(st); }
		catch(NumberFormatException e) { out = (int) Double.parseDouble(st);}
		return out;
	}

	public Color getHexColor(String field)
	{
		setSt(field);
		if (isNA()) return NA_COLOR;
		
		/* The decode() method doesn't accept hexes with alpha, must prune to six hex digits. */
		if (st.length() > 6) st = st.substring(0, 7);
		Color out = Color.decode(st);
		return out;
	}

	/** Retrieve the double value of the parameter property "value", or the value of NA_DOUBLE if it is not set. */
	public double getDouble() { return getDouble("value"); }
	/** Retrieve the double value of the specified parameter field, or the value of NA_DOUBLE if it is not set. */
	public double getDouble(String field)
	{
		setSt(field);
		if (isNA()) return NA_DOUBLE;
		return Double.parseDouble(st);
	}

	/** Retrieve the boolean value of the specified parameter field, or the value of NA_BOOLEAN if it is not set. */
	public boolean getBool(String field)
	{
		setSt(field);
		if (isNA()) return NA_BOOLEAN;
		return Boolean.parseBoolean(st);
	}

	/** Retrieve the double value of the parameter property "value", or the value of NA_BOOLEAN if it is not set. */
	public boolean getBool() { return getBool("value"); };
	//	{ return Boolean.parseBoolean(value); }

	public int getIntMin() { return getInt("min"); }
	public int getIntMax() { return getInt("max"); }

	public double getDoubleMin() { return getDouble("min"); }
	public double getDoubleMax() { return getDouble("max"); }

	public int[] getIntArray() 
	{
		setSt("value");
		String[] outSt = st.split(" ");

		int[] outInt = new int[outSt.length];
		for(int i = 0; i < outSt.length; i++)
			outInt[i] = Integer.parseInt(outSt[i]);
		return outInt;
	}

	public double[] getDoubleArray() 
	{
		setSt("value");
		String[] outSt = st.split(" ");
		double[] outDub = new double[outSt.length];
		for(int i = 0; i < outSt.length; i++)
			outDub[i] = Integer.parseInt(outSt[i]);
		return outDub;
	}

	public boolean[] getBoolArray()
	{
		setSt("value");
		String[] outSt = st.split(" ");
		boolean[] outB = new boolean[outSt.length];
		for(int i = 0; i < outSt.length; i++)
			outB[i] = Boolean.parseBoolean(outSt[i]);
		return outB;
	}


	public static ModelParameter readParameterRow(Row row, int namesIndex, int valuesIndex, int minIndex, int maxIndex, int typeIndex)
	{
		String name = "";
		String value = "";
		String min = "";
		String max = "";
		String type = "";

		if (row.getCell(namesIndex) != null) name = row.getCell(namesIndex).toString();
		if (row.getCell(valuesIndex) != null) value = row.getCell(valuesIndex).toString();
		if (row.getCell(minIndex) != null) min = row.getCell(minIndex).toString();
		if (row.getCell(maxIndex) != null) max = row.getCell(maxIndex).toString();
		if (row.getCell(typeIndex) != null) type = row.getCell(typeIndex).toString();

		return new ModelParameter(name, value, min, max, type);
	}
}
