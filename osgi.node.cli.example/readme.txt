Program arguments:
-os ${target.os} -ws ${target.ws} -arch ${target.arch} -nl ${target.nl} -consoleLog -console

VM arguments:
-Declipse.ignoreApp=true -Dosgi.noShutdown=true -Dorg.osgi.service.http.port=9090 -Dnode.http.host=localhost -Dnode.http.port=9090

-Dosgi.requiredJavaVersion=1.7 -XstartOnFirstThread -Dorg.eclipse.swt.internal.carbon.smallFonts -XX:MaxPermSize=256m -Xms256m -Xmx1024m -Xdock:icon=../Resources/Eclipse.icns -XstartOnFirstThread -Dorg.eclipse.swt.internal.carbon.smallFonts -Declipse.ignoreApp=true -Dosgi.noShutdown=true -Dnode.http.host=localhost -Dnode.http.port=9090