package smu.sm.processing;

import java.util.ArrayList;
import java.util.List;

import smu.sm.entity.MyDocument;
import edu.smu.csv.CSVFileReader;
import edu.smu.json.JSONMultiFileReader;
import edu.smu.json.JSONMultiFileWriter;
import edu.smu.utils.FileUtils;
import edu.smu.utils.ObjectUtils;
import edu.smu.utils.StringUtils;

public class DocumentCollector {

	public static MyDocument[] collectFromDirectory(String directory){
		String[] filePaths = FileUtils.listName(directory);
		
		List<MyDocument> docs = new ArrayList<MyDocument>();
		
		for(String filePath: filePaths){
			String[] lines = FileUtils.readLines(filePath);
			if(ObjectUtils.isNull(lines) || lines.length == 0) continue;
			
			for(String line: lines)
				docs.add(createDocument(line));
		}
		
		return docs.toArray(new MyDocument[docs.size()]);
	}

	public static MyDocument[] collectFromJson(String directory) throws Exception{
		List<MyDocument> docs = new ArrayList<MyDocument>();
		
		JSONMultiFileReader jsonReader = new JSONMultiFileReader(directory);
		MyDocument doc = null;
		
		while((doc = jsonReader.next(MyDocument.class)) != null)
			docs.add(doc);
		
		return docs.toArray(new MyDocument[docs.size()]);
	}
	
	public static void collectFromCSVAndConvertToJson(String csvFilePath, String outputDir) throws Exception{
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
	
	private static MyDocument createDocument(String line){
		MyDocument doc = new MyDocument();
		
		String[] files = StringUtils.explode(line, "[\\t]");
		
		String documentId = files[0];
		String category = files[1];
		String description = "";
		
		for(int i = 2; i < files.length; i++)
			description += files[i] + ". ";
		
		doc.setDocumentId(documentId);
		doc.setCategory(category);
		doc.setDescription(description);
		
		return doc;
	}
	
	public static void main(String[] args){
		MyDocument[] docs = DocumentCollector.collectFromDirectory("data/raw2/microsoft");
		System.out.println("Total docs: " + docs.length);
		for(MyDocument doc: docs)
			System.out.println(doc.getCategory() + " | " + doc.getDocumentId() + " | " + doc.getDescription());
	}
}
