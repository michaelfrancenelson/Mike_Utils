package objectIO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriterSettings;

/** Utility class for reading POJOs from input files
 *  @author michaelfrancenelson
 */
public class ObjectReader {
	/** represents the value of non-initialized fields. */
	public static final int NON_INITIALIZED_INT = Integer.MIN_VALUE;
	/** represents the value of non-initialized fields. */
	public static final double NON_INITIALIZED_DOUBLE = Double.MIN_VALUE;
	public static final String NON_INITIALIZED_STRING = "";

	/** Allows the isInitialized() methods to verify that the objects were corretly initialized form the input files. */
	@Retention(RetentionPolicy.RUNTIME) public static @interface Initialized {}

	/** Test cases on ForetClassicSpecies and JaBoWaParameters */
	public static void _main(String[] args) throws IllegalArgumentException, IllegalAccessException 
	{

		String paramFile = "testData/TestDataClassParams.xlsx";

		TestDataClass.readStatic(paramFile, "staticParams");
		TestDataClass.isStaticInitialized();
		@SuppressWarnings("unused")
		List<TestDataClass> list = TestDataClass.readInstanceList(paramFile, "instanceParams");
	}

	/** Check whether a field contains a value corresponding to one of the NA markers. 
	 * 
	 * @param f 
	 * @param o
	 * @param verboseFields print information to the console?
	 * @param instance is the field an instance variable? Should be 'false' for static fields.
	 * @return
	 */
	private static <T extends ObjectReader> boolean isFieldInitialized(
			Field f, T o, boolean instance, boolean verboseFields)
	{
		String message = "Field ", message2 = " is not initialized.", fVal = "";

		f.setAccessible(true);
		if (f.isAnnotationPresent(Initialized.class))
		{

			boolean isStatic = Modifier.isStatic(f.getModifiers()); 
			String simpleType = f.getType().getSimpleName();
			String fName = f.getName();
			String message3 = message + fName + message2;
			boolean b = true;
			if (o == null && (instance == true)) 
				throw new IllegalArgumentException("Object of type " + 
						f.getDeclaringClass().getSimpleName() + " is not initialized");
			if (isStatic == instance)
				throw new IllegalArgumentException("Trying to get instance value from a static member.");
			try {
				switch (simpleType) {
				case("int"): if (f.getInt(o)           == NON_INITIALIZED_INT)     b = false; break;
				case("Integer"): if (((int) f.getInt(o))         == NON_INITIALIZED_INT)     b = false; break;
				case("double"):  if (f.getDouble(o)    == NON_INITIALIZED_DOUBLE)  b = false; break;
//				case("Double"):  if (f.getDouble(o)    == NON_INITIALIZED_DOUBLE)  b = false; break;
				case("Double"):  if (((Double) f.get(o))    == NON_INITIALIZED_DOUBLE)  b = false; break;
				case("boolean"): if (f.get(o)          == null)                    b = false; break;
				case("String"):  if ((String) f.get(o) == NON_INITIALIZED_STRING)  b = false; break;
				}
				if (f.get(o) == null) throw new IllegalArgumentException("Field " + fName + " not found in parameter file");
				if (verboseFields) System.out.print(System.lineSeparator() + "field " + fName);
				fVal = f.get(o).toString();
			} catch (IllegalArgumentException | IllegalAccessException e1) { e1.printStackTrace(); } 
			if (b == false) throw new IllegalArgumentException(message3);
			if (verboseFields) System.out.print(" is initialized to " + fVal);
		}
		return true;
	}

	/** Set a field with the @Initialized annotation to the appropriate NA marker value. */
	private static <T extends ObjectReader> void initializeFieldToNA(Field f, T o, boolean instance)
	{
		if (o == null && instance == true)
			throw new IllegalArgumentException("Object of type " + 
					f.getDeclaringClass().getSimpleName() + " is not initialized");
		try {
			f.setAccessible(true);
			if (f.isAnnotationPresent(Initialized.class))
			{
				String shortName = f.getType().getSimpleName();
				switch (shortName) {
				case("int"):     f.setInt(o,     NON_INITIALIZED_INT); break;
				case("double"):  f.setDouble(o,  NON_INITIALIZED_DOUBLE); break;
				case("boolean"): f.setBoolean(o, Boolean.valueOf(null)); break;
				case("String"):	 f.set(o,        (String)NON_INITIALIZED_STRING); break;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) { e.printStackTrace();}
	}


	public <T extends ObjectReader> void initializeInstanceFieldsToNA(T o)
	{
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>)o.getClass();
		for (Field f : clazz.getDeclaredFields()) initializeFieldToNA(f, o, true); }

	/** Initialize static fields to the NA vals for the isInitalizedCheckers. */
	protected static <T extends ObjectReader> void initializeStaticFieldsToNA(Class<T> clazz)
	{ for (Field f : clazz.getDeclaredFields()) if (Modifier.isStatic(f.getModifiers())) initializeFieldToNA(f, null, false); }

	/** Verify that all the fields that have the "Initialized" annotation are initialized. */
	public static <T extends ObjectReader> boolean areInstanceFieldsInitailized(
			T t, boolean verboseFieldName, boolean verboseFieldValue)
	{
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) t.getClass(); 
		for (Field f : clazz.getDeclaredFields()) 
		{
			if (!Modifier.isStatic(f.getModifiers()))
				isFieldInitialized(f, t, true, verboseFieldValue); 
			else isFieldInitialized(f, t, false, verboseFieldValue); 
		}
		return true;
	}

