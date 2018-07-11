CURRENT_DIR=$(dirname $0)
PARENT_DIR1="$(dirname ${CURRENT_DIR})"
PARENT_DIR2="$(dirname ${PARENT_DIR1})"
PARENT_DIR3="$(dirname ${PARENT_DIR2})"
java -jar ${PARENT_DIR3}/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration ${PARENT_DIR1}/configuration -consoleLog -console
