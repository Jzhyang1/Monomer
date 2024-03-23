import Code from "../../components/Code";
import DownloadLink from "../../components/DownloadLink";
import SectionHead from "../../components/SectionHead";
import Title from "../../components/Title";

const releases = [
  {
    href: "https://drive.google.com/uc?export=download&id=1-yhlSnrdMdTKElfvv6Z7sQAXckxJvTJ1",
    name: "V 1.0.5 (JAR)",
    type: "universal",
    date: "3/23/2024",
    desc: "Updated compiler and interpreter to be more stable. Bug fixes.",
  },
  {
    href: "https://drive.google.com/uc?export=download&id=1pLaVZAJkyG4VipO95OTmGhn4ybEYSv53",
    name: "V 1.0.4 (JAR)",
    type: "universal",
    date: "12/25/2023",
    desc: "Implemented missing operators, added structure casting, bug fixes.",
  },
  {
    href: "https://drive.google.com/uc?export=download&id=1Uo8HUdXs1TbYZrSQB5eVuGEr0J4HcikF",
    name: "V 1.0.3 (JAR)",
    type: "universal",
    date: "11/26/2023",
    desc: "Added ranges",
  },
  {
    href: "https://drive.google.com/uc?export=download&id=1b1_PtsnGgxuKwPIRorXF_0fOwgTAKrsG",
    name: "V 1.0.2 (JAR)",
    type: "universal",
    date: "9/29/2023",
    desc: "Classes and named function arguments",
  },
  {
    href: "https://drive.google.com/uc?export=download&id=1Ey3CIQftEq5zLsK7gtlXx1RyMd_Bgc-4",
    name: "V 1.0.1 (JAR)",
    type: "universal",
    date: "9/17/2023",
    desc: "Bug fixes and IO features",
  },
  {
    href: "https://drive.google.com/uc?export=download&id=19a_3U8dXGp9AvOiFTFQDreG7o3YsMcRk",
    name: "V 1.0.0 (JAR)",
    type: "universal",
    date: "8/20/2023",
    desc: "Monomer v1",
  },
  {
    href: "/MERLInstaller.exe",
    name: "V 0.0 (JAR)",
    type: "windows",
    date: "10/2/2022",
    desc: "MERL",
  },
];

export default function InstallPage() {
  return (
    <>
      <Title>Install</Title>
      <div>As of now, Java is required to use Monomer</div>
      <div className="flex flex-row justify-evenly p-[10px] divide-x-[1px] rounded-lg bg-black/5">
        <Machine
          name="Universal"
          sources={releases.filter((release) => release.type === "universal")}
        />
        <Machine
          name="Windows"
          sources={releases.filter(
            (release) =>
              release.type === "universal" || release.type === "windows"
          )}
        />
        <Machine
          name="Mac"
          sources={releases.filter(
            (release) => release.type === "universal" || release.type === "mac"
          )}
        />
        <Machine
          name="Linux"
          sources={releases.filter(
            (release) =>
              release.type === "universal" || release.type === "linux"
          )}
        />
      </div>
      <div>
        <SectionHead className="mt-5">CLI</SectionHead>
        <div>
          <span className="font-bold">(V 1.0.*)</span> Use the IDE
        </div>
        <div>
          <span className="font-bold">(V 0.0)</span> To run the CLI, install the
          JAR and type the following into the command line:
          <Code blocked colored={false}>
            merl _path/_to/_file.m
          </Code>
        </div>
      </div>
    </>
  );
}

function Machine({ name, sources }) {
  return (
    <div className="flex flex-col flex-1 items-center px-[10px]">
      <div className="font-bold mb-[10px] border-b-[1px] border-black">
        {name}
      </div>
      <div className="flex flex-col gap-3">
        {sources.map(({ href, name, desc }, i) => (
          <div
            key={i}
            className={`gap-2 ${i !== 0 && "italic text-sm opacity-50"} group`}
          >
            <div className="text-center">
              <DownloadLink href={href}>{name}</DownloadLink>
            </div>
            <div className="text-sm rounded-lg bg-white/30 p-3 hidden group-hover:block">
              {desc}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
