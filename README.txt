Please refer to Test to see how to use the Assembler.
All documentation is here: http://inspire-software.com/confluence/display/GeDA/GeDA+-+Generic+DTO+Assembler

To run performance tests you need to add memory parameters:
-Xmx1024M
-XX:MaxPermSize=500M

On unix you can do this:
export MAVEN_OPTS='-Xmx1024M -XX:MaxPermSize=500M'
mvn test
