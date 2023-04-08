import { Action, configureStore, ThunkAction } from '@reduxjs/toolkit';
import systemSlice from './systemSlice';
import s2iSlice from '../s2i/s2iSlice';

export const store = configureStore({
  reducer: {
    system: systemSlice,
    s2i: s2iSlice,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;

export const selectState = (state: RootState) => state;
