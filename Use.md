# Use

Monomer can be executed via command line interface (CLI) or through visual interface.

## CLI
The command line interface can be used for [shell scripts](#Shell%20Scripts), executing a Monomer file via [interpreter](###Interpreter), or compiling a Monomer file

### Shell Scripts
The shell environment is where code can be inputted into the command line and the command line will respond directly (after each line is entered, unless multiline is used). To initiate a shell environment, enter any of the following into the command line.

    mono shell
    mono s

Below are additional options that can be appended to the command.

 - `-multiline` or `-ml` for several lines of shell script, to be executed at the entry of the character produced by typing *ctrl+Z*. This will also exit the shell environment immediately after.
 - `-file [file names]` or `-f [file names]` for starting the shell environment after executing the list of files. Any variables will be accessable in the shell.

### Interpreter
The interpreter runs code from a file directly, which is advantageous in testing as it will start faster than if run from the compiler. However the code should be compiled for release. To interpret scripts, enter any of the following into the command line.

    mono inter [file names]
    mono i [file names]

> The interpreter will interpret the list of files in order.

Below are additional options that can be appended to the command.

 - `

<!--stackedit_data:
eyJoaXN0b3J5IjpbMTE1NDk0MzU4OSwtMTU4NjIxMjY4Myw3Mz
A5OTgxMTZdfQ==
-->