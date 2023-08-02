import { Outlet } from "react-router-dom";
import Title from "../components/Title";
import DocsPageWrapper from "../custom_components/DocsPageWrapper";

export default function DocumentationPage() {
  return (
    <DocsPageWrapper>
      <Outlet />
    </DocsPageWrapper>
  );
}
