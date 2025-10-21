/**
 * OutputFormat represents the format configuration used to
 * format the XML output
 */
public class OutputFormat implements Cloneable {
	/**
	 * standard value to indent by, if we are indenting
	 */
	protected static final String STANDARD_INDENT = "  ";

	/**
	 * Whether or not to suppress the XML declaration - default is
	 * false
	 */
	private boolean suppressDeclaration = false;

	/**
	 * Whether or not to print new line after the XML declaration - default is
	 * true
	 */
	private boolean newLineAfterDeclaration = true;

	/**
	 * The encoding format
	 */
	private String encoding = "UTF-8";

	/**
	 * Whether or not to output the encoding in the XML declaration - default is
	 * false
	 */
	private boolean omitEncoding = false;

	/**
	 * The default indent is no spaces (as original document)
	 */
	private String indent = null;

	/**
	 * Whether or not to expand empty elements to
	 * &lt;tagName&gt;&lt;/tagName&gt; - default is false
	 */
	private boolean expandEmptyElements = false;

	/**
	 * The default new line flag, set to do new lines only as in original
	 * document
	 */
	private boolean newlines = false;

	/**
	 * New line separator
	 */
	private String lineSeparator = "\n";

	/**
	 * should we preserve whitespace or not in text nodes?
	 */
	private boolean trimText = false;

	/**
	 * pad string-element boundaries with whitespace
	 */
	private boolean padText = false;

	/**
	 * Whether or not to use XHTML standard.
	 */
	private boolean doXHTML = false;

	/**
	 * Controls when to output a line.separtor every so many tags in case of no
	 * lines and total text trimming.
	 */
	private int newLineAfterNTags = 0; // zero means don't bother.

	/**
	 * Quote character to use when writing attributes.
	 */
	private char attributeQuoteChar = '\"';

	/**
	 * Creates an OutputFormat with no additional whitespace
	 * (indent or new lines) added. The whitespace from the element text content
	 * is fully preserved.
	 */
	public OutputFormat() {
	}

	/**
	 * Creates an OutputFormat with the given indent added but no
	 * new lines added. All whitespace from element text will be included.
	 *
	 * @param indent is the indent string to be used for indentation (usually a
	 *               number of spaces).
	 */
	public OutputFormat(String indent) {
		this.indent = indent;
	}

	/**
	 * Creates an OutputFormat with the given indent added with
	 * optional newlines between the Elements. All whitespace from element text
	 * will be included.
	 *
	 * @param indent   is the indent string to be used for indentation (usually a
	 *                 number of spaces).
	 * @param newlines whether new lines are added to layout the
	 */
	public OutputFormat(String indent, boolean newlines) {
		this.indent = indent;
		this.newlines = newlines;
	}

	/**
	 * Creates an OutputFormat with the given indent added with
	 * optional newlines between the Elements and the given encoding format.
	 *
	 * @param indent   is the indent string to be used for indentation (usually a
	 *                 number of spaces).
	 * @param newlines whether new lines are added to layout the
	 * @param encoding is the text encoding to use for writing the XML
	 */
	public OutputFormat(String indent, boolean newlines, String encoding) {
		this.indent = indent;
		this.newlines = newlines;
		this.encoding = encoding;
	}

	public String getLineSeparator() {
		return lineSeparator;
	}

	/**
	 * 
	 * This will set the new-line separator. The default is \n.
	 * Note that if the "newlines" property is false, this value is irrelevant.
	 * To make it output the system default line ending string, call
	 * setLineSeparator(System.getProperty("line.separator"))
	 * 
	 *
	 * @param separator String line separator to use.
	 * @see #setNewlines(boolean)
	 */
	public void setLineSeparator(String separator) {
		lineSeparator = separator;
	}

	public boolean isNewlines() {
		return newlines;
	}

	/**
	 * @param newlines true indicates new lines should be printed,
	 *                 else new lines are ignored (compacted).
	 * @see #setLineSeparator(String)
	 */
	public void setNewlines(boolean newlines) {
		this.newlines = newlines;
	}

	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding encoding format
	 */
	public void setEncoding(String encoding) {
		if (encoding != null) {
			this.encoding = encoding;
		}
	}

	public boolean isOmitEncoding() {
		return omitEncoding;
	}

