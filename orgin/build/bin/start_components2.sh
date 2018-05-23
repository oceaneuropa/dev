CURRENT_DIR="$(cd "$(dirname "$0")"; pwd -P)"
PARENT_DIR="$(dirname "$CURRENT_DIR")"
OSGI_JAR="org.eclipse.osgi_3.10.101.v20150820-1432.jar"
java -jar ${PARENT_DIR}/plugins/${OSGI_JAR} -configuration ${PARENT_DIR}/configurations/components2 -console