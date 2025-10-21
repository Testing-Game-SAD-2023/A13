import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * This class improves on the StringTokenzier in the following ways:
 * 
 * Clients have access to the delimiters that separate the tokens
 */
public class ImprovedTokenizer 
{
	public static final int BUF_SIZE = 128;

	//
	// Nothing has been done at this point to find the next token.
	// * the buffer is null
	// * the token is null
	// * the delimiter is null
	// * there may be more tokens
	//	
	public static final int STATE_START = 0;
	
	// 
	// End of input has been reached and either there were no tokens in
	// the input stream or the last token has been retrieved.
	// * the buffer is undefined
	// * the token is undefined
	// * the delimiter is undefined
	// * there are no more tokens
	//	
	public static final int STATE_STOP = 1;
	
	//
	// Either the object is in the process of traversing delimiter characters
	// before a token, or a token was just delivered to the client via the 
	// next method.  One or more delimiter characters have been encountered.
	// * the buffer contains one or more delimiter characters
	// * the token is undefined
	// * the delimiter is undefined
	// * there may be more tokens
	//	
	public static final int STATE_BEFORE_TOKEN = 2;
	
	//
	// One or more non-delimiter tokens have been encountered and the instance
	// is in the process of collecting the characters for the next token.  
	// The object should only be in this state while processing a call from
	// the client, it should not be in this state after a call to hasNext or 
	// next.
	//
	// * the buffer contains one or more non-delimiter characters
	// * the token is undefined
	// * the delimiter contains the last delimiter encountered, or null if
	//   there was no previous delimiter
	// * there is at least one more token (the one being collected).
	//	
	public static final int STATE_MATCHING_TOKEN = 3;
	
	//
	// A token has been encountered and exactly one non-delimiter character
	// has been encountered after the current token.
	//
	// * the buffer contains exactly one delimiter character
	// * the token contains the current token
	// * the delimiter contains the last delimiter encountered, or null if
	//   there was no previous delimiter.
	// * there may be more tokens
	//	
	public static final int STATE_AFTER_TOKEN = 4;
	
	//
	// A token has been encountered but has not been delivered to the user.
	// The end of input has been reached after the current token.
	//
	// * the buffer is undefined
	// * the token contains the current token
	// * the delimiter contains the last delimiter encountered, or null if
	//   there was no previous delimiter
	// * there is one more token, but none after that.
	//	
	public static final int STATE_MATCH_STOP = 5;
	
	protected String myToken;
	protected String myDelimiters;
	protected String myPreviousDelimiter;
	protected Reader myInput;
	protected StringBuffer myBuffer;
	protected int myState;


	public ImprovedTokenizer (Reader r, String delimiters)
	{
		initialize(r, delimiters);
	}
	
	
	public void initialize (Reader r, String delimiters)
	{
		myState = STATE_START;
		myBuffer = null;
		myToken = null;
		myPreviousDelimiter = null;
		myInput = r;
		myDelimiters = delimiters;
	}
	
	
	public void initialize (InputStream istream, String delimiters)
	{
		InputStreamReader isr = new InputStreamReader(istream);
		initialize(isr, delimiters);
	}
	
	
	public ImprovedTokenizer (String buf, String delimiters)
	{
		StringReader r = new StringReader(buf);
		initialize(r, delimiters);
	}
	
	
	public boolean hasNext()
		throws IOException
	{
		boolean thereIsAnotherToken = false;
		
		//
		// if definitely know whether another token exists, then just return
		// the result and be done with it
		//
		switch (myState)
		{
			case STATE_START :
			case STATE_BEFORE_TOKEN :
				thereIsAnotherToken = advance();
				break;
			
			case STATE_AFTER_TOKEN :
			case STATE_MATCH_STOP :
				thereIsAnotherToken = true;
				break;
			
			case STATE_STOP :
				thereIsAnotherToken = false;
				break;
		}
		
		return thereIsAnotherToken;
	}
	
	
	public boolean keepParsing (int state)
	{
		return 
			STATE_MATCHING_TOKEN == state
			|| STATE_BEFORE_TOKEN == state
			|| STATE_START == state;
	}
	
