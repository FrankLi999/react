import { createSlice, PayloadAction } from '@reduxjs/toolkit';

import { ErrorApi, PageError } from '@/state/stateType';

// declaring the types for system state
export type SystemState = {
  flowStarted?: boolean;
  sessionId?: string;
  transactionId?: string;
  errorApi?: ErrorApi;
  httpError?: string;
  pageErrors?: PageError[];
  isBrowserWarningReg?: boolean;
  showAppConfirm?: boolean;
  status: 'idle' | 'loading' | 'failed';
};

const initialState: SystemState = {
  flowStarted: false,
  sessionId: '',
  transactionId: '',
  errorApi: null,
  httpError: null,
  pageErrors: [],
  isBrowserWarningReg: false,
  showAppConfirm: true,
  status: `idle`,
};

export const systemSlice = createSlice({
  name: `system`,
  initialState,
  reducers: {
    setFlowStarted: (state, action: PayloadAction<boolean>) => {
      state.flowStarted = action.payload;
    },
    setSessionId: (state, action: PayloadAction<string>) => {
      state.sessionId = action.payload;
    },
    setTransactionId: (state, action: PayloadAction<string>) => {
      state.transactionId = action.payload;
    },
    setErrorApi: (state, action: PayloadAction<ErrorApi>) => {
      state.errorApi = action.payload;
    },
    setHttpError: (state, action: PayloadAction<string>) => {
      state.httpError = action.payload;
    },
    setPageErrors: (state, action: PayloadAction<PageError[]>) => {
      state.pageErrors = action.payload;
    },
  },
});

export const {
  setFlowStarted,
  setSessionId,
  setTransactionId,
  setHttpError,
  setErrorApi,
  setPageErrors,
} = systemSlice.actions;

export default systemSlice.reducer;
