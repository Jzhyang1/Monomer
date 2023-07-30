import React from "react";
//TODO replace all lines from being handled by <br> to <div>
//TODO replace all &emsp; with spaces/tabs in usages of Code

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
      <span className="select-none w-[30px] font-serif border-r-[1px] border-black mr-[15px] text-black">
        {line}
      </span>
    );
  }
  function processStandard(code) {
    const commentIndex = code.indexOf("\\\\");
    let comment = undefined;
    if (commentIndex >= 0) {
      comment = code.slice(commentIndex);
      code = code.slice(0, commentIndex);
    }
    const parts = code
      .split(/(\s+|[^_\w\s]+)/g)
      .filter(Boolean)
      .map((part) => {
        if (keywords.has(part))
          return <span className="text-[#23E]">{part}</span>;
        if (types.has(part)) return <span className="text-[#93d]">{part}</span>;
        if (part.startsWith("_") || part.startsWith("`"))
          return <span className="italic text-[#5a1f1f]">{part.slice(1)}</span>;
        if (/[^_\w\s]+/.test(part))
          return <span className="text-[#a12]">{part}</span>;
        if (/\s+/.test(part))
          return part.replace(" ", "\u2003").replace("\t", "\u2003".repeat(4));
        if (!isNaN(part)) return <span className="text-[#c1a]">{part}</span>;
        return part;
      });
    if (comment) parts.push(<span className="text-[#AAA]">{comment}</span>);
    return parts;
  }
  function processLines(code, isString = false) {
    let finalParts = [];
    let start = 0;
    let end = 0;

    function push() {
      const line = code.slice(start, end);
      console.log([...line]);
      // let leading = 0,
      //   count = 0;
      // while (true) {
      //   if (line.charAt(count) === " ") leading++;
      //   else if (line.charAt(count) === "\t") leading += 4;
      //   else break;
      //   ++count;
      // }

      const paddedLine = line; //"\u2003".repeat(leading) + line.slice(count);
      const addedPart = isString ? paddedLine : processStandard(paddedLine);
      start = end + 1;

      if (blocked) {
        finalParts.push(
          <div className="flex flex-row">
            <LineNumberLabel line={++lineNumber} />
            {addedPart}
          </div>
        );
      } else finalParts.push(addedPart);
    }

    while (end < code.length) {
      if (code.charAt(end) === "\n") {
        push();
      }
      ++end;
    }
    push();
    return finalParts;
  }
  function processString(code) {
    let parts = code.split(/(?<!\\)"/g);
    if (!parts[0]) parts = parts.slice(1);

    let finalParts = [];
    parts.forEach((part, i) =>
      i % 2 === 0
        ? (finalParts = finalParts.concat(processLines(part)))
        : finalParts.push(
            <span className="text-[#1a2]">{processLines(part, true)}</span>
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
