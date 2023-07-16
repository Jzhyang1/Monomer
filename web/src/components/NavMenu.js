import "./NavMenu.css";

const pages = [
  {
    name: "home",
    url: "/",
  },
  {
    name: "start",
    url: "/",
  },
  {
    name: "docs",
    url: "/",
  },
  {
    name: "source",
    url: "/",
  },
];

function LinkButton({ name, url }) {
  return (
    <a
      className="bg-black hover:opacity-25 px-[10px] py-[5px] text-white rounded-xl"
      href={url}
    >
      {name}
    </a>
  );
}

export default function NavMenu() {
  return (
    <nav className="Nav-bar">
      <div className="Nav-menu">
        <img src="/favicon.ico" alt="logo" />
        <div className="flex flex-row gap-2">
          {pages.map((page, key) => (
            <LinkButton {...page} key={key} />
          ))}
        </div>
      </div>
    </nav>
  );
}
