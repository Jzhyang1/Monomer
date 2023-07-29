import Title from "../components/Title";
import Code from "../components/Code";
import Link from "../components/Link";
import FineDetails from "./FineDetails";

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
    ? "_col"
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
    ? "_list"
    : setOps
    ? "_set"
    : mapOps
    ? "_map"
    : "_x";

  return (
    <div className="relative">
      <FineDetails
        version={version}
        seeAlso={seeAlso}
        requirements={requirements}
        notes={notes}
      />
      <Title>{name}</Title>
      Structure:
      <Code blocked>
        {example ||
          [
            isPrefix && `${symbol}${ops}`,
            isPrefix && isSuffix && <br />,
            isSuffix && `${ops}${symbol}`,
            isSuffix && isBinary && <br />,
            (isBinary || isChained) && `${ops} ${symbol} ${ops}`,
            isChained && <br />,
            isChained && `${ops} ${symbol} ${ops} ${symbol} ...`,
          ].filter(Boolean)}
      </Code>
      <Link>
        <small>see here for an explaination of our example conventions</small>
      </Link>
      <div>{description}</div>
    </div>
  );
}
