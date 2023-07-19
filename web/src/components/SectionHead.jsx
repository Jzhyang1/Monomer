import React from "react";

export default function SectionHead(props) {
  // eslint-disable-next-line jsx-a11y/heading-has-content
  return <h3 {...props} className={`text-[32px] ${props.className}`} />;
}
