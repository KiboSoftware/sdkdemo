<h1>How to clone and run this project</h1>

1) From command line, run  `git clone git@github.com:KiboSoftware/sdkdemo.git` in the directory of your choosing
2) Open the project up within your IDE
3) From command line of the sdk (kibo.sdk.java) project, run `mvn clean install`
    - Ensure that maven installs to your .m2 directory, which is your local repo (for example mine is located at /Users/michael.kytka/.m2)
4) Within this sdk demo project, download the sources and documentation for all pom dependencies
    - Ensure once again that maven is pointed to your local repository 
5) Add your properties to application.properties file within the demo project
    - Make sure to provide all properties to the `resources/application.properties` file.
6) Set IDE to use JDK 11
    - For example, in intellij change the Java and SDK version in the following places
        - File -> Project Structure -> Project Settings
        - File -> Project Structure -> Module Settings -> Tab: Sources: Language Level
        - File -> Project Structure -> Module Settings -> Tab: Dependencies: Module SDK
        - File -> Settings -> Compiler -> Java Compiler -> Target bytecode version
7) Run Project using the IDE's Spring boot run configuration
