import React from "react";

export default function Code({
  className,
  colored,
  blocked,
  children,
  ...props
}) {
  const code =
    typeof children === "string"
      ? children
      : children
          .map((child) => (typeof child === "string" ? child : "\n"))
          .join("");
  console.log("code:", code);
  return blocked ? (
    <div
      {...props}
      className={`p-3 m-3 bg-slate-100 rounded-lg tracking-wide font-mono text-md ${className}`}
    >
      <ProcessedCode code={code} colored={colored} blocked></ProcessedCode>
    </div>
  ) : (
    <p
      {...props}
      className={`inline-block tracking-wide font-mono text-sm ${className}`}
    >
      <ProcessedCode code={code} colored={colored}></ProcessedCode>
    </p>
  );
}

const keywords = new Set([
  "if",
  "else",
  "any",
  "all",
  "while",
  "repeat",
  "for",
  "in",
  "it",
  "var",
  "break",
  "continue",
  "with",
  "then",
  "return",
]);
const types = new Set([
  "bool",
  "true",
  "false",
  "int",
  "float",
  "string",
  "char",
  "io",
]);

function ProcessedCode({ code, blocked, colored = true }) {
  let lineNumber = 0;
  function LineNumberLabel({ line }) {
    return (
      <span className="inline-block w-[30px] font-serif border-r-[1px] border-black mr-[3px] text-black">
        {line}
      </span>
    );
  }
  function processStandard(code) {
    const parts = code.split(/(\s+|[^\w\s]+)/g);
    return parts.filter(Boolean).map((part) => {
      if (keywords.has(part))
        return <span className="text-[#23E]">{part}</span>;
      if (types.has(part)) return <span className="text-[#93d]">{part}</span>;
      if (/[^\w\s]+/g.test(part))
        return <span className="text-[#a12]">{part}</span>;
      if (!isNaN(part)) return <span className="text-[#c1a]">{part}</span>;
      return part;
    });
  }
  function processLines(code, isString = false) {
    console.log({ code });
    let finalParts = [];
    let start = 0;
    let end = 0;

    function push(last = false, value = undefined) {
      if (!value)
        finalParts.push(
          isString
            ? code.slice(start, end)
            : processStandard(code.slice(start, end))
        );
      else finalParts.push(value);
      start = end + 1;

      if (!last && blocked) {
        finalParts.push(<br />);
        finalParts.push(<LineNumberLabel line={++lineNumber} />);
      }
    }

    while (end < code.length) {
      if (code.charAt(end) === "\n") {
        push();
      } else if (code.charAt(end) === "\\" && code.charAt(end + 1) === "\\") {
        push(true);
        while (end < code.length && code.charAt(end) !== "\n") ++end;
        const comment = code.slice(start, end);
        push(
          end === code.length,
          <span className="text-[#AAA]">\{comment}</span>
        );
      }
      ++end;
    }
    push(true);
    return finalParts;
  }
  function processString(code) {
    const parts = code.split(/(?<!\\)"/g);
    let finalParts = [];
    parts.forEach((part, i) =>
      i % 2 === 0
        ? (finalParts = finalParts.concat(processLines(part)))
        : finalParts.push(
            <span className="text-[#1a2]">"{processLines(part, true)}"</span>
          )
    );
    return finalParts;
  }
  return (
    <>
      {blocked && <LineNumberLabel line={++lineNumber} />}
      {(colored ? processString(code) : processLines(code, true)).map(
        (part, i) => (
          <React.Fragment key={i}>{part}</React.Fragment>
        )
      )}
    </>
  );
}