	protected static <T extends ObjectReader> boolean areStaticFieldsInitailized(Class<T> clazz, boolean verboseFieldName, boolean verboseFieldValue)
	{
		for (Field f : clazz.getDeclaredFields()) 
			if (Modifier.isStatic(f.getModifiers()))
				isFieldInitialized(f, null, false, verboseFieldValue); 
		if (verboseFieldName)
			System.out.println("Class '" + clazz.getSimpleName() +
					"' static fields are initialized." + System.lineSeparator());
		return true;
	}

	/** Reads parameter values from a file arranged in columns.<br>
	 *  Column 1 is the parameter name <br>
	 *  Column 2 is the parameter value
	 * @param filename input file (Excel or csv)
	 * @param sheetName (optional) name of sheet within Excel file to read.  If not provided, reads the first sheet.
	 * @param c Class of objects (must extend {@link ObjectReader}).
	 * @return
	 */
	public static <T extends ObjectReader> T readParametersColumns(String filename, String sheetName, Class<T> c)
	{
		List<String[]> columns = fileToRows(filename, sheetName);

		/* for vertically-arranged parameters, keep only the first two rows. */
		List<String[]> tr = transpose(columns);
		List<String[]> rows = new ArrayList<>();

		rows.add(tr.get(0)); rows.add(tr.get(1));

		InputStream bain = rowsToStream(rows);
		return streamToObjectList(bain, c).get(0);	
	}

	/** Create POJOs from input file with data arranged in columns.  <br>
	 *  First column must include the field names, additional columns have values. 
	 * @param filename input file (Excel or csv)
	 * @param c Class of objects (must extend {@link ObjectReader}).
	 * @return the POJO 
	 */
	public static <T extends ObjectReader> List<T> readObjectListColumns(String filename, Class<T> c)
	{ return readObjectListColumns(filename, null, c); }
	/** Create POJOs from input file with data arranged in columns.  <br>
	 *  First column must include the field names, additional columns have values. 
	 * @param filename input file (Excel or csv)
	 * @param sheetName (optional) name of sheet within Excel file to read.  If not provided, reads the first sheet.
	 * @param c Class of objects (must extend {@link ObjectReader}).
	 * @return the POJO 
	 */
	public static <T extends ObjectReader> List<T> readObjectListColumns(String filename, String sheetName, Class<T> c)
	{
		List<String[]> rows = fileToRows(filename, sheetName);
		rows = transpose(rows);
		InputStream bain = rowsToStream(rows);
		return streamToObjectList(bain, c);
	}

	/**Create POJOs from input file with data arranged in columns.  <br>
	 *  First column must include the field names, additional columns have values. 
	 * @param filename input file (Excel or csv)
	 * @param c Class of objects (must extend {@link ObjectReader}).
	 * @return the POJO 
	 */
	public static <T extends ObjectReader> List<T> readObjectListRows(
			String filename, Class<T> c)
	{ return readObjectList(filename, c, null); }

	/**Create POJOs from input file with data arranged in columns.  <br>
	 * @param filename input file (Excel or csv)
	 * @param c Class of objects (must extend {@link ObjectReader}).
	 * @param sheetname the name of the sheet int the Excel file (ignored if file is csv).
	 * @return the POJO 
	 */
	public static <T extends ObjectReader> List<T> readObjectListRows(String filename, String sheetname, Class<T> c)
	{ return readObjectList(filename, c, sheetname); }


	/** Create POJOs from input file with data arranged in columns.  <br>
	 *  First column must include the field names, additional columns have values. 
	 * @param filename input file (Excel or csv)
	 * @param sheetName (optional) name of sheet within Excel file to read.  If not provided, reads the first sheet.
	 * @param c Class of objects (must extend {@link ObjectReader}).
	 * @return the POJO 
	 */
	public static <T extends ObjectReader> List<T> readObjectList(String filename, Class<T> c, String sheetName)
	{
		List<String[]> rows = fileToRows(filename, sheetName);
		InputStream bain = rowsToStream(rows);
		return streamToObjectList(bain, c);
	}

	/** Create list of POJOs from a text stream. */
	private static <T extends ObjectReader> List<T> streamToObjectList(InputStream stream, Class<T> c)
	{
		BeanListProcessor<T> rowProcessor = new BeanListProcessor<T>(c);

		CsvParserSettings parserSettings = new CsvParserSettings();
		parserSettings.setEmptyValue("-9999");
		parserSettings.setProcessor(rowProcessor);
		parserSettings.setHeaderExtractionEnabled(true);

		CsvParser parser = new CsvParser(parserSettings);
		parser.parse(stream);
		List<T> out = rowProcessor.getBeans();
		return out;
	}

