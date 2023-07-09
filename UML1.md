

note for SourceIndex "x and y will be the indices [0...), whereas row and col will be the positions [1...)"

note for SourceContext "toString will be the string from the source; errorString will have the source title and be underlined"

note for ErrorBlock "throws a SyntaxError with context printed along with msg"

note for Node "getVariableKey will default to null. interpretVariable and compileMemory will default to error. compileMemory is the location of a variable while compileValue can also specify calculations, constant value or register location"

note for StringBuilderNode "parts being built together will be stored in children"

```mermaid
classDiagram

class SourceIndex{
	-x: int
	-y: int
	+SourceIndex(int x, int y)  
	+getRow() int  
	+getCol() int  
	+getX() int  
	+getY() int
}
class SourceContext{
	-source: Source  
	-start: Index  
	-end: Index
}
class ErrorBlock{
	-context: SourceContext
	+throwError(String msg)  
	+setContext(context)
}
class Source{
	+$class Line  
	+$class Token  
	+buffer: Deque<Line>  
	-int y
	+getLine() Line  
	+ungetLine(Line)  
	#bufferLines(int num)*  
	+parse(): Token  
	+parseStringLiteral() Token  
	+parseNumberLiteral() Token  
	+parseIdentifier()* Token  
	+getTitle()* String
}
class SourceFile{
	+reader: FileReader  
	+file: File  
	+fileName: String
	+SourceFile(String path)  
	#bufferLines(int num)  
	+getTitle() String
}
class SourceString{
	-source: String
	+SourceString(String)  
	#bufferLines(int num)  
	+getTitle() String
}

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
LiteralNode <-- StringNode
LiteralNode <-- StringBuilderNode
LiteralNode <-- TupleNode
LiteralNode <-- ListNode
LiteralNode <-- SetNode
LiteralNode <-- MapNode
LiteralNode <-- StructureNode
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbNTcyMzQ3MDddfQ==
-->