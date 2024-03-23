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
    condition: "_bool",
    description:
      "performs and returns a block if 1) no previous condition is satisfied and 2) either the current condition is satisfied or no condition is specified",
  },
  {
    name: "any",
    condition: "_bool",
    description:
      "performs and returns a block if 1) any previous condition is satisfied and 2) either the current condition is satisfied or no condition is specified",
  },
  {
    name: "all",
    condition: "_bool",
    description:
      "performs and returns a block if 1) all previous condition is satisfied and 2) either the current condition is satisfied or no condition is specified",
  },
  {
    name: "repeat",
    condition: "_n",
    description:
      "performs a block a fixed number of times or, if not specified, until broken out of",
  },
  {
    name: "while",
    condition: "_bool",
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

export default function ControlsPage() {
  return (
    <>
      <Title>Controls</Title>
      <List className="text-lg font-thin my-5">
        {controls.map((c, i) => (
          <Link href={`/docs/controls/${c.name}`} key={i}>
            {c.name}
          </Link>
        ))}
      </List>
    </>
  );
}
