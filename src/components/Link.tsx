import React from "react";
import { Link as LocalLink } from "react-router-dom";
import { PropsType } from "../types";

export default function Link({ samePage, ...props }: PropsType) {
  return samePage || props.href?.startsWith("/") ? (
    <LocalLink {...props} to={props.href ?? "/"} />
  ) : (
    // eslint-disable-next-line jsx-a11y/anchor-has-content
    <a target="_blank" {...props} />
  );
}
