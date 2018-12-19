package fileIO;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLSXUtils {
	static Logger logger = LogManager.getLogger();

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
		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet != null) return sheetToMap(sheet, keyCol);
		else throw new IllegalArgumentException("Parameter sheet '" + sheetName + "' not found in " + filename);
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
		{ if (row.getCell(i) != null) out.add(row.getCell(i).toString().trim());
		else out.add("");
		}
		return out;
	}


}
