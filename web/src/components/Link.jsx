import React from "react";
import { Link as LocalLink } from "react-router-dom";

export default function Link(props) {
  return props.href?.startsWith("/") ? (
    <LocalLink {...props} to={props.href} />
  ) : (
    // eslint-disable-next-line jsx-a11y/anchor-has-content
    <a target="_blank" {...props} />
  );
}