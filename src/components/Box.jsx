import { useContext, useState } from "react";
import LargeText from "./LargeText";
import Modal from "react-modal";
import { ThemeContext } from "../contexts";
import Link from "./Link";

export default function Box({
  title,
  header,
  children,
  expand,
  className,
  link,
  ...props
}) {
  const [isExpanded, setExpanded] = useState(false);

  const expandView = expand ? (
    <>
      <div onClick={() => setExpanded(true)}>
        <BoxContent
          className={className}
          header={header}
          title={title}
          expand={isExpanded && expand}
          children={children}
          {...props}
        />
      </div>
      <Modal
        isOpen={isExpanded}
        onRequestClose={() => setExpanded(false)}
        style={{
          overlay: {
            backgroundColor: "rgba(0, 0, 0, 0.5)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          },
          content: {
            border: "none",
            width: "fit-content",
            minWidth: "40%",
            height: "fit-content",
            minHeight: "50%",
            padding: 0,
            background: "none",
            margin: "auto",
          },
        }}
        shouldCloseOnOverlayClick
      >
        {expand}
      </Modal>
    </>
  ) : (
    <BoxContent
      className={className}
      header={header}
      title={title}
      children={children}
      {...props}
    />
  );

  return link ? <Link href={link}>{expandView}</Link> : expandView;
}

export function BoxContent({ className, header, title, children, ...props }) {
  const { isDarkMode } = useContext(ThemeContext);

  return (
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
}
