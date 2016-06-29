git
http://stackoverflow.com/questions/14046341/how-to-remove-bin-from-egit-versioning-after-accidentally-including-it-gitign
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Remove 'bin' from gitignore
git rm -r -f bin
git commit -m 'remove bin'
add 'bin' back to gitignore
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


Program arguments:
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-os ${target.os} -ws ${target.ws} -arch ${target.arch} -nl ${target.nl} -consoleLog -console
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

VM arguments:
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
-Declipse.ignoreApp=true -Dosgi.noShutdown=true -Dnode.http.host=localhost -Dorg.osgi.service.http.port=9090 -Dnode.http.host=localhost -Dnode.http.port=9090 -Dcluster.persistence.local.dir=C:\cluster\data -Dcluster.home.dir=C:\cluster\home1
-Declipse.ignoreApp=true -Dosgi.noShutdown=true -Dnode.http.host=localhost -Dorg.osgi.service.http.port=9090 -Dnode.http.host=localhost -Dnode.http.port=9090 -Dcluster.persistence.local.dir=/Users/yayang/cluster/data -Dcluster.home.dir=/Users/yayang/cluster/home1
------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

My Bundles:
------------------------------------------------------------------------------------------

------------------------------------------------------------------------------------------

Required Bundles:
------------------------------------------------------------------------------------------
com.eclipsesource.jaxrs.jersey-all
javax.activation
javax.annotation
javax.inject
javax.servlet
javax.ws.rs-api
log4j
org.apache.felix.gogo.command
org.apache.felix.gogo.runtime
org.apache.felix.gog.shell
org.eclipse.equinox.common
org.eclipse.equinox.console
org.eclipse.equinox.ds
org.eclipse.equinox.event
org.eclipse.equinox.frameworkadmin
org.eclipse.equinox.frameworkadmin.equinox
org.eclipse.equinox.http.jetty
org.eclipse.equinox.http.servlet
org.eclipse.equinox.util
org.eclipse.jetty.http
org.eclipse.jetty.io
org.eclipse.jetty.security
org.eclipse.jetty.server
org.eclipse.jetty.servlet
org.eclipse.jetty.util
org.eclipse.osgi
org.eclipse.osgi.services
org.eclipse.osgi.util
slf4j.api
slf4j.log4j
------------------------------------------------------------------------------------------



Management web service

Host
1. Get list of Hosts
2. Add new Host
3. Edit a Host
4. Delete a Host

	Home
	1. Get list of Homes of a Host
	2. Add new Home to a Host
	3. Edit a Home
	4. Delete a Home from a Host

Sector
1. Get list of Sectors
2. Add new Sector
3. Edit a Sector
4. Delete a Sector
5. Upload resources to a Sector

	Artifact
	1. Get list of Artifacts of a Sector
	2. Add Artifacts to a Sector
	3. Delete Artifacts from a Sector

	Space
	1. Get list of Space of a Sector
	2. Add new Space to a Sector
	3. Edit a Space
	4. Delete a Space from a Sector

		Home
		1. Add Home to a Space
		2. Remove Home from a Space

			Node
			1. Get list of Nodes in a Home-Space
			2. Add new Node to a Home-Space
			3. Remove a Node from a Home-Space

			4. Start a Node in a Home-Space
			5. Stop a Node in a Home-Space

		Artifacts
		1. Deploy Artifacts to a Space
		2. Start Artifacts deployed to a Space
		3. Stop Artifacts deployed to a Space

Q&A
How to access parameters in a RESTful POST method
http://stackoverflow.com/questions/8194408/how-to-access-parameters-in-a-restful-post-method

