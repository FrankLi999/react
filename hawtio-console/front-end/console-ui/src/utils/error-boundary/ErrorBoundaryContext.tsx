import { createContext } from "react";

export type ErrorBoundaryContextType = {
  hasError: boolean;
  error: any;
  resetErrorBoundary: (...args: any[]) => void;
  showBoundary: (error: any[]) => void;
};

export const ErrorBoundaryContext =
  createContext<ErrorBoundaryContextType | null>(null);