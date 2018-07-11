echo "Script executed from: ${PWD}"

CURRENT_DIR=$(dirname $0)
echo "Script location: ${WORKING_DIR}"

java -jar ${CURRENT_DIR}/plugins/org.eclipse.equinox.launcher_1.3.0.v20140415-2008.jar -configuration ${CURRENT_DIR}/configuration -console
