import Title from "../components/Title";
import Code from "../components/Code";
import Link from "../components/Link";
import FineDetails from "./FineDetails";
//TODO show parent's fields

export default function TypeDetails({
  name,
  aliases,
  parents,
  version,
  seeAlso,
  description,
  requirements,
  notes,
  fields,
}) {
  return (
    <div className="relative">
      <FineDetails
        version={version}
        seeAlso={seeAlso}
        requirements={requirements}
        notes={notes}
        page="/docs/types"
      />
      <Title>{name}</Title>
      {fields ? (
        <Code blocked>
          {aliases ? `\\\\also ${aliases.join(", ")}\n` : ""}
          {name}
          {" {\n"}
          {parents?.map((parent) => "\t..." + parent + "\n").join("") || ""}
          {expandFields(fields)}
          {"}"}
        </Code>
      ) : (
        <Code blocked>
          {name} \\also {aliases.join()}
        </Code>
      )}
      <Link>
        <small>see here for an explaination of our example conventions</small>
      </Link>
    </div>
  );
}

function expandFields(obj, indent = 1) {
  const keys = Object.keys(obj);
  let pad = "\t".repeat(indent);
  let ret = [];
  for (const key of keys) {
    ret.push(pad + key);
    if (typeof obj[key] === "string") {
      ret.push(" = " + obj[key] + "\n");
    } else {
      ret.push(" {\n" + expandFields(obj[key], indent + 1) + pad + "}\n");
    }
  }
  return ret.join("");
}
