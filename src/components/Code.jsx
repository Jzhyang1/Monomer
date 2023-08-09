import React, { useContext } from "react";
import { ThemeContext } from "../contexts";
//TODO replace all lines from being handled by <br> to <div>
//TODO replace all &emsp; with spaces/tabs in usages of Code

export default function Code({
  className,
  colored,
  blocked,
  children,
  symbol,
  ...props
}) {
  const { isDarkMode } = useContext(ThemeContext);

  const code =
    typeof children === "string"
      ? children
      : children
          .map((child) => (typeof child === "string" ? child : "\n"))
          .join("");
  return blocked ? (
    <div
      {...props}
      className={`p-3 m-3 rounded-lg tracking-wide font-mono text-md ${
        isDarkMode ? "bg-slate-800" : "bg-slate-100 "
      } ${className}`}
    >
      <ProcessedCode
        code={code}
        colored={colored}
        symbol={symbol}
        isDarkMode={isDarkMode}
        blocked
      ></ProcessedCode>
    </div>
  ) : (
    <p
      {...props}
      className={`inline-block tracking-wide font-mono text-sm ${className}`}
    >
      <ProcessedCode
        code={code}
        colored={colored}
        symbol={symbol}
        isDarkMode={isDarkMode}
      ></ProcessedCode>
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

function ProcessedCode({ code, blocked, symbol, isDarkMode, colored = true }) {
  let lineNumber = 0;
  function LineNumberLabel({ line }) {
    return (
      symbol ?? (
        <span
          className={
            "inline-block w-[30px] font-serif border-r-[1px] mr-[15px] " +
            (isDarkMode
              ? "border-white text-gray-200"
              : "border-black text-gray-700")
          }
        >
          {line}
        </span>
      )
    );
  }
  function processStandard(code) {
    const parts = code.split(/(\s+|[^_\w\s]+)/g);
    return parts.filter(Boolean).map((part) => {
      if (keywords.has(part))
        return (
          <span className={isDarkMode ? "text-[#67F]" : "text-[#23E]"}>
            {part}
          </span>
        );
      if (types.has(part))
        return (
          <span className={isDarkMode ? "text-[#c5f]" : "text-[#93d]"}>
            {part}
          </span>
        );
      if (part.startsWith("_") || part.startsWith("`"))
        return (
          <span
            className={
              "italic " + (isDarkMode ? "text-[#fbbbbb]" : "text-[#5a1f1f]")
            }
          >
            {part.slice(1)}
          </span>
        );
      if (/[^_\w\s]+/.test(part))
        return (
          <span className={isDarkMode ? "text-[#e56]" : "text-[#a12]"}>
            {part}
          </span>
        );
      if (0 <= part[0] && part[0] <= 9)
        return (
          <span className={isDarkMode ? "text-[#c2b]" : "text-[#c1a]"}>
            {part}
          </span>
        );
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
          <span className={isDarkMode ? "text-[#777]" : "text-[#AAA]"}>
            \{comment}
          </span>
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
        const endString = code.charAt(end) === delim;
        const escape = code.charAt(end) === "\\";
        if (endString || escape) {
          parts.push(
            <span className="text-[#1a2]">
              {processLines(code.substring(start, end + endString), true)}
            </span>
          );
          start = end + endString;
        }
        if (endString) {
          delim = undefined;
        } else if (escape) {
          ++end;
          const c = code.charAt(end);
          let escaped =
            c === "u"
              ? code.substr(end, 5)
              : c === "a"
              ? code.substr(end, 3)
              : c;
          parts.push(<span className="text-[#ca2]">\{escaped}</span>);
          start = end + escaped.length;
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
