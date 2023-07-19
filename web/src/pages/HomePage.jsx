import Dropdown from "../components/Dropdown";
import Link from "../components/Link";
import List from "../components/List";
import SectionHead from "../components/SectionHead";
import Title from "../components/Title";

const goals = [
  "reduce the number of unique identifiers to remember, and keep those that do need memory to an intuitive and combinable set",
  "reduce the number of lines without information, and increasing the amount of information able to be conveyed in one line",
  "reduce the depth of nested statements",
  "increase the resemblance to natural language",
  "increase the intuitiveness of operators, including order of operators and functional definition",
  "reduce the need to explicitly declare information when that information is used intermediately",
  "provide support for common modern programming needs, and a language structure that allows for more to be added without creating new syntax",
];

const keyFeatures = [
  "making use of white space and tabs fo signalling groups to reduce extra lines used to convey delimiters with a clearly defined conversion between tabs and spaces (1 tab = 4 spaces) to prevent confusion",
  "adhering generally to left-to-right order of operations (except in the case of arithmetic and delimiters) to reduce confusion",
  "providing a comprehensive set of operators, including powerful type casting, that reduces the amount of named-and-defined functions to reduce the need for naming and parenthesis",
  "providing easy operator overloading to reduce the need for naming and parenthesis",
  "powerful types with type inheritance to support intuitive enums and overloading: the variable itself is the most relavent type followed by ancester variables (if applicable)",
  "types based on the variable structure as a least-relavent catch-all type to support ease of casting and overloading",
  '"loose structures" in which a structure is defined as several multi-part variables sharing the same base rather than an independent group, to increase the flatness of code and support multi-part names',
  "declaring and inferring variable and its types by use to reduce redundency in declaration",
  "determining an object's fields by its use inside of the structure definition to support need to encapsulate information",
  "signalling a structure's methods and fields with spaces to promote the natural description-item relationship",
];

export default function HomePage() {
  return (
    <>
      <Title>Home</Title>
      <Dropdown
        toggle={<SectionHead>Goals</SectionHead>}
        className="bg-orange-200"
      >
        <div>
          We strive to improve the readability and writability of code by
          drawing inspiration from&nbsp;
          <Link href="https://www.python.org">Python</Link>,&nbsp;
          <Link href="https://isocpp.org">C++</Link>,&nbsp;
          <Link href="https://www.java.com/en/">Java</Link>, and other
          additions:
        </div>
        <List className="p-[10px] text-lg leading-10" isNumber>
          {goals}
        </List>
      </Dropdown>
      <Dropdown
        toggle={<SectionHead>Implementation</SectionHead>}
        className="bg-orange-200"
      >
        <List className="p-[10px]">{keyFeatures}</List>
      </Dropdown>
    </>
  );
}
