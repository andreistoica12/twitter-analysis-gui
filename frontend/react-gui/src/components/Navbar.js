import React from 'react'
import { Link } from "react-router-dom"


const Navbar = () => {
  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
    <a className="navbar-brand" href="/">Home</a>
    <button
      className="navbar-toggler"
      type="button"
      data-toggle="collapse"
      data-target="#navbarNav"
      aria-controls="navbarNav"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <span className="navbar-toggler-icon"></span>
      <Link to="/" className="nav-link">
          Home
      </Link>
    </button>
    <div className="collapse navbar-collapse" id="navbarNav">
      <ul className="navbar-nav">
        {/* Add other menu items here */}
        <li className="nav-item">
            <Link to="/provenance/model2" className="nav-link">
                Provenance - Model 2
            </Link>
        </li>
        <li className="nav-item">
            <Link to="/model3" className="nav-link">
                Model 3
            </Link>
        </li>
      </ul>
    </div>
  </nav>  )
}

export default Navbar