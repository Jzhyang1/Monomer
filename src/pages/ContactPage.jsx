import LargeText from "../components/LargeText";
import Link from "../components/Link";
import List from "../components/List";
import Title from "../components/Title";

const repos = [
  {
    title: "GitHub",
    image:
      "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
    link: "https://github.com/Jzhyang1/Monomer",
    desc: "Our current GitHub repository",
    links: [],
    important: true,
  },
  {
    title: "MERL Website",
    image:
      "https://www.gstatic.com/images/branding/product/2x/sites_2020q4_48dp.png",
    link: "https://www.merl.systems",
    desc: "Our former website",
    links: [],
  },
  {
    title: "MERLr2 GitHub",
    image:
      "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
    link: "https://github.com/kisdjonathan/MERLr2",
    desc: "Our former GitHub repository",
    links: [],
  },
  {
    title: "MERLr1 GitHub",
    image:
      "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
    link: "https://github.com/kisdjonathan/MERL",
    desc: "Our former GitHub repository",
    links: [],
  },
];

const contacts = [
  {
    title: "Jonathan Yang",
    image: "https://avatars.githubusercontent.com/u/82784096?v=4",
    link: "http://jzhyang1.github.io",
    desc: "founder",
    links: [
      {
        title: "jzhyang1@gmail.com",
        link: "",
      },
    ],
    important: true,
  },
  {
    title: "Aidan Lai",
    image: "https://avatars.githubusercontent.com/u/68030201?v=4",
    link: "",
    desc: "developer",
    links: [],
    important: true,
  },
  {
    title: "Frank Li",
    image: "https://avatars.githubusercontent.com/u/79488460?s=48&v=4",
    link: "",
    desc: "developer",
    links: [],
  },
  {
    title: "Kason Gu",
    image: "https://avatars.githubusercontent.com/u/80986485?s=64&v=4",
    link: "",
    desc: "commentator",
    links: [],
  },
];

export default function ContactPage() {
  return (
    <>
      <Title>Sources</Title>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-[10px] mt-[10px] mb-[30px]">
        {repos.map((contact, index) => (
          <ContactBox key={index} contact={contact} />
        ))}
      </div>
      <Title>Members</Title>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-[10px] mt-[10px] mb-[30px]">
        {contacts.map((contact, index) => (
          <ContactBox key={index} contact={contact} />
        ))}
      </div>
    </>
  );
}

function ContactBox({ contact }) {
  const { title, link, image, desc, links, important } = contact;
  return (
    <div
      className={
        "rounded-lg bg-slate-100 max-w-[300px] p-[10px] font-thin border-[2px] " +
        (important ? "border-orange-200" : "m-[10px]")
      }
    >
      <div className="flex justify-center">
        <img
          src={image}
          alt={title}
          className={
            "border-[2px] rounded-full " +
            (important ? "h-[150px] border-gray-400" : "h-[125px]")
          }
        />
      </div>
      <LargeText>
        <Link href={link}>{title}</Link>
      </LargeText>
      <div>{desc}</div>
      <List>
        {links.map(({ title, link }, i) => (
          <a key={i} href={link}>
            {title}
          </a>
        ))}
      </List>
    </div>
  );
}
