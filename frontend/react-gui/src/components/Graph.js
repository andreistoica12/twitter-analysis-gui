import React from 'react';

const Graph = () => {
  // Use your chosen graph visualization library to render the graph
  // Example: D3.js, React-Vis, Vis.js, etc.
  return (
    <div>
        <img src={process.env.PUBLIC_URL + '/graphs/model3/march_1/doc3_march_1_evening.svg'} alt="Image" />
    </div>
  );
};

export default Graph;
