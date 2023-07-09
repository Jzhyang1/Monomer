


note for Node "getVariableKey will default to null. interpretVariable and compileMemory will default to error. compileMemory is the location of a variable while compileValue can also specify calculations, constant value or register location"

```mermaid
classDiagram

class Node{
	#$enum: Usage
	-name: String  
	-parent: Node  
	-children: List<Node>
	+Node(String)  
	+getName() String
	+getUsage() Usage
	+getParent() Node
	+setParent(Node) 
	+getType() Type
	+setType(Type) 
	+add(Node)  
	+getVariable(String) VariableKey  
	+putVariable(String, VariableKey)  
	+matchVariables()  
	+getVariableKey() VariableKey  
	+matchTypes()  
	+matchOverloads()  
	+interpretVariable() InterpretVariable  
	+interpretValue()* InterpretValue
	+compileMemory() CompileMemory  
	+compileValue()* CompileValue 
	+compileSize()* CompileSize
}
class Node_Usage{
	<<Enumeration>>
	OPERATOR
	LITERAL
	IDENTIFIER
}
class VariableNode{
	-key: VariableKey
	+VariableNode(String)  
	+matchVariables()  
	+matchTypes()  
	+getVariableKey() VariableKey  
	+interpretVariable() InterpretVariable  
	+interpretValue() interpretValue  
	+compileMemory() CompileMemory  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class ModuleNode{
	-exports: Map<String,VariableKey>
	+ModuleNode(String path)  
	+matchVariables()  
	+matchTypes()  
	+interpretValue() InterpretValue  
	+compileMemory() CompileMemory  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class SourceNode{
	-exports: Map<String,VariableKey>
	+ModuleNode(String path)  
	+matchVariables()  
	+matchTypes()  
	+interpretValue() InterpretValue  
	+compileMemory() CompileMemory  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class LiteralNode{
	
}
class CharNode{
	-value: Character
	+CharNode(Character)
	+interpretValue() InterpretValue  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class IntNode{
	-value: Integer
	+IntNode(Integer)
	+interpretValue() InterpretValue  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class FloatNode{
	-value: Double
	+FloatNode(Double)
	+interpretValue() InterpretValue  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class StringNode{
	-value: String
	+StringNode(String)
	+interpretValue() InterpretValue  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class StringBuilderNode{
	+StringBuilderNode()
	+StringBuilderNode(List<Node>)
	+interpretValue() InterpretValue  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class TupleNode{
	+asTuple() Tuple$
	+TupleNode()
	+TupleNode(List<Node>)
	+interpretValue() InterpretValue  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class ListNode{
	+ListNode()
	+ListNode(List<Node>)
	+interpretValue() InterpretValue  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class SetNode{
	+SetNode()
	+SetNode(List<Node>)
	+interpretValue() InterpretValue  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class MapNode{
	+MapNode()
	+MapNode(List<Node>)
	+interpretValue() InterpretValue  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}
class StructureNode{
	+StructureNode()
	+StructureNode(List<Node>)
	+interpretValue() InterpretValue  
	+compileValue() CompileValue  
	+compileSize() CompileSize
}

Node *-- Node_Usage
Node <-- ModuleNode
Node <-- VariableNode
Node <-- LiteralNode

ModuleNode <-- SourceNode

LiteralNode <-- CharNode
LiteralNode <-- IntNode
LiteralNode <-- FloatNode
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTI1MTI2NTAxMl19
-->