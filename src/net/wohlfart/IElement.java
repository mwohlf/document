package net.wohlfart;

public interface IElement {
	
	IElement readHtml(String string);
	
	String writeHtml();
	
	IElement readMarkdown(String string);
	
	String writeMarkdown();

	void appendStructure(int inset, StringBuilder builder);

}
