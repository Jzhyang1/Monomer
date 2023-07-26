import React from "react";

export default function LargeText(props) {
  return (
    <p
      {...props}
      className={`text-[28px] tracking-wide font-thin ${props.className}`}
    />
  );
}
