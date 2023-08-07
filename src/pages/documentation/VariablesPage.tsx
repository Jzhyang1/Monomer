import React from "react";
import Code from "../../components/Code";
import Title from "../../components/Title";

export default function VariablesPage() {
  return (
    <>
      <Title>Variables</Title>
      <Code blocked>
        \\declare constant{"\n"}
        _name = _x{"\n\n"}
        \\declare constant with type{"\n"}
        _name = _t : _x{"\n\n"}
        \\declare variable{"\n"}
        _name = var __t : _x
      </Code>
      A structure is a block that holds several variables.
      <Code blocked>
        {"{\n\t"}
        _name1 = _x{"\n\t"}
        _name2 = _x{"\n\t"}
        `...{"\n}"}
      </Code>
      Every variable is either a basic value (int, bool, list, etc) or a
      structure. Structures define the fields of a variable and are used as
      types to convert other structures.
      <Code blocked>
        a = {"{\n\t"}
        x, y, z = 1, 2, 3{"\n}\n\n"}b = {"{"}x, y{"}"} : a \\{"{"}x = 1; y = 2
        {"}"}
      </Code>
    </>
  );
}
