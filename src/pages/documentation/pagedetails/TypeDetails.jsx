import Title from "../../../components/Title";
import Code from "../../../components/Code";
import Link from "../../../components/Link";
import Tabs from "../../../components/Tabs";
import FineDetails from "../../../custom_components/FineDetails";
//TODO show parent's fields

export default function TypeDetails({
  name,
  aliases,
  literals,
  parents,
  version,
  seeAlso,
  description,
  literalDescription,
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
      <Tabs
        pages={literals ? ["Type", "Literal"] : ["Type"]}
        background={"#E2E8F0"}
      >
        <>
          <div>
            {fields ? (
              <Code blocked>
                {aliases ? `\\\\also ${aliases.join(", ")}\n` : ""}
                {name}
                {" {\n"}
                {parents?.map((parent) => "\t..." + parent + "\n").join("") ||
                  ""}
                {expandFields(fields)}
                {"}"}
              </Code>
            ) : (
              <Code blocked>
                {name} {aliases ? `\\\\also ${aliases.join(", ")}` : ""}
              </Code>
            )}
            <Link className="text-slate-400">
              <small>
                see here for an explaination of our example conventions
              </small>
            </Link>
          </div>
          <div>{description}</div>
        </>
        {literals && (
          <>
            <Code blocked>{literals.join("\n")}</Code>
            <div>{literalDescription}</div>
          </>
        )}
      </Tabs>
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
