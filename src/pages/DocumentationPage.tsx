import { Outlet } from "react-router-dom";
import DocsPageWrapper from "../custom_components/DocsPageWrapper";
import React from "react";

export default function DocumentationPage() {
  return (
    <DocsPageWrapper>
      <Outlet />
    </DocsPageWrapper>
  );
}
