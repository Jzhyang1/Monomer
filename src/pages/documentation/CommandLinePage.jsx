import Code from "../../components/Code";
import Title from "../../components/Title";

export default function CommandLinePage() {
  const symbol = (
    <span className="inline-block w-[20px] font-serif text-black">$</span>
  );
  return (
    <>
      <Title>Command Line Interface (CLI)</Title>
      To get the information of the installed Monomer version, run the following
      in the command line:
      <Code
        colored={false}
        symbol={symbol}
        className={"text-slate-700"}
        blocked
      >
        mono
      </Code>
      Which will output:
      <Code colored={false} symbol="" className={"text-slate-700"} blocked>
        Monomer version 1.0.0{"\n"}For help, type:{"\n\t"}mm help
      </Code>
      To get a list of all commands, run the following:
      <Code
        colored={false}
        symbol={symbol}
        className={"text-slate-700"}
        blocked
      >
        mono help
      </Code>
      To get information on a specific command, run the following:
      <Code
        colored={false}
        symbol={symbol}
        className={"text-slate-700"}
        blocked
      >
        mono help [command_name]
      </Code>
      To run Monomer shell, run the following:
      <Code
        colored={false}
        symbol={symbol}
        className={"text-slate-700"}
        blocked
      >
        mono shell
      </Code>
      To run one or many files, run either of the following:
      <Code
        colored={false}
        symbol={symbol}
        className={"text-slate-700"}
        blocked
      >
        mono int "[file1.m]" {"\n"}
        mono int "[file1.m]" "[file2.m]" [...]
      </Code>
      To compile one or many files, run either of the following:
      <Code
        colored={false}
        symbol={symbol}
        className={"text-slate-700"}
        blocked
      >
        mono comp "[file1.m]" {"\n"}
        mono comp "[file1.m]" "[file2.m]" [...]
      </Code>
    </>
  );
}
