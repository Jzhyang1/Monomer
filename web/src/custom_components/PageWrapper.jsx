import NavMenu from "./NavMenu";

export default function PageWrapper({ title, children }) {
  return (
    <>
      <NavMenu title={title} />
      <div className="relative flex flex-col p-[40px] overflow-x-clip">
        {children}
      </div>
    </>
  );
}
