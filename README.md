# Text-parser

## An application for searching for the same words and analyzing the correctness of the alignment of brackets

## Running the application
In order to run the application you need:
1. open the application folder
2. open the console
3. Launch the application:
 ## 3.1 First launch
  ### mvn clean install  ||  mvn spring-boot:start ||  mvn spring-boot:run
 ## 3.2 Second and other
  ### mvn spring-boot:run
4. open the browser and go to localhost:8080

### To work with the application you need
1. Java 8
2. Maven

### Warning
For correct work with Russian text, it is necessary to transfer a file with the encoding of UTF-8
Example file in the project folder

 Description of files

File name              		  |  Description
----------------------------|----------------------
BracketsAnalyzer.java  		  | Analysis of the correctness of the separation of brackets
WordsAnalyzer.java	   		  | Analysis of repetitive words
UploadController.java  		  | Controller
GlobalExceptionHandler.java | Exception handler
ReadingFile.java	   		    | reading text from a file
replaceSymbol.java 	   		  | Cleaning content from characters
Application.java   	   		  | launcher application
