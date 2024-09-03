import { ErrorBoundaryContextType } from "./ErrorBoundaryContext";

export function assertErrorBoundaryContext(
  value: any
): asserts value is ErrorBoundaryContextType {
  if (
    value == null ||
    typeof value.hasError !== "boolean" ||
    typeof value.resetErrorBoundary !== "function" ||
    typeof value.showBoundary !== "function"
  ) {
    throw new Error("ErrorBoundaryContext not found");
  }
}