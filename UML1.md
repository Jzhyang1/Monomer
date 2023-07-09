


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

class 
```
<!--stackedit_data:
eyJoaXN0b3J5IjpbOTk3NjUzMjk5XX0=
-->