	/**
	 * 
	 * This will set whether the XML declaration (&lt;?xml version="1.0"
	 * encoding="UTF-8"?&gt;)
	 * includes the encoding of the document. It is common to suppress this in
	 * protocols such as WML and SOAP.
	 * 
	 *
	 * @param omitEncoding boolean indicating whether or not the XML
	 *                     declaration should indicate the document encoding.
	 */
	public void setOmitEncoding(boolean omitEncoding) {
		this.omitEncoding = omitEncoding;
	}

	/**
	 * 
	 * This will set whether the XML declaration (&lt;?xml version="1.0"
	 * encoding="UTF-8"?&gt;)
	 * is included or not. It is common to suppress this in protocols such as
	 * WML and SOAP.
	 * 
	 *
	 * @param suppressDeclaration boolean indicating whether or not the XML
	 *                            declaration should be suppressed.
	 */
	public void setSuppressDeclaration(boolean suppressDeclaration) {
		this.suppressDeclaration = suppressDeclaration;
	}

	/**
	 * @return true if the output of the XML declaration (&lt;?xml
	 *         version="1.0"?&gt;)
	 *         should be suppressed else false.
	 */
	public boolean isSuppressDeclaration() {
		return suppressDeclaration;
	}

	/**
	 * 
	 * This will set whether a new line is printed after the XML declaration
	 * (assuming it is not supressed.)
	 * 
	 *
	 * @param newLineAfterDeclaration boolean indicating whether or not to print new
	 *                                line following the XML declaration. The default is true.
	 */
	public void setNewLineAfterDeclaration(boolean newLineAfterDeclaration) {
		this.newLineAfterDeclaration = newLineAfterDeclaration;
	}

	/**
	 * @return true if a new line should be printed following XML declaration
	 */
	public boolean isNewLineAfterDeclaration() {
		return newLineAfterDeclaration;
	}

	public boolean isExpandEmptyElements() {
		return expandEmptyElements;
	}

	/**
	 * 
	 * This will set whether empty elements are expanded from
	 * &lt;tagName&gt; to
	 * &lt;tagName&gt;&lt;/tagName&gt;.
	 * 
	 *
	 * @param expandEmptyElements boolean indicating whether or not empty
	 *                            elements should be expanded.
	 */
	public void setExpandEmptyElements(boolean expandEmptyElements) {
		this.expandEmptyElements = expandEmptyElements;
	}

	public boolean isTrimText() {
		return trimText;
	}

	/**
	 * Default: false
	 * 
	 *
	 * @param trimText boolean true=>trim the whitespace, false=>use
	 *                 text verbatim
	 */
	public void setTrimText(boolean trimText) {
		this.trimText = trimText;
	}

	public boolean isPadText() {
		return padText;
	}

	/**
	 * 
	 * Ensure that text immediately preceded by or followed by an element will
	 * be "padded" with a single space. This is used to allow make
	 * browser-friendly HTML, avoiding trimText's transformation of, e.g.,
	 * The quick &lt;b&gt;brown&lt;/b&gt; fox into The
	 * quick&lt;b&gt;brown&lt;/b&gt;fox
	 * (the latter will run the three separate words together into a single
	 * word).
	 * 
	 * 
	 * 
	 * The padding string will only be added if the text itself starts or ends
	 * with some whitespace characters.
	 * 
	 * 
	 * 
	 * Default: false
	 * 
	 *
	 * @param padText boolean if true, pad string-element boundaries
	 */
	public void setPadText(boolean padText) {
		this.padText = padText;
	}

	public String getIndent() {
		return indent;
	}

	/**
	 * 
	 * This will set the indent String to use; this is usually a
	 * String of empty spaces. If you pass null, or the empty
	 * string (""), then no indentation will happen.
	 * 
	 * Default: none (null)
	 *
	 * @param indent String to use for indentation.
	 */
	public void setIndent(String indent) {
		// nullify empty string to void unnecessary indentation code
		if ((indent != null) && (indent.length() <= 0)) {
			indent = null;
		}

		this.indent = indent;
	}

	/**
	 * Set the indent on or off. If setting on, will use the value of
	 * STANDARD_INDENT, which is usually two spaces.
	 *
	 * @param doIndent if true, set indenting on; if false, set indenting off
	 */
	public void setIndent(boolean doIndent) {
		if (doIndent) {
			this.indent = STANDARD_INDENT;
		} else {
			this.indent = null;
		}
	}

	/**
	 * 
	 * This will set the indent String's size; an indentSize of
	 * 4 would result in the indention being equivalent to the
	 * String "&nbsp;&nbsp;&nbsp;&nbsp;" (four space characters).
	 * 
	 *
	 * @param indentSize int number of spaces in indentation.
	 */
	public void setIndentSize(int indentSize) {
		StringBuffer indentBuffer = new StringBuffer();

		for (int i = 0; i < indentSize; i++) {
			indentBuffer.append(" ");
		}

		this.indent = indentBuffer.toString();
	}

