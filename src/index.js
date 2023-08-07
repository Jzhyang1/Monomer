import React from "react";
import "./style.css";
import ReactDOM from "react-dom";
import {
  BrowserRouter as Router,
} from "react-router-dom";
import App from "./pages/App";

ReactDOM.render(
  <Router path="%PUBLIC_URL%">
    <App />
  </Router>,
  document.getElementById('root')
);
