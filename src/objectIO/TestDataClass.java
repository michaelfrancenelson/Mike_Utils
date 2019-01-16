package objectIO;

import java.util.List;

import com.univocity.parsers.annotations.Parsed;


/**
 *  @author michaelfrancenelson
 *
 */

public class TestDataClass extends ObjectReader
{

	@Parsed @Initialized private int int1, int2;
	@Parsed @Initialized private double dbl1, dbl2;
	@Parsed @Initialized private String str1, str2;
	@Parsed @Initialized private String name;
	@Initialized private int initi1;

	@Parsed @Initialized private static String st1, st2;
	@Parsed @Initialized public static int si1, si2;
	@Parsed @Initialized private static double sd1, sd2;
	@Initialized private static double inits1;

	static { initializeStaticFieldsToNA(TestDataClass.class); };

	public static void readStatic(String filename, String sheetName)
	{
		initializeStaticFieldsToNA(TestDataClass.class);
		readObjectListColumns(filename, sheetName, TestDataClass.class);
		inits1 = si2 * si2 + si2;
	}

	public static List<TestDataClass> readInstanceList(String filename, String sheetName)
	{
		List<TestDataClass> list = readObjectListRows(filename, sheetName, TestDataClass.class);
		for (TestDataClass t : list) t.initialize();
		for (TestDataClass t : list) t.isInstanceInitialized();
		return list;
	}

	public void isInstanceInitialized() { areInstanceFieldsInitailized(this, true, true); }
	public static void isStaticInitialized() { areStaticFieldsInitailized(TestDataClass.class, true, true); }
	public void initialize()
	{
		/* Require the static members to be initialized first. */
		isStaticInitialized();
		initi1 = si1 * int1;
	}
	@Override public String toString() { return name; }
}
