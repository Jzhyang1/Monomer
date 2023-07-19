import React from "react";

export default function Image(props) {
  return (
    // eslint-disable-next-line jsx-a11y/alt-text
    <img
      {...props}
      src={
        props.src?.startsWith("/")
          ? process.env.PUBLIC_URL + props.src
          : props.src
      }
    />
  );
}
