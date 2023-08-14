/**
 * Contains the classes for the operators in the syntax tree.
 * <p>
 *     The operators are divided into two categories: control operators and generic operators.
 *     Control operators (found in a separate module) are used to control the flow of the program,
 *     while generic operators are used to perform operations on values. Certain operators with
 *     specific needs are created separately from the generic operators.
 *     The classes are listed below:
 *     <ul>
 *         <li>{@link systems.monomer.syntaxtree.operators.OperatorNode} all operators inherit from this. And manages all operators through its static helper functions</li>
 *         <li>{@link systems.monomer.syntaxtree.operators.Arithmetic} contains helper methods for arithmetic operators</li>
 *         <li>{@link systems.monomer.syntaxtree.operators.Comparison} contains helper methods for comparison operators</li>
 *         <li>{@link systems.monomer.syntaxtree.operators.Bitwise} contains helper methods for bitwise/logical operators</li>
 *         <li>{@link systems.monomer.syntaxtree.operators.GenericOperatorNode} all generic operators (add, equals, etc)</li>
 *         <li>{@link systems.monomer.syntaxtree.operators.AssignNode} the assignment node</li>
 *         <li>{@link systems.monomer.syntaxtree.operators.CallNode} the call node</li>
 *         <li>{@link systems.monomer.syntaxtree.operators.CastNode} the casting (treating as another type) node</li>
 *         <li>{@link systems.monomer.syntaxtree.operators.ConvertNode} the casting (creating another of a different type) node</li>
 *         <li>{@link systems.monomer.syntaxtree.operators.FieldNode} the field node</li>
 *     </ul>
 *     This module should only be accessed with the rest of the syntax tree.
 *
 * @since 1.0
 * @author ansere
 * @version 1.0
 */
package systems.monomer.syntaxtree.operators;