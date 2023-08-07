import React from "react";
import { PropsType } from "../types";

export default function Title(props: PropsType) {
  // eslint-disable-next-line jsx-a11y/heading-has-content
  return <h1 {...props} className={`text-[50px] ${props.className}`} />;
}
