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

export default function NavMenu() {
  return (
    <nav className="Nav-bar">
      <div className="Nav-menu">
        <img src="/logo192.png" className="Nav-logo" alt="logo" />
        <div>
          {pages.map((page, key) => (
            <a className="Nav-link" href={page.url} key={key}>
              {page.name}
            </a>
          ))}
        </div>
      </div>
    </nav>
  );
}
