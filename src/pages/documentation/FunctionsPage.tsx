import React from "react";
import Code from "../../components/Code";
import Title from "../../components/Title";

export default function FunctionsPage() {
  return (
    <>
      <Title>Functions</Title>
      <Code blocked>
        \\lambda style{"\n"}
        _f( `._args`. ) = _body{"\n\n"}
        \\traditional style{"\n"}
        _f( `._args`. ) ={"\n\t"}`...
        {"\n\t"}return _x{"\n\n"}
        \\customized lambda style{"\n"}
        _f{"{ `._named_args`. }"}( `._args`. ) = _body{"\n\n"}
        \\customized style{"\n"}
        _f{"{ `._named_args`. }"}( `._args`. ) ={"\n\t"}`...
        {"\n\t"}return _x
      </Code>
    </>
  );
}
