package com.groom.manvsclass.util.upload;

import java.nio.file.Path;
import java.nio.file.Paths;

public class OpponentPathResolver {

    public static final String VOLUME_T0_BASE_PATH = "/VolumeT0/FolderTree/ClassUT/";
    public static final String UNMODIFIED_SRC = "unmodified_src";
    public static final String BASE_SRC_PATH = "src/main/java";
    public static final String BASE_TEST_PATH = "src/test/java";
    public static final String BASE_COVERAGE_PATH = "coverage";
    public static final String BASE_CODE_PATH = "project";
    private static final String PATH_FORMAT_SIX_PARAMS = "%s/%s/%s/%s/%s/%s";
    private static final String PATH_FORMAT_FIVE_PARAMS = "%s/%s/%s/%s/%s";
    private static final String PATH_FORMAT_THREE_PARAMS = "%s/%s/%s";
    private static final String PATH_FORMAT_TWO_PARAMS = "%s/%s";

    private OpponentPathResolver() {
        // Private constructor to hide implicit public one
    }

    public static class OpponentPaths {
        public final Path srcPath;
        public final Path testPath;
        public final Path coveragePath;
        public final Path sourcePath;

        public OpponentPaths(Path srcPath, Path testPath, Path coveragePath, Path sourcePath) {
            this.srcPath = srcPath;
            this.testPath = testPath;
            this.coveragePath = coveragePath;
            this.sourcePath = sourcePath;
        }
    }

    public static class RobotTestPaths {
        public final Path testPath;
        public final Path coveragePath;

        public RobotTestPaths(Path testPath, Path coveragePath) {
            this.testPath = testPath;
            this.coveragePath = coveragePath;
        }
    }

    public static Path getUnmodifiedSrcPath(String classUTName) {
        return Paths.get(String.format(PATH_FORMAT_THREE_PARAMS, VOLUME_T0_BASE_PATH, UNMODIFIED_SRC, classUTName));
    }

    public static Path getTempOperationFolder(String classUTName) {
        return Paths.get(String.format(PATH_FORMAT_THREE_PARAMS, VOLUME_T0_BASE_PATH, classUTName, "tmp"));
    }

    public static Path getTempZipFolder(String classUTName) {
        return Paths.get(String.format(PATH_FORMAT_THREE_PARAMS, VOLUME_T0_BASE_PATH, classUTName, "tmp_zip"));
    }

    public static OpponentPaths buildOpponentPaths(String classUTName, String robotType, String level) {
        Path srcPath = Paths.get(String.format(PATH_FORMAT_SIX_PARAMS,
                VOLUME_T0_BASE_PATH, classUTName, robotType, BASE_CODE_PATH, level, BASE_SRC_PATH));
        
        Path testPath = Paths.get(String.format(PATH_FORMAT_SIX_PARAMS,
                VOLUME_T0_BASE_PATH, classUTName, robotType, BASE_CODE_PATH, level, BASE_TEST_PATH));
        
        Path coveragePath = Paths.get(String.format(PATH_FORMAT_FIVE_PARAMS,
                VOLUME_T0_BASE_PATH, classUTName, robotType, BASE_COVERAGE_PATH, level));
        
        Path sourcePath = Paths.get(String.format(PATH_FORMAT_SIX_PARAMS,
                VOLUME_T0_BASE_PATH, classUTName, robotType, BASE_CODE_PATH, level, BASE_SRC_PATH));

        return new OpponentPaths(srcPath, testPath, coveragePath, sourcePath);
    }

    public static RobotTestPaths buildRobotTestPaths(Path levelFolder, String robotType) {
        Path testPath;
        Path coveragePath;

        if ("evosuite".equalsIgnoreCase(robotType)) {
            testPath = Paths.get(String.format(PATH_FORMAT_TWO_PARAMS, levelFolder, "TestSourceCode/evosuite-tests"));
            coveragePath = Paths.get(String.format(PATH_FORMAT_TWO_PARAMS, levelFolder, "TestReport"));
        } else {
            testPath = levelFolder;
            coveragePath = levelFolder;
        }

        return new RobotTestPaths(testPath, coveragePath);
    }
}
