https://docs.oracle.com/javase/tutorial/ext/basics/spi.html

http://blog.osgi.org/2013/02/javautilserviceloader-in-osgi.html

http://stackoverflow.com/questions/1959991/when-to-use-serviceloader-over-something-like-osgi
	The best abstraction would be something like dependency injection. In your code that requires some service "Foo", 
	don't try to lookup that service but simply allow it to be injected through a setFoo() method. From some other class, 
	make the call to ServiceLoader to get an instance of Foo and inject it into the code that needs it. If you later move 
	to OSGi, simply scrap the second class and use Declarative Services or Blueprint to handle injection of OSGi Services.
	¨C Neil Bartlett Jul 30 '10 at 0:29


https://www.eclipse.org/forums/index.php/t/474446/
	That indicates to me that you do not have an osgi.servlvice.loader.registrar capability provider installed. So far I 
	only know of one implementation of this capability in the Apache Aries project called SPI Fly (http://aries.apache.org/modules/spi-fly.html). 
	Did you install and start the bundles from SPI Fly?

http://aries.apache.org/modules/spi-fly.html
	org.apache.aries.spifly.dynamic.bundle-1.0.8.jar
	org.apache.aries.util-1.1.1.jar
	asm-all-5.0.4.jar
