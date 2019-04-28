# Space Explorer
2019 SENG201 Game Project

Created by:
* Matthew Johnson <mmj37@uclive.ac.nz>
* Oliver Egan <@uclive.ac.nz>


## Building `.jar` from source
This project uses maven to manage dependencies needed for testing and json. Maven must be installed to
use `pom.xml` 

>Please follow [These Instructions](https://maven.apache.org/install.html) to install and configure maven on your machine.
>
Once maven is installed run:
```
maven build
maven test
maven package
```
This will build, test and package the program to provide a `.jar` which can be run from the console.

## Pre-complied `.jar`
>The complied `.jar` can be run from either windows, linux or mac.

#### From the console
Open any console with JRE in its path and run:
```$xslt
java -jar SpaceExplorer.jar
```
#### From GUI File Manager
>Simply double clicking the `.jar` will run the game.