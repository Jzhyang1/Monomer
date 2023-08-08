import Title from "../../components/Title";
import Link from "../../components/Link";
import List from "../../components/List";
import Code from "../../components/Code";
import Box from "../../components/Box";

const arithmeticOps = [
  {
    name: "Add",
    symbol: "+",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Subtract", "Multiply", "Concat"],
    requirements: "none",
    notes: "Add is always commutitive. If concatenation is desired, see concat",
  },
  {
    name: "Subtract",
    symbol: "-",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Add", "Divide"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Multiply",
    symbol: "*",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Add", "Divide", "Cross", "Power"],
    requirements: "none",
    notes:
      "Multiply is always commutative, if a non-commutative multiply is desired, use Cross",
  },
  {
    name: "Divide",
    symbol: "/",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Subtract", "Multiply", "Root"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Modulo",
    symbol: "%",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Divide"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Cross",
    symbol: "><",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Multiply", "Concat"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Parallel",
    symbol: "||",
    isBinary: true,
    isChained: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Multiply", "Divide"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Power",
    symbol: "**",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Multiply", "Root"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Root",
    symbol: "*/",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Divide", "Power"],
    requirements: "none",
    notes: "none",
  },
];

const booleanOps = [
  {
    name: "Is",
    symbol: "?",
    isPrefix: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Not", "And", "Nand", "Or", "Nor", "Xor", "Xnor"],
    requirements: "none",
    notes:
      "Is will yield a bool value of whether x is considered true. Is will require overloading",
  },
  {
    name: "Not",
    symbol: "!",
    isPrefix: true,
    boolOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "And", "Nand", "Or", "Nor", "Xor", "Xnor"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "And",
    symbol: "&",
    isBinary: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "Not", "Nand", "Or", "Nor", "Xor", "Xnor"],
    requirements: "none",
    notes:
      "And will short to false if the first operator yields false with the Is operator",
  },
  {
    name: "Nand",
    symbol: "!&",
    isBinary: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "Not", "And", "Or", "Nor", "Xor", "Xnor"],
    requirements: "none",
    notes:
      "Nand will short to true if the first operator yields true with the Is operator",
  },
  {
    name: "Or",
    symbol: "|",
    isBinary: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "Not", "And", "Nand", "Nor", "Xor", "Xnor"],
    requirements: "none",
    notes:
      "Or will short to true if the first operator yields true with the Is operator",
  },
  {
    name: "Nor",
    symbol: "!|",
    isBinary: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "Not", "And", "Nand", "Or", "Xor", "Xnor"],
    requirements: "none",
    notes:
      "Nor will short to false if the first operator yields true with the Is operator",
  },
  {
    name: "Xor",
    symbol: "^",
    isBinary: true,
    boolOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "Not", "And", "Nand", "Or", "Xor", "Xnor"],
    requirements: "none",
    notes: "Xor will not short",
  },
  {
    name: "Xnor",
    symbol: "!^",
    isBinary: true,
    boolOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "Not", "And", "Nand", "Or", "Nor", "Xor"],
    requirements: "none",
    notes: "Xnor will not short",
  },
];