	public boolean advance ()
		throws IOException 
	{
		//
		// when this method is called, the state must be either 
		// STATE_START or STATE_BEFORE_TOKEN.
		//
		int c;
		do {
			c = myInput.read();
			if (-1 == c)
			{
				myState = stop();
				continue;
			}
			
			char temp = (char) c;
			
			switch (myState)
			{
				case STATE_START :
					myState = start(temp);
					break;
				
				case STATE_BEFORE_TOKEN :
					myState = beforeToken(temp);
					break;
				
				case STATE_MATCHING_TOKEN :
					myState = matchingToken(temp);
					break;
				
				case STATE_AFTER_TOKEN :
					myState = afterToken(temp);
					break;

				case -1 :
					myState = stop();
					break;
									
				default :
					throw new RuntimeException ("Invalid state, " + myState);
			}
		} while (keepParsing(myState));
		
		return (STATE_AFTER_TOKEN == myState || STATE_MATCH_STOP == myState);
	}
	
	
	public int stop ()
	{
		int newState = -1;
		
		switch (myState)
		{
			case STATE_START :
			case STATE_BEFORE_TOKEN :
			case STATE_STOP :
				myToken = null;
				myPreviousDelimiter = null;
				myBuffer = null;
				newState = STATE_STOP;
				break;
			
			case STATE_MATCHING_TOKEN :
				myToken = myBuffer.toString();
				myBuffer = null;
				newState = STATE_MATCH_STOP;
				break;
			
			case STATE_AFTER_TOKEN :
				myBuffer = null;
				newState = STATE_MATCH_STOP;
				break;
			
			default :
				throw new RuntimeException ("Invalid state, " + myState);
		}
		
		return newState;
	}
	
	
	public int start (char c)
	{
		int newState = -1;
		myBuffer = new StringBuffer(BUF_SIZE);
		
		
		//
		// started matching new token
		//
		if (-1 == myDelimiters.indexOf(c))
		{
			myBuffer.append(c);
			myPreviousDelimiter = null;
			newState = STATE_MATCHING_TOKEN;
		}
		
		//
		// delimiter before the token
		//
		else
		{
			myBuffer.append(c);
			newState = STATE_BEFORE_TOKEN;
		}
		
		return newState;
	}
	
	public int beforeToken (char c)
	{
		int newState = -1;

		//
		// start of a token, copy over the previous delimiter
		//
		if (-1 == myDelimiters.indexOf(c))
		{
			myPreviousDelimiter = myBuffer.toString();
			myBuffer = new StringBuffer(BUF_SIZE);
			myBuffer.append(c);
			newState = STATE_MATCHING_TOKEN;
		}

		//
		// another delimiter --- we are still matching delmiters before the 
		// token
		//
		else
		{
			myBuffer.append(c);
			newState = STATE_BEFORE_TOKEN;
		}
		
		return newState;
	}
	
	
	public int matchingToken (char c)
	{
		int newState = -1;
		
		//
		// another character for the current token
		//
		if (-1 == myDelimiters.indexOf(c))
		{
			myBuffer.append(c);
			newState = STATE_MATCHING_TOKEN;
		}
		//
		// a delimiter --- end of the current token
		//
		else
		{
			myToken = myBuffer.toString();
			myBuffer = new StringBuffer(BUF_SIZE);
			myBuffer.append(c);
			newState = STATE_AFTER_TOKEN;
		}
		
		return newState;
	}
	
	
	public int afterToken (char c)
	{
		int newState = -1;
		
		//
		// start of token
		//
		if (-1 == myDelimiters.indexOf(c))
		{
			myPreviousDelimiter = myBuffer.toString();
			myBuffer = new StringBuffer(BUF_SIZE);
			myBuffer.append(c);
			newState = STATE_MATCHING_TOKEN;			
		}
		
		//
		// continuation of delimiter
		//
		else
		{
			myBuffer.append(c);
			newState = STATE_BEFORE_TOKEN;
		}
		
		return newState;
	}
	
	public String next()
		throws IOException
	{
		switch (myState)
		{
			//
			// There may be more input, and we have not found the next
			// token yet.  Try and find the next token.
			//
			case STATE_START :
			case STATE_BEFORE_TOKEN :
				advance();
				break;
			
			//
			// We have found the next token and there may be another one
			// after this one.  Return the current token and go to the state 
			// where we can try to find the next one.
			//
			case STATE_AFTER_TOKEN :
				myState = STATE_BEFORE_TOKEN;
				break;
			
			//
			// We have found the next token, but there is definitely no more 
			// input.  Return the current token and to to the stop state
			//
			case STATE_MATCH_STOP :
				myState = STATE_STOP;
				break;
				
			default :
				throw new IllegalStateException ("invalid state: " + myState);							
		}
		
		return myToken;
	}
	
	public String previousDelimiter()
	{
		return myPreviousDelimiter;
	}
}
