import Code from "../../components/Code";
import DownloadLink from "../../components/DownloadLink";
import SectionHead from "../../components/SectionHead";
import Title from "../../components/Title";

const releases = [
  {
    href: "https://drive.google.com/uc?export=download&id=1Bs6wlNtd2Geafg3Vz2TPPR_Akhefwj8W",
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
    <div className="flex flex-1 flex-col items-center px-[10px]">
      <div className="font-bold mb-[10px] border-b-[1px] border-black">
        {name}
      </div>
      {sources.map(({ href, name }, i) => (
        <div key={i} className={`gap-1 ${i !== 0 && "italic"}`}>
          <DownloadLink href={href}>{name}</DownloadLink>
        </div>
      ))}
    </div>
  );
}
