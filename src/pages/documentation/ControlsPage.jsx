import Title from "../../components/Title";
import Link from "../../components/Link";
import List from "../../components/List";

export const controls = [
  {
    name: "if",
    condition: "_bool",
    description: "performs and returns a block if a condition is satisfied",
  },
  {
    name: "else",
    condition: "__bool",
    description:
      "performs and returns a block if 1) no previous condition is satisfied and 2) either the current condition is satisfied or no condition is specified",
  },
  {
    name: "any",
    condition: "__bool",
    description:
      "performs and returns a block if 1) any previous condition is satisfied and 2) either the current condition is satisfied or no condition is specified",
  },
  {
    name: "all",
    condition: "__bool",
    description:
      "performs and returns a block if 1) all previous condition is satisfied and 2) either the current condition is satisfied or no condition is specified",
  },
  {
    name: "repeat",
    condition: "__int",
    description:
      "performs a block a fixed number of times or, if not specified, until broken out of",
  },
  {
    name: "while",
    condition: "__bool",
    description:
      "performs a block until the condition is false and returns a list",
  },
  {
    name: "for",
    condition: "_x in `[_col`]",
    description:
      "performs a block for every element in a collection and returns a list",
  },
  {
    name: "switch",
    condition: "_x",
    body: ["if _x : `...", "`..."],
    description: "performs a block depending on the value of the switch",
  },
];

export default function TypesPage() {
  return (
    <>
      <Title>Types</Title>
      This is the page for types
      <List>
        {controls.map((c, i) => (
          <Link href={`/docs/controls/${c.name}`} key={i}>
            {c.name}
          </Link>
        ))}
      </List>
    </>
  );
}
