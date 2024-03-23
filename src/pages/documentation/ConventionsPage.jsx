import Box from "../../components/Box";
import Code from "../../components/Code";
import List from "../../components/List";
import Title from "../../components/Title";

export default function ConventionsPage() {
  return (
    <>
      <Title>The symbols, Mason, what do they mean?</Title>
      <div className="my-10">
        Replacable symbols are italicized. These are either values or
        expressions with a type given by the word of the symbol, defined below
        <List>
          <>
            <Code>_x</Code> is anything
          </>
          <>
            <Code>_name</Code> is an identifier, which is a variable
          </>
          <>
            <Code>_n</Code> is a number
          </>
          <>
            <Code>_bool</Code> is a boolean values (true/false)
          </>
          <>
            <Code>`[_col`]</Code> is a collection (list, set, range, map)
          </>
          <>
            <Code>_type</Code> is any value with a defined type
          </>
        </List>
      </div>
    </>
  );
}
