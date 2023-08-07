// import { ClickAwayListener } from "@mui/material";
import { FaBars } from "react-icons/fa";
import React from "react";
import { PropsType } from "../types";

export default function HamburgerMenu({
  children,
  className,
  ...props
}: PropsType) {
  const [open, setOpen] = React.useState(false);
  const handleClick = () => {
    setOpen(!open);
  };

  return (
    // <ClickAwayListener onClickAway={handleClose}>
    <div className="relative dropdown">
      <button onClick={handleClick}>
        <FaBars />
      </button>
      {open && (
        <div
          className={
            "absolute right-0 flex flex-col z-50 p-[5px] rounded-lg bg-white " +
            className
          }
          {...props}
        >
          {children}
        </div>
      )}
    </div>
    // </ClickAwayListener>
  );
}
