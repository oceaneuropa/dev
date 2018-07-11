# CURRENT_DIR=$(dirname $0)
CURRENT_DIR="$(cd "$(dirname "$0")"; pwd -P)"
PARENT_DIR="$(dirname "$CURRENT_DIR")"
PARENT_DIR2="$(dirname "$PARENT_DIR")"
ROOT_DIR="$(dirname "$PARENT_DIR2")"

# echo "executed from: '${PWD}'"
echo "current folder: '${CURRENT_DIR}'"
echo "parent folder: '${PARENT_DIR}'"
echo "parent folder2: '${PARENT_DIR2}'"
echo "root folder: '${ROOT_DIR}'"

# OSGI_JAR="org.eclipse.osgi_3.10.100.v20150529-1857.jar"
OSGI_JAR="org.eclipse.osgi_3.10.101.v20150820-1432.jar"

java -jar ${ROOT_DIR}/plugins/${OSGI_JAR} -configuration ${PARENT_DIR}/configuration -console
