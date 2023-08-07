import React from "react";
import { PropsType } from "../types";

export default function LargeText(props: PropsType) {
  return (
    <p
      {...props}
      className={`text-[28px] tracking-wide font-thin ${props.className}`}
    />
  );
}
