import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Represents VCard data.
 */
public class VCardBean implements Serializable {
	private String vCard;

	// Name fields
	private String firstName;
	private String lastName;
	private String middleName;
	private String formattedName;

	private String title;
	private String organization;
	private String email;
	private String phone;
	private String fax;


	public void setVCard(String vCard) {
		this.vCard = vCard;

		firstName = lastName = middleName = formattedName = null;
		title = null;
		organization = null;
		email = null;
		phone = fax = null;

		parseVCard(this);
	}

	/**
	 * @return A String representation of this vCard object.  If a
	 * vCard cannot be found or generated, this will return
	 * null.
	 */
	public String getVCard() {
		String ret;
		if (null == vCard) {
			// tries to generate a vcard for this object
			// (this may return null if a vcard cannot be generated)
			ret = generateVCard(this);
		} else {
			ret = vCard;
		}
		return ret;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFormattedName() {
		return formattedName;
	}

	public void setFormattedName(String formattedName) {
		this.formattedName = formattedName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String toString() {
		return getVCard();
	}

	/**
	 * Generates a vCard String from the various member variables. If no
	 * vCard can be generated, it returns null.
	 * <p>
	 * DESIGN DECISION: Since this is used primarily to generate vCards
	 * inputted via contribute and/or imported records, there are extra
	 * restrictions beyond the N and FN (name fields) requirements for
	 * a vCard -- to generate a vcard we additionally require an EMAIL,
	 * an ORG, and a TITLE.
	 */
	private static String generateVCard(VCardBean vcb) {
		// Makes sure required fields are there
		if (null == vcb.getFirstName() || null == vcb.getLastName() || null == vcb.getEmail()
				|| null == vcb.getOrganization()) {
			return null;
		}
		StringBuffer vCard = new StringBuffer("BEGIN:VCARD\n");

		// FN field
		{
			StringBuffer name;
			if (vcb.getFormattedName() != null) {
				name = new StringBuffer(vcb.getFormattedName());
			} else {
				name = new StringBuffer(vcb.getFirstName()).append(" ");
				String middleName = vcb.getMiddleName();
				if (middleName != null && middleName.length() != 0) {
					name.append(middleName).append(" ");
				}
				name.append(vcb.getLastName());
			}
			vCard.append("FN:").append(name.toString()).append("\n");
		}

		// N field
		vCard.append("N:").append(vcb.getLastName()).append(";");
		if (vcb.getMiddleName() != null) {
			vCard.append(vcb.getMiddleName());
		}
		vCard.append(";").append(vcb.getFirstName()).append(";").append("\n");

		// TITLE field
		if (null != vcb.getTitle()) {
			vCard.append("TITLE:").append(addBackslashes(vcb.getTitle())).append("\n");
		}

		// ORG field
		vCard.append("ORG:").append(addBackslashes(vcb.getOrganization())).append("\n");

		// EMAIL field
		vCard.append("EMAIL;TYPE=INTERNET:").append(vcb.getEmail()).append("\n");

		// TEL;TYPE=VOICE field
		if (vcb.getPhone() != null && vcb.getPhone().length() != 0) {
			vCard.append("TEL;TYPE=VOICE:").append(vcb.getPhone()).append("\n");
		}

		// TEL;TYPE=FAX field
		if (vcb.getFax() != null && vcb.getFax().length() != 0) {
			vCard.append("TEL;TYPE=FAX:").append(vcb.getFax()).append("\n");
		}

		vCard.append("END:VCARD");
		return vCard.toString();
//        this.vCard = vCard.toString();
	}

	/**
	 * Folds lines which are more than 75 characters long into multiple lines
	 * delimited by "\r\n\t" (a CRLF + a whitespace character).
	 *
	 * @param str An unfolded String.
	 * @return A folded String.
	 */
	private static String foldLines(String str) {
		StringBuffer ret = new StringBuffer();
		while (str.length() > 75) {
			ret.append(str.substring(0, 75) + "\r\n\t");
			str = str.substring(75, str.length());
		}
		ret.append(str);
		return ret.toString();
	}

	/**
	 * If lines are folded using a CRLF + a whitespace character, this unfolds
	 * the line in the String.
	 *
	 * @param str A String with folded lines.
	 * @return A String without folded lines.
	 */
	private static String unfoldLines(String str) {
		return str.replaceAll("\r\n\\s", "");
	}

	/**
	 * Takes what's in the vCard field and seperates it out to the seperate
	 * vCard variables.  If the vCard is valid for our purposes, we set the
	 * VCardBean properties accordingly.
	 */
	private static void parseVCard(VCardBean vcb) {
		String vCard = vcb.getVCard();
		if (null == vCard || !vCard.toUpperCase().startsWith("BEGIN:VCARD")) {
			return;
		}

		vCard = unfoldLines(vCard);
		StringTokenizer tokenizer = new StringTokenizer(vCard, "\n");

		// Basic vCard types
		String FN = null;
		String N = null;
		String ORG = null;
		String EMAIL = null;
		String TEL = null;
		String TITLE = null;
		String TEL_FAX = null;

		boolean beginFound = false;
		boolean endFound = false;
		while (tokenizer.hasMoreElements() && !endFound) {
			String element = (String) tokenizer.nextElement();

			int index = element.indexOf(":");

			// Malformed vCard, so reset the field data and return without setting
			// the VCardBean properties
			if (index == -1) {
				return;
			}

			if (index == element.length() - 1) {
				continue;
			}

			String type = element.substring(0, index);
			String data = element.substring(index + 1, element.length());
			String subtype = "";

			// Checks for subtypes
			if ((index = type.indexOf(";")) != -1) {
				if (index + 1 != type.length()) {
					subtype = type.substring(index + 1, type.length());
				}
				type = type.substring(0, index);
			}

			// Cleans up the strings
			type = type.trim();
			data = data.trim();

			// Looks for the beginning of the vCard, which should be the first element
			if (!beginFound) {
				if (type.equalsIgnoreCase("begin") && data.equalsIgnoreCase("vcard")) {
					beginFound = true;
				}
				continue;
			}

			// Looks for the end element
			if (type.equalsIgnoreCase("end") && data.equalsIgnoreCase("vcard")) {
				endFound = true;
				continue;
			}

			// FN type
			if (type.equalsIgnoreCase("fn")) {
				FN = data;
				continue;
			}

			// N type
			if (type.equalsIgnoreCase("n")) {
				N = data;
				continue;

			}

			// ORG type
			if (type.equalsIgnoreCase("org")) {
				ORG = data;
				continue;
			}

			// EMAIL type
			if (type.equalsIgnoreCase("email")) {
				EMAIL = data;
				continue;
			}

			// TEL type
			if (type.equalsIgnoreCase("tel")) {
				if ("type=fax".equalsIgnoreCase(subtype)) {
					TEL_FAX = data;
				} else {
					TEL = data;
				}
				continue;
			}

			// TITLE type
			if (type.equalsIgnoreCase("title")) {
				TITLE = data;
				continue;
			}
		} // end loop


		// Parses vCard into the seperate fields

		// this doesn't take into account \;'s which should be escaped, but
		// this is only a partial implementation of vcard so for now we don't worry about it
		if (N != null) {
			String[] name = N.split(";");
			if (name.length >= 3) {
				vcb.setFirstName(name[2].trim());
				vcb.setMiddleName(name[1].trim());
				vcb.setLastName(name[0].trim());
			}
		}

		if (null != FN) {
			vcb.setFormattedName(FN.trim());
		}

		if (null != ORG) {
			vcb.setOrganization(removeBackslashes(ORG.trim()));
		}
		if (null != EMAIL) {
			vcb.setEmail(EMAIL.trim());
		}
		if (null != TITLE) {
			vcb.setTitle(removeBackslashes(TITLE.trim()));
		}

		if (TEL != null) {
			vcb.setPhone(TEL.trim());
		}

		if (null != TEL_FAX) {
			vcb.setFax(TEL_FAX.trim());
		}
	}

	/**
	 * Adds escape characters (backslashes) to characters with special meaning in vCard attributres.
	 *
	 * @param str
	 * @return The string with backslashes added before commas and semicolons.
	 */
	private static String addBackslashes(String str) {
		return str.replaceAll(",", "\\,").replaceAll(";", "\\;");
	}

	/**
	 * Removes escape characters (backslashes) from characters with special meaning in vCard attributes.
	 * Used when translating a vCard String
	 *
	 * @param str
	 * @return The String with backslashes removed from before commas and semicolons.
	 */
	private static String removeBackslashes(String str) {
		return str.replaceAll("\\,", ",").replaceAll("\\;", ";");
	}

	public boolean isValidVCard() {
		return (null == vCard ? false : vCard.toLowerCase().startsWith("begin:vcard"));
	}
}