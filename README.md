1. Certificate needs to be added to java keystore

keytool -import -alias intranetcert -keystore   -file intranetcert.cer "C:\Program Files (x86)\Java\jre1.8.0_191\lib\security\cacerts"

keytool -import -noprompt -trustcacerts -alias intranetcert -file   intranetcert.cer -keystore "%JAVA_HOME%/jre/lib/security/cacerts" -storepass changeit