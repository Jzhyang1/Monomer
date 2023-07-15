import logo from "./logo.svg";
import "./App.css";
import NavMenu from "./components/NavMenu";

function App() {
  return (
    <div className="App">
      <NavMenu />
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>Monomer</p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
