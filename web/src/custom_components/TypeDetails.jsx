import Title from "../components/Title";
import Code from "../components/Code";
import Link from "../components/Link";

export default function TypeDetails({
  name,
  version,
  seeAlso,
  description,
  requirements,
  notes,
  fields,
}) {
  return (
    <>
      <div>
        <Title>{name}</Title>

        <Link>
          <small>see here for an explaination of our example conventions</small>
        </Link>
      </div>
    </>
  );
}
