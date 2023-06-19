# Use

Monomer can be executed via command line interface (CLI) or through visual interface.

## CLI
The command line interface can be used for [shell scripts](#shell-scripts), executing a Monomer file via [interpreter](#interpreter), or compiling a Monomer file

### Updates
Run any of the following in the command line to check the installed Monomer CLI version.

    mono -version
    mono -v

Run the following in the command line to check the latest Monomer CLI version.

    mono -latest

Run the following in the command line to update Monomer and the CLI.

    mono update

Run the following to uninstall Monomer and the CLI.

    mono uninstall

### Shell Scripts
The shell environment is where code can be inputted into the command line and the command line will respond directly (after each line is entered, unless multiline is used). To initiate a shell environment, enter any of the following into the command line.

    mono shell
    mono s

Below are additional options that can be appended to the command.

 - `-multiline` or `-ml` for several lines of shell script, to be executed at the entry of the character produced by typing *ctrl+Z*. This will also exit the shell environment immediately after.
 - `-file [file names]` or `-f [file names]` for starting the shell environment after executing the list of files. Any variables will be accessable in the shell.

### Interpreter
The interpreter runs code from a file directly, which is advantageous in testing as it will start faster than if run from the compiler. However the code should be compiled for release. To interpret scripts, enter any of the following into the command line.

    mono interpret [file names]
    mono i [file names]

> The interpreter will interpret the list of files in order.

Below are additional options that can be appended to the command.

 - `-version [number]` or `-v [number]` to use a specific version

### Compiler
The compile converts the code to an executable, which is advantageous for distribution. To compile scripts, enter any of the following into the command line.

    mono compile [file name]
    mono c [file name]

Below are additional options that can be appended to the command.

 - `[output name]` to rename the output to a specific name. The default is the original file with an executable extension. Extensions are NOT automatically added to the output name.
 - `-o` to output the intermediate output f

<!--stackedit_data:
eyJoaXN0b3J5IjpbLTEyNTg2OTMzMTMsLTE1ODYyMTI2ODMsNz
MwOTk4MTE2XX0=
-->