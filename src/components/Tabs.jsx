import { useContext, useState } from "react";
import { ThemeContext } from "../contexts";

export default function Tabs({
  pages,
  children,
  background,
  className,
  ...props
}) {
  const { isDarkMode } = useContext(ThemeContext);
  const [page, setPage] = useState(0);

  return (
    <>
      <div className="flex flex-row w-full mt-[20px]">
        {pages.map((title, i) => {
          return (
            <div
              className={`rounded-t-lg p-[5px] hover:cursor-pointer`}
              style={{
                opacity: page === i ? 1 : isDarkMode ? 0.5 : 0.2,
                backgroundColor:
                  background || (isDarkMode ? "#333043" : "#E2E8F0"),
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
        style={{
          backgroundColor: background || (isDarkMode ? "#333043" : "#E2E8F0"),
        }}
        {...props}
      >
        {children[page]}
      </div>
    </>
  );
}
