#!/bin/bash

# Verify if the correct parameters are provided
if [ "$#" -lt 4 ] || [ "$#" -gt 5 ]; then
    echo "Usage: $0 <tool> <class_name> [<package_name>] <class_path> <host_output_dir>"
    echo "<tool>: randoop | evosuite"
    exit 1
fi

TOOL=$1
CLASS_NAME=$2
CONTAINER_ID=t0-generator

if [ "$#" -eq 5 ]; then
    PACKAGE_NAME=$3
    CLASS_PATH=$4
    HOST_OUTPUT_DIR=$5
else
    PACKAGE_NAME=""
    CLASS_PATH=$3
    HOST_OUTPUT_DIR=$4
fi

NUM_LEVELS=3
# Define directories and scripts based on the chosen robot
if [ "$TOOL" == "randoop" ]; then
    ROOT_DIR="/generator/RandoopGenerator"
    CLASS_DEST_DIR="$ROOT_DIR/FolderTree/${CLASS_NAME}/${CLASS_NAME}SourceCode"
    CLASS_DEST_PATH="$CLASS_DEST_DIR/${CLASS_NAME}.java"
    RESULTS_DIR="$ROOT_DIR/FolderTree/${CLASS_NAME}/RobotTest/RandoopTest"
    SCRIPT="$ROOT_DIR/generate.sh"
elif [ "$TOOL" == "evosuite" ]; then
    ROOT_DIR="/generator/EvosuiteGenerator"
    CLASS_DEST_DIR="$ROOT_DIR/evosuite"
    CLASS_DEST_PATH="$CLASS_DEST_DIR/${CLASS_NAME}.java"
    RESULTS_DIR="/generator/FolderTreeEvo/${CLASS_NAME}/RobotTest/EvoSuiteTest"
    SCRIPT="$ROOT_DIR/generate.sh"
else
    echo "Error: Invalid tool specified. Use 'randoop' or 'evosuite'." >&2
    exit 1
fi

# Ensure the class destination directory exists in the container
if ! docker exec "$CONTAINER_ID" mkdir -p "$CLASS_DEST_DIR"; then
    echo "Error: Failed to create directory ${CLASS_DEST_DIR} in container ${CONTAINER_ID}" >&2
    exit 1
fi

# Copy the class to the container
if ! docker cp "$CLASS_PATH" "${CONTAINER_ID}:${CLASS_DEST_PATH}"; then
    echo "Error: Failed to copy class ${CLASS_NAME} to container ${CONTAINER_ID}" >&2
    exit 1
fi

echo "Class ${CLASS_NAME} successfully copied to ${CLASS_DEST_PATH} in container ${CONTAINER_ID}"

# Ensure the script has execution permissions
if ! docker exec "$CONTAINER_ID" chmod +x "$SCRIPT"; then
    echo "Error: Failed to set execute permissions for ${SCRIPT} in container ${CONTAINER_ID}" >&2
    exit 1
fi

# Execute the script inside the container
if [ "$TOOL" == "randoop" ]; then
    if ! docker exec "$CONTAINER_ID" bash -c "$SCRIPT"; then
        echo "Error: Failed to execute ${SCRIPT} in container ${CONTAINER_ID}" >&2
        exit 1
    fi
else
    if ! docker exec "$CONTAINER_ID" bash -c "$SCRIPT '$CLASS_NAME' '$PACKAGE_NAME' '$CLASS_DEST_DIR' '$NUM_LEVELS'"; then
        echo "Error: Failed to execute ${SCRIPT} in container ${CONTAINER_ID}" >&2
        exit 1
    fi
fi


echo "Generation script executed successfully for class ${CLASS_NAME} using ${TOOL}"

# Copy the results back to the host
if ! docker cp "${CONTAINER_ID}:${RESULTS_DIR}" "$HOST_OUTPUT_DIR"; then
    echo "Error: Failed to copy test results from container ${CONTAINER_ID} to host ${HOST_OUTPUT_DIR}" >&2
    exit 1
fi

echo "Test results successfully copied to host at ${HOST_OUTPUT_DIR}"

# Remove the class file from the container
if ! docker exec "$CONTAINER_ID" sh -c "rm -f $CLASS_DEST_PATH"; then
    echo "Error: Cleanup failed" >&2
    exit 1
fi
if ! docker exec "$CONTAINER_ID" sh -c "rm -rf /generator/FolderTreeEvo/*"; then
    echo "Error: Cleanup failed" >&2
    exit 1
fi
if ! docker exec "$CONTAINER_ID" sh -c "rm -rf /generator/RandoopGenerator/FolderTree/*"; then
    echo "Error: Cleanup failed" >&2
    exit 1
fi

echo "Cleanup completed"
