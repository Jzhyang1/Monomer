import Box, { BoxContent } from "../../components/Box";
import Image from "../../components/Image";
import List from "../../components/List";
import Title from "../../components/Title";
import Link from "../../components/Link";
import { useContext } from "react";
import { ThemeContext } from "../../contexts";
import LargeText from "../../components/LargeText";

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
    tasks: [
      "main developer for MERL",
      "planning and management",
      "documentation",
      "CLI",
      "tokenization",
      "web development",
    ],
    links: [
      {
        title: "jzhyang1@gmail.com",
      },
      {
        title: "Github: Jzhyang1",
        href: "https://github.com/Jzhyang1",
      },
      {
        title: "Github: kisdjonathan",
        href: "https://github.com/kisdjonathan",
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
    tasks: ["developer for MERL", "syntax tree", "interpreter"],
    links: [
      {
        title: "GitHub: Ansere",
        href: "https://github.com/Ansere",
      },
    ],
    important: true,
  },
  {
    title: "Frank Li",
    image: "https://avatars.githubusercontent.com/u/79488460?s=48&v=4",
    creds: "University of Texas at Austin",
    link: "",
    desc: "developer",
    tasks: ["error handling"],
    links: [
      {
        title: "GitHub: realfulffybunnyy",
        href: "https://github.com/realfluffybunnyy",
      },
    ],
    important: true,
  },
  // {
  //   title: "Phoenix Wu",
  //   image: "/people/Phoenix.jpg",
  //   creds: "Massachusetts Institute of Technology",
  //   link: "",
  //   desc: "tester",
  //   tasks: ["bug finding and documentation"],
  //   links: [],
  //   important: true,
  // },
  {
    title: "Kason Gu",
    image: "https://avatars.githubusercontent.com/u/80986485?s=64&v=4",
    creds: "Seven Lakes High School",
    link: "",
    desc: "developer",
    tasks: ["Code editor", "Development integration"],
    links: [
      {
        title: "GitHub: croissant676",
        href: "https://github.com/croissant676",
      },
    ],
    important: true,
  },
  // {
  //   title: "Gordon Jin",
  //   image: "/people/Gordon.jpg",
  //   creds: "Seven Lakes High School",
  //   link: "",
  //   desc: "developer",
  //   tasks: ["Cross-language transpilation"],
  //   links: [],
  //   important: true,
  // },
  // {
  //   title: "Derek Yu",
  //   image: "/icon.jpg",
  //   creds: "Seven Lakes High School",
  //   link: "",
  //   desc: "commentator",
  //   tasks: [],
  //   links: [],
  //   important: true,
  // },
  {
    title: "James Guo",
    image: "/people/JamesGuo.jpg",
    creds: "Seven Lakes High School",
    link: "",
    desc: "tester",
    tasks: ["bug finding and documentation"],
    links: [],
    important: true,
  },
  {
    title: "Aayush Ishware",
    image: "/icon-dark.jpg",
    creds: "Seven Lakes High School",
    link: "",
    desc: "web developer",
    tasks: ["customization"],
    links: [],
    important: true,
  },
];

export default function ContactPage() {
  return (
    <>
      <Title>Sources</Title>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-3 2xl:grid-cols-4 gap-[20px] mt-[10px] mb-[30px]">
        {repos.map((repo, index) => (
          <RepoBox key={index} repo={repo} />
        ))}
      </div>
      <Title>Members</Title>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-[10px] mt-[10px] mb-[30px]">
        {contacts.map((contact, index) => (
          <ContactBox key={index} contact={contact} />
        ))}
      </div>
    </>
  );
}

function RepoBox({ repo }) {
  const { isDarkMode } = useContext(ThemeContext);
  const { title, link, image, desc, important } = repo;
  return (
    <Box
      className={
        "rounded-lg max-w-[400px] p-[10px] font-thin border-[2px] text-end " +
        (important ? "" : "my-[30px] ") +
        (isDarkMode
          ? "border-blue-800 bg-slate-600"
          : "border-orange-200 bg-slate-100")
      }
      header={
        <div className="flex flex-row my-4 gap-4 justify-evenly items-center">
          <LargeText>{title}</LargeText>
          <Image
            src={image}
            alt={title}
            className={
              "border-[2px] rounded-full object-cover " +
              (important ? "h-[100px] w-[100px] " : "h-[50px] w-[50px] ") +
              (isDarkMode ? "border-blue-800" : "border-slate-200")
            }
          />
        </div>
      }
      link={link}
    >
      <div className="uppercase">{desc}</div>
      <div className="text-[14px] underline text-gray-400">
        {link.replace(/(^\w+:|^)\/\//, "")}
      </div>
    </Box>
  );
}

function ContactBox({ contact }) {
  const { isDarkMode } = useContext(ThemeContext);
  const { title, creds, link, image, desc, links, important, tasks } = contact;
  return (
    <Box
      className={
        "rounded-lg max-w-[300px] p-[10px] font-thin border-[2px] " +
        (important
          ? isDarkMode
            ? "border-blue-800 "
            : "border-orange-200 "
          : "m-[10px] border-slate-200 ") +
        (isDarkMode ? "bg-slate-600" : "bg-slate-100")
      }
      header={
        <div className="flex justify-center">
          <Image
            src={image}
            alt={title}
            className={
              "border-[2px] rounded-full object-cover " +
              (important ? "h-[150px] w-[150px] " : "h-[125px] w-[125px] ") +
              (isDarkMode ? "border-blue-800" : "border-slate-200")
            }
          />
        </div>
      }
      title={title}
      expand={
        <BoxContent
          className={
            "rounded-lg p-[20px] md:p-[50px] w-full font-thin border-[2px] " +
            (isDarkMode ? "bg-slate-600 text-white" : "bg-slate-100 text-black")
          }
          header={
            <Link href={link}>
              <div className="flex justify-center">
                <Image
                  src={image}
                  alt={title}
                  className={
                    "border-[2px] rounded-full object-cover h-[200px] w-[200px] " +
                    (isDarkMode ? "border-blue-800" : "border-slate-200")
                  }
                />
              </div>
            </Link>
          }
          title={<Link href={link}>{title}</Link>}
        >
          <div className="text-sm">{creds}</div>
          <div className="uppercase text-xl mb-2">{desc}</div>
          <List className="text-base">
            {links.map(({ title, href }, i) =>
              href ? <Link href={href}>{title}</Link> : title
            )}
          </List>
          {tasks && (
            <div className="mt-1 border-[1px] border-slate-400">
              <List className="text-base">{tasks}</List>
            </div>
          )}
        </BoxContent>
      }
    >
      <div>
        <small>{creds}</small>
      </div>
      <div className="uppercase">{desc}</div>
      <List className="text-sm">
        {links.map(({ title, href }, i) =>
          href ? <Link href={href}>{title}</Link> : title
        )}
      </List>
    </Box>
  );
}
