package com.groom.manvsclass.util.upload;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for extracting Java package names from source code.
 * Provides methods to parse package declarations and imports from Java files.
 */
public class JavaMetadataExtractor {

    private static final Pattern PACKAGE_DECLARATION_PATTERN = 
        Pattern.compile("\\bpackage\\s+([\\w.]+?)\\s*;");

    private static final String EVOSUITE_CLASSNAME_TEMPLATE = 
        "org\\.evosuite\\.runtime\\.RuntimeSettings\\.className\\s*=\\s*\"([\\w.]+)\\.%s\"";

    private static final String IMPORT_STATEMENT_TEMPLATE = 
        "\\bimport\\s+(\\w+(?:\\.\\w+)*)\\.%s\\s*;";

    private JavaMetadataExtractor() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Extracts package name from a package declaration statement.
     * Example: "package com.example.test;" returns ["com", "example", "test"]
     *
     * @param code Java source code containing a package declaration
     * @return array of package name components, or null if not found
     */
    public static String[] extractPackageFromDeclaration(String code) {
        Matcher matcher = PACKAGE_DECLARATION_PATTERN.matcher(code);
        if (matcher.find()) {
            String packageName = matcher.group(1).trim();
            return packageName.split("\\.");
        }
        return null;
    }

    /**
     * Extracts source package name from test code based on robot type.
     * For Evosuite: looks for RuntimeSettings.className pattern
     * For others: looks for import statement
     *
     * @param code Java test source code
     * @param className name of the class under test
     * @param robotType type of test robot (e.g., "Evosuite", "Randoop")
     * @return array of package name components, or null if not found
     */
    public static String[] extractSourcePackageFromTestCode(String code, String className, String robotType) {
        if ("Evosuite".equalsIgnoreCase(robotType)) {
            return extractFromEvosuitePattern(code, className);
        } else {
            return extractFromImportStatement(code, className);
        }
    }

    /**
     * Extracts package name from Evosuite RuntimeSettings.className pattern.
     * Pattern: org.evosuite.runtime.RuntimeSettings.className = "package.name.ClassName"
     */
    private static String[] extractFromEvosuitePattern(String code, String className) {
        String regex = String.format(EVOSUITE_CLASSNAME_TEMPLATE, Pattern.quote(className));
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            String packageName = matcher.group(1);
            return packageName.split("\\.");
        }
        return null;
    }

    /**
     * Extracts package name from import statement.
     * Pattern: import package.name.ClassName;
     */
    private static String[] extractFromImportStatement(String code, String className) {
        String regex = String.format(IMPORT_STATEMENT_TEMPLATE, Pattern.quote(className));
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            String packageName = matcher.group(1);
            return packageName.split("\\.");
        }
        return null;
    }

    /**
     * Extracts class name
     */
    public static String getClassNameFromJavaSourceFile(byte[] classUTFileContent) {
        String fileContent = new String(classUTFileContent);

        StringBuilder buffer = new StringBuilder(fileContent);

        // Remove single-line comments
        Pattern pattern = Pattern.compile("^//.*", Pattern.MULTILINE);
        buffer = new StringBuilder(pattern.matcher(buffer).replaceAll(" "));

        // Remove multi-line comments
        pattern = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
        buffer = new StringBuilder(pattern.matcher(buffer).replaceAll(" "));

        // Find the first occurrence of the word "class" (surrounded by whitespace)
        pattern = Pattern.compile("\\s*class\\s*", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(buffer);

        if (!matcher.find()) {
            return null;
        }

        String foundSubString = matcher.group();
        int index = buffer.indexOf(foundSubString);
        StringBuilder className = new StringBuilder();

        for (int i = index + foundSubString.length(); i < buffer.length(); i++) {
            if (Character.isWhitespace(buffer.charAt(i))) {
                break;
            }
            className.append(buffer.charAt(i));
        }

        if (className.length() < 1) {
            return null;
        }

        return className.toString();
    }
}