	/**
	 * 
	 * Whether or not to use the XHTML standard: like HTML but passes an XML
	 * parser with real, closed tags.
	 * 
	 * Default is false
	 * 
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isXHTML() {
		return doXHTML;
	}

	/**
	 * 
	 * This will set whether or not to use the XHTML standard: like HTML but
	 * passes an XML parser with real, closed tags. 
	 * 
	 * 
	 * 
	 * Default: false
	 * 
	 *
	 * @param xhtml boolean true=>conform to XHTML, false=>conform
	 *              to HTML, can have unclosed tags, etc.
	 */
	public void setXHTML(boolean xhtml) {
		doXHTML = xhtml;
	}

	public int getNewLineAfterNTags() {
		return newLineAfterNTags;
	}

	/**
	 * Controls output of a line.separator every tagCount tags when isNewlines
	 * is false. If tagCount equals zero, it means don't do anything special. If
	 * greater than zero, then a line.separator will be output after tagCount
	 * tags have been output. Used when you would like to squeeze the html as
	 * much as possible, but some browsers don't like really long lines. A tag
	 * count of 10 would produce a line.separator in the output after 10 close
	 * tags (including single tags).
	 *
	 */
	public void setNewLineAfterNTags(int tagCount) {
		newLineAfterNTags = tagCount;
	}

	public char getAttributeQuoteCharacter() {
		return attributeQuoteChar;
	}

	/**
	 * Sets the character used to quote attribute values. The specified
	 * character must be a valid XML attribute quote character, otherwise an
	 * IllegalArgumentException will be thrown.
	 *
	 * @param quoteChar The character to use when quoting attribute values.
	 * @throws IllegalArgumentException If the specified character is not a valid XML attribute quote
	 *                                  character.
	 */
	public void setAttributeQuoteCharacter(char quoteChar) {
		if ((quoteChar == '\'') || (quoteChar == '"')) {
			attributeQuoteChar = quoteChar;
		} else {
			throw new IllegalArgumentException("Invalid attribute quote "
					+ "character (" + quoteChar + ")");
		}
	}

	/**
	 * Parses command line arguments of the form -omitEncoding
	 * -indentSize 3 -newlines -trimText
	 *
	 * @param args is the array of command line arguments
	 * @param i    is the index in args to start parsing options
	 * @return the index of first parameter that we didn't understand
	 */
	public int parseOptions(String[] args, int i) {
		for (int size = args.length; i < size; i++) {
			if (args[i].equals("-suppressDeclaration")) {
				setSuppressDeclaration(true);
			} else if (args[i].equals("-omitEncoding")) {
				setOmitEncoding(true);
			} else if (args[i].equals("-indent")) {
				setIndent(args[++i]);
			} else if (args[i].equals("-indentSize")) {
				setIndentSize(Integer.parseInt(args[++i]));
			} else if (args[i].startsWith("-expandEmpty")) {
				setExpandEmptyElements(true);
			} else if (args[i].equals("-encoding")) {
				setEncoding(args[++i]);
			} else if (args[i].equals("-newlines")) {
				setNewlines(true);
			} else if (args[i].equals("-lineSeparator")) {
				setLineSeparator(args[++i]);
			} else if (args[i].equals("-trimText")) {
				setTrimText(true);
			} else if (args[i].equals("-padText")) {
				setPadText(true);
			} else if (args[i].startsWith("-xhtml")) {
				setXHTML(true);
			} else {
				return i;
			}
		}

		return i;
	}

	/**
	 * A static helper method to create the default pretty printing format. This
	 * format consists of an indent of 2 spaces, newlines after each element and
	 * all other whitespace trimmed, and XMTML is false.
	 *
	 */
	public static OutputFormat createPrettyPrint() {
		OutputFormat format = new OutputFormat();
		format.setIndentSize(2);
		format.setNewlines(true);
		format.setTrimText(true);
		format.setPadText(true);

		return format;
	}

	/**
	 * A static helper method to create the default compact format. This format
	 * does not have any indentation or newlines after an alement and all other
	 * whitespace trimmed
	 *
	 */
	public static OutputFormat createCompactFormat() {
		OutputFormat format = new OutputFormat();
		format.setIndent(false);
		format.setNewlines(false);
		format.setTrimText(true);

		return format;
	}
}