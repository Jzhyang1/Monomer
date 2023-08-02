import Title from "../../components/Title";
import Link from "../../components/Link";
import List from "../../components/List";
import Code from "../../components/Code";

export const types = [
  {
    name: "int",
    aliases: ["i"],
    seeAlso: ["float", "number", "char"],
    description: "stores at least a 64-bit integer",
    requirements: "a 32-bit system or above",
    notes: "overflow raises an error",
  },
  {
    name: "string",
    aliases: ["s", "str"],
    seeAlso: ["char", "list"],
    parents: ["list"],
    description: "stores a list of characters",
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
];

export default function TypesPage() {
  return (
    <>
      <Title>Types</Title>
      This is the page for types
      <List>
        {types.map((t, i) => (
          <Link href={`/docs/types/${t.name}`} key={i}>
            {t.name} ({t.aliases.join()})
          </Link>
        ))}
      </List>
    </>
  );
}
