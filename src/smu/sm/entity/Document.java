package smu.sm.entity;

import java.io.Serializable;

public class Document implements Serializable {
	private static final long serialVersionUID = -4225516300931026766L;
	
	private String documentId;
	private String title;
	private String description;
	private String others;
	
	public Document(){}
	
	public Document(String documentId) {
		super();
		this.documentId = documentId;
	}

	public Document(String documentId, String title, String description) {
		super();
		this.documentId = documentId;
		this.title = title;
		this.description = description;
	}

	public Document(String documentId, String title, String description,
			String others) {
		super();
		this.documentId = documentId;
		this.title = title;
		this.description = description;
		this.others = others;
	}
	
	public String getDocumentId() { return documentId; }
	public void setDocumentId(String documentId) { this.documentId = documentId; }
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public String getOthers() { return others; }
	public void setOthers(String others) { this.others = others; }
}
