# groovy-directory-alchemy

A simple project that renames files in a given directory and its subdirectories.

---

## Development
- JDK: temurin-21
- Groovy: 3.0.19
- IDE: IntelliJ IDEA CE 2024.1.2
- Platform: Windows 11 x64

---

## Usage

In `Main.groovy`, replace the following variables depending
on your use case
- `workingDirectory`: The directory where the project will look for file names
- `backupDirectory`: The directory where the project will back up the files before renaming them
- `contains`: The character sequence that the project will look for in the file name
- `replaceWith`: The character that the project will replace the matches with. The project will replace every occurrence of the found match in a file name.
- `logFilePath`: The path where the project will write its logs.
