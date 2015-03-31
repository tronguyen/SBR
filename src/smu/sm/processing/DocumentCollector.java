package smu.sm.processing;

import java.util.ArrayList;
import java.util.List;

import smu.sm.entity.MyDocument;
import edu.smu.csv.CSVFileReader;
import edu.smu.json.JSONMultiFileReader;
import edu.smu.json.JSONMultiFileWriter;

public class DocumentCollector {

	public static MyDocument[] collectFromXml(String directory){
		return null;
	}

	public static MyDocument[] collectFromJson(String directory) throws Exception{
		List<MyDocument> docs = new ArrayList<MyDocument>();
		
		JSONMultiFileReader jsonReader = new JSONMultiFileReader(directory);
		MyDocument doc = null;
		
		while((doc = jsonReader.next(MyDocument.class)) != null)
			docs.add(doc);
		
		return docs.toArray(new MyDocument[docs.size()]);
	}
	
	public static void collectAndConvertToJson(String csvFilePath, String outputDir) throws Exception{
		CSVFileReader csvReader = new CSVFileReader(csvFilePath);
		
		JSONMultiFileWriter jsonWriter = new JSONMultiFileWriter(outputDir);
		jsonWriter.setCompress(true);
		jsonWriter.setMaxDocumentPerFile(100);
		
		int count = 0;
		while(csvReader.hasNext()){
			String[] fields = csvReader.nextRecordAsArray();
			MyDocument doc = createDocument(fields);
			jsonWriter.write(doc);
			count++;
			if(count % 100 == 0) Thread.sleep(1000);
		}
		jsonWriter.close();
		System.out.println("Total documents: " + count);
	}
	
	private static MyDocument createDocument(String[] fields){
		MyDocument doc = new MyDocument();
		
		
		return doc;
	}
	
}
