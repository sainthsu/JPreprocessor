# Overview

The Gradle JPP Plugin allows you to preprocess macros in your java source code.

# Getting Started Using the Plugin
Please follow the below steps to add the Gradle JPP Plugin to your Gradle build script.

#### Step 1: Apply the plugin to your Gradle build script

To apply the plugin, please add one of the following snippets to your `build.gradle` file:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'org.flakor.jpp:jpp-plugin:0.1.0-Final'
    }
}
apply plugin: 'jpp'
```
* If you have a multi project build make sure to apply the plugin and the plugin configuration to every project which its source code you wish to preprocess.

#### Step 2: Add the `jpp` configuration closure to your `build.gradle` file

Add the below "jpp" closure with some properties.

```groovy
jpp {
    destDir = 'dest'
    defines {
        sdk_debugable = false
    }
    sourceSets {
        main {
            java {
                srcDirs = ['src/main/java']
            }
        }
    }'
    ...
}
```
######Mandatory parameters:
1. destDir --- destination directory your code files 
2. sourceSets --- same as java plugin's sourceSets

######Optional parameters
1. defines  --- macros you defined
2. defineFile --- a file holded macros
3. encode --- source file encoding (default utf-8)

#### Step 3: Run the build

> gradle javaPreprocess

**JVM Compatibility:**
Java 6 and above.

# License
This plugin is available under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

(c) All rights reserved Steve Hsu
