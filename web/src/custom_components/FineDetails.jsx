import { useState } from "react";
import { FaInfoCircle } from "react-icons/fa";
import Link from "../components/Link";

export default function FineDetails({
  version,
  seeAlso,
  requirements,
  notes,
  page,
}) {
  const [open, setOpen] = useState(false);
  return (
    <div className="absolute right-0 top-0 max-w-[350px]">
      <div className="flex flex-row-reverse hover:cursor-pointer">
        <FaInfoCircle size={24} onClick={() => setOpen(!open)} />
      </div>
      {open && (
        <div className="flex flex-col rounded-lg bg-slate-200 p-[10px] gap-[10px]">
          <DetailPart label="Version:">{version}</DetailPart>
          <DetailPart label="See also:">
            {seeAlso.map((title, i) => (
              <>
                {i === 0 || ", "}
                <Link href={`${page}/${title}`} key={i}>
                  {title}
                </Link>
              </>
            ))}
          </DetailPart>
          <DetailPart label="Requires:">{requirements}</DetailPart>
          <DetailPart label="Notes:">{notes}</DetailPart>
        </div>
      )}
    </div>
  );
}

function DetailPart({ label, children }) {
  return (
    <div className="flex flex-row justify-between border-b-[1px] border-slate-500 gap-[15px]">
      <span className="text-slate-500">{label}</span>
      <span className="text-right">{children}</span>
    </div>
  );
}
