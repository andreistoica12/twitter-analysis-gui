import axios from 'axios';
import React, { useState } from 'react';
import { Container, Alert, Button } from 'react-bootstrap';
import MultiRangeSlider  from '../multiRangeSlider/MultiRangeSlider'
import LoadingIndicator from '../components/LoadingIndicator'


const Model2Page = () => {
  const [intervalStart, setIntervalStart] = useState(0);
  const [intervalEnd, setIntervalEnd] = useState(15);
  const [isLoading, setIsLoading] = useState(false);
  const [svgContent, setSvgContent] = useState(null); // To hold SVG content
  const [backendError, setBackendError] = useState(null); // When the backend server is not running


  // Handle interval change
  const handleIntervalChange = (values) => {
    setIntervalStart(values[0]);
    setIntervalEnd(values[1]);
  };

  const handleGenerateGraph = () => {
    setIsLoading(true);
    setSvgContent(null);
    fetchSvgResource()
  };

  // Fetch SVG resource from backend
  const fetchSvgResource = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/provenance/model2/graph?startTime=${intervalStart.toString()}&endTime=${intervalEnd.toString()}`
      );
      setSvgContent(response.data); // Update SVG content state
      setBackendError(null); // Clear any previous errors
    } catch (error) {
      console.error('Error fetching SVG:', error);
      setBackendError(error); // Set the backend error
    } finally {
      setIsLoading(false) // No need to display the loading indicator
    }
  };

  return (
    <div className='model2-container'>
      <div className='content-container'>

        <Alert variant="info">
          Select Two Intervals: Drag the thumbs to choose two intervals on the slider.
        </Alert>

        <Container className="my-5 slider-container">
          <div className="mb-4">
              <MultiRangeSlider
                  min={0}
                  max={2000}
                  onChange={({ min, max }) => {
                    handleIntervalChange([min, max]);
                  }}
              />
          </div>
        </Container>

        <Button variant="primary" onClick={handleGenerateGraph}>
          Show Graph
        </Button>

        <div className="svg-container">
          {isLoading ? (
            <LoadingIndicator />
          ) : backendError ? (
            <Alert variant="danger">
              <strong>Error:</strong> Failed to fetch graph from the backend.
            </Alert>
          ) : (
            <div dangerouslySetInnerHTML={{ __html: svgContent }} />
          )}
        </div>

      </div>
    </div>
  );
};

export default Model2Page;
