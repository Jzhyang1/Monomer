if (nextStarting > starting {
	tokens.add(tokenize(source));
} else if (nextStarting < starting) {
	return tokens;
} else {
	line.skipSpaces();
}