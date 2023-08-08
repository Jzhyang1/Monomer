import { useState } from "react";
import { FaChevronUp, FaChevronDown } from "react-icons/fa";
import { useContext } from "react";
import { ThemeContext } from "../contexts";

export default function Dropdown({ toggle, initial, children, ...props }) {
  const [showing, setShowing] = useState(false);
  const { isDarkMode, setDarkMode } = useContext(ThemeContext);

  return (
    <div {...props}>
      <button
        className="flex flex-row justify-between items-center p-3 w-full hover:bg-white/25 transition ease-in-out"
        onClick={() => setShowing(!showing)}
      >
        {toggle}
        {showing ? <FaChevronUp size={32} /> : <FaChevronDown size={32} />}
      </button>
      {showing && (
        <div className={"flex flex-col p-4 bg-white/" + (isDarkMode ? "30" : "50")}>{children}</div>
      )}
    </div>
  );
}
