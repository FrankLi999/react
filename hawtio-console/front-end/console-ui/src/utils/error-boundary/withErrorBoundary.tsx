import { ComponentType } from 'react';
import ErrorBoundary from './ErrorBoundary';

export const withErrorBoundary = <P extends object>(WrappedComponent: ComponentType<P>) => {
  const componentWithErrorBoundary = (props: P) => {
    return (
      <ErrorBoundary>
        <WrappedComponent {...props} />
      </ErrorBoundary>
    );
  };
  return componentWithErrorBoundary;
};
