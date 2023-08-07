import React from "react";
import { Route, Routes, useHistory } from "react-router-dom";
import HomePage from "./HomePage";
import StartPage from "./StartPage";
import ContactPage from "./ContactPage";
import ErrorPage from "./ErrorPage";
import PageWrapper from "../custom_components/PageWrapper";
import InstallPage from "./InstallPage";
import DocumentationPage from "./DocumentationPage";
import OperatorsPage, { operators } from "./documentation/OperatorsPage";
import TypesPage, { types } from "./documentation/TypesPage";
import ControlsPage, { controls } from "./documentation/ControlsPage";
import DocumentationHomePage from "./documentation/DocumentationPage";
import OperatorDetails from "../custom_components/OperatorDetails";
import TypeDetails from "../custom_components/TypeDetails";
import ControlDetails from "../custom_components/ControlDetails";
import FunctionsPage from "./documentation/FunctionsPage";
import VariablesPage from "./documentation/VariablesPage";
import CommandLinePage from "./documentation/CommandLinePage";

export default function App() {
  return (
    <React.StrictMode>
      <PageWrapper>
        <Routes>
          <Route path="*" element={<ErrorPage />} />
          <Route exact path="/" element={<HomePage />} />
          <Route exact path="/contact" element={<ContactPage />} />
          <Route exact path="/docs" element={<DocumentationPage />}>
            <Route path="" element={<DocumentationHomePage />} />
            <Route path="*" element={<DocumentationHomePage />} />
            <Route path="operators">
              <Route path="" element={<OperatorsPage />} />
              <Route path="*" element={<OperatorsPage />} />
              {operators.map((op, i) => (
                <Route
                  path={op.name}
                  key={i}
                  element={<OperatorDetails {...op} />}
                />
              ))}
            </Route>
            <Route path="types">
              <Route path="" element={<TypesPage />} />
              <Route path="*" element={<TypesPage />} />
              {types.map((t, i) => (
                <Route path={t.name} key={i} element={<TypeDetails {...t} />} />
              ))}
            </Route>
            <Route path="controls">
              <Route path="" element={<ControlsPage />} />
              <Route path="*" element={<ControlsPage />} />
              {controls.map((t, i) => (
                <Route
                  path={t.name}
                  key={i}
                  element={<ControlDetails {...t} />}
                />
              ))}
            </Route>
            <Route path="functions" element={<FunctionsPage />} />
            <Route path="variables" element={<VariablesPage />} />
            <Route path="cli" element={<CommandLinePage />} />
          </Route>
          <Route exact path="/start" element={<StartPage />} />
          <Route exact path="/install" element={<InstallPage />} />
        </Routes>
      </PageWrapper>
    </React.StrictMode>
  );
}
