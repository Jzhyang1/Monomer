import React from "react";

export default function Link({ ...props }) {
  // eslint-disable-next-line jsx-a11y/anchor-has-content
  return <a download {...props} />;
}
