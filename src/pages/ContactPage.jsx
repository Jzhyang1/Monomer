import Box from "../components/Box";
import Image from "../components/Image";
import List from "../components/List";
import Title from "../components/Title";
import Link from "../components/Link";

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
    image: "/people/Jonathan.jpg",
    link: "http://jzhyang1.github.io",
    creds: "Seven Lakes High School",
    desc: "founder",
    links: [
      {
        title: "jzhyang1@gmail.com",
      },
    ],
    important: true,
  },
  {
    title: "Aidan Lai",
    image: "/people/Aidan.jpg",
    creds: "Seven Lakes High School",
    link: "",
    desc: "developer",
    links: [],
    important: true,
  },
  {
    title: "Frank Li",
    image: "https://avatars.githubusercontent.com/u/79488460?s=48&v=4",
    creds: "University of Texas at Austin",
    link: "",
    desc: "developer",
    links: [],
    important: true,
  },
  {
    title: "Phoenix Wu",
    image: "/people/Phoenix.jpg",
    creds: "Massachusetts Institute of Technology",
    link: "",
    desc: "commentator",
    links: [],
    important: true,
  },
  {
    title: "Kason Gu",
    image: "https://avatars.githubusercontent.com/u/80986485?s=64&v=4",
    creds: "Seven Lakes High School",
    link: "",
    desc: "commentator",
    links: [],
    important: true,
  },
  {
    title: "Gordon Jin",
    image: "/people/Gordon.jpg",
    creds: "Seven Lakes High School",
    link: "",
    desc: "commentator",
    links: [],
    important: true,
  },
  {
    title: "Derek Yu",
    image: "/icon.jpg",
    creds: "Seven Lakes High School",
    link: "",
    desc: "commentator",
    links: [],
    important: true,
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
  //TODO make ContactBox pop out when clicked on
  const { title, creds, link, image, desc, links, important } = contact;
  return (
    <Box
      className={
        "rounded-lg bg-slate-100 max-w-[300px] p-[10px] font-thin border-[2px] " +
        (important ? "border-orange-200" : "m-[10px]")
      }
      header={
        <div className="flex justify-center">
          <Image
            src={image}
            alt={title}
            className={
              "border-[2px] rounded-full object-cover " +
              (important
                ? "h-[150px] w-[150px] border-gray-400"
                : "h-[125px] w-[125px]")
            }
          />
        </div>
      }
      title={title}
      expand
    >
      <div>
        <small>{creds}</small>
      </div>
      <div className="uppercase">{desc}</div>
      <List className="text-sm">
        {links.map(({ title, link }, i) =>
          link ? <Link href={link}>{title}</Link> : title
        )}
      </List>
    </Box>
  );
}
