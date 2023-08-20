import { configureStore } from '@reduxjs/toolkit';
import sliderReducer from './sliderSlice';

const store = configureStore({
  reducer: {
    slider: sliderReducer,
  },
});

export default store;
