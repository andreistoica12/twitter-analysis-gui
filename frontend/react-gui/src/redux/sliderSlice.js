import { createSlice } from '@reduxjs/toolkit';

export const sliderSlice = createSlice({
  name: 'slider',
  initialState: {
    intervalStart: 0,
    intervalEnd: 15,
  },
  reducers: {
    setIntervalStart: (state, action) => {
      state.intervalStart = action.payload;
    },
    setIntervalEnd: (state, action) => {
      state.intervalEnd = action.payload;
    },
  },
});

export const { setIntervalStart, setIntervalEnd, setCombination } = sliderSlice.actions
export default sliderSlice.reducer
