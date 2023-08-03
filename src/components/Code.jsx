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
      <span className="inline-block w-[30px] font-serif border-r-[1px] border-black mr-[15px] text-black">
        {line}
      </span>
    );
  }
  function processStandard(code) {
    const parts = code.split(/(\s+|[^_\w\s]+)/g);
    return parts.filter(Boolean).map((part) => {
      if (keywords.has(part))
        return <span className="text-[#23E]">{part}</span>;
      if (types.has(part)) return <span className="text-[#93d]">{part}</span>;
      if (part.startsWith("_") || part.startsWith("`"))
        return <span className="italic text-[#5a1f1f]">{part.slice(1)}</span>;
      if (/[^_\w\s]+/.test(part))
        return <span className="text-[#a12]">{part}</span>;
      if (0 <= part[0] && part[0] <= 9)
        return <span className="text-[#c1a]">{part}</span>;
      return part;
    });
  }
  function processLines(code, isString = false) {
    let finalParts = [];
    let start = 0;
    let end = 0;

    function push(last = false, value = undefined) {
      if (!value) {
        const line = code.slice(start, end);
        let leading = 0,
          count = 0;
        while (true) {
          if (line.charAt(count) === " ") leading++;
          else if (line.charAt(count) === "\t") leading += 4;
          else break;
          ++count;
        }

        const paddedLine = "\u2003".repeat(leading) + line.slice(count);
        finalParts.push(isString ? paddedLine : processStandard(paddedLine));
      } else finalParts.push(value);
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
    // console.log(code);
    const parts = [];

    let start = 0;
    let delim = undefined;
    for (let end = 0; end < code.length; ++end) {
      if (delim) {
        if (code.charAt(end) === delim || code.charAt(end) === "\\") {
          parts.push(
            <span className="text-[#1a2]">
              {processLines(code.substring(start, end + 1), true)}
            </span>
          );
          start = end + 1;
        }
        if (code.charAt(end) === delim) {
          delim = undefined;
        } else if (code.charAt(end) === "\\") {
          ++end;
          parts.push(
            <span className="text-[#ca2]">{code.charAt(end + 1)}</span>
          );
        }
      } else if (code.charAt(end) === '"' || code.charAt(end) === "'") {
        if (start !== end)
          parts.push(...processLines(code.substring(start, end)));
        delim = code.charAt(end);
        start = end;
      }
    }
    if (start !== code.length)
      parts.push(...processLines(code.substring(start)));
    return parts;
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
