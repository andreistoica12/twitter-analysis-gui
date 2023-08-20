import React, { useState, useEffect } from 'react';
import Graph from '../components/Graph';

const Model3Page = () => {
  const [showGraph, setShowGraph] = useState(false);

  const handleButtonClick = () => {
    setShowGraph(true);
  };

  return (
    <div>
      <button onClick={handleButtonClick}>Show Graph</button>
      {showGraph && (
        <div>
            <Graph />
        </div>
      )}
    </div>
  );
};

export default Model3Page;
