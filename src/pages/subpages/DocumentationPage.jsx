import { Outlet } from "react-router-dom";
import DocsNavMenu from "../../custom_components/DocsNavMenu";

/**
 * see "../documentation" for subpages
 */
export default function DocumentationPage() {
  return (
    <>
      <DocsNavMenu />
      <Outlet />
    </>
  );
}
