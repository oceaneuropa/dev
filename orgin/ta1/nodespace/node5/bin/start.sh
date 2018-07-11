PLATFORM_HOME=/Users/yayang/origin/ta1
CURRENT_DIR=$(dirname $0)
PARENT_DIR="$(dirname ${CURRENT_DIR})"
java -jar ${PLATFORM_HOME}/system/org.eclipse.osgi.jar -configuration ${PARENT_DIR}/configuration_v1 -console
