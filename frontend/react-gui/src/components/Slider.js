import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import MultiRangeSlider from '../multiRangeSlider/MultiRangeSlider';
import { setIntervalStart, setIntervalEnd } from '../redux/sliderSlice'; // Import the action creators


const Slider = () => {
  const dispatch = useDispatch();
  const intervalStart = useSelector((state) => state.slider.intervalStart);
  const intervalEnd = useSelector((state) => state.slider.intervalEnd);

  const handleIntervalChange = (values) => {
    dispatch(setIntervalStart(values[0]));
    dispatch(setIntervalEnd(values[1]));
  };

  return (
    <div>
      <MultiRangeSlider
        min={0}
        max={1000}
        values={[intervalStart, intervalEnd]}
        onChange={({ min, max }) => {
          handleIntervalChange([min, max]);
        }}      
      />
    </div>
  );
};

export default Slider;
