import { useContext } from "react";
import Code from "../../components/Code";
import Title from "../../components/Title";
import { ThemeContext } from "../../contexts";

export default function CommandLinePage() {
  const { isDarkMode } = useContext(ThemeContext);

  const symbol = (
    <span className={"inline-block w-[70px] font-serif"}>
      <span className={isDarkMode ? "text-slate-400" : "text-slate-500"}>
        {">"}
      </span>{" "}
      <span className={isDarkMode ? "text-[#e2b1ff]/80" : "text-[#524123]/70"}>
        mono
      </span>
    </span>
  );
  const className = isDarkMode ? "text-slate-300" : "text-slate-700";

  return (
    <>
      <Title>Command Line Interface (CLI)</Title>
      To get the information of the installed Monomer version, run the following
      in the command line:
      <Code colored={false} symbol={symbol} className={className} blocked>
        {" "}
      </Code>
      Which will output:
      <Code colored={false} symbol="" className={className} blocked>
        Monomer version 1.0.0{"\n"}For help, type:{"\n\t"}mono help
      </Code>
      To get a list of all commands, run the following:
      <Code colored={false} symbol={symbol} className={className} blocked>
        help
      </Code>
      To get information on a specific command, run the following:
      <Code colored={false} symbol={symbol} className={className} blocked>
        help [command_name]
      </Code>
      To run Monomer shell, run the following:
      <Code colored={false} symbol={symbol} className={className} blocked>
        shell
      </Code>
      To run one or many files, run either of the following:
      <Code colored={false} symbol={symbol} className={className} blocked>
        int "[file1.m]" {"\n"}
        int "[file1.m]" "[file2.m]" [...]
      </Code>
      To compile one or many files, run either of the following:
      <Code colored={false} symbol={symbol} className={className} blocked>
        comp "[file1.m]" {"\n"}
        comp "[file1.m]" "[file2.m]" [...]
      </Code>
    </>
  );
}
