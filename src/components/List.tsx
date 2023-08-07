import React from "react";
import { PropsType } from "../types";

export default function List({
  children,
  isNumber = false,
  className,
  ...props
}: PropsType & { isNumber?: boolean }) {
  if (!Array.isArray(children)) children = [children];
  if (!Array.isArray(children)) return;

  return (
    <div className={`pl-6 ${className}`} {...props}>
      <ul className={`list-outside ${isNumber ? "list-decimal" : "list-disc"}`}>
        {children.map((child, i) => (
          <li key={i}>{child}</li>
        ))}
      </ul>
    </div>
  );
}
