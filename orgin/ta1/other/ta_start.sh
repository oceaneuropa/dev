CURRENT_DIR=$(dirname $0)
PARENT_DIR="$(dirname ${CURRENT_DIR})"

# echo ".sh script executed from: '${PWD}'"
# echo ".sh script location: '${CURRENT_DIR}'"
# echo ".sh script parent dir: '${PARENT_DIR}'"

# java -jar /Users/ocean/origin/ta1/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration /Users/ocean/origin/ta1/configuration -console

java -jar ${CURRENT_DIR}/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration ${CURRENT_DIR}/configuration -console
# java -jar ${CURRENT_DIR}/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration ${CURRENT_DIR}/configuration -console

# java -jar ${PARENT_DIR}/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar -configuration ${PARENT_DIR}/configuration -console
