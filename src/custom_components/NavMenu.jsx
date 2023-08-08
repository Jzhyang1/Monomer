import { BrowserView, MobileView } from "react-device-detect";
import Image from "../components/Image";
import Link from "../components/Link";
import HamburgerMenu from "../components/HamburgerMenu";
import { useContext } from "react";
import { ThemeContext } from "../contexts";

const pages = [
  {
    name: "Home",
    url: "/",
  },
  {
    name: "Getting Started",
    url: "/start",
  },
  {
    name: "Documentation",
    url: "/docs",
  },
  {
    name: "Install",
    url: "/install",
  },
  {
    name: "Contact",
    url: "/contact",
  },
];

function LinkButton({ name, url }) {
  return (
    <Link
      className="md:bg-black hover:opacity-50 px-[10px] py-[5px] text-xs md:text-sm text-black md:text-white rounded-lg md:rounded-xl"
      href={url}
    >
      {name}
    </Link>
  );
}

export default function NavMenu({ title }) {
  const { isDarkMode, setDarkMode } = useContext(ThemeContext);

  return (
    <nav>
      <div className="flex flex-row justify-between items-center bg-[#D74]">
        <Link href="/">
          <Image src="/icon.jpg" alt="logo" className="h-[48px]" />
        </Link>
        <h1>{title}</h1>
        <div>
          <label class="relative items-center cursor-pointer">
            <input
              type="checkbox"
              className="sr-only peer"
              checked={isDarkMode}
              readOnly
            />
            <div
              onClick={() => {
                setDarkMode(!isDarkMode);
              }}
              className="w-11 h-6 bg-gray-200 rounded-full peer  peer-focus:ring-slate-700  peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-0.5 after:left-[2px] after:bg-white after:border-slate-500 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-slate-800"
            ></div>
          </label>
        </div>
        <MobileView>
          <div className="mx-[10px] my-auto">
            <HamburgerMenu>
              {pages.map((page, key) => (
                <LinkButton {...page} key={key} />
              ))}
            </HamburgerMenu>
          </div>
        </MobileView>
        <BrowserView>
          <div className="flex my-auto mx-[10px] gap-2">
            {pages.map((page, key) => (
              <LinkButton {...page} key={key} />
            ))}
          </div>
        </BrowserView>
      </div>
    </nav>
  );
}
