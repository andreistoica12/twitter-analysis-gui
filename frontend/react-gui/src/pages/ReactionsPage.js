import axios from 'axios';
import React, { useState, useEffect, useRef } from 'react';
import { useSelector } from 'react-redux';
import { Alert, Button } from 'react-bootstrap';

import LoadingIndicator from '../components/LoadingIndicator'
import Slider from '../components/Slider';


const ReactionsPage = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [svgContent, setSvgContent] = useState(null); // To hold SVG content
  const [isProvenanceGraph, setIsProvenanceGraph] = useState(false);
  const [pngContent, setPngContent] = useState(null); // To hold PNG content
  const [isNetworkXGraph, setIsNetworkXGraph] = useState(false);
  const [backendError, setBackendError] = useState(null); // When the backend server is not running

  const intervalStart = useSelector((state) => state.slider.intervalStart);
  const intervalEnd = useSelector((state) => state.slider.intervalEnd);


  const handleGenerateProvenanceGraph = () => {
    setIsProvenanceGraph(true);
    setIsNetworkXGraph(false); 
    setBackendError(false);
    setIsLoading(true);
    setSvgContent(null);
    fetchProvenanceSvgResource()
  };

  const handleGenerateNetworkXGraph = () => {
    setIsNetworkXGraph(true); 
    setIsProvenanceGraph(false);
    setBackendError(false);
    setIsLoading(true);
    setPngContent(null);
    fetchNetworkXPngResource()
  };

  // Fetch Provenance SVG resource from backend
  const fetchProvenanceSvgResource = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/provenance/model2/graph?startTime=${intervalStart.toString()}&endTime=${intervalEnd.toString()}`
      );
      setSvgContent(response.data); // Update SVG content state
      setBackendError(null); // Clear any previous errors
    } catch (error) {
      console.error('Error fetching Provenance SVG:', error);
      setBackendError(error); // Set the backend error
    } finally {
      setIsLoading(false) // No need to display the loading indicator
    }
  };

    const fetchNetworkXPngResource = () => {
      setIsLoading(false)
    }

    // // Fetch Provenance SVG resource from backend
    // const fetchNetworkXPngResource = async () => {
    //   try {
    //     const response = await axios.get(
    //       `http://localhost:8080/api/networkx/graph?startTime=${intervalStart.toString()}&endTime=${intervalEnd.toString()}`
    //     );
    //     setPngContent(response.data); // Update SVG content state
    //     setBackendError(null); // Clear any previous errors
    //   } catch (error) {
    //     console.error('Error fetching NetworkX PNG:', error);
    //     setBackendError(error); // Set the backend error
    //   } finally {
    //     setIsLoading(false) // No need to display the loading indicator
    //   }
    // };

  return (
    <div className='model2-container'>
      <div className='content-container'>

        <Alert variant="info">
          Select Two Intervals: Drag the thumbs to choose two intervals on the slider.
        </Alert>

        <Slider />

        <div className="button-container">
          <Button variant="primary" onClick={handleGenerateProvenanceGraph}>
            Show Provenance Graph
          </Button>
          <Button variant="primary" onClick={handleGenerateNetworkXGraph}>
            Show NetworkX Graph
          </Button>
        </div>


        <div className="graph-container">
          {isLoading ? (
            <LoadingIndicator />
          ) : backendError ? (
            <Alert variant="danger">
              <strong>Error:</strong> Failed to fetch graph from the backend.
            </Alert>
          ) : (
            <>
              {/* Conditionally render the SVG or PNG content based on selected graph */}
              {isProvenanceGraph && <div dangerouslySetInnerHTML={{ __html: svgContent }} />}
              {isNetworkXGraph && <p>NetworkX Graph</p>}
            </>
          )}
        </div>

      </div>
    </div>
  );
};

export default ReactionsPage;
