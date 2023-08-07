import Box from "../../components/Box";
import Title from "../../components/Title";

export default function DocumentationPage() {
  const PageBox = ({ title, link }) => (
    <Box
      className="p-[10px] min-w-[150px] rounded-lg bg-[#f4b33b]/20 text-lg border-[1px] border-black"
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
    </>
  );
}
