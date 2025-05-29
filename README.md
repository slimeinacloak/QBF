# QBF

QBF is a standalone Java application built with Maven. It is packaged as a `.jar` file that can be run directly from the command line.

## Prerequisites

You must have **Java 17 or higher** installed to run this application. Older versions such as Java 8 are not supported.

To verify your Java version, open a terminal or command prompt and run:

```bash
java -version
```
java version "17.0.x"
If you see a version like 1.8.0_xxx, you're running Java 8 and will need to upgrade.

Building
To build the application, ensure you have Maven installed. Then run:

```bash
mvn clean package
```
This will compile the project and produce a runnable .jar file in the target/ directory.

Running
To run the application after building:

```bash
java -jar target/QBF-1.0-SNAPSHOT.jar
```
