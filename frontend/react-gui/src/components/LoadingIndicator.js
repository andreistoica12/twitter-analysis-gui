import React from 'react';
import { Spinner } from 'react-bootstrap';

const LoadingIndicator = () => {
  return (
    <div className="loading-indicator">
      <Spinner animation="border" role="status">
        <span className="sr-only">Loading...</span>
      </Spinner>
    </div>
  );
};

export default LoadingIndicator;
