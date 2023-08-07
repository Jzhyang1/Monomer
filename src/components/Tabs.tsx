import React from "react";
import { useState } from "react";
import { PropsType } from "../types";

export default function Tabs({
  pages,
  children,
  color,
  className,
  ...props
}: PropsType) {
  const [page, setPage] = useState(0);

  if (!pages || !children) return;
  if (!Array.isArray(children)) children = [children];

  return (
    <>
      <div className="flex flex-row w-full mt-[20px]">
        {pages.map((title, i) => {
          return (
            <div
              className={`rounded-t-lg p-[5px] hover:cursor-pointer`}
              style={{
                opacity: page === i ? 1 : 0.2,
                backgroundColor: color,
              }}
              onClick={() => setPage(i)}
            >
              {title}
            </div>
          );
        })}
      </div>
      <div
        className={`px-[25px] py-[5px] ${className}`}
        style={{ backgroundColor: color }}
        {...props}
      >
        {children[page]}
      </div>
    </>
  );
}
