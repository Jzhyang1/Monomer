import React from "react";
import { PropsType } from "../types";

export default function SectionHead(props: PropsType) {
  return (
    // eslint-disable-next-line jsx-a11y/heading-has-content
    <h3
      {...props}
      className={`text-[32px] tracking-wider ${props.className}`}
    />
  );
}
