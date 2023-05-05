# JavaVariableExtractor 4.0.2

JavaVariableExtractor checks variables in a Java source file, and print their names, types and scope information in tab-separated format.

## Requirement
- Java (>= 1.8.0)
- JavaParser (>= 3.24.2)

## Usage
  java -jar JavaVariableExtractor [-v] (java-file | java-file-directory)

-v : verbose mode

## Output format
Each printed line corresponds to a record of a variable consists of the following items:
- file path
- variable declaration line number
- variable's kind (F: field, M: method's argument, L: local variable)
- variable's name
- variable's type
- line number of scope starting position
- line number of scope end position
