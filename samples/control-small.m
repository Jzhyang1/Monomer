if nextStarting > starting:
	tokens add(tokenize(source))
else nextStarting < starting:
	return tokens
else:
	line skipSpaces()
