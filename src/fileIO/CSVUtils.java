package fileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVUtils {  

	public static <T> void write2DArrayToCSV(String[] headers, T[][] array, String filePath) {  
		FileWriter fw = null;
		BufferedWriter bw;
		try {  
			fw = new FileWriter(filePath);
			bw = new BufferedWriter(fw);	
			bw.write(csvRow(headers, ","));

			for (int i = 0; i < array.length; i++) {  
				bw.write("\n" + csvRow(array[i], ","));
			}
			bw.close();
		} catch (IOException e) {  
			e.printStackTrace();
		}
	}
	
	
	public static <T> void writeListToFile(List<T> list, String filePath)
	{
		FileWriter fw = null;
		BufferedWriter bw;
		try {  
			fw = new FileWriter(filePath);
			bw = new BufferedWriter(fw);	
			
			for (int i = 0; i < list.size(); i++) {  
				bw.write("\n" + list.get(i));
			}
			bw.close();
		} catch (IOException e) {  
			e.printStackTrace();
		}
		
		
	}
	
	public static <T> void write2DArrayToCSV(T[][] array, String filePath) {  
		FileWriter fw = null;
		BufferedWriter bw;
		try {  
			fw = new FileWriter(filePath);
			bw = new BufferedWriter(fw);	

			for (int i = 0; i < array.length; i++) {  
				bw.write("\n" + csvRow(array[i], ","));
			}
			bw.close();
		} catch (IOException e) {  
			e.printStackTrace();
		}
	}
	
	static <T> String csvRow(T[] a, String sep) {  
		String out = a[0].toString();
		for (int i = 1; i < a.length; i++) {  
			out = out + sep + a[i].toString();
		}
		return out;
	}
	
	public static Double[][] castPrimitiveDoubleToContainer(double[][] a) {  
		Double[][] b = new Double[a.length][a[0].length];
		
		for (int i = 0; i < a.length; i++)
		for (int j = 0; j < a[0].length; j++) {  
			b[i][j] = (Double)a[i][j];
		}
		return b;
	}
	
	
	public static Map<String, Map<String, String>> csvToNestedMap(String filename, String idColumnName)
	{
		Map<String, Map<String, String>> out = null;
		FileReader fw = null; BufferedReader bw = null;
		List<String> headers, elements;

		try {  
			fw = new FileReader(filename);
			bw = new BufferedReader(fw);
		} catch (FileNotFoundException e) {  
			e.printStackTrace();
		}
		
		try {
			String line = bw.readLine();
			headers = Arrays.asList(line.split(","));
			
			int mainColIndex = headers.indexOf(idColumnName);
			out = new HashMap<String, Map<String, String>>();
			
			while ((line = bw.readLine()) != null) {  
				
				elements = Arrays.asList(line.split(","));
				
				Map<String, String> map = new HashMap<String, String>();
				for(int i = 0; i < headers.size(); i++){
					map.put(headers.get(i), elements.get(i));
				}
				
				out.put(elements.get(mainColIndex), map);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public static Map<String, String[]> csvToHashMap(String filename) {  
		
		Map<String, String[]> mp = new HashMap<String, String[]>();
		FileReader fw = null;
		try {  
			fw = new FileReader(filename);
		} catch (FileNotFoundException e) {  
			e.printStackTrace();
		}
		BufferedReader bw = new BufferedReader(fw);
		
		String line;
		try {  
			while ((line = bw.readLine()) != null) {  
				
				String[] elements = line.split(",");
				
				
				/* How many of the value elements have real values? */
				int count = 0;
				for (int i = 0; i < elements.length; i++) if (!elements[i].equals("")) count++;
				
				
				String key = elements[0];
				String[] value = new String[count - 1];
				value[0] = elements[1];
				
				if (count > 2)
				 {  
					for (int i = 2; i < elements.length; i++)
					{
						value[i - 1] = elements[i];
					}
				}
				
				
				mp.put(key, value);
			}
		} catch (IOException e) {  
			e.printStackTrace();
		}
		
		return mp;
	}
}
