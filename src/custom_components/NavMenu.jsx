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
      <div
        className={
          "flex flex-row justify-between items-center " +
          (isDarkMode ? "bg-blue-600" : "bg-[#D74]")
        }
      >
        <div className="flex flex-row items-center gap-1 md:gap-3">
          <Link href="/">
            <Image
              src={isDarkMode ? "/icon-dark.jpg" : "/icon.jpg"}
              alt="logo"
              className="h-[48px]"
            />
          </Link>
          <h1>{title}</h1>
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
              className="w-11 h-6 rounded-full peer peer-checked:after:translate-x-full after:absolute after:top-0.5 after:left-[2px] after:border after:rounded-full after:h-5 after:w-5 after:transition-all 
                bg-orange-300 peer-focus:ring-orange-200
                after:bg-orange-100 after:border-slate-500 
                peer-checked:before:border-blue-200 peer-checked:before:bg-blue-200 peer-checked:bg-slate-800"
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
