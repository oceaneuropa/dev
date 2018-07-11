CURRENT_DIR=$(dirname $0)
PARENT_DIR1="$(dirname ${CURRENT_DIR})"
PARENT_DIR2="$(dirname ${PARENT_DIR1})"
PARENT_DIR3="$(dirname ${PARENT_DIR2})"

# echo ".sh script executed from: '${PWD}'"
# echo ".sh script location: '${CURRENT_DIR}'"
# echo ".sh script parent dir: '${PARENT_DIR}'"

# java -jar /Users/yayang/origin/ta1/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration /Users/yayang/origin/ta1/configuration -console
# java -jar ${CURRENT_DIR}/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration ${CURRENT_DIR}/configuration -console
java -jar ${PARENT_DIR3}/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration ${PARENT_DIR1}/configuration -console
