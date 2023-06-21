# Syntax

The syntax of Monomer includes 6 main structural types:

 1. Basic Literals
 2. Operators
 3. Variables
 4. Groups
 5. Structured Values 
 6. Comments

## Basic Literals

Monomer has 10 built-in literal constructions:

 1. int
 2. float
 3. char
 4. string
 5. list
 6. set
 7. map
 8. range
 9. enum
 10. tuple 

### int

Ints are any integer number without preceding, succeeding, nor intermittent characters (ie no commas, no dots) except when representing hexadecimal, octal, and binary format integers, which are preceded with prefixes *0x*, *0o*, and *0b*, respectively. 
In the case of hexadecimal literals, the digits for [10...15] are denoted by [A...F], case insensitive. 
Negative integers are created by inverting an integer literal with the negative operator (`-`).
Valid int literals include

    0
    123
    
    0xAB123
    0o123
    0b101


Invalid int literals include

    .99
    1e7
    2.998e8
    
    1,000
    
    0xH99
    0x.99

> The first 3 cases are not int literals, but float literals. 
> *1,000* is invalid because it has a comma. 
> *0xH99* is invalid because *H* is not a hexadecimal digit (the same restrictions also apply to octal and binary numbers)
> *0x.99* is invalid because it implies a float. Additionally, hexadecimal numbers can not be floats.

### float

Float literals are used to store fractional values. They are numbers that also contain a decimal point (`.`) and/or the letter `E`, which indicates scientific notation (`1e3` is the same as `1x10^3`). 
The general form of the float literal is `[NUMBER1].[NUMBER2]e[NUMBER3]` where each number would be a valid integer literal on its own. One of `[NUMBER1]` or `[NUMBER2]` can be omitted, and the entire `e[NUMBER3]` can be omitted.
Non base-10 float literals are not supported. Valid float literals include

    .99
    1e7
    2.998e8

Invalid int literals include

    0x.99
    .0.1
    2e5.5
 

### char
### string

Strings are a collection of characters that form any length of symbols or words that are taken as-is. Strings are contained by quotation marks (`"..."`). 

Within a string, identifiers, expressions, and certain symbols can be *escaped*, ie they take on values that are different from if they are typed in directly. This is needed for some symbols such as the quotation mark (`"`) and the escape character (`\`) that would otherwise mean differently to the compiler. Escaped sequences occur as an escape character followed by either an identifier, a parenthetically enclosed expression, or either a quotation mark, an escape character, or a new line. Escaped identifiers and expressions will have their evaluated value converted to string and embedded into the string literal.

Valid string literals include

    "Hello, world!"
    
    "this is a \"string\""
    "¯\\_(ツ)_/¯"
    
    "$\(100+20*0.1) annually"
    "VV\tab^^"
    "Hi\(newline)Hello"			
    
> The value of the above lines, when displayed, will respectively be: 
> `Hello, world!`
> `this is a "string"`
> `¯\_(ツ)_/¯`
> `$102.0 annually`
> `VV	^^`
> and the last example will be (on two separate lines)
> `Hi`
> `Hello`
   
Invalid String literals include 

    "this is a "string""
    "¯\_(ツ)_/¯"
    
    "Hi\newlineHello"			

> The first example will have two strings: `"this is a "` and `""` sandwiching the identifier `string` rather than a single string with `"string"` in it. 
> The second example will have the sequence `\_` which implies an *escaped sequence*. 
> The last example will search for `newlineHello` as the identifier.

### list

    [1,2,3,4]

### set

    set{"no", "repeats", "and", "no", "order"}

### map

    map{"alex":1, "bill":2, "carl":3, "david":4}

### range

    [1...4]
    [1...5)
    (0...4]
    (0...5)
    
    [0...x)

> discrete ranges can by created by performing a `by` operation on the range. I.E. `[0...x) by 1` for every integer from 0 to x excluding x

### enum

    enum{a,b,c,d}
    enum{a=0,b,c,d}
    enum{a=9,b=49,c=121,d=225}

> In the first case, a="a", b="b", etc
> In the second case, a=0, b=1, c=2, d=3
> In the last case, each will be given their respective values

### tuple

    a,b,c
    (a,b,c)
    (a,(b,c))

## Operators
## Variables
## Groups

## Structured Values

## Comments

Lastly, to keep a program readable, comments are embedded with the program. Comments contain human-redable information, but evaluate to a space for computers. Comments are styled as follows, where elipsis (`...`) indicates any other code content (excluding comments), `word` indicates any identifier, `space` indicates a space, tab, or new line, and `symbol` indicates any recognized operator symbols (ie `+`, `||`, etc).

    ...\word...
    
    ...\space...
    
    ...\symbol...
    
    ...\\comment to the end of the line
    
    ...\"comment that can be embedded into one line,
	    or be expanded across several lines"\...

Comments are used in situations where code is not self-explanatory, or the beginning of the logic is separated by a lot of code, such as in the following cases.

    c = random(0x000000,0xffffff) \\random background color

> In this case, the use of the variable `c` is unclear, but the following comment clarifies it as a `\random background color`

    view{style = screen style}(
	    menu{style = menu style}(
		    ......
	    )\menu
	    view{style = body style}(
		    ......
	    )\view
    )\view

> In addition to indention, the `\view` and `\menu` comments clarify the function calls that the end parenthesis terminates, the same is also done for brackets and braces.


<!--stackedit_data:
eyJoaXN0b3J5IjpbLTgwOTg3ODY4NCwxMTIyNTI1OTUxXX0=
-->