	/** 
	 * 
	 * @param inputRows
	 * @return new list of String arrays with transposed entries
	 */
	private static List<String[]> transpose(List<String[]> inputRows)
	{
		int nRows = inputRows.get(0).length;
		int nCols = inputRows.size();
		List<String[]> newRows = new ArrayList<>();

		for (int row = 0; row < nRows; row++)
		{
			String[] r = new String[nCols];
			for (int i = 0; i < nCols; i++)
			{
				String temp = inputRows.get(i)[row];
				if (temp != null)
					temp = temp.trim();
				else temp = "";
				r[i] = temp;
			}
			newRows.add(r);
		}
		return newRows;
	}

	/**
	 * @param rows 
	 * @return
	 */
	private static ByteArrayInputStream rowsToStream(List<String[]> rows)
	{
		CsvWriterSettings settings = new CsvWriterSettings();
		settings.setNullValue("?");
		settings.getFormat().setComment('-');
		settings.setEmptyValue("!");
		settings.setSkipEmptyLines(false);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			for (int i = 0; i < rows.size(); i++)
				baos.write((String.join(",", rows.get(i)) + System.lineSeparator()).getBytes());
			//				baos.write(System.lineSeparator().getBytes());
		} catch (IOException e) { e.printStackTrace();	}

		return new ByteArrayInputStream(baos.toByteArray());
	}

	/** 
	 * @param filename input file (Excel or csv)
	 * @param sheetName (optional) name of sheet within Excel file to read.  If not provided, reads the first sheet.
	 * @return list of 1-D string arrays representing the rows in the file.
	 */
	private static List<String[]> fileToRows(String filename, String sheetName)
	{
		/* Check whether the input is a csv file or an Excel spreadsheet. */
		if (filename.contains(".xlsx") || filename.contains(".xls"))
			return xlsxToRows(filename, sheetName);
		else if (filename.contains(".csv"))
			return csvToRows(filename);
		else throw new IllegalArgumentException("Input file must be an Excel spreadsheet or a csv text file.");
	}

	/**
	 * @param filename input csv file
	 * @return list of 1-D string arrays representing the rows in the file.
	 */
	private static List<String[]> csvToRows(String filename)
	{
		File f = new File(filename);
		CsvParserSettings parserSettings = new CsvParserSettings();
		CsvParser parser = new CsvParser(parserSettings);
		List<String[]> rows = parser.parseAll(f);
		return rows;
	}

	/**
	 * 
	 * @param sheet a sheet of an Excel spreadsheet
	 * @return list of 1-D string arrays representing the rows in the file.
	 */
	private static List<String[]> xlsSheetToRows(Sheet sheet)
	{

		List<String[]> rows = new ArrayList<>();
		for (Row row : sheet) rows.add(rowToStringArray(row));
		return rows;
	}

	/**
	 * 
	 * @param filename name of the input Excel file
	 * @param sheetName optional sheet name, if null the first sheet is used
	 * @return list of 1-D string arrays representing the rows in the file.
	 */
	private static List<String[]> xlsxToRows(String filename, String sheetName)
	{
		Workbook wb;
		List<String[]> rows = null;
		String[] temp;
		try { 
			wb = new XSSFWorkbook(new File(filename));
			if (sheetName == null) 
				rows = xlsSheetToRows(wb.getSheetAt(0));
			else 
				rows = xlsSheetToRows(wb.getSheet(sheetName));
			wb.close();
		} catch (InvalidFormatException | IOException e) {e.printStackTrace(); }

		/* Make sure all the rows have the same number of entries and remove any empty rows. */
		int nCols = rows.get(0).length;
		List<String[]> newRows = new ArrayList<>();
		for (String[] st : rows) 
		{ if (st.length > 0) 
//		{ if (st.length >= nCols) 
		{
			temp = new String[nCols];
			for (int i = 0; i < nCols; i++) if (i >= st.length) temp[i] = ""; else temp[i] = st[i];
			//			for (int i = 0; i < st.length; i++) temp[i] = st[i];
			newRows.add(temp); }
		}
		return newRows;
	}

	/**
	 * 
	 * @param row Spreadsheet row
	 * @return 1-D string array representing the cells in the row
	 */
	private static String[] rowToStringArray(Row row)
	{
		DataFormatter formatter = new DataFormatter();
		String[] out = new String[row.getPhysicalNumberOfCells()];
		FormulaEvaluator evaluator = new XSSFFormulaEvaluator((XSSFWorkbook) row.getSheet().getWorkbook()); 	
		int index = 0;
		for (Cell c : row)
		{
			out[index] = formatter.formatCellValue(c, evaluator);
			index ++;
		}
		return out;
	}

}
