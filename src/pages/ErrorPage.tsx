import React from "react";
import { useRouteError } from "react-router-dom";

export default function ErrorPage() {
  const error = useRouteError();

  return (
    <div className="flex flex-col items-center content-center justify-center">
      <h1>404</h1>
      {error as string}
    </div>
  );
}
