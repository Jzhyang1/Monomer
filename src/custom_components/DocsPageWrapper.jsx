import DocsNavMenu from "./DocsNavMenu";

export default function DocsPageWrapper({ children }) {
  return (
    <>
      <DocsNavMenu />
      {children}
    </>
  );
}
