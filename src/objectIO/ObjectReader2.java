//package objectIO;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class ObjectReader2 {
//
////	@Retention(RetentionPolicy.RUNTIME)
////	public static @interface FieldColumn{String column();};
////	@Retention(RetentionPolicy.RUNTIME)
////	public static @interface FieldColumnGetter{String column();};
//
//	static Logger logger = LoggerFactory.getLogger(ObjectReader2.class);
//
////	public static <T, A extends Annotation> List<Method> 
////	getAnnotatedMethods(Class<T> classT, Class<A> classA)
////	{
////		Method[] mm = classT.getMethods();
////		List<Method> ml = new ArrayList<>();
////
////		for (Method m : mm)
////		{
////			if (m.isAnnotationPresent(classA)) 
////			{
////				ml.add(m);
////			}
////		}
////		return ml;
////	}
//
////	public static <T, A extends FieldColumnGetter> 
////	List<Function<T, String>>
////	getReporterFunctions(Class<T> t, Class<A> a)
////	{
////		List<Method> ml = getAnnotatedMethods(t, a);
////		List<Function<T, String>> out = new ArrayList<>();
////		try {
////			for (Method m : ml)
////				out.add(getStringGetter(m, t));
////		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
////			e.printStackTrace();
////		}
////		return out;
////	}
//
////	public static <T, A extends FieldColumnGetter> String[] 
////			getColumnHeaders(Class<T> t, Class<A> a, String... additionalColumnHeaders)
////	{
////		List<Method> ml = getAnnotatedMethods(t, a);
////		String[] out = new String[ml.size() + additionalColumnHeaders.length];
////		int i = 0;
////		for (Method m : ml)
////		{
////			out[i] = m.getAnnotation(a).column();
////			i++;
////		}
////		
////		for (String s : additionalColumnHeaders)
////		{
////			out[i] = s; i++;
////		}
////		return out;
////	}
////
////
////	public static <T> Function<T, String> 
////	//	public static <T extends ObjectReporter2> Function<T, String> 
////	getStringGetter(Method m, Class<T> t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
////	{
////		Function<T, String> func;
////		String returnType = m.getReturnType().getSimpleName();
////		switch(returnType)
////		{
////		case("int"):
////			func = (tt) -> { 
////				try {return String.format("%d", m.invoke(tt));}
////				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
////				return null;
////			}; break;
////		case("double"):
////			func = (tt) -> { 
////				try {return String.format(dblFmt, m.invoke(tt));}
////				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
////				return null;
////			}; break;
////
////		default:
////			func = (tt) -> { 
////				try {return String.format("%s", m.invoke(tt));}
////				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
////				return null;
////			}; break;
////		}
////		return func;
////	}
//
//
//}