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

import objectIO.ObjectReader.FieldColumnGetter;

/**
 *  @author michaelfrancenelson
 *
 */
public class ObjectReporter<T>
{

	protected BeanWriterProcessor<T> processor;
	protected List<String> outputFileHeaderList;
	protected String[] processorHeaders;
	protected CsvWriter writer;
	protected CsvWriterSettings settings;
	protected OutputStream streamOut;
	protected Class<T> clazz;

	public ObjectReporter() {}

	public static <T, A extends FieldColumnGetter> ObjectReporter<T> 
	factory(Class<T> clazz, String... additionalColumnNames)
	{
		ObjectReporter<T> out = new ObjectReporter<>();
		out.clazz = clazz;
		out.streamOut = new ByteArrayOutputStream();
		out.processor = new BeanWriterProcessor<T>(clazz);
		out.setHeaders(additionalColumnNames);

		out.settings = new CsvWriterSettings();
		out.settings.setRowWriterProcessor(out.processor);
		out.settings.setHeaders(out.processorHeaders);
		out.settings.setNullValue("null");
		out.writer = new CsvWriter(out.streamOut, out.settings);
		out.writer.addValues(out.outputFileHeaderList);
		out.writer.writeValuesToRow();

		return out;
	}

	public void appendToReport(Class<T> clazz, Object... extraColumns)
	{
		T t = null;
		try {
			t = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		writer.addValues(processor.write(t, processorHeaders, null));
		for (Object st : extraColumns) writer.addValue(st.toString());
		writer.writeValuesToRow();
	}	

	/** Add data to the reporter. */
	public void appendListToReport(List<T> list, Object... extraColumns)
	{ for (T t : list) appendToReport(t, extraColumns); }

	/** Add data to the reporter. */
	public void appendToReport(T item, Object... extraColumns)
	{
		writer.addValues(processor.write(item, processorHeaders, null));
		for (Object st : extraColumns) writer.addValue(st.toString());
		writer.writeValuesToRow();
	}

	/** Write the data to file and close the reporter. */
	public void write(String filename)
	{
		writer.close();
		streamOut.toString();
		FileWriter writer;
		try {
			writer = new FileWriter(filename);
			writer.write(streamOut.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Sets up the headers for the row processor and the output file. */
	public void setHeaders(String... additionalColumnNames)
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
	public static void main(String[] args) throws IOException {
		List<DataValueObject> objList = new ArrayList<DataValueObject>();
		List<DataValueObject> objList2 = new ArrayList<DataValueObject>();
		@SuppressWarnings("rawtypes")
		List<DV2> objList3 = new ArrayList<DV2>();
		
		ObjectReporter<DataValueObject> reporter =
				ObjectReporter.factory(DataValueObject.class, "cohort_id", "step");
		ObjectReporter<DataValueObject> reporter2 =
				ObjectReporter.factory(DataValueObject.class, "cohort_id", "step");

		@SuppressWarnings("rawtypes")
		ObjectReporter<DV2> reporter3 =
				ObjectReporter.factory(DV2.class, "cohort_id", "step");
		
		objList.add(new DataValueObject(1, 11, 111, 1111, "abc1"));
		objList.add(new DataValueObject(2, 21, 211, 2111, "abc2"));
		objList.add(new DataValueObject(3, 31, 311, 3111, "abc3"));
		objList.add(new DataValueObject(4, 41, 411, 4111, "abc4"));
		objList.add(new DataValueObject(5, 51, 511, 5111, "abc5"));
		objList.add(new DV2<String>(51, 511, 5111, 51111, "dv1"));
		objList.add(new DV2<Integer>(512, 512, 5112, 51112, "dv2"));

		objList2.add(new DV2<Double>(51, 511, 5111, 51111, "dv1"));
		objList2.add(new DV2<Boolean>(512, 512, 5112, 51112, "dv2"));

		objList3.add(new DV2<Double>(51, 511, 5111, 51111, "dv1"));
		objList3.add(new DV2<Boolean>(512, 512, 5112, 51112, "dv2"));

		reporter.appendListToReport(objList, "1", "1234");
		reporter.appendListToReport(objList, "asdf", "14");
		reporter2.appendListToReport(objList2, "1", "1234");
		reporter2.appendListToReport(objList2, "asdf", "14");
		reporter3.appendListToReport(objList3, "asdf", "15a");

		reporter.write("testOutput/ObjectReporterTest.csv");
		reporter2.write("testOutput/ObjectReporterTest2.csv");
		reporter3.write("testOutput/ObjectReporterTestWrongHeaders.csv");
	}

	
	private interface DV {}
	
	/** Simple test data class. */
	private static class DataValueObject implements DV 
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
	private static class DV2<T extends Object> extends DataValueObject
	{

		public DV2(int one, int two, int three, int four, String str) {
			super(one, two, three, four, str);
		}
	}
}
