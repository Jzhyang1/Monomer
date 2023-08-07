import Footer from "./Footer";
import NavMenu from "./NavMenu";

export default function PageWrapper({ title, children }) {
  return (
    <>
      <NavMenu title={title} />
      <div className="relative flex flex-col p-[20px] md:p-[40px] overflow-y-scroll overflow-x-clip">
        {children}
        <Footer />
      </div>
    </>
  );
}
