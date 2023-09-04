import Code from "../../components/Code";
import DownloadLink from "../../components/DownloadLink";
import SectionHead from "../../components/SectionHead";
import Title from "../../components/Title";

const release1_0_0 = {
  href: "https://drive.google.com/uc?export=download&id=19a_3U8dXGp9AvOiFTFQDreG7o3YsMcRk",
  name: "V 1.0.0 (JAR)",
};

export default function InstallPage() {
  return (
    <>
      <Title>Install</Title>
      <div>As of now, Java is required to use Monomer</div>
      <div className="flex flex-row justify-evenly p-[10px] divide-x-[1px] rounded-lg bg-black/5">
        <Machine name="Universal" sources={[release1_0_0]} />
        <Machine
          name="Windows"
          sources={[
            release1_0_0,
            { href: "/MERLInstaller.exe", name: "V 0.0 (JAR)" },
          ]}
        />
        <Machine name="Mac" sources={[release1_0_0]} />
        <Machine name="Linux" sources={[release1_0_0]} />
      </div>
      <div>
        <SectionHead className="mt-5">CLI</SectionHead>
        To run the CLI, install the JAR and type the following into the command
        line:
        <Code blocked colored={false}>
          merl _path/_to/_file.m
        </Code>
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
        <div key={i} className={i !== 0 && "italic"}>
          <DownloadLink href={href}>{name}</DownloadLink>
        </div>
      ))}
    </div>
  );
}
