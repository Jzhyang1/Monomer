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
import DocumentationHomePage from "./pages/documentation/DocumentationPage";
import OperatorDetails from "./custom_components/OperatorDetails";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <Router path="%PUBLIC_URL%">
      <PageWrapper>
        <Routes>
          <Route errorElement element={<ErrorPage />} />
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
                  element={<OperatorDetails {...op} key={i} />}
                />
              ))}
            </Route>
          </Route>
          <Route exact path="/start" element={<StartPage />} />
          <Route exact path="/install" element={<InstallPage />} />
        </Routes>
      </PageWrapper>
    </Router>
  </React.StrictMode>
);
