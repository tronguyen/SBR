package smu.sm.processing;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException; 
import org.apache.lucene.util.Version;

public class MyWriter {
	
	
	
//	IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
	
	new FSDirectory().open("/tmp/testindex");
	Directory directory = null;
	FSDirectory direc = null;
	IndexWriter iWr=null;
	public MyWriter(String dir){
		Analyzer analyzer=new StandardAnalyzer();
		try {
			direc = new FSDirectory();
			iWr = new IndexWriter(direc, iwc);
			
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addDoc(String title, String text){
		Document doc = mygenDoc(title, text);
		try {
			iWr.addDocument(doc);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Document mygenDoc(String title, String content) {
		Document doc = new Document();
		doc.add(new Field("title", title, Store.YES, Index.ANALYZED));
		doc.add(new Field("content", content, Store.YES, Index.ANALYZED));
		return doc;
	}
	
	private Document genDoc(String cat, String cls) {
		Document doc = new Document();
		doc.add(new Field("category", cat, Store.YES, Index.ANALYZED));
		doc.add(new Field("class", cls, Store.YES, Index.NOT_ANALYZED));
		return doc;
	}
	
	public void close(){
		try {
			iWr.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
