import Title from "../components/Title";
import Code from "../components/Code";
import Link from "../components/Link";
import FineDetails from "./FineDetails";
import React from "react";

export default function OperatorDetails({
  name,
  version,
  seeAlso,
  requirements,
  notes,
  description,
  example,

  //if example is provided, the following do not need to be provided
  symbol,
  isBinary,
  isPrefix,
  isSuffix,
  isChained,
  mixWith,
  intOps,
  doubleOps,
  numOps,
  charOps,
  strOps,
  boolOps,
  listOps,
  setOps,
  mapOps,
  collectionOps,
}) {
  let ops = collectionOps
    ? "`[_col`]"
    : numOps
    ? "_n"
    : boolOps
    ? "_bool"
    : strOps
    ? "_s"
    : charOps
    ? "_c"
    : intOps
    ? "_i"
    : doubleOps
    ? "_d"
    : listOps
    ? "`[_list`]"
    : setOps
    ? "`{_set`}"
    : mapOps
    ? "`{_map`}"
    : "_x";

  let mixWithLines = !mixWith
    ? []
    : Array.isArray(mixWith[0])
    ? mixWith.reduce(
        (prev, mixWith, i) => [
          ...prev,
          i > 0 ? "\n\n" : undefined,
          ...mix({ ops, symbol, mixWith }),
        ],
        []
      )
    : mix({ ops, symbol, mixWith });

  return (
    <div className="relative">
      <FineDetails
        version={version}
        seeAlso={seeAlso}
        requirements={requirements}
        notes={notes}
        page="/docs/operators"
      />
      <Title>{name}</Title>
      Structure:
      {example || (
        <Code blocked>
          {[
            isPrefix && `${symbol} ${ops}`,
            isPrefix && isSuffix && "\n",
            isSuffix && `${ops} ${symbol}`,
            isSuffix && isBinary && "\n",
            (isBinary || isChained) && `${ops} ${symbol} ${ops}`,
            isChained && "\n",
            isChained && `${ops} ${symbol} ${ops} ${symbol} \`...`,
            mixWith && "\n\n",
            ...mixWithLines,
          ].filter(Boolean)}
        </Code>
      )}
      <Link>
        <small>see here for an explaination of our example conventions</small>
      </Link>
      <div>{description}</div>
    </div>
  );
}

function mix({ ops, symbol, mixWith }) {
  let mixWithFirstLines: React.ReactNode[] = [];
  // let mixWithSecondLines = [];
  let mixWithBetweenLines: React.ReactNode[] = [];
  mixWith.forEach((symbol2, i) => {
    mixWithFirstLines.push(`${ops} ${symbol} ${ops} ${symbol2} \`...`);
    mixWithFirstLines.push(<br />);
    // mixWithSecondLines.push(`${ops} ${symbol2} ${ops} ${symbol} \`...`);
    // mixWithSecondLines.push(<br />);
    for (let j = i; j < mixWith.length; ++j) {
      const symbol3 = mixWith[j];
      mixWithBetweenLines.push(
        `${ops} ${symbol2} ${ops} ${symbol} ${ops} ${symbol3} \`...`
      );
      mixWithBetweenLines.push(<br />);
    }
  });
  return [
    ...mixWithFirstLines,
    // ...mixWithSecondLines,
    ...mixWithBetweenLines,
    "`...",
  ];
}
