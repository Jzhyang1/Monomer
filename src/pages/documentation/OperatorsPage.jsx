import Title from "../../components/Title";
import Link from "../../components/Link";
import List from "../../components/List";
import Code from "../../components/Code";
import Box from "../../components/Box";
import { useCallback, useContext, useEffect, useState } from "react";
import { ThemeContext } from "../../contexts";

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
    description: `Takes the sum of two values in a way that preserves aspects of both values without respect to the order they are added. 
    Will be undone by subtract, repeated add will result in multiply.`,
    precedence: 17,
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
    description: `Takes the difference of two values by undoing add.`,
    precedence: 17,
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
    description: `Takes the product of two values in a way that preserves aspects of both values without respect to the order they are multiplied. 
      Will be undone by divide, repeated add will result in power.`,
    precedence: 14,
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
    description: `Takes the quotient of two values by undoing multiply.`,
    precedence: 14,
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
    description: `Takes the modulus of a number by another. 
    The result will always be positive, and is equal to the remainder for division for positive numbers.`,
    precedence: 14,
  },
  {
    name: "Cross",
    symbol: "><",
    isBinary: true,
    listOps: true,
    version: "1.0.0",
    seeAlso: ["Multiply", "Concat"],
    requirements: "none",
    notes: "none",
    description: `Creates a matrix from two lists consisting of the pairwise products of the elements.`,
    precedence: 15,
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
    description: `takes the harmonic sum of two values. 
    Commonly known to take the product and divided by the sum of two values, but not necessary.`,
    precedence: 16,
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
    description: `Takes the exponent of the first value to the second. 
    Will be undone by root.`,
    precedence: 14,
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
    description: `Undoes power.`,
    precedence: 14,
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
    description: `Determines if a value is considered "truthy" 
      (whether for a non-boolean to be considered true or false).`,
    precedence: 0,
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
    description: `Takes the logical inverse.`,
    precedence: 0,
  },
  {
    name: "And",
    symbol: "&",
    isBinary: true,
    boolOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "Not", "Nand", "Or", "Nor", "Xor", "Xnor"],
    requirements: "none",
    notes: "And will short to false if the first value is false",
    description: `Returns whether both values is true.`,
    precedence: 8,
  },
  {
    name: "Nand",
    symbol: "!&",
    isBinary: true,
    boolOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "Not", "And", "Or", "Nor", "Xor", "Xnor"],
    requirements: "none",
    notes: "Nand will short to true if the first operator is true",
    description: `Returns whether both values is false.`,
    precedence: 8,
  },
  {
    name: "Or",
    symbol: "|",
    isBinary: true,
    boolOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "Not", "And", "Nand", "Nor", "Xor", "Xnor"],
    requirements: "none",
    notes: "Or will short to true if the first operator is true",
    description: `Returns whether one or both values is true.`,
    precedence: 9,
  },
  {
    name: "Nor",
    symbol: "!|",
    isBinary: true,
    boolOps: true,
    version: "1.0.0",
    seeAlso: ["Is", "Not", "And", "Nand", "Or", "Xor", "Xnor"],
    requirements: "none",
    notes: "Nor will short to false if the first operator is true",
    description: `Returns whether both values is false.`,
    precedence: 9,
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
    description: `Returns if exactly one of the two values is true.`,
    precedence: 9,
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
    description: `Returns if both of the values are true or both of the values are false.`,
    precedence: 9,
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
    numOps: true,
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
    precedence: 22,
  },
  {
    name: "Not-Equal",
    symbol: "!=",
    isChained: true,
    mixWith: [
      ["<", "<=", "=="],
      [">", ">=", "=="],
    ],
    numOps: true,
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
    precedence: 22,
  },
  {
    name: "Greater",
    symbol: ">",
    isChained: true,
    mixWith: [">=", "=="],
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Equals", "Lesser", "Greater-Equal", "Compare"],
    requirements: "none",
    notes: "none",
    precedence: 22,
  },
  {
    name: "Lesser",
    symbol: "<",
    isChained: true,
    mixWith: ["<=", "=="],
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Equals", "Greater", "Greater-Equal", "Compare"],
    requirements: "none",
    notes: "none",
    precedence: 22,
  },
  {
    name: "Greater-Equal",
    symbol: ">=",
    isChained: true,
    mixWith: [">", "=="],
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Equals", "Greater", "Lesser-Equal", "Compare"],
    requirements: "none",
    notes: "none",
    precedence: 22,
  },
  {
    name: "Lesser-Equal",
    symbol: "<=",
    isChained: true,
    mixWith: ["<", "=="],
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Equals", "Lesser", "Greater-Equal", "Compare"],
    requirements: "none",
    notes: "none",
    precedence: 22,
  },
  {
    name: "Compare",
    symbol: "?=",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["Equals", "Greater", "Lesser"],
    requirements: "none",
    notes:
      "Compare, by default, tests the Greater operator, then the Lesser operator",
    precedence: 21,
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
    precedence: 28,
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
    precedence: 20,
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
    precedence: 19,
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
    precedence: 20,
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
    precedence: 8,
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
    precedence: 27,
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
    precedence: 18,
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
    precedence: 25,
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
    precedence: 25,
  },
];

