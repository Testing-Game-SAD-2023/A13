package com.groom.manvsclass.util.upload;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for extracting Java package names from source code.
 * Provides methods to parse package declarations and imports from Java files.
 */
public class JavaPackageExtractor {

    private static final Pattern PACKAGE_DECLARATION_PATTERN = 
        Pattern.compile("\\bpackage\\s+([\\w.]+?)\\s*;");

    private static final String EVOSUITE_CLASSNAME_TEMPLATE = 
        "org\\.evosuite\\.runtime\\.RuntimeSettings\\.className\\s*=\\s*\"([\\w.]+)\\.%s\"";

    private static final String IMPORT_STATEMENT_TEMPLATE = 
        "\\bimport\\s+(\\w+(?:\\.\\w+)*)\\.%s\\s*;";

    private JavaPackageExtractor() {
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
}
