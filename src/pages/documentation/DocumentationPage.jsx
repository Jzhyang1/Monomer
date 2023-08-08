import Box from "../../components/Box";
import Code from "../../components/Code";
import List from "../../components/List";
import Title from "../../components/Title";

export default function DocumentationPage() {
  const PageBox = ({ title, link }) => (
    <Box
      className="min-w-[150px] text-lg"
      title={title}
      link={`/docs/${link}`}
    />
  );

  return (
    <>
      <Title>Documentation</Title>
      <div className="flex flex-wrap gap-[10px] m-[20px]">
        <PageBox title="Command Line" link="cli" />
        <PageBox title="Operators" link="operators" />
        <PageBox title="Control Structures" link="controls" />
        <PageBox title="Types" link="types" />
        <PageBox title="Functions" link="functions" />
        <PageBox title="Variables" link="variables" />
      </div>
      <div>
        General information:{" "}
        <List>
          <>
            files have extension <Code>*.m</Code> for Monomer script,{" "}
            <Code>*.mm</Code> for Monomer module (either a type or a library of
            functions), <Code>*.mw</Code> for Monomer web, and <Code>*.mc</Code>{" "}
            for Monomer config
          </>
          <>
            Things are very similar to Python for control structures and
            assignment, similar to Typescript for types (except type comes
            first), and uses constants for default (compared to no constants in
            Python) and spaces for fields (as compared to dots (<Code>.</Code>))
          </>
        </List>
      </div>
    </>
  );
}
