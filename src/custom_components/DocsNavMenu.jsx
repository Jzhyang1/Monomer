import { BrowserView } from "react-device-detect";
import Link from "../components/Link";
import { useContext } from "react";
import { ThemeContext } from "../contexts";

const usagePages = [
  {
    name: "Command Line",
    url: "/cli",
  },
];

const syntaxPages = [
  {
    name: "Operators",
    url: "/operators",
  },
  {
    name: "Control Structures",
    url: "/controls",
  },
  {
    name: "Types",
    url: "/types",
  },
  {
    name: "Functions",
    url: "/functions",
  },
  {
    name: "Variables",
    url: "/variables",
  },
];

const libraryPages = [
  {
    name: "InOut",
    url: "/io",
  },
];

function LinkButton({ name, url }) {
  const { isDarkMode } = useContext(ThemeContext);
  return (
    <Link
      className={
        "px-[10px] py-[5px] text-xs md:text-sm rounded-lg md:rounded-xl " +
        (isDarkMode
          ? "hover:bg-blue-600/30 text-slate-400"
          : "hover:bg-orange-100 text-slate-600/50")
      }
      href={"/docs" + url}
    >
      {name}
    </Link>
  );
}

export default function DocsNavMenu() {
  const { isDarkMode } = useContext(ThemeContext);

  return (
    <nav className="w-full">
      <div className="flex flex-row">
        <input
          type="text"
          placeholder="Search documentation"
          className={
            "border-[1px] py-1 px-3 rounded-lg " +
            (isDarkMode
              ? "border-slate-800 bg-slate-700 text-slate-200"
              : "border-slate-200")
          }
        />
        <BrowserView>
          <div className="flex my-auto mx-[10px] gap-2">
            {usagePages.map((page, key) => (
              <LinkButton {...page} key={key} />
            ))}
            {syntaxPages.map((page, key) => (
              <LinkButton {...page} key={key} />
            ))}
            {libraryPages.map((page, key) => (
              <LinkButton {...page} key={key} />
            ))}
          </div>
        </BrowserView>
      </div>
    </nav>
  );
}
