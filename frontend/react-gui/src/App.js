import React from "react";
import { BrowserRouter as Router } from "react-router-dom";
import Routes from "./Routes";
import Navbar from "./components/Navbar";

export default function App() {
  return (
    <>
      <Router>
        <>
          <Navbar />
          <Routes />
        </>
      </Router>
    </>

  );
}
