import java.util.*;

public class SubjectParser
{

 private String Subject;
 private String Title;
 private String RangeString;
 private int UpperRange;
 private int LowerRange;

 public SubjectParser(String s)
 {
  Subject = s;
  UpperRange = 1;
  LowerRange = 1;
 }

 public long getId()
 {
  try
  {
   StringTokenizer st = new StringTokenizer(Subject);
   return(Long.parseLong(st.nextToken()));
  }
  catch(Exception e) { return -1; }
 }

 public int getThisRange()
 {
  try
  {
   int[] parts = this.messageParts();
   if(parts != null)
   {
    LowerRange = parts[0];
   }
  }
  catch(Exception e) { }
  return LowerRange;
 }
  
 public int getUpperRange()
 {
  try
  {
   int[] parts = this.messageParts();
   if(parts != null)
   {
    UpperRange = parts[1];
   }
  }
  catch(Exception e){ }
  return UpperRange;
 }


 private int[] messageParts()
 {
  try
  {
   String mainrange = this.getRangeString(); 
   int low = -1;
   int high = -1;
   try
   {
    String tmpRange = mainrange.substring(mainrange.lastIndexOf("("),
	mainrange.length());
    String range = tmpRange.substring(0,tmpRange.indexOf(")"));
    StringTokenizer st = new StringTokenizer(range,"/");
    String sLow = st.nextToken();
    String sHigh = st.nextToken();
    low = Integer.parseInt(sLow.substring(1,sLow.length()));
    high = Integer.parseInt(sHigh);
   }
   catch(Exception inte)
   {
    try
    {
     String tmpRange2 = mainrange.substring(mainrange.lastIndexOf("["),
	mainrange.length());
     String range2 = tmpRange2.substring(0,tmpRange2.indexOf("]"));
     StringTokenizer st2 = new StringTokenizer(range2,"/");
     String sLow2 = st2.nextToken();
     String sHigh2 = st2.nextToken();
     low = Integer.parseInt(sLow2.substring(1,sLow2.length()));
     high = Integer.parseInt(sHigh2);
    }
    catch(Exception subE)
    {
     low = -1;
     high = -1;
     return null;
    }
   }
   int[] ia = new int[2];
   ia[0] = low;
   ia[1] = high;
   return ia;
  }
  catch(Exception e)
  {
   e.printStackTrace();
   return null;
  }
 }

 public String getRangeString()
 {
  try
  {
   if(RangeString == null)
   {
    this.getTitle();
   }
   return RangeString;
  }
  catch(Exception e)
  {
   System.err.println(e.getMessage());
   return null;
  }
 }

 public String getTitle()
 {
  try
  {
   char tokentype;
   String tmpSubject = Subject.substring(Subject.indexOf(" ")+1,
 	Subject.length());
   StringBuffer sb = new StringBuffer();
   int startindex=0;
   boolean FoundRange = false;
 MAINLOOP:
   for(int i=tmpSubject.length()-1; i>=0 ; i--)
   {
    char testchar = tmpSubject.charAt(i);
    if((testchar == ')' || testchar == ']') && FoundRange == false)
    {
     StringBuffer tmpbuf = new StringBuffer();
     tmpbuf.insert(0,testchar);
     tokentype = testchar;
     startindex = i;
     char endchar;
     if(testchar == ')') endchar = '(';
     else endchar = '[';
     char nextchar;
     while((nextchar = tmpSubject.charAt(--i)) != endchar)
     {
      tmpbuf.insert(0,nextchar); 
      if((Character.isDigit(nextchar) == false) && nextchar != '/')
      {
       sb.insert(0,tmpbuf.toString());
       continue MAINLOOP;
      }
     }
     int endindex=-1;
     if(tmpbuf.toString().indexOf("/") != -1)
     {
      tmpbuf.insert(0,endchar);
      FoundRange = true;
      RangeString = tmpbuf.toString();
     }
    }
    else
    {
     sb.insert(0,testchar);
    }
   }
   return sb.toString();
  }
  catch(Exception parseE)
  {
   parseE.printStackTrace();
   return null;
  }
 }

 public static void main(String[] args)
 {
  try
  {
   if(args.length < 1)
   {
    System.out.println("Usage: java SubjectParser <args>");
    System.exit(1);
   }
   StringBuffer sb = new StringBuffer("");
   for(int i=0 ; i<args.length ; i++)
   {
    sb.append(args[i] + " ");
   }
   SubjectParser sp = new SubjectParser(sb.toString().trim());
   System.out.println("The upper range is " + sp.getUpperRange());
   System.out.println("The lower range is " + sp.getThisRange());
   System.out.println("The message id is " + sp.getId());
   System.out.println("The subject title is " + sp.getTitle());
  }
  catch(Exception e)
  {
   System.err.println(e.getMessage());
  }
 }
}