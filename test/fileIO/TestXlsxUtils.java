package fileIO;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class TestXlsxUtils {

	Logger logger = LogManager.getLogger();
	
	@Test
	public void test() {
		
		
		String filename = "testData/parameterSpace100.xlsx";
//		File file = new File(filename);
//		FileInputStream stream = new FileInputStream(file);
//		XSSFWorkbook workbook = new XSSFWorkbook(stream);
		
//		XSSFSheet sheet = workbook.getSheetAt(0);
//            logger.info("=> " + sheet.getSheetName());
//        XSSFRow row;
		
		Map<String, List<String>> map = XLSXUtils.XslxToMap(filename, 0, "name");
		
		for (List<String> val : map.values())
		{
			for (String st : val)
			logger.info(st);
		}
		
		fail("Not yet implemented");
	}

}
