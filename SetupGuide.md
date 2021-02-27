
# Fork project on GitHub

Fork project at [GitHub](https://github.com/inspire-software/geda-genericdto)

All released versions of GeDA are tagged so have a look at the appropriate tag in https://github.com/inspire-software/geda-genericdto and fork from there

# Remove maven deployment specific configurations

GeDA is a maven project and as such it inherits from the main maven pom. This is necessary for all projects available through maven repository. Unfortunately this means that it contains signing phase and bunch of other stuff that will prevent you from being able to building the project easily. So you will need to remove the following code from geda-genericdto/pom.xml file

Maven parent declaration - Remove this

```xml
    <parent>
        <groupId>org.sonatype.oss</groupId>
        <artifactId>oss-parent</artifactId>
        <version>7</version>
    </parent>
Maven signing - Remove this


                <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-gpg-plugin</artifactId>
                <executions>
                    <execution>
                        <id>sign-artifacts</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>sign</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

# [optional] Build the project

Now use standard maven command to build the project. This will run all tests, compile and install the modules to your local maven repository

```bash
mvn install
```

# Setup project in IDE of your choice

I strongly recommend using Intellij IDEA for working with java projects, but the choice of IDE is up to you.

In IDEA all you need to do is choose create new project, import from external model and choose maven. IDEA will setup all the modules automatically. And you are done with the setup.

The actual setup of GeDA in IDE is beyond the scope of this article.
