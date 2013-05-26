Official site is:
http://www.genericdtoassembler.org/

Please refer to examples module to see how to use the Assembler.
All documentation is here: http://www.inspire-software.com/confluence/display/GeDA/GeDA+-+Generic+DTO+Assembler

Module core-btest contains Performance benchmarks. To run those use -Pwith-btest option in maven:
mvn install -Pwith-btest

Module core-ptest is used for multithreading tests and load testing. To run those use -Pwith-ptest option in maven:
mvn install -Pwith-ptest

Module osgi-itest is the PAX exam for OSGI. Due to the nature of mvn osgi installation this has to be a separate module.
mvn install -Pwith-itest

To run all extra's use
mvn install -Pall

Also see how to setup GeDA in IDE guide:
http://www.inspire-software.com/confluence/display/GeDA/Setting+up+GeDA+project

Spring integration schemas can be found:
http://www.genericdtoassembler.org/schema/[schema name]