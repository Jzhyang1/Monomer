import Title from "../../components/Title";
import Link from "../../components/Link";
import List from "../../components/List";
import React from "react";

export const types = [
  {
    name: "int",
    aliases: ["i"],
    literals: ["123", "0", "299792458"],
    seeAlso: ["float", "number", "char"],
    description: "stores at least a 64-bit integer",
    literalDescription: "an unbroken sequence of digits that form a number",
    requirements: "a 32-bit system or above",
    notes: "overflow raises an error",
  },
  {
    name: "float",
    aliases: ["f", "double"],
    literals: ["15.5", "0.", "3e8", "6.626e-34"],
    seeAlso: ["int", "number"],
    description: "stores at least a 64-bit float",
    literalDescription:
      "an unbroken sequence of digits that can contain a decimal and an exponent that together form a number",
    requirements: "a 32-bit system or above",
    notes: "overflow raises an error",
  },
  {
    name: "bool",
    aliases: ["boolean"],
    literals: ["true", "false"],
    seeAlso: ["int", "string"],
    description: "stores either true or false",
    literalDescription: "Either true or false",
  },
  {
    name: "char",
    aliases: ["c", "chr"],
    literals: ["'c'", "'0'", "'\\u2587'"],
    seeAlso: ["int", "number", "string"],
    description: "stores a unicode character",
    literalDescription:
      "a single character enclosed in single quotes. Escape sequences are considered single characters.",
    notes: "overflow raises an error",
  },
  {
    name: "string",
    aliases: ["s", "str"],
    literals: [
      '""',
      '"Hello, world!"',
      '"\n\tHappiness can exist only in acceptance.\n\t\t-George Orwell\n"',
    ],
    seeAlso: ["char", "list"],
    parents: ["list"],
    description: "stores a list of characters",
    literalDescription:
      "a sequence of characters contained within double quotes. If meant to extend multiple lines, " +
      "the quotes are separated from the body of the string. Escape sequences are considered characters.",
    notes: "unlike in other languages, strings are mutable",
    fields: {
      pad: {
        start: "(int){_self = string}",
        end: "(int){_self = string}",
        to: "{_start = bool, _end = bool}(int){_self = string}",
      },
    },
  },
  {
    name: "list",
    aliases: ["array"],
    literals: ["[_x]"],
    seeAlso: ["string", "set"],
    description: "stores a flexible list of items with the same type",
    notes: "attempting to index outside of the list raises an error",
    fields: {
      length: "int",
      last: "_x",
      push: "(_x1){_x1}",
      pop: "(){_x}",
    },
  },
  {
    name: "set",
    seeAlso: ["map", "list"],
    literals: ["set{_x}"],
    description: "stores a flexible set of items with the same type",
    notes:
      "no indexing. Stores unique objects only. Generally used for testing if something is in a set.",
    fields: {
      length: "int",
      push: "(_x1){_x1}",
    },
  },
  {
    name: "map",
    aliases: ["dict", "dictionary"],
    seeAlso: ["set", "list"],
    literals: ["map{_x: _x}"],
    description:
      "stores a flexible map of items with the same type to items with the same time.",
    notes: "attempting to access an entry before entered raises an error.",
    fields: {
      length: "int",
    },
  },
];

export default function TypesPage() {
  return (
    <>
      <Title>Types</Title>
      This is the page for types
      <List>
        {types.map((t, i) => (
          <Link href={`/docs/types/${t.name}`} key={i}>
            {t.name} {t.aliases && `(${t.aliases.join(", ")})`}
          </Link>
        ))}
      </List>
    </>
  );
}
