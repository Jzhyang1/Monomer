import Dropdown from "../components/Dropdown";
import LargeText from "../components/LargeText";
import List from "../components/List";
import SectionHead from "../components/SectionHead";
import Title from "../components/Title";

const goals = [
  {
    title: "Maintainable",
    desc: "A project can be modified easily on the small scale without disrupting other parts of the program",
    features: [
      {
        title: "Componentization",
        desc: "A program structure that favors independent parts that build up the program",
      },
      {
        title: "Adjacency",
        desc: "Supporting the independence of components by separating the shared interactions between components from either component",
      },
    ],
  },
  {
    title: "Efficient",
    desc: "Implementing logic takes up minimal space and syntax does not pose a difficulty to implement logic",
    features: [
      {
        title: "Implication",
        desc: "A small number of keywords and symbols that take on different meanings by varying contexts",
      },
      {
        title: "Reuse",
        desc: "The few core symbols are redefined in the program to fulfill specific program needs",
      },
    ],
  },
  {
    title: "Readable",
    desc: "The logic written for any program is easily understood without additional explaination",
    features: [
      {
        title: "Opinionation",
        desc: "A program with strict rules to its syntax that forces the same approach to the same problem",
      },
      {
        title: "Default",
        desc: "A predefined value is used in cases where arbitrary naming is likely to occur",
      },
    ],
  },
  {
    title: "Learnable",
    desc: "A short amount of time is needed for individuals unfamiliar to programming to implement programs",
    features: [
      {
        title: "Foundation",
        desc: "The program is comprised of only a few fixed symbols and organizational structures",
      },
      {
        title: "Script",
        desc: "There is little to no excess (conveying no new information) formatting required for the program to run",
      },
    ],
  },
];

const reasoning = [
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
      <Title>Monomer</Title>
      <SectionHead>Goals</SectionHead>
      {goals.map((goal, index) => (
        <GoalBox key={index} goal={goal} isEven={index % 2 === 0} />
      ))}
    </>
  );
}

function GoalBox({ isEven, goal }) {
  function TitleBox() {
    return (
      <div className="flex flex-col md:flex-row m-[20px] align-middle gap-[20px]">
        <div className="w-[150px]">
          <LargeText>{goal.title}</LargeText>
        </div>
        <div className="flex flex-1 font-thin leading-[18px] items-center">
          {goal.desc}
        </div>
      </div>
    );
  }
  function FeatureBox({ feature }) {
    return (
      <>
        <strong>{feature.title}:</strong> {feature.desc}
      </>
    );
  }

  return (
    <Dropdown
      toggle={<TitleBox />}
      className={isEven ? "bg-orange-200" : "bg-none"}
    >
      <List className="p-[10px]">
        {goal.features.map((feature, i) => (
          <FeatureBox feature={feature} key={i} />
        ))}
      </List>
    </Dropdown>
  );
}
