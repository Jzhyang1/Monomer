import React from "react";

export default function SectionHead(props) {
  return (
    // eslint-disable-next-line jsx-a11y/heading-has-content
    <h3
      {...props}
      className={`text-[32px] tracking-wider ${props.className}`}
    />
  );
}
