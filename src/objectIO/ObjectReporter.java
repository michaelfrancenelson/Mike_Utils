package objectIO;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

/**
 *  @author michaelfrancenelson
 *
 */
public class ObjectReporter<T> {
// TODO create reporter ability with custom annotation for fields
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target(ElementType.FIELD)
//	public @interface State {}
//	private void setHeaders(Class<State> clazz, String...additionalColumnNames)
//	{
//		outputFileHeaderList = new ArrayList<>();
//		Field[] fields = clazz.getDeclaredFields();
//		for (Field f : fields) 
//		{	f.setAccessible(true);
//			if (f.isAnnotationPresent(clazz)) 
//			{ String fname = f.getName();
//				outputFileHeaderList.add(fname);
//			}}
//
//		int i = 0; String[] processorHeaders = new String[outputFileHeaderList.size()];
//		for (String st : outputFileHeaderList) { processorHeaders[i] = st; i++; }
//		for (String st : additionalColumnNames) outputFileHeaderList.add(st);		
//	}
	
	BeanWriterProcessor<T> processor;
	List<String> outputFileHeaderList;
	String[] processorHeaders;
	CsvWriter writer;
	OutputStream streamOut;
	Class<T> clazz;
	
	public ObjectReporter(Class<T> clazz, String... additionalColumnNames)
	{
		this.clazz = clazz;
		streamOut = new ByteArrayOutputStream();
		processor = new BeanWriterProcessor<T>(clazz);
		setHeaders(additionalColumnNames);
		
		CsvWriterSettings settings = new CsvWriterSettings();
		settings.setRowWriterProcessor(processor);
		settings.setHeaders(processorHeaders);
		settings.setNullValue("null");
		writer = new CsvWriter(streamOut, settings);
		writer.addValues(outputFileHeaderList);
		writer.writeValuesToRow();
	}


	/** Add data to the reporter. */
	public void appendToReport(List<T> list, Object... extraColumns)
	{
		for (T t : list)
		{
			writer.addValues(processor.write(t, processorHeaders, null));
			for (Object st : extraColumns) writer.addValue(st.toString());
			writer.writeValuesToRow();
		}
	}

	/** Write the data to file and close the reporter. */
	public void write(String filename) throws IOException
	{
		writer.close();
		FileWriter writer = new FileWriter(filename);
		writer.write(streamOut.toString());
		writer.close();
	}

	/** Sets up the headers for the row processor and the output file. */
	private void setHeaders(String... additionalColumnNames)
	{
		outputFileHeaderList = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) 
		{	f.setAccessible(true);
			if (f.isAnnotationPresent(Parsed.class)) 
			{ String fname = f.getName();
				outputFileHeaderList.add(fname);
			}}

		int i = 0; String[] processorHeaders = new String[outputFileHeaderList.size()];
		for (String st : outputFileHeaderList) { processorHeaders[i] = st; i++; }
		for (String st : additionalColumnNames) outputFileHeaderList.add(st);
	}

	/** Simple test write case. */
	public static void _main(String[] args) throws IOException {
		List<DataValueObject> objList = new ArrayList<DataValueObject>();
		ObjectReporter<DataValueObject> reporter =
				new ObjectReporter<DataValueObject>(DataValueObject.class, "cohort_id", "step");
		
		objList.add(new DataValueObject(1, 11, 111, 1111, "abc1"));
		objList.add(new DataValueObject(2, 21, 211, 2111, "abc2"));
		objList.add(new DataValueObject(3, 31, 311, 3111, "abc3"));
		objList.add(new DataValueObject(4, 41, 411, 4111, "abc4"));
		objList.add(new DataValueObject(5, 51, 511, 5111, "abc5"));

		reporter.appendToReport(objList, "1", "1234");
		reporter.appendToReport(objList, "asdf", "14");

		reporter.write("test.csv");
	}

	/** Simple test data class. */
	private static class DataValueObject 
	{
		
		@Parsed protected int one, two, three, four;
		@Parsed protected String str;
		@SuppressWarnings("unused")
		protected String str2, str3;

		public DataValueObject(int one, int two, int three, int four, String str)
		{
			this.one = one; this.two = two; this.three = three; this.four = four; this.str = str;
			str2 = "ignored";
		}
	}
}
