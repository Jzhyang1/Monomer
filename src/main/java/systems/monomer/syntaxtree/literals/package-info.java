/**
 * Contains the classes for the literal nodes of the syntax tree.
 * <p>
 * The literal nodes are the nodes that represent the values with a built-in conversion to a typed value in the
 * source code. These include:
 * <ul>
 *      <li>{@link systems.monomer.syntaxtree.literals.LiteralNode} that all literal nodes inherit from</li>
 *      <li>{@link systems.monomer.syntaxtree.literals.BoolNode}</li>
 *      <li>{@link systems.monomer.syntaxtree.literals.CharNode}</li>
 *      <li>{@link systems.monomer.syntaxtree.literals.StringNode}</li>
 *      <li>{@link systems.monomer.syntaxtree.literals.FloatNode}</li>
 *      <li>{@link systems.monomer.syntaxtree.literals.IntNode}</li>
 * </ul>
 * as well as the
 * <ul>
 *      <li>{@link systems.monomer.syntaxtree.literals.StringBuilderNode string builder} node</li>
 *      <li>{@link systems.monomer.syntaxtree.literals.TupleNode tuple} node</li>
 *      <li>{@link systems.monomer.syntaxtree.literals.ListNode list} node</li>
 *      <li>{@link systems.monomer.syntaxtree.literals.SetNode set} node</li>
 *      <li>{@link systems.monomer.syntaxtree.literals.MapNode map} node</li>
 * </ul>
 * This module should only be accessed with the rest of the syntax tree.
 *
 * @since 1.0
 * @author jzhyang
 * @version 1.0
 */
package systems.monomer.syntaxtree.literals;