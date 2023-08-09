\\is and not

?2      \\true
?true   \\true
!true   \\false
!?"hi"  \\false


\\and

true & false    \\false
false & true    \\false
false & @true   \\false, prints nothing
true & @true    \\true, prints true


\\nand

true !& true    \\false
true !& false   \\true
false !& @true  \\true, prints nothing
true !& @true   \\false, prints true


\\or

false | true    \\true
true | true     \\true
false | @false  \\false, prints false
true | @false   \\true, prints nothing


\\nor

false !| true   \\false
true !| true    \\false
false !| @false \\true, prints false
true !| @false  \\false, prints nothing


\\xor

false ^ true    \\true
true ^ true     \\false
false ^ @false  \\false, prints false
true ^ @false   \\true, prints false


\\xnor

false !^ true   \\false
true !^ true    \\true
false !^ @false \\true, prints false
true !^ @false  \\false, prints false

