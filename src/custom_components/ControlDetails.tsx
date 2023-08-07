import Title from "../components/Title";
import Code from "../components/Code";
import Link from "../components/Link";
import FineDetails from "./FineDetails";
import React from "react";
export default function ControlDetails({
  name,
  condition,
  body,
  version,
  seeAlso = ["else", "any", "all", "while", "repeat", "for"],
  description,
  requirements,
  notes,
}) {
  return (
    <div className="relative">
      <FineDetails
        version={version}
        seeAlso={seeAlso}
        requirements={requirements}
        notes={notes}
        page="/docs/controls"
      />
      <Title>{name}</Title>
      <div>
        {
          <Code blocked>
            {name} {condition}
            {" :\n"}
            {body ? body.map((line) => "\t" + line).join("\n") : "\t_body `..."}
          </Code>
        }
        <Link className="text-slate-400">
          <small>see here for an explaination of our example conventions</small>
        </Link>
      </div>
      <div>{description}</div>
    </div>
  );
}
