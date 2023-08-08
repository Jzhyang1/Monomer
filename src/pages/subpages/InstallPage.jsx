import Code from "../../components/Code";
import Link from "../../components/Link";
import Title from "../../components/Title";

export default function InstallPage() {
  return (
    <>
      <Title>Install</Title>
      <div>
        To run the Universal JAR on Windows, install the JAR and in the
        directory containing the JAR, open the command line and type the
        following:
        <Code blocked colored={false}>
          merl.jar _path/_to/_file.m
        </Code>
      </div>
      <div className="flex flex-row justify-evenly p-[10px] rounded-lg bg-black/5">
        <Machine name="Universal (jar)" sources={["merl.jar"]} />
        <Machine name="Windows (jar)" sources={["MERLInstaller.exe"]} />
      </div>
    </>
  );
}

function Machine({ name, sources }) {
  return (
    <div className="flex flex-col px-[10px] border-x-[1px] border-x-black">
      <div className="font-bold mb-[10px]">{name}</div>
      {sources.map((source, i) => (
        <div key={i}>
          <Link href={source}>{source}</Link>
        </div>
      ))}
    </div>
  );
}
