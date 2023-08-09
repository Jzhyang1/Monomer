import React, { useMemo } from "react";
import { Route, Routes } from "react-router-dom";
import HomePage from "./subpages/HomePage";
import StartPage from "./subpages/StartPage";
import ContactPage from "./subpages/ContactPage";
import ErrorPage from "./subpages/ErrorPage";
import InstallPage from "./subpages/InstallPage";
import DocumentationPage from "./subpages/DocumentationPage";
import OperatorsPage, { operators } from "./documentation/OperatorsPage";
import TypesPage, { types } from "./documentation/TypesPage";
import ControlsPage, { controls } from "./documentation/ControlsPage";
import DocumentationHomePage from "./documentation/DocumentationPage";
import OperatorDetails from "./documentation/pagedetails/OperatorDetails";
import TypeDetails from "./documentation/pagedetails/TypeDetails";
import ControlDetails from "./documentation/pagedetails/ControlDetails";
import FunctionsPage from "./documentation/FunctionsPage";
import VariablesPage from "./documentation/VariablesPage";
import CommandLinePage from "./documentation/CommandLinePage";
import NavMenu from "../custom_components/NavMenu";
import Footer from "../custom_components/Footer";
import { useState } from "react";
import { ThemeContext } from "../contexts";

export default function App() {
  const [isDarkMode, setDarkMode] = useState(
    window.matchMedia &&
      window.matchMedia("(prefers-color-scheme: dark)").matches
  );
  const contextValue = useMemo(
    () => ({ isDarkMode, setDarkMode }),
    [isDarkMode]
  );

  window
    .matchMedia("(prefers-color-scheme: dark)")
    .addEventListener("change", (event) => {
      setDarkMode(event.matches);
    });

  return (
    <React.StrictMode>
      <ThemeContext.Provider value={contextValue}>
        <div
          className={
            "flex flex-col md:block " + (isDarkMode ? "bg-black" : "bg-white")
          }
        >
          <NavMenu />
          <div
            className={
              "relative h-full flex flex-col px-[20px] md:px-[40px] pt-[40px] pb-[60px] overflow-y-auto overflow-x-clip " +
              (isDarkMode ? "bg-black text-gray-300" : "bg-white text-black")
            }
          >
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
                    <Route
                      path={t.name}
                      key={i}
                      element={<TypeDetails {...t} />}
                    />
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
            <Footer />
          </div>
        </div>
      </ThemeContext.Provider>
    </React.StrictMode>
  );
}
