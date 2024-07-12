/**
 * Command line parsing and execution.
 * <p>
 *     This module contains the command line interface for Monomer.
 *     It uses the picocli library to parse the command line arguments.
 *     The main class is {@link systems.monomer.execution.commandline.CommandLineInterface}.
 *     The {@link systems.monomer.execution.commandline.Interpret} class is used to interpret Monomer files.
 *     The {@link systems.monomer.execution.commandline.Compile} class is used to compile Monomer files.
 *     The {@link systems.monomer.execution.commandline.Shell} class is used to handle the Monomer shell environment.
 *     The IDE is handled in a different module
 * </p>
 *
 * @since 1.0
 * @author jzhyang
 * @version 1.0
 * @see systems.monomer.ide
 */
package systems.monomer.execution.commandline;