export const operators = [
  //Arithmetic
  ...arithmeticOps.map((e) => ({ ...e, category: "Arithmetic" })),
  //Boolean
  ...booleanOps.map((e) => ({ ...e, category: "Boolean" })),
  //Comparison
  ...comparisonOps.map((e) => ({ ...e, category: "Comparison" })),
  //Assignment
  ...assignmentOps.map((e) => ({ ...e, category: "Assignment" })),
  //Type
  ...typeOps.map((e) => ({ ...e, category: "Type" })),
  //List
  ...listOps.map((e) => ({ ...e, category: "List" })),
];

export default function OperatorsPage() {
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
      <div className="m-20">
        <PrecedenceTable />
      </div>
    </>
  );
}

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

function PrecedenceTable() {
  const { isDarkMode } = useContext(ThemeContext);
  const [order, setOrder] = useState(1);
  const [ordered, setOrdered] = useState(operators);

  const toggleOrder = useCallback(() => {
    if (order + 1 === 2)
      setOrdered(
        ordered.sort(
          (a, b) =>
            a.category.localeCompare(b.category) * 1000 +
            (a.precedence - b.precedence)
        )
      );
    else setOrdered(ordered.sort((a, b) => a.precedence - b.precedence));
    setOrder((order + 1) % 3);
  }, [order, setOrder, ordered, setOrdered]);

  useEffect(toggleOrder, []);

  const isFirstCategory = (e, i) =>
    order === 2 && (i === 0 || ordered[i - 1].category !== e.category);
  const countCategory = (category) =>
    ordered.reduce((c, t) => c + (t.category === category), 0);

  return (
    <div className="w-full md:w-[700px]">
      <div className="w-full flex justify-between">
        <span className="font-light text-lg m-3">Order of Operations</span>
        <button
          onClick={toggleOrder}
          className="rounded-lg text-sm px-2 py-1 m-3 border-[1px]"
          style={{
            background: isDarkMode ? "#002D62" : "#FFF6EF",
            borderColor: isDarkMode ? "white" : "#DEDEDE",
          }}
        >
          reorder
        </button>
      </div>
      <table className="table-fixed w-full text-center text-sm font-light text-surface dark:text-white">
        <tbody>
          {ordered.map((e, i) => (
            <tr
              key={i}
              style={{
                borderTop: isFirstCategory(e, i)
                  ? "double"
                  : "1px solid #DEDEDE",
                backgroundColor:
                  i % 2 === 0 ? (isDarkMode ? "#171058" : "#FFF6EF") : "",
              }}
            >
              {isFirstCategory(e, i) && (
                <td rowSpan={countCategory(e.category)}>{e.category}</td>
              )}
              <OperatorSummary order={order} {...e} />
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

//order: 0=name first, 1=symbol first, 2=category first(but not handled by this function) and include precedence
function OperatorSummary({
  name,
  symbol,
  category,
  isBinary,
  isChained,
  isPrefix,
  isSuffix,
  precedence,
  order,
}) {
  const representation = (
    <Link href={`/docs/operators/${name}`}>
      {isBinary || isChained ? (
        <Code>_value {symbol} _value</Code>
      ) : isPrefix ? (
        <Code>{symbol} _value</Code>
      ) : isSuffix ? (
        <Code>_value {symbol}</Code>
      ) : (
        symbol
      )}
    </Link>
  );
  const linkName = <Link href={`/docs/operators/${name}`}>{name}</Link>;
  const ordered =
    order === 0
      ? [linkName, representation, category]
      : order === 1
      ? [representation, linkName, category]
      : order === 2
      ? [linkName, representation, precedence]
      : [];

  return ordered.map((e, i) => <td key={i}>{e}</td>);
}
