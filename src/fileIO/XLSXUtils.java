package fileIO;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author michaelfrancenelson
 *
 */
public class XLSXUtils {

	/** Parameter tables for objects created with XLSXUtils.sheetToObjects() 
	 *  or XLSXUtils.XlsXToObjects() don't have to contain 
	 *  columns corresponding to all public fields. <br>
	 *  For fields not included in the parameter file, the class of the objects needs to have a method to set the
	 *  public fields that are not included in the parameter file.
	 * @author michaelfrancenelson
	 *
	 */
	public interface Initializable 
	{ 
		public void initialize(); 
		default public void printFieldValues()
		{
			System.out.println("Object of class " + this.getClass().getSimpleName() + " - Summary of field values.");
			Field[] fields = this.getClass().getFields(); 
			
			int nChars = 0;
			for (Field f : fields)
				nChars = Math.max(nChars, f.getName().length());
			String nameFormat = "%1$" + nChars + "s";
			
			for (Field f : fields)
				try {
					System.out.println("Field: " + String.format(nameFormat, f.getName()) + ": " + f.get(this).toString());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
		}
	}

	private static FormulaEvaluator evaluator;
	private static Workbook workbook = null;

	private static void openWorkbook(String filename)
	{
		File file = new File(filename);
		FileInputStream stream;
		try {
			stream = new FileInputStream(file);
			workbook = new XSSFWorkbook(stream);
			workbook.close();
		} catch (Exception e2) { e2.printStackTrace(); }
	}

	public static Map<String, List<String>> XslxToMap(String filename, String sheetName, String keyCol)
	{
		openWorkbook(filename);
		evaluator = workbook.getCreationHelper().createFormulaEvaluator();

		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet != null) return sheetToMap(sheet, keyCol);
		else throw new IllegalArgumentException("Parameter sheet '" + sheetName + "' not found in " + filename);
	}

	/**
	 * @param filename location of the input XLSX file <br>  
	 *                 The first row in the input spreadsheet contains the names of fields in the generated objects. <br>
	 *                 Each additional row contains the values for the fields of an instance of 'c'.
	 * @param the index of the sheet from which to read object parameters
	 * @param keyCol The column containing the keys for of the object instances in the output HashMap.
	 * @param C Class of objects to generate from the spreadsheet.  Must implement the Initializable interface.
	 * @return Map of newly generated objects, keyed by the entries in the column 'keyCol' in the input spreadsheet.
	 */
	public static <T> Map<String, T> XlsXToObjects(String filename, String keyCol, Class<T> C)
	{
		return XlsXToObjects(filename, 0, keyCol, C, false);
	}

	/**
	 * 
	 * @param filename location of the input XLSX file <br>  
	 *                 The first row in the input spreadsheet contains the names of fields in the generated objects. <br>
	 *                 Each additional row contains the values for the fields of an instance of 'c'.
	 * @param the index of the sheet from which to read object parameters
	 * @param keyCol The column containing the keys for of the object instances in the output HashMap.
	 * @param C Class of objects to generate from the spreadsheet.  Must implement the Initializable interface.
	 * @return Map of newly generated objects, keyed by the entries in the column 'keyCol' in the input spreadsheet.
	 */
	public static <T> Map<String, T> XlsXToObjects(String filename, int sheetNum, String keyCol, Class<T> C)
	{
		return XlsXToObjects(filename, sheetNum, keyCol, C, false);
	}

	/**
	 * 
	 * @param filename location of the input XLSX file <br>  
	 *                 The first row in the input spreadsheet contains the names of fields in the generated objects. <br>
	 *                 Each additional row contains the values for the fields of an instance of 'c'.
	 * @param the index of the sheet from which to read object parameters
	 * @param keyCol The column containing the keys for of the object instances in the output HashMap.
	 * @param C Class of objects to generate from the spreadsheet.  Must implement the Initializable interface.
	 * @return Map of newly generated objects, keyed by the entries in the column 'keyCol' in the input spreadsheet.
	 * @param printWarnings print info to the console?
	 * @return Map of newly generated objects, keyed by the entries in the column 'keyCol' in the input spreadsheet.
	 */
	public static <T> Map<String, T> XlsXToObjects(String filename, int sheetNum, String keyCol, Class<T> C, boolean printWarnings)
	{
		openWorkbook(filename);
		evaluator = workbook.getCreationHelper().createFormulaEvaluator();

		Sheet sheet = workbook.getSheetAt(sheetNum);
		if (sheet != null) return sheetToObjects(sheet, keyCol, C, printWarnings);
		else throw new IllegalArgumentException("Parameter sheet at index " + sheetNum + " not found in " + filename);
	}

	/**
	 * 
	 * @param filename location of the input XLSX file <br>  
	 *                 The first row in the input spreadsheet contains the names of fields in the generated objects. <br>
	 *                 Each additional row contains the values for the fields of an instance of 'c'.
	 * @param sheetName name of the sheet from which to read the object parameters
	 * @param keyCol The column containing the keys for of the object instances in the output HashMap.
	 * @param C Class of objects to generate from the spreadsheet.  Must implement the Initializable interface.
	 * @return Map of newly generated objects, keyed by the entries in the column 'keyCol' in the input spreadsheet.
	 */
	public static <T> Map<String, T> XlsXToObjects(String filename, String sheetName, String keyCol, Class<T> C){
		return XlsXToObjects(filename, sheetName, keyCol, C, false);
	}

