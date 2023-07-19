import { Outlet } from "react-router-dom";
import NavMenu from "./custom_components/NavMenu";

export default function Root() {
  return (
    <>
      <NavMenu />
      <Outlet />
    </>
  );
}
