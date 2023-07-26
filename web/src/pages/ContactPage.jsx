import LargeText from "../components/LargeText";
import Link from "../components/Link";
import List from "../components/List";
import Title from "../components/Title";

const contacts = [
  {
    title: "GitHub",
    image:
      "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
    link: "https://github.com/Jzhyang1/Monomer",
    desc: "Our GitHub repository",
    links: [],
  },
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
  },
  {
    title: "Aidan Lai",
    image: "https://avatars.githubusercontent.com/u/68030201?v=4",
    link: "",
    desc: "developer",
    links: [],
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
    image:
      "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
    link: "",
    desc: "commentator",
    links: [],
  },
];

export default function ContactPage() {
  return (
    <>
      <Title>Contact</Title>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-[10px]">
        {contacts.map((contact, index) => (
          <ContactBox key={index} contact={contact} />
        ))}
      </div>
    </>
  );
}

function ContactBox({ contact }) {
  const { title, link, image, desc, links } = contact;
  return (
    <div className="rounded-lg bg-slate-100 max-w-[300px] p-[10px] font-thin">
      <div className="flex justify-center">
        <img src={image} alt={title} className="max-h-[150px] rounded-full" />
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
