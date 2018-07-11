BIN_DIR="$(cd "$(dirname "$0")"; pwd -P)"
NODE_DIR="$(dirname "$BIN_DIR")"
NODESPACES_DIR="$(dirname "$NODE_DIR")"
ROOT_DIR="$(dirname "$NODESPACES_DIR")"

OSGI_JAR="org.eclipse.osgi_3.10.101.v20150820-1432.jar"

java -jar ${ROOT_DIR}/plugins/${OSGI_JAR} -configuration ${NODE_DIR}/configuration -console
