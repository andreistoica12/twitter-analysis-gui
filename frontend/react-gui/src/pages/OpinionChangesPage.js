import React, { useState } from 'react'
import { Container, Form } from 'react-bootstrap';

const OpinionChangesPage = () => {

  const [combination, setCombination] = useState([]);
  const [displayedImage, setDisplayedImage] = useState(null);

  const handleOptionChange = (option) => {
    if (combination.includes(option)) {
      // If option is already in the combination, remove it
      setCombination(combination.filter((item) => item !== option));
    } else {
      // If option is not in the combination, add it
      setCombination([...combination, option]);
    }
  };

  // Determine the image filename based on the selected combination
  const getImageFilename = () => {
    if (combination.includes('replies') && combination.includes('quotes')) {
      return 'replied_to_quoted_deltas_OC_25_days.png';
    } else if (combination.includes('replies')) {
      return 'replied_to_deltas_OC_25_days.png';
    } else if (combination.includes('quotes')) {
      return 'quoted_deltas_OC_25_days.png';
    } else {
      return null;
    }
  };

  // Update displayedImage state based on combination changes
  React.useEffect(() => {
    const imageFilename = getImageFilename();
    setDisplayedImage(imageFilename);
  }, [combination]);

  return (
    <div>
      <h3>Select reaction type(s):</h3>

      <Container>
        <Form>
          <Form.Check
            type="checkbox"
            label="Replies"
            value="replies"
            checked={combination.includes('replies')}
            onChange={() => handleOptionChange('replies')}
          />
          <Form.Check
            type="checkbox"
            label="Quotes"
            value="quotes"
            checked={combination.includes('quotes')}
            onChange={() => handleOptionChange('quotes')}
          />
        </Form>
      </Container>


      <h3>Intensities of opinion changes - distribution:</h3>
      {displayedImage && <img src={process.env.PUBLIC_URL + '/OC-graphs/' + displayedImage} alt="Selected Image" />}
    </div>
  );
}

export default OpinionChangesPage