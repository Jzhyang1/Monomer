import React from "react";
import { useState } from "react";
import { FaChevronUp, FaChevronDown } from "react-icons/fa";
import { PropsType } from "../types";

export default function Dropdown({
  header,
  initial,
  children,
  ...props
}: PropsType) {
  const [showing, setShowing] = useState(initial);

  return (
    <div {...props}>
      <button
        className="flex flex-row justify-between items-center p-3 w-full hover:bg-white/25 transition ease-in-out"
        onClick={() => setShowing(!showing)}
      >
        {header}
        {showing ? <FaChevronUp size={32} /> : <FaChevronDown size={32} />}
      </button>
      {showing && (
        <div className="flex flex-col p-4 bg-white/50">{children}</div>
      )}
    </div>
  );
}
