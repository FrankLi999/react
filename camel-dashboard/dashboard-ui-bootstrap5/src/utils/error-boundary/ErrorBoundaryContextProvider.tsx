import React from "react";
import { useErrorBoundary  } from './useErrorBoundary';
import { ErrorBoundaryContext } from "./ErrorBoundaryContext";
type ErrorBoundaryState =
  | {
      hasError: true;
      error: any;
    }
  | {
      hasError: false;
      error: null;
    };

const initialState: ErrorBoundaryState = {
  hasError: false,
  error: null,
};

const ErrorBoundaryContextProvider = ({ children }) => {
  const [state, setState] = React.useState<ErrorBoundaryState>(initialState);
  const resetErrorBoundary = () => {
     setState(initialState);
  }
  const showBoundary = (error) => {
    setState({
        hasError: true,
        error: error
        
    });
 }
  return <ErrorBoundaryContext.Provider value={{
    hasError: state.hasError,
    error: state.error,
    resetErrorBoundary: resetErrorBoundary,
    showBoundary: showBoundary
  }}>
    {children}
    </ErrorBoundaryContext.Provider>;
};
export default ErrorBoundaryContextProvider;