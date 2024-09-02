import { useErrorBoundary  } from './useErrorBoundary';
const ErrorBoundary = ({ children }) => {
  const { hasError, error, resetBoundary } = useErrorBoundary();
  return <>
    {hasError && (
       <div role="alert">
          <p>Place holder for error handling:</p>
          <pre>{error["message"]}</pre>
          <button onClick={resetBoundary}>Try again</button>
      </div>)}
    {children}
    </>;
};
export default ErrorBoundary;