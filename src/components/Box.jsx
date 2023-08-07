import LargeText from "./LargeText";
import Link from "./Link";

export default function Box({
  title,
  header,
  children,
  link,
  expand,
  ...props
}) {
  const inner = (
    <div {...props}>
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