const comparisonOps = [
  {
    name: "Equals",
    symbol: "==",
    mixWith: [
      ["<", "<=", "!="],
      [">", ">=", "!="],
    ],
    isChained: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: [
      "Not-Equal",
      "Greater",
      "Lesser",
      "Greater-Equal",
      "Lesser-Equal",
      "Compare",
    ],
    requirements: "none",
    notes:
      "Equals defaults to field-by-field comparison. Other uses will require overloading",
  },
  {
    name: "Not-Equal",
    symbol: "!=",
    isChained: true,
    mixWith: [
      ["<", "<=", "=="],
      [">", ">=", "=="],
    ],
    anyOps: true,
    version: "1.0.0",
    seeAlso: [
      "Equals",
      "Greater",
      "Lesser",
      "Greater-Equal",
      "Lesser-Equal",
      "Compare",
    ],
    requirements: "none",
    notes:
      "Not equals defaults to negating the result of Equals. Other uses will require overloading",
  },
  {
    name: "Greater",
    symbol: ">",
    isChained: true,
    mixWith: [">=", "=="],
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Equals", "Lesser", "Greater-Equal", "Compare"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Lesser",
    symbol: "<",
    isChained: true,
    mixWith: ["<=", "=="],
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Equals", "Greater", "Greater-Equal", "Compare"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Greater-Equal",
    symbol: ">=",
    isChained: true,
    mixWith: [">", "=="],
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Equals", "Greater", "Lesser-Equal", "Compare"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Lesser-Equal",
    symbol: "<=",
    isChained: true,
    mixWith: ["<", "=="],
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Equals", "Lesser", "Greater-Equal", "Compare"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Compare",
    symbol: "?=",
    isBinary: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Equals", "Greater", "Lesser"],
    requirements: "none",
    notes:
      "Compare, by default, tests the Greater operator, then the Lesser operator",
  },
];

const assignmentOps = [
  {
    name: "Assign",
    symbol: "=",
    example: (
      <Code blocked>
        _name {"="} _x
        <br />
        _name {"="} _name {"="} ... {"="} _x
      </Code>
    ),
    isChained: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Equals"],
    requirements: "none",
    notes: "none",
  },
];

const typeOps = [
  {
    name: "Convert-to",
    symbol: "to",
    example: <Code blocked>_x to _type</Code>,
    isBinary: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Convert-from", "Cast"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Convert-from",
    symbol: ":",
    example: <Code blocked>_type : _x</Code>,
    isBinary: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Convert-to", "Cast"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Cast",
    symbol: "as",
    example: <Code blocked>_x as _type</Code>,
    isBinary: true,
    anyOps: true,
    version: "1.0.0",
    seeAlso: ["Convert-to", "Convert-from"],
    requirements: "none",
    notes: "none",
  },
];

const listOps = [
  {
    name: "Size",
    symbol: "#",
    isPrefix: true,
    collectionOps: true,
    version: "1.0.0",
    seeAlso: [],
    requirements: "none",
    notes: "none",
  },
  {
    name: "In",
    symbol: "in",
    example: <Code blocked>_x in [_col]</Code>,
    isBinary: true,
    collectionOps: true,
    version: "1.0.0",
    seeAlso: [],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Concat",
    symbol: ".",
    isChained: true,
    collectionOps: true,
    version: "1.0.0",
    seeAlso: ["Range", "Spread"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Range",
    symbol: "...",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Spread", "Concat"],
    requirements: "none",
    notes: "none",
  },
  {
    name: "Spread",
    symbol: "...",
    isPrefix: true,
    collectionOps: true,
    version: "1.0.0",
    seeAlso: ["Range", "Concat"],
    requirements: "none",
    notes: "none",
  },
];

export const operators = [
  //Arithmetic
  ...arithmeticOps,
  //Boolean
  ...booleanOps,
  //Comparison
  ...comparisonOps,
  //Assignment
  ...assignmentOps,
  //Type
  ...typeOps,
  //List
  ...listOps,
];

export default function OperatorsPage() {
  const PageBox = ({ title, ops }) => {
    return (
      <Box title={title} className="min-w-[150px] text-lg">
        <List className="text-sm">
          {ops.map((op, i) => (
            <Link href={`/docs/operators/${op.name}`} key={i}>
              {op.name} (<Code>{op.symbol}</Code>)
            </Link>
          ))}
        </List>
      </Box>
    );
  };

  return (
    <>
      <Title>Operators</Title>
      <div className="flex flex-wrap gap-[10px] p-[20px]">
        <PageBox title="Arithmetic" ops={arithmeticOps} />
        <PageBox title="Bool" ops={booleanOps} />
        <PageBox title="Comparison" ops={comparisonOps} />
        <PageBox title="Assignment" ops={assignmentOps} />
        <PageBox title="Type" ops={typeOps} />
        <PageBox title="List" ops={listOps} />
      </div>
    </>
  );
}
