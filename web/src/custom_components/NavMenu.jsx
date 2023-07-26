import Image from "../components/Image";
import Link from "../components/Link";

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
      className="bg-black hover:opacity-50 px-[10px] py-[5px] text-white rounded-xl"
      href={url}
    >
      {name}
    </Link>
  );
}

export default function NavMenu({ title }) {
  return (
    <nav className="w-full">
      <div className="flex flex-row justify-between bg-[#D74]">
        <Link href="/">
          <Image src="/icon.jpg" alt="logo" className="h-[48px]" />
        </Link>
        <h1>{title}</h1>
        <div className="flex my-auto mx-[10px] gap-2">
          {pages.map((page, key) => (
            <LinkButton {...page} key={key} />
          ))}
        </div>
      </div>
    </nav>
  );
}
