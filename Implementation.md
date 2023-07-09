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

[Full UML](UMLs/FullUML)
[SymtaxT](UMLs/FullUML)

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
eyJoaXN0b3J5IjpbMTU2MTMzMzA1NywtNDc2Mzc5MjMyLDg3NT
YzNzk4NSwxMjQ1ODY3NzA3LDExMDM0ODEzNjYsLTYyOTE5Mjc1
MiwtMTAwNjkyMjE1NSwtMTY4NzcwNDE0NSw0NzgxMDM1OTcsLT
UyNTk0NzY2LC02NzE4ODcwOTgsMTM1OTc4MzM2LDE1NTY1NjY5
OTYsLTk3MDUzOTU1MCwzMDk5NjkyMDIsLTE0MDcxNDUwNiw2Mz
k1MTA2MDMsMTkyMDMxMDczMyw2MzY2NjIzMjIsMzgyMTgyNDQ5
XX0=
-->