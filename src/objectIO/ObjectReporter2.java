package objectIO;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import objectIO.ObjectReader.FieldColumnGetter;

public class ObjectReporter2 <T>
{

	protected List<Function<T, String>> reporterFunctions;
	protected int nFields, nExtraColumns;
	String[] headers;
	ByteArrayOutputStream streamOut;
	protected Class<T> clazz;
	Writer writer;
	String sep, dblFmt = "%.4f";

	public String headerReport() { return String.join(sep, headers); }
	public String stringReport(T t, Object... additionalColumns)
	{
		String[] out = new String[nFields + nExtraColumns];
		int i = 0;
		for (Function<T, String> f : reporterFunctions)	{ out[i] = f.apply(t); i++; }
		if (nExtraColumns > 0)
			for (Object s : additionalColumns) { out[i] = s.toString(); i++; }
		return String.join(sep, out);
	}

	
	
	public static <T, A extends FieldColumnGetter> ObjectReporter2<T>
	factory(Class<T> clazz, String sep, String dblFmt, Class <A> appClazz, String... additionalColumnNames)
	{
		ObjectReporter2<T> out =  new ObjectReporter2<T>();
		out.clazz = clazz;
		out.sep = sep;
		out.dblFmt = dblFmt;
		out.reporterFunctions = ObjectReader.getReporterFunctions(clazz, appClazz, dblFmt);
		out.headers = ObjectReader.getColumnHeaders(clazz, appClazz, additionalColumnNames);
		out.nFields = out.reporterFunctions.size();
		out.nExtraColumns = additionalColumnNames.length;
		out.streamOut = new ByteArrayOutputStream();
		try	{ out.streamOut.write(out.headerReport().getBytes()); } 
		catch (IOException e) { e.printStackTrace(); }
		return out;
	}

	public static void main(String[] args) {
		ObjectReporter2<Empl> emplRep = factory(
				Empl.class, ",", "%.2f", FieldColumnGetter.class, "step", "sim");

		List<Empl> ll = Empl.randList(10);

		for (Empl e : ll) emplRep.appendToReport(e, 243, "ab");		
		System.out.println(emplRep.headerReport());
		System.out.println(emplRep.stringReport(ll.get(1), 34, "b"));

		String filename = "testOutput/ObjectReporter2.csv";
		emplRep.write(filename);
	}


	
	public void appendToReport(T item, Object... additionalColumns)
	{
		try {
			streamOut.write(System.lineSeparator().getBytes());
			streamOut.write(stringReport(item, additionalColumns).getBytes());
		}
		catch (IOException e) { e.printStackTrace(); }	
	}
	
	public void write(String filename)
	{
		try {
			OutputStream out = new FileOutputStream(filename);
			streamOut.writeTo(out);
			streamOut.close();
			out.close();
		} 
		catch (IOException e) { e.printStackTrace();}

	}


	static class Empl extends ObjectReader
	{
		public static String randomStr(int nChars, Random r)
		{
			String s = "";
			for (int i = 0; i < nChars; i++)
				s += (char)(r.nextInt('z' - 'a') + 'a');

			return s;
		}

		static Random r = new Random();
		public static List<Empl> randList(int n)
		{
			List<Empl> e = new ArrayList<>();
			for (int i = 0; i < n; i++) e.add(rand());
			return e;
		}
		public static Empl rand() {
			Empl e = new Empl();
			e.id = randomStr(4, r);
			e.name = randomStr(9, r);
			e.age = randomStr(3, r);
			e.country = randomStr(12, r);
			e.ddd = r.nextDouble();
			e.iii = r.nextInt(100);
			return e;
		}

		private double ddd;
		private int iii;
		@FieldColumn(column = "id") private String id;
		@FieldColumn(column = "name") private String name;
		@FieldColumn(column = "age") private String age;
		@FieldColumn(column = "country") private String country;

		public static Random getR() { return r; }

		@FieldColumnGetter(column = "ddd") public double getDdd() { return ddd; }
		@FieldColumnGetter(column = "iii") public int getIii() { return iii; }
		@FieldColumnGetter(column = "id") public String getId() { return id; }
		@FieldColumnGetter(column = "name") public String getName() { return name; }
		@FieldColumnGetter(column = "name") public String getAge() { return age; }
		@FieldColumnGetter(column = "name") public String getCountry() { return country; }
	}

}
