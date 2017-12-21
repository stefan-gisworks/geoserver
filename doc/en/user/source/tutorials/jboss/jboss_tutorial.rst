.. _jboss_tutorial:

geoserver on JBoss
==================
This tutorial documents how to install various versions of geoserver onto various versions of JBoss.


geoserver 2.7.0 on JBoss AS 5.1
-------------------------------
To install geoserver onto JBoss AS 5.1, the following is required:

1. Create the file ``jboss-classloading.xml`` with the following content then copy it into the ``WEB-INF`` directory in the geoserver.war:

.. code-block:: xml

	<classloading xmlns="urn:jboss:classloading:1.0"
		name="geoserver.war"
		domain="GeoServerDomain">
	</classloading>

2. Extract the ``hsqldb-2.2.8.jar`` file from the ``WEB-INF/lib`` directory from the geoserver.war and copy it as ``hsqldb.jar`` to the ``common/lib`` directory in the JBoss deployment.

3. Add the following text to the ``WEB-INF/web.xml`` file in the geoserver.war so that JBoss logging does not end up in the geoserver.log:

.. code-block:: xml

	<context-param>
		<param-name>RELINQUISH_LOG4J_CONTROL</param-name>
		<param-value>true</param-value>
	</context-param>    


geoserver 2.12.1 on JBoss EAP 7.1
---------------------------------
To install geoserver onto JBoss EAP 7.1, on windows follow these steps.

1. unzip geoserver.war into a folder geoserver.war and copy this folder to jboss-eap-7.1\standalone\deployments
2. create the file geoserver.war.dodeploy in jboss-eap-7.1\standalone\deployments

3. Geoservers strong encryption depends on bouncycastle. To avoid java.lang.NoClassDefFoundError: org/bouncycastle/jce/provider/BouncyCastleProvider
Create the file ``jboss-deployment-structure.xml`` with the following content then copy it into the ``WEB-INF`` directory in the geoserver.war:
.. code-block:: xml

	<?xml version="1.0" encoding="UTF-8"?>  
	<jboss-deployment-structure>  
		<deployment>  
			<dependencies>  
				<module name="org.bouncycastle" slot="main" export="true"/> 
			</dependencies>  
		</deployment>  
	</jboss-deployment-structure>

4. Some actions (e.g. removing a geoserver user) may lead to following error:
Caused by: org.apache.xml.serializer.utils.WrappedRuntimeException: org.apache.xml.serializer.ToXMLSAXHandler cannot be cast to org.apache.xml.serializer.SerializationHandler
	at org.apache.xml.serializer.SerializerFactory.getSerializer(SerializerFactory.java:179)
	at org.apache.xalan.transformer.TransformerIdentityImpl.createResultContentHandler(TransformerIdentityImpl.java:260)
	at org.apache.xalan.transformer.TransformerIdentityImpl.transform(TransformerIdentityImpl.java:330)
	at org.geoserver.security.xml.XMLUserGroupStore.serialize(XMLUserGroupStore.java:164)
	... 153 more

To solve this remove ``serializer-2.7.1.jar`` from geoserver.war\WEB-INF\lib (see https://issues.jboss.org/browse/JBEAP-19).

5. To use MarlinRenderer copy ``marlin-0.7.5-Unsafe.jar`` to jboss-eap-7.1\standalone\lib\ and edit jboss-eap-7.1\bin\standalone.conf.bat:
rem # GeoServer Marlin Renderer
set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/a:D:\programme\jboss-eap-7.1\standalone\lib\marlin-0.7.5-Unsafe.jar"
set "JAVA_OPTS=%JAVA_OPTS% -Dsun.java2d.renderer=org.marlin.pisces.MarlinRenderingEngine"

Serverstatus - Java Rendering Engine will show "Unknown", but Modules - Rendering Engine will have Configuration: -Dsun.java2d.renderer=org.marlin.pisces.MarlinRenderingEngine.

start jboss eap with
cd jboss-eap-7.1\bin\
.\standalone.bat

6. If jboss is complaining about "Address already in use: bind /127.0.0.1:9990" you maybe could stop "NVIDIA Network Service" which is using the same port.

Ignore warnings like (german):
WFLYSRV0059: Eintrag für Class-Path activation.jar in jboss-eap-7.1/standalone/deployments/geoserver.war/WEB-INF/lib/mail-1.4.jar 
zeigt nicht auf ein gültiges jar für eine Class-Path Referenz.

7. Solve ``log4j:WARN File option not set for appender [geoserverlogfile]`` by adding
log4j.appender.geoserverlogfile.File = geoserver.log 
to DATA_DIR\logs\xxx_LOGGING.properties.


Optional: Adding CORS-FILTER to allow cross-domain requests:
copy ``cors-filter-2.6.jar`` and ``java-property-utils-1.9.jar`` into WEB-INF/lib and add the folowing to web.xml

.. code-block:: xml

	<filter>
		<filter-name>CORS</filter-name>
		<filter-class>com.thetransactioncompany.cors.CORSFilter</filter-class>
		<init-param>
			<param-name>cors.allowOrigin</param-name>
			<param-value>*</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>CORS</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>	
