# Implementation

## Parts of the interpreter-compiler

|Component|Role|
|--|--|
|Tokenizer|takes in a file and breaks it into processable Tokens that have a heirarchial structure that account for parenthesis and tabification|
|Syntax Tree|stores the components of the program in an organized format to be used to find variables, match types, interpreted, or compiled|
|Variable|provides the Variable Key that is shared between occurances of variable identifiers and also describes the type of the variable|
|Interpreter|provides the classes that are used for interpretation|
|Compiler|provides the classes that are used for compilation|


## Classes

|Class|Role|
|--|--|
|Buffer|keeps track of the position of a file while spitting out contents of the file line-by-line. Also allows un-getting part of the most recent line|
|LineContext|the information regarding a token's position, value, and the rest of its line that is used to display errors|
|**Node**|the ancestral class for all syntax tree nodes|
|**ModuleNode**|the node for a folder|
|**IdentifierNode**|the node for a variable. Variables are declared by their first use|
|**OperatorNode**|the node for an operator|
|**LiteralNode**|the node for a literal|
|GenericOperatorNode|any operator that handles only values (ie arithmetic, boolean)|
|AssignNode|used to assign a variable|
|DefineNode|used to define a function|
|FieldNode|used to access a field. Fields are declared by their first use|
|ToNode|converts a value to a copy with the type specified|
|AsNode|treats a value as the type specified|
|CallNode|calls a function|
|ControlNode|ancestral class for the control statement classes|
|LambdaNode|the "value" of a function|
|CharNode||
|StringNode||
|IntNode||
|FloatNode||
|SetNode||
|MapNode||
|ListNode||
|RangeNode||
|EnumNode||
|TupleNode|a structured list with any type children|
|SequenceNode|a flat list|
|StructureNode|a block of code enclosed by braces (`{}`) that stores local variables as fields|
|VariableKey|the Key of a variable that is shared between identifiers referring to the same variable. Doubles as the type, and, during interpretation, the value|
|FunctionKey||
|InterpretVariable|requested from nodes for interpreter actions that require variable modification|
|InterpretValue|requested from nodes for interpreter actions that require values|
|CompileSize|a union of an int or a Node that evaluates to an int that provides the size of something during runtime|
|CompileMemory|where a variable will be at runtime|
|CompileValue|where a value will be at runtime|




## UML diagrams
```mermaid
classDiagram
Tokenizer o-- Buffer
Buffer *-- BufferLine

Buffer: Queue buffer
Buffer: Reader file
Buffer: getLine() BufferLine
Buffer: ungetLine(BufferLine)
Buffer: eof() bool

BufferLine: int location
BufferLine: getFullLine() String
BufferLine: peek() char
BufferLine: get() char
BufferLine: matchNext(String[]) String|null
```

```mermaid
classDiagram
Tokenizer o-- Token
Node --o Token

Node <|-- OperatorNode: See Below
Node <|-- LiteralNode: See Below
Node <|-- IdentifierNode
Node o-- InterpretVariable
Node o-- InterpretValue
IdentifierNode <|-- ModuleNode
IdentifierNode *-- VariableKey
VariableKey <|-- FunctionKey
VariableKey <|-- BuiltinTypeKey
VariableKey *-- CompileMemory
InterpretValue <|-- InterpretVariable
InterpretValue <|-- TemporaryValue
Type <|.. InterpretValue
InterpretVariable <|-- VariableKey
CompileValue *-- CompileSize
Node o-- CompileValue

Token: String name
Token: enum{operator,int,float,char,string, group, word} usage
Token: Token[] children
Token: LineContext context
Token: makeNode() Node

Node: get String name
Node: get enum{operator,literal,identifier} usage
Node: getset Node parent
Node: Node[] children
Node: Map<String, VariableKey> variables
Node: LineContext context
Node: getType() Type
Node: getVariable(String) VariableKey
Node: putVariable(String,VariableKey)
Node: add(Node)
Node: LocateVariables()
Node: matchVariables()
Node: matchTypes()
Node: interpretVariable() InterpretVariable
Node: interpretValue() interpretValue
Node: compileMemory() CompileMemory
Node: compileValue() CompileValue
Node: compileSize() CompileSize

IdentifierNode: VariableKey key
IdentifierNode: LocateVariables() over
IdentifierNode: matchVariables() over
IdentifierNode: matchTypes() over
IdentifierNode: interpretVariable() InterpretVariable over
IdentifierNode: interpretValue() interpretValue over
IdentifierNode: compileMemory() CompileMemory over
IdentifierNode: compileValue() CompileValue over
IdentifierNode: compileSize() CompileSize over

InterpretValue: Map<String, InterpretValue> children
InterpretValue: put(String, InterpretValue)
InterpretValue: get(String) InterpretValue
InterpretValue: setValue(InterpretValue)
InterpretValue: valueString() String
InterpretValue: typeContains(Type) bool

InterpretVariable: copy() VariableKey

VariableKey: interpretValue
VariableKey: VariableKey parent

<<interface>> Type
Type: typeContains(Type) bool
Type: getFields() Map<String,Type>
```

```mermaid
classDiagram
OperatorNode <|-- GenericOperatorNode
OperatorNode <|-- AssignNode
OperatorNode <|-- DefineNode
OperatorNode <|-- AsNode
AsNode <|-- ToNode
OperatorNode <|-- FieldNode
OperatorNode <|-- CallNode
OperatorNode <|-- ControlNode
ControlNode *-- ControlChildNode
Node <|-- ControlChildNode
ControlChildNode <|-- IfNode
ControlChildNode <|-- RepeatNode
ControlChildNode <|-- WhileNode
ControlChildNode <|-- ForNode
ControlChildNode <|-- ElseNode
ControlChildNode <|-- AnyNode

OperatorNode: staticMap<String,Supplier<OperatorNode>> defs

GenericOperatorNode: Type type
GenericOperatorNode: Supplier<InterpretValue> interpretValue
GenericOperatorNode: Supplier<InterpretVariable> interpretVariable
GenericOperatorNode: inpterpretValue() InterpretValue
GenericOperatorNode: interpretVariable() InterpretVariable
GenericOperatorNode: setType(Type)
GenericOperatorNode: getType() Type

AssignNode: matchTypes()

DefineNode: Map<String,VariableKey> variables
DefineNode: putVariable(String, VariableKey)
DefineNode: getVariable(String) VariableKey

ControlNode: Map<String,VariableKey> variables

ControlChildNode: Map<String,VariableKey> variables
```

```mermaid
classDiagram
LiteralNode <|-- RawLiteralNode
RawLiteralNode <|-- CharNode
RawLiteralNode <|-- IntNode
RawLiteralNode <|-- FloatNode
RawLiteralNode <|-- StringNode

LiteralNode <|-- LambdaNode
LiteralNode <|-- ListNode
LiteralNode <|-- RangeNode

LiteralNode <|-- SetNode
SetNode <|-- MapNode

LiteralNode <|-- TupleNode
LiteralNode <|-- SequenceNode

LiteralNode <|-- StructureNode
StructureNode <|-- EnumNode
```

And this will produce a flow chart:

```mermaid
graph LR
A[Square Rect] -- Link text --> B((Circle))
A --> C(Round Rect)
B --> D{Rhombus}
C --> D
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbMjEwNDc3MTQwOSwtMTQwNzE0NTA2LDYzOT
UxMDYwMywxOTIwMzEwNzMzLDYzNjY2MjMyMiwzODIxODI0NDks
LTU0Njc1MjU5OSw3ODcwNzEwOTIsMTYzMzI5MjA3OF19
-->