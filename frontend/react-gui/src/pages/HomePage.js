import React from 'react';
import { Helmet } from 'react-helmet';


const HomePage = () => {
  return (
    <>
      <Helmet>
        <title>SNA - Home Page</title>
      </Helmet>
      <div className="home-page">
        <div className="content">
          <h1>Social Network Analysis - integration GUI</h1>
          <p>Pipeline to easily analyze and visualize social network data.</p>
        </div>
      </div>
    </>
  );
};

export default HomePage;
