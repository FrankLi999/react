import { useContext, useMemo } from "react";
import { assertErrorBoundaryContext } from "./assertErrorBoundaryContext";
import { ErrorBoundaryContext } from "./ErrorBoundaryContext";

// type UseErrorBoundaryState<TError> =
//   | { error: TError; hasError: true }
//   | { error: null; hasError: false };

export type UseErrorBoundaryApi<TError> = {
  hasError: boolean,
  error: any,   
  resetBoundary: () => void;
  showBoundary: (error: TError) => void;
};

export function useErrorBoundary<TError = any>(): UseErrorBoundaryApi<TError> {
  const context = useContext(ErrorBoundaryContext);

  assertErrorBoundaryContext(context);

//   const [state, setState] = useState<UseErrorBoundaryState<TError>>({
//     error: null,
//     hasError: false,
//   });

  const memoized = useMemo(
    () => ({
      hasError: context.hasError,
      error: context.error,  
      resetBoundary: () => {
        context.resetErrorBoundary();
        // setState({ error: null, hasError: false });
      },
      showBoundary: (error: TError) => {
        context.showBoundary(error as any);
        // setState({
        //    error,
        //    hasError: true,
        // })
      }
    }),
    [context.resetErrorBoundary]
  );

//   if (state.hasError) {
//     throw state.error;
//   }

  return memoized;
}