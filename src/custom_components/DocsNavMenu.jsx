import { BrowserView } from "react-device-detect";
import Link from "../components/Link";

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
  return (
    <Link
      className="hover:bg-orange-100 px-[10px] py-[5px] text-xs md:text-sm text-slate-600 rounded-lg md:rounded-xl"
      href={"/docs" + url}
    >
      {name}
    </Link>
  );
}

export default function DocsNavMenu() {
  return (
    <nav className="w-full">
      <div className="flex flex-row">
        <input
          type="text"
          placeholder="Search documentation"
          className="border-[1px] border-slate-200 p-1 rounded-lg"
        />
        <BrowserView>
          <div className="flex my-auto mx-[10px] gap-2">
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
