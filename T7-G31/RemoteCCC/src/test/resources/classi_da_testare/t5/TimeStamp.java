import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/***
 * TimeStamp class represents the Network Time Protocol (NTP) timestamp
 * as defined in RFC-1305 and SNTP (RFC-2030). It is represented as a
 * 64-bit unsigned fixed-point number in seconds relative to 0-hour on 1-January-1900.
 * The 32-bit low-order bits are the fractional seconds whose precision is
 * about 200 picoseconds. Assumes overflow date when date passes MAX_LONG
 * and reverts back to 0 is 2036 and not 1900. Test for most significant
 * bit: if MSB=0 then 2036 basis is used otherwise 1900 if MSB=1.
 *
 * Methods exist to convert NTP timestamps to and from the equivalent Java date
 * representation, which is the number of milliseconds since the standard base
 * time known as "the epoch", namely January 1, 1970, 00:00:00 GMT.
 */
 //Controlla se una classe di test vuota genera un errore di compilazione
public class TimeStamp implements java.io.Serializable, Comparable<TimeStamp> {
	

}