	/**
	 * 
	 * @param filename location of the input XLSX file <br>  
	 *                 The first row in the input spreadsheet contains the names of fields in the generated objects. <br>
	 *                 Each additional row contains the values for the fields of an instance of 'c'.
	 * @param sheetName name of the sheet from which to read the object parameters
	 * @param keyCol The column containing the keys for of the object instances in the output HashMap.
	 * @param C Class of objects to generate from the spreadsheet.  Must implement the Initializable interface.
	 * @param printWarnings print info to the console?
	 * @return
	 */
	public static <T> Map<String, T> XlsXToObjects(String filename, String sheetName, String keyCol, Class<T> C, boolean printWarnings)
	{
		openWorkbook(filename);
		evaluator = workbook.getCreationHelper().createFormulaEvaluator();

		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet != null) return sheetToObjects(sheet, keyCol, C, printWarnings);
		else throw new IllegalArgumentException("Parameter sheet '" + sheetName + "' not found in " + filename);
	}

	public static <T> Map<String, T> sheetToObjects(Sheet sheet, String keyCol, Class<T> c, boolean printWarnings)
	{

		if (! Initializable.class.isAssignableFrom(c)) throw new IllegalArgumentException("Class must implement Initializeable.");
		Map<String, T> map = new HashMap<String, T>();

		Field[] fields = c.getFields();

		T t = null;

		/* A map of lists of strings containing the row contents of the sheet keyed by the text in keyCol. */
		Map<String, List<String>> dataMap = sheetToMap(sheet, keyCol);

		List<String> headers = dataMap.get("headers");
		if (printWarnings) for (String s : headers) System.out.println(s);
		/* Check that all the fields in T have corresponding columns in the sheet: */

		for (String name : dataMap.keySet())
		{
			if (! name.equals("headers"))
			{
				t = null;
				try { t = c.newInstance();
				} catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }

				if (printWarnings) System.out.println("Reading object " + name);
				for (int i = 0; i < fields.length; i++)
				{
					Field f = fields[i];
					String fName = f.getName();
					if (headers.contains(fName))
					{
						int valIndex = headers.indexOf(fName);
						String val = dataMap.get(name).get(valIndex);

						String type = f.getType().getSimpleName();
						try {
							switch(type)
							{
							case "int": 
								int valInt = (int)(Double.parseDouble(val));
								f.setInt(t, valInt);
								break;
							case "double": f.setDouble(t, Double.parseDouble(val)); break;
							case "String": f.set(t, val); break;
							case "boolean": f.setBoolean(t, Boolean.parseBoolean(val)); break;
							}
						}
						catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					else
						if (printWarnings) 
							System.out.println("Warning: parameter " + fName + " not found in the parameter file.  "
									+ "Class " + c.getSimpleName() + " should initialize this field in the initialize() method.");
				}
				((Initializable) t).initialize();
				map.put(name, (T)t);
			}
		}
		return map;
	}

	private static Map<String, List<String>> sheetToMap(Sheet sheet, String keyCol)
	{
		Map<String, List<String>> out = new HashMap<String, List<String>>();

		Row row = sheet.getRow(0);
		int nCols = row.getPhysicalNumberOfCells();
		int nRows = sheet.getLastRowNum();
		List<String> headers = readXlsxRow(sheet, 0, nCols);

		int colIndex = headers.indexOf(keyCol);
		if (colIndex == -1) throw new IllegalArgumentException("Column containing the keys not found (" + keyCol + ").");

		out.put("headers", headers);
		for (int i = 1; i < nRows + 1; i++)
		{
			if (sheet.getRow(i) != null)
			{
				List<String> rowIn = readXlsxRow(sheet, i, nCols);
				if (rowIn.size() > 0 && rowIn.get(colIndex).length() > 0) out.put(rowIn.get(colIndex).trim(), rowIn);
			}
		}
		return out;
	}

	/** Return the contents of a properly formatted XLSX sheet in the form of a map: <br>
	 *  File format is: <br>
	 *  first row: column names <br>
	 *  additional rows:  values of the attributes of the column names. <br>
	 *  key name column:  Must be strings.  Values in this column will become the keys for the output map.
	 * @param filename
	 * @param sheetIndex
	 * @param keyCol
	 * @return A map with spreadsheet row contents as lists, keyed by the key name column. 
	 *  Contains a key "headers" that contains a list of the original spreadsheet column names. 
	 */
	public static Map<String, List<String>> XslxToMap(String filename, int sheetIndex, String keyCol)
	{
		Sheet sheet = null;

		openWorkbook(filename);

		if (sheetIndex > workbook.getNumberOfSheets() -1)
			throw new IllegalArgumentException("Trying to retrieve a sheet that is not in the file");
		sheet = workbook.getSheetAt(sheetIndex);

		return sheetToMap(sheet, keyCol);
	}


	private static List<String> readXlsxRow(Sheet sheet, int rowIndex, int nCols)
	{

		List<String> out = new ArrayList<String>();

		Row row = sheet.getRow(rowIndex);

		for (int i = 0; i < nCols; i++) 
		{ 
			if (row.getCell(i) != null) 
			{
				CellValue c = evaluator.evaluate(row.getCell(i));
				String val = c.formatAsString();
				String val2 = val.replace("\"", "");
				//				String val2 = val.replaceAll("^\"+ \"+$", "");
				//				out.add(c.toString().trim());
				out.add(val2.trim());
			}
			else out.add("");
		}
		return out;
	}


}
