import { useState } from "react";
import OperatorDetails from "../../custom_components/OperatorDetails";

const operators = [
  {
    name: "Add",
    symbol: "+",
    isBinary: true,
    numOps: true,
    version: "1.0.0",
    seeAlso: ["subtract", "multiply", "concat"],
    requirements: "none",
    notes: "Add is always commutitive. If concatenation is desired, see concat",
  },
];

export default function OperatorsPage() {
  const [op, setOp] = useState(operators[0]); //TODO useState(undefined) and have a home

  return <OperatorDetails {...op}></OperatorDetails>;
}
