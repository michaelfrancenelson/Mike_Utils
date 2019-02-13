package objectIO;

import java.io.ByteArrayOutputStream;

import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

@Deprecated
public class CSVBeanReporter<T> extends ObjectReporter<T>
{
	private int simulationID;
	int timeStep;
	
	public static <T> CSVBeanReporter<T> factory(Class<T> clazz, int simulationID)
	{ return factory(clazz, simulationID, ""); }
	public static <T> CSVBeanReporter<T> factory(Class<T> clazz, int runID, String... additionalColumnNames)
	{ 
		CSVBeanReporter<T> out = new CSVBeanReporter<T>(); // factory(clazz, "runID", "timeStep");
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
		
		out.simulationID = runID;
		out.timeStep = Integer.MIN_VALUE;
		return out;
	}
	
	public void appendToReport(T item) //, Object... extraColumns)
	{
			writer.addValues(processor.write(item, processorHeaders, null));
			writer.addValue(simulationID, timeStep);
			writer.writeValuesToRow();
	}

	public void setSimulationID(int simID) { this.simulationID = simID; }
	
}
