package objectIO;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CsvWriter {




	public static String randomString(int length, Random r)
	{
		String out = "";
		for (int i = 0; i < length; i++)
		{
//			out += (char)r.nextInt(55));
			out += "a";
		}
		return out;
	}




	public static void main(String[] args) {
		List<Employee> emps = new ArrayList<>();


		String outFile = "testOutput/testCsv.csv";

		Random r = new Random();

		for (int i = 0; i < 10; i++)
		{
			Employee e = new Employee();
			e.setId(randomString(3, r));
			e.setName(randomString(6, r));
			e.setAge(randomString(2, r));
			e.setCountry(randomString(6, r));
			emps.add(e);
		}

		
	     // List<MyBean> beans comes from somewhere earlier in your code.
	     Writer writer;
		try {
			writer = new FileWriter(outFile);
			StatefulBeanToCsv<Employee> beanToCsv = new StatefulBeanToCsvBuilder<Employee>(writer).build();
			beanToCsv.write(emps);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvDataTypeMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CsvRequiredFieldEmptyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
//		Writer writer = null;
//		try {
//			writer = Files.newBufferedWriter(Paths.get(outFile));
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
//		
//		CsvToBean cb = new CsvToBeanBuilder(reader)
//		
//		ColumnPositionMappingStrategy<Employee> ms = new ColumnPositionMappingStrategy<>();
//		ms.setType(Employee.class);
//
//
//
//		StatefulBeanToCsv<Employee> beanToCsv = new StatefulBeanToCsvBuilder<Employee>(writer).
//				withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();
//
//		try {
//			beanToCsv.write(emps);
//			writer.close();
//		} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException | IOException e1) {
//			e1.printStackTrace();
//		}

//		try {
//			FileOutputStream fout = new FileOutputStream(outFile);
//			ObjectOutputStream oos = new ObjectOutputStream(fout);
//			oos.writeObject(emps);
//			fout.close();
//			oos.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


	}


	public static class Employee 
	{

		@CsvBindByName(column = "id") private String id;
		@CsvBindByName(column = "name") private String name;
		@CsvBindByName(column = "age") private String age;
		@CsvBindByName(column = "country") private String country;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		@Override
		public String toString() {
			return "{" + id + "::" + name + "::" + age + "::" + country + "}";
		}
	}

}
