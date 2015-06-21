package net.wohlfart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document implements IElement {

	Pattern paragraphPattern = Pattern.compile("^<p>(.*?)</p>(.*)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Pattern olPattern = Pattern.compile("^<ol>(.*?)</ol>(.*)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Pattern ulPattern = Pattern.compile("^<ul>(.*?)</ul>(.*)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	Pattern italicPattern = Pattern.compile("^<i>(.*?)</i>(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Pattern underlinePattern = Pattern.compile("^<u>(.*?)</u>(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Pattern boldPattern = Pattern.compile("^<b>(.*?)</b>(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Pattern supPattern = Pattern.compile("^<sup>(.*?)</sup>(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	Pattern liPattern = Pattern.compile("^<li>(.*?)</li>(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	Pattern plainTextPattern = Pattern.compile("^([^<]+)(.*)$");

	List<IElement> children = new ArrayList<>();


	@Override
	public Document readHtml(String string) {
		readHtmlParagraphs(string);
		return this;
	}

	private void readHtmlParagraphs(String string) {
		String tail = string;
		boolean anyMatch;
		do {
			anyMatch = false;
			Matcher paragraphMatcher = paragraphPattern.matcher(tail);
			if (paragraphMatcher.matches()) {
				children.add(new Paragraph().readHtml(paragraphMatcher.group(1)));
				tail = paragraphMatcher.group(2);
				anyMatch = true;
			}
			Matcher olMatcher = olPattern.matcher(tail);
			if (olMatcher.matches()) {
				children.add(new Ol().readHtml(olMatcher.group(1)));
				tail = olMatcher.group(2);
				anyMatch = true;
			}

			Matcher ulMatcher = ulPattern.matcher(tail);
			if (ulMatcher.matches()) {
				children.add(new Ul().readHtml(ulMatcher.group(1)));
				tail = ulMatcher.group(2);
				anyMatch = true;
			}
		} while (anyMatch);
	}


	@Override
	public String writeHtml() {
		StringBuilder builder = new StringBuilder();
		children.forEach(new Consumer<IElement>() {
			@Override
			public void accept(IElement t) {
				builder.append(t.writeHtml());
			}
		});
		return builder.toString();
	}

	@Override
	public Document readMarkdown(String string) {
		return this;
	}

	@Override
	public String writeMarkdown() {
		StringBuilder builder = new StringBuilder();
		children.forEach(new Consumer<IElement>() {
			@Override
			public void accept(IElement t) {
				builder.append(t.writeMarkdown());
			}
		});
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Document:");
		appendStructure(0, builder);
		return builder.toString();
	}

	@Override
	public void appendStructure(int i, StringBuilder builder) {
		for (IElement elem : children) {
			elem.appendStructure(i, builder);
		}
	}

	abstract class InlineContainer implements IElement {
		List<IElement> children = new ArrayList<>();

		@Override
		public IElement readHtml(String string) {
			String tail = string;
			boolean anyMatch;
			do {
				anyMatch = false;
				Matcher italicMatcher = italicPattern.matcher(tail);
				if (italicMatcher.matches()) {
					children.add(new Italic().readHtml(italicMatcher.group(1)));
					tail = italicMatcher.group(2);
					anyMatch = true;
				}
				Matcher underlineMatcher = underlinePattern.matcher(tail);
				if (underlineMatcher.matches()) {
					children.add(new Underline().readHtml(underlineMatcher.group(1)));
					tail = underlineMatcher.group(2);
					anyMatch = true;
				}
				Matcher supMatcher = supPattern.matcher(tail);
				if (supMatcher.matches()) {
					children.add(new Sup().readHtml(supMatcher.group(1)));
					tail = supMatcher.group(2);
					anyMatch = true;
				}
				Matcher boldMatcher = boldPattern.matcher(tail);
				if (boldMatcher.matches()) {
					children.add(new Bold().readHtml(boldMatcher.group(1)));
					tail = boldMatcher.group(2);
					anyMatch = true;
				}

				Matcher plainTextMatcher = plainTextPattern.matcher(tail);
				if (plainTextMatcher.matches()) {
					children.add(new PlainText().readHtml(plainTextMatcher.group(1)));
					tail = plainTextMatcher.group(2);
					anyMatch = true;
				}
			} while (anyMatch);
			return this;
		}

		@Override
		public String writeHtml() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IElement readMarkdown(String string) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String writeMarkdown() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void appendStructure(int inset, StringBuilder builder) {
			appendInset(builder, inset);
			builder.append(this.getClass().getSimpleName());
			for (IElement elem : children) {
				elem.appendStructure(inset + 2, builder);
			}
		}
	}

	abstract class ListContainer implements IElement {
		List<IElement> children = new ArrayList<>();

		@Override
		public IElement readHtml(String string) {
			String tail = string;
			boolean anyMatch;
			do {
				anyMatch = false;
				Matcher liMatcher = liPattern.matcher(tail);
				if (liMatcher.matches()) {
					children.add(new Li().readHtml(liMatcher.group(1)));
					tail = liMatcher.group(2);
					anyMatch = true;
				}

			} while (anyMatch);
			return this;
		}

		@Override
		public String writeHtml() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IElement readMarkdown(String string) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String writeMarkdown() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void appendStructure(int inset, StringBuilder builder) {
			appendInset(builder, inset);
			builder.append(this.getClass().getSimpleName());
			for (IElement elem : children) {
				elem.appendStructure(0 + 2, builder);
			}
		}
	}



	class Paragraph extends InlineContainer {}
	
	class Ol extends ListContainer {}
	
	class Ul extends ListContainer {}

	class Italic extends InlineContainer {}

	class Underline extends InlineContainer {}
	
	class Bold extends InlineContainer {}

	class Sup extends InlineContainer {}

	class Li extends InlineContainer {}


	class PlainText implements IElement {

		String content;

		@Override
		public IElement readHtml(String string) {
			this.content = string;
			return this;
		}

		@Override
		public String writeHtml() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IElement readMarkdown(String string) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String writeMarkdown() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void appendStructure(int inset, StringBuilder builder) {
			appendInset(builder, inset);
			builder.append("[");
			builder.append(content);
			builder.append("]");
		}

	}

	private static void appendInset(StringBuilder builder, int inset) {
		char[] blanks = new char[inset];
		Arrays.fill(blanks, ' ');
		builder.append("\n");
		builder.append(blanks);
	}

}
