import { useState } from "react";

export default function Tabs({
  pages,
  children,
  background,
  className,
  ...props
}) {
  const [page, setPage] = useState(0);
  return (
    <>
      <div className="flex flex-row w-full mt-[20px]">
        {pages.map((title, i) => {
          return (
            <div
              className={`rounded-t-lg p-[5px] hover:cursor-pointer`}
              style={{
                opacity: page === i ? 1 : 0.2,
                backgroundColor: background,
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
        style={{ backgroundColor: background }}
        {...props}
      >
        {children[page]}
      </div>
    </>
  );
}
