import React from "react";
import "./index.css";
import ReactDOM from "react-dom/client";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import HomePage from "./pages/HomePage";
import StartPage from "./pages/StartPage";
import ContactPage from "./pages/ContactPage";
import ErrorPage from "./pages/ErrorPage";
import PageWrapper from "./custom_components/PageWrapper";
import InstallPage from "./pages/InstallPage";
import DocumentationPage from "./pages/DocumentationPage";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <Router basename="/Monomer">
      <PageWrapper>
        <Routes>
          <Route errorElement element={<ErrorPage />} />
          <Route exact path="/" element={<HomePage />} />
          <Route exact path="/contact" element={<ContactPage />} />
          <Route exact path="/docs" element={<DocumentationPage />} />
          <Route exact path="/start" element={<StartPage />} />
          <Route exact path="/install" element={<InstallPage />} />
        </Routes>
      </PageWrapper>
    </Router>
  </React.StrictMode>
);
