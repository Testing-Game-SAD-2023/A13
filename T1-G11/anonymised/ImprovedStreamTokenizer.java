import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

/**
 * A stream tokenizer that recognizes "words" and many of the 
 * basic java types.
 */
public class ImprovedStreamTokenizer extends StreamTokenizer {
    /**
     * Create a new instance and initialize to the default syntax.
     * 
     * @param r The input stream to parse.
     */
    public ImprovedStreamTokenizer (Reader r)
    {
        super(r);
        initializeSyntax();
        quoteChar(QUOTE_CHAR);
        slashSlashComments(true);
    }
    
    public ImprovedStreamTokenizer (
        Reader r,
        String whitespace,
        String quotes,
        boolean useSlashSlashComments
    )
    {
        super(r);
        initializeSyntax(
            whitespace,
            quotes,
            useSlashSlashComments
        );
    }
    
    public static final int QUOTE_CHAR = '"';
    
    /**
     * Get the next word from the input stream.  A word is a string
     * of characters delineated by one or more space, tab, or 
     * newline characters.  A word is also a string surrounded by 
     * double quotes.  An end of file will also demarkate a word.
     * 
     * @return A string which is the next word, as defined by the above
     * description, from the input stream; or null if there is no
     * next word.
     *
     * @exception java.io.IOException
     */
     
    public String nextWord ()
        throws IOException
    {
        String s = null;
        
        nextToken();
        switch (ttype)
        {
            case TT_EOF :
                s = null;
                break;
            
            case TT_WORD :
            case QUOTE_CHAR :
                s = sval;
                break;
            
            default :
                throw new IOException ("non-string");
        }
        
        return s;
    }
    /**
     * Read in the next word and attempt to parse it into an
     * Integer object.
     * 
     * @return The next Integer from the input stream or null if there is 
     * no next word.
     * @exception java.io.IOException
     */
    
    
    public Integer nextInteger ()
        throws IOException
    {
        Integer theValue = null;
            
        nextToken();
        switch (ttype)
        {
            case TT_EOF :
                theValue = null;
                break;
                
            case TT_WORD :
                theValue = new Integer(sval);
                break;
                
            default :
                throw new IOException ("non-number");
        }
            
        return theValue;
    }
    
    
    public int nextInt ()
        throws IOException
    {
        Integer i = nextInteger();
        if (null == i)
            throw new IOException ("unexpected end of input");
        else
            return i.intValue();
    }
    
    public Boolean nextBoolean ()
        throws IOException
    {
        Boolean b = null;
        
        nextToken();
        switch (ttype)
        {
            case TT_EOF :
                b = null;
                break;
            
            case TT_WORD :
                b = new Boolean(sval);
                break;
            
            case TT_NUMBER :
            {
                if (0 == (int) nval)
                    b = new Boolean (false);
                else
                    b = new Boolean (true);
                break;
            }
            
            default :
                throw new IOException ("non-boolean");
        }
        
        return b;
    }
    
    public boolean nextBool ()
        throws IOException 
    {
        Boolean b = nextBoolean();
        if (null == b)
            throw new IOException ("unexpected end of input");
        else
            return b.booleanValue();
    }
    
    
    public Byte nextByteObject ()
        throws IOException
    {
        Byte b = null;
        
        switch (nextToken())
        {
            case TT_EOF :
                b = null;
                break;
            
            case TT_WORD :
                b = new Byte(parseByte(sval));
                break;
            
            default :
                throw new IOException ("non-byte");
        }
        
        return b;
    }
    
    public byte nextByte ()
        throws IOException
    {
        Byte b = nextByteObject();
        return b.byteValue();
    }
    
    public static byte parseByte (String s)
    {
        s = s.toUpperCase();
        byte theValue = 0;
        
        theValue = (byte) Character.digit(s.charAt(0), 16);
        theValue = (byte) (theValue << 4);
        theValue = (byte) (theValue | Character.digit(s.charAt(1), 16));
        return theValue;
    }
    
    public static byte charToHex (char c)
    {
        int temp = (int) c;
        
        if (c >= '0' && c <= '9')
            temp = c - '0';
        else
        {
            temp = c - 'A';
            temp = temp + 10;
        }
        
        return (byte) temp;
    }
    

    /**
     * Reset the grammar used by the parser.  This defaults to
     * setting spaces, tabs, carriage returns and linefeeds to 
     * whitespace; word characters are any printable ASCII 
     * character; quote character is " or ', and double slashes
     * (a la C++) introduce a comment.
     */
    public void initializeSyntax ()
    {
        resetSyntax();
        
        //
        // whitespace characters are space, tab, newline and carriage return
        // 
        whitespaceChars (' ', ' ');
        whitespaceChars ('\t', '\t');
        whitespaceChars (10, 10);
        whitespaceChars (13, 13);
        
        //
        // "word" characters are basically all printable characters
        //
        wordChars (33, 126);
        
        //
        // Quoted string characters are the single and double quote chars
        //
        quoteChar ('"');
        quoteChar ('\'');
        
        //
        // Consider C++ style comments: "//" to be comments
        //
        slashSlashComments(true);
        
        //
        // Do *not* parse numbers by default.  All other characters, like
        // the "whacky" ASCII characters like backspace, etc., are returned 
        // as is --- just return the character and be done with it.
        //
    }
    
    public void whiteSpaceCharacter (int theChar)
    {
        whitespaceChars (theChar, theChar);
    }
    
    public void whiteSpaceCharacters (String s)
    {
        for (int i = 0; i < s.length(); i++)
        {
            whiteSpaceCharacter (s.charAt(i));
        }
    }
    
    public void quoteCharacters (String s)
    {
        for (int i = 0; i < s.length(); i++)
        {
            quoteChar(s.charAt(i));
        }
    }
    
    public void initializeSyntax (
        String theWhitespaceCharacters,
        String theQuoteCharacters,
        boolean useSlashSlashComments
    )
    {
        resetSyntax();
        wordChars (33, 126);
        whitespaceChars (10, 10);
        whitespaceChars (13, 13);
        whiteSpaceCharacters(theWhitespaceCharacters);
        quoteCharacters(theQuoteCharacters);
        slashSlashComments(useSlashSlashComments);
    }
}

    
