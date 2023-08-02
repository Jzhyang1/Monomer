import React from "react";

export default function Title(props) {
  // eslint-disable-next-line jsx-a11y/heading-has-content
  return <h1 {...props} className={`text-[50px] ${props.className}`} />;
}
