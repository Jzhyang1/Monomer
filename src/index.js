import React from "react";
import "./index.css";
import ReactDOM from "react-dom/client";
import {
  HashRouter as Router,
  Route,
  BrowserRouter,
  Routes,
} from "react-router-dom";
import HomePage from "./pages/HomePage";
import StartPage from "./pages/StartPage";
import ContactPage from "./pages/ContactPage";
import ErrorPage from "./pages/ErrorPage";
import PageWrapper from "./custom_components/PageWrapper";
import InstallPage from "./pages/InstallPage";
import DocumentationPage from "./pages/DocumentationPage";
import OperatorsPage, { operators } from "./pages/documentation/OperatorsPage";
import TypesPage, { types } from "./pages/documentation/TypesPage";
import ControlsPage, { controls } from "./pages/documentation/ControlsPage";
import DocumentationHomePage from "./pages/documentation/DocumentationPage";
import OperatorDetails from "./custom_components/OperatorDetails";
import TypeDetails from "./custom_components/TypeDetails";
import ControlDetails from "./custom_components/ControlDetails";
import FunctionsPage from "./pages/documentation/FunctionsPage";
import Code from "./components/Code";
import VariablesPage from "./pages/documentation/VariablesPage";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <Router path="%PUBLIC_URL%">
      <PageWrapper>
        <Routes>
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
          </Route>
          <Route exact path="/start" element={<StartPage />} />
          <Route exact path="/install" element={<InstallPage />} />
        </Routes>
      </PageWrapper>
    </Router>
  </React.StrictMode>
);
