package net.wohlfart;

import org.junit.Test;

public class DocumentTest {
	
	@Test
	public void smokeTest () {
        String documentText = ""
                +  "<p><u>example&nbsp;document</u></p>"
                +  "<p>ordered&nbsp;list:</p>"
                +  "<ol>"
                +  "<li>item1</li>"
                +  "<li>item2</li>"
                +  "</ol>"
                +  "<p></p>"
                +  "<p>unordered&nbsp;list:</p>"
                +  "<ul>"
                +  "<li>point&nbsp;1</li>"
                +  "<li>point&nbsp;2</li>"
                +  "</ul>"
                +  "<p></p>"
                +  "<ul>"
                +  "<li>point&nbsp;3</li>"
                +  "</ul>"
                +  "<p></p>"
                +  "<p>text&nbsp;formatting:<br />"
                +  "<b>b</b><b>old&nbsp;text</b>"
                +  "&nbsp;<i>itallic</i>"
                +  "&nbsp;<u>underline</u>&nbsp;"
                +  "<sup>sup</sup></p>"
                +  "<p><u><b></b></u><u><i></i><b></b><sup></sup></u></p>";

        Document doc = new Document().readHtml(documentText);
        
        System.out.println(doc);
		
	}

}
