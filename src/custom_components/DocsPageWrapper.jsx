import DocsNavMenu from "./DocsNavMenu";

export default function DocsPageWrapper({ title, children }) {
  return (
    <>
      <DocsNavMenu title={title} />
      {children}
    </>
  );
}
