


note for Node "getVariableKey will default to null. interpretVariable and compileMemory will default to error. compileMemory is the location of a variable while compileValue can also specify calculations, constant value or register location"

```mermaid
classDiagram

class Node{
	#static_enum: Usage  
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
	/+ interpretValue() InterpretValue/  
	+ compileMemory() CompileMemory  
	/+ compileValue() CompileValue/  
	/+ compileSize() CompileSize/
}
class Usage_Node
<<Enumeration>> Usage

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
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE3MTA1NzIzNzFdfQ==
-->