# Implementation

## Parts of the interpreter-compiler

|Component|Role|
|--|--|
|Tokenizer|takes in a file and breaks it into processable Tokens that have a heirarchial structure that account for parenthesis and tabification|
|Syntax Tree|stores the components of the program in an organized format to be used to find variables, match types, interpreted, or compiled|
|Variable|provides the Variable Key that is shared between occurances of variable identifiers and also describes the type of the variable|
|Interpreter|provides the classes that are used for interpretation|
|Compiler|provides the classes that are used for compilation|
|Error Handling|provides the classes that are used for error messages|


## Classes

|Class|Role|
|--|--|
|Buffer|keeps track of the position of a file while spitting out contents of the file line-by-line. Also allows un-getting part of the most recent line|
|SourceContext|the information regarding a token's position, value, and the rest of its line that is used to display errors|
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
|StringNode|stores a string. Possibly with embeds|
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
|systems.merl.monomer.variables.VariableKey|the Key of a variable that is shared between identifiers referring to the same variable. Doubles as the type, and, during interpretation, the value|
|FunctionKey||
|InterpretVariable|requested from nodes for interpreter actions that require variable modification|
|InterpretValue|requested from nodes for interpreter actions that require values|
|CompileSize|a union of an int or a Node that evaluates to an int that provides the size of something during runtime|
|CompileMemory|where a variable will be at runtime|
|CompileValue|where a value will be at runtime|




## UML diagrams
```mermaid
classDiagram
Tokenizer o-- Source
Source *-- SourceLine

Tokenizer: tokenize(Source) Token

Source: int row
Source: Queue buffer
Source: Scanner input
Source: getLine() SourceLine
Source: ungetLine(SourceLine)
Source: eof() bool
Source: getRow() int

SourceLine: int row
SourceLine: int col
SourceLine: String line
SourceLine: getFullLine() String
SourceLine: peek() char
SourceLine: get() char
SourceLine: matchNext(Collection<String>) String|null
SourceLine: startingSpaces() int
SourceLine: skipSpaces() int
SourceLine: getRow() int
SourceLine: getCol() int
```

```mermaid
classDiagram
Tokenizer o-- Token
Node --o Token
Token *-- SourceContext

Node <|-- OperatorNode: See Below
Node <|-- LiteralNode: See Below
Node <|-- IdentifierNode
Node o-- InterpretVariable
Node o-- InterpretValue
IdentifierNode <|-- ModuleNode
IdentifierNode *-- systems.merl.monomer.variables.VariableKey
systems.merl.monomer.variables.VariableKey <|-- FunctionKey
systems.merl.monomer.variables.VariableKey <|-- BuiltinTypeKey
systems.merl.monomer.variables.VariableKey *-- CompileMemory
InterpretValue <|-- InterpretVariable
InterpretValue <|-- TemporaryValue
systems.merl.monomer.variables.Type <|.. InterpretValue
InterpretVariable <|-- systems.merl.monomer.variables.VariableKey
CompileValue *-- CompileSize
Node o-- CompileValue

Token: String name
Token: enum{operator,int,float,char,stringbuild,string,group,word} usage
Token: Token[] children
Token: SourceContext context
Token: with(String name) Token
Token: makeNode() Node

Node: get String name
Node: get enum{operator,literal,identifier} usage
Node: getset Node parent
Node: Node[] children
Node: Map<String, systems.merl.monomer.variables.VariableKey> variables
Node: SourceContext context
Node: getType() systems.merl.monomer.variables.Type
Node: setType(systems.merl.monomer.variables.Type)
Node: getVariable(String) systems.merl.monomer.variables.VariableKey
Node: setVariable(String,systems.merl.monomer.variables.VariableKey)
Node: add(Node)
Node: LocateVariables()
Node: matchVariables()
Node: matchTypes()
Node: matchOverloads()
Node: interpretVariable() InterpretVariable
Node: interpretValue() interpretValue
Node: compileMemory() CompileMemory
Node: compileValue() CompileValue
Node: compileSize() CompileSize

IdentifierNode: systems.merl.monomer.variables.VariableKey key
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
InterpretValue: typeContains(systems.merl.monomer.variables.Type) bool

InterpretVariable: copy() systems.merl.monomer.variables.VariableKey

systems.merl.monomer.variables.VariableKey: interpretValue
systems.merl.monomer.variables.VariableKey: systems.merl.monomer.variables.VariableKey parent

<<interface>> systems.merl.monomer.variables.Type
systems.merl.monomer.variables.Type: typeContains(systems.merl.monomer.variables.Type) bool
systems.merl.monomer.variables.Type: getFields() Map<String,systems.merl.monomer.variables.Type>
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

GenericOperatorNode: systems.merl.monomer.variables.Type type
GenericOperatorNode: Supplier<InterpretValue> interpretValue
GenericOperatorNode: Supplier<InterpretVariable> interpretVariable
GenericOperatorNode: inpterpretValue() InterpretValue
GenericOperatorNode: interpretVariable() InterpretVariable
GenericOperatorNode: setType(systems.merl.monomer.variables.Type)
GenericOperatorNode: getType() systems.merl.monomer.variables.Type

AssignNode: matchTypes()

DefineNode: Map<String,systems.merl.monomer.variables.VariableKey> variables
DefineNode: setVariable(String, systems.merl.monomer.variables.VariableKey)
DefineNode: getVariable(String) systems.merl.monomer.variables.VariableKey

ControlNode: Map<String,systems.merl.monomer.variables.VariableKey> variables

CallNode: LambdaNode overload

ControlChildNode: Map<String,systems.merl.monomer.variables.VariableKey> variables
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

The general program flowchart:

```mermaid
graph LR
X[[CLI Input]] --> A
Y[[IDE Direct Call]] --> A
A[[Buffer]] --> B[[Tokenize]]
B --> C[[Make Tree]]
C --> D[[Locate Variables]]
D --> E[[Match Variables]]
E --> F[[Match Types]]
F --> G[[Run]]
F --> H[[Compile]]
```

```mermaid
graph LR
subgraph sg1["main function"]
	A[String inputs] --> B[Toogle config bools]
	B --> C{Check input format}
	C -- Run --> H[Start IDE]
	C -- CLI --> D{Console Input?}
	D -- Yes --> E[Get string input from console]
	D -- No --> F[Get file]
	E --> I[Create buffer, Tokenize, Make tree, etc]
	F --> I
	I --> J{Config set to}
	J -- Interpret --> K[Run]
	J -- Compile --> L[Compile]
end
```
```mermaid
graph LR
subgraph sg1["buffer"]
	A[FileReader] --> C
	B[String] --> C[Scanner]
	C --> D[Read first N lines as BufferLines]
	D --track n--> E[As more lines are taken from the buffer, clear and reload the N-line buffer]
	D --> F["Unget by setting n to n-1"]
end
```
```mermaid
graph LR
subgraph sg1["Tokenize"]
	A["tokenize(Buffer)"] --> A1["BufferLine line"] --> B[Get number of starting spaces of this line]
	--> C1{end of line}
	C1 -- Y --> C2["return tokenize(buffer)"]
	C1 -- N --> C[Variables strbuild, tokens]
	 --> D[For char, pos in line] 
	 --> E{what is char?}
	E -- Escape --> E1{followed by?}
	E1 --"Open Parenthesis"--> E11["readMultilineComment(Buffer)"]
	E1 --"b-slash"--> E12[next line]
	E1 --"New line"--> E13[clear new line and spaces]
	E -- Newline --> E2["append(';') getLine()"]
	--> EA{Count spaces?}
	EA -- "same as before" --> EA1[clear spaces]
	EA -- "fewer" --> EA2[return]
	EA -- "more" --> EA3["append(tokenize(buffer))"]
	E -- Space --> E3["append(strbuild) clear()"]
	E -- Other --> F["buffer.match(OperatorNode.symbolOperators())"]
	F --> G{null?}
	G -- Y --> H["push(char)"]
	G -- N --> Z{what is it?}
	Z -- quote --> J["append(strbuild) clear() unget(line)"]
	J --> P["readString(buffer)"] 
	Z -- "open group"
	 --> AC["append(tokenize(buffer).with(char))"]
	Z -- "close group" --> BA["append(strbuild) unget(line)"]
	--> BB[return tokens]
	Z -- dot --> K{"is strbuild[0] a number?"}
	K -- Y --> L["strbuild.push(char)"]
	K -- N --> I
	Z -- other --> I["append(strbuild) append(operatorSymbol) clear()"]

AP["readString(buffer)"] --> Q{escape char?}
	Q -- Y --> R{what is it?}
	R -- "open parneth" --> T["unget(line) append(tokenize(buffer))"]
	R -- "quote, b-slash" --> S
	Q -- N --> S["push(char)"]
end
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTQ3NjM3OTIzMiw4NzU2Mzc5ODUsMTI0NT
g2NzcwNywxMTAzNDgxMzY2LC02MjkxOTI3NTIsLTEwMDY5MjIx
NTUsLTE2ODc3MDQxNDUsNDc4MTAzNTk3LC01MjU5NDc2NiwtNj
cxODg3MDk4LDEzNTk3ODMzNiwxNTU2NTY2OTk2LC05NzA1Mzk1
NTAsMzA5OTY5MjAyLC0xNDA3MTQ1MDYsNjM5NTEwNjAzLDE5Mj
AzMTA3MzMsNjM2NjYyMzIyLDM4MjE4MjQ0OSwtNTQ2NzUyNTk5
XX0=
-->