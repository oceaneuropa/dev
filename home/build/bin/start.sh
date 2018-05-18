# CURRENT_DIR=$(dirname $0)
CURRENT_DIR="$(cd "$(dirname "$0")"; pwd -P)"
PARENT_DIR="$(dirname "$CURRENT_DIR")"

# echo "executed from: '${PWD}'"
# echo "current folder: '${CURRENT_DIR}'"
# echo "parent folder: '${PARENT_DIR}'"

# OSGI_JAR="org.eclipse.osgi_3.10.100.v20150529-1857.jar"
OSGI_JAR="org.eclipse.osgi_3.10.101.v20150820-1432.jar"

java -jar ${PARENT_DIR}/plugins/${OSGI_JAR} -configuration ${PARENT_DIR}/configurations/configuration -console
