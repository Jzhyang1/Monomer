import { useContext } from "react";
import LargeText from "./LargeText";
import Link from "./Link";
import { ThemeContext } from "../contexts";

export default function Box({
  title,
  header,
  children,
  link,
  expand,
  className,
  ...props
}) {
  const { isDarkMode } = useContext(ThemeContext);
  const inner = (
    <div
      className={
        "p-[10px] rounded-lg " +
        (isDarkMode
          ? "bg-[#3433fb]/20 border-[1px] border-blue-700 "
          : "bg-[#f4b33b]/20 border-[1px] border-black ") +
        className
      }
      {...props}
    >
      <div>{header}</div>
      <LargeText>{title}</LargeText>
      {children && (
        <div className="border-t-[1px] border-t-black">{children}</div>
      )}
    </div>
  );
  return link ? (
    <Link href={link}>{inner}</Link>
  ) : expand ? (
    <>{inner}</> //TODO
  ) : (
    inner
  );
}
