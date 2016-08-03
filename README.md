#Heaven

Heaven is an open source software framework for RSP engine stress testing.

It is composed by

- RSP Engine Test Stand 
- Four baselines RSP Engine developed with [Esper](http://www.espertech.com/esper/) and [Jena](http://jena.apache.org/index.html)
- some R scripts for data analysis

# Maven dependency of the whole project

```xml
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
</repositories>

<dependency>
	<groupId>com.github.streamreasoning</groupId>
	<artifactId>Heaven</artifactId>
	<version>tag-version</version>
</dependency>

```
Example

```xml
<dependency>
	<groupId>com.github.streamreasoning</groupId>
	<artifactId>Heaven</artifactId>
	<version>0.9</version>
</dependency>
```

If you want to add only a specifyc module of Heaven

```xml

<dependency>
	    <groupId>com.github.streamreasoning.heaven</groupId>
	    <artifactId>heaven-core</artifactId>
	    <version>0.9</version>
</dependency>
```
Example

```xml
<dependency>
	    <groupId>com.github.streamreasoning.heaven</groupId>
	    <artifactId>module-name</artifactId>
	    <version>tag-version</version>
</dependency>

```

# Dependency List

This project depends on the following external projects

- Project Lombok [1.16.10] [link](https://projectlombok.org)
