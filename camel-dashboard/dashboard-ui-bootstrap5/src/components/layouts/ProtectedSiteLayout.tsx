import React, { Suspense } from "react";
import { ErrorBoundary, useErrorBoundary  } from 'react-error-boundary'
import ProtectedSiteNavbar from "../Navbars/ProtectedSiteNavbar";
import ProtectedSiteFooter from "../Footers/ProtectedSiteFooter";
import ProtectedSiteSidebar from "../Sidebar/ProtectedSiteSidebar";
import TopNav from "../TopNav/TopNav";
import { logger } from "../../utils/logging/Logger";

// function ErrorFallback({ error, resetErrorBoundary }) {
//     // Call resetErrorBoundary() to reset the error boundary and retry the render.
//     return (
//         <div role="alert">
//         <p>Place holder for error handling:</p>
//         <pre>{error.message}</pre>
//         <button onClick={resetErrorBoundary}>Try again</button>
//         </div>
//     )
// }

function ErrorFallback({ error }) {
    // Call resetErrorBoundary() to reset the error boundary and retry the render.
    const { resetBoundary } = useErrorBoundary();
    return (
        <div role="alert">
            <p>Place holder for error handling:</p>
            <pre>{error.message}</pre>
            {/* Dismiss the nearest error boundary*/}
            <button onClick={resetBoundary}>Try again</button>
        </div>
    )
}

// @ts-ignore
const ProtectedSiteLayout = ({ children }) => {
    logger.log("generated ProtectedSiteLayout.", "log level");
     //  const [errorBoundaryKey, setErrorBoundaryKey] = React.useState(null)
    const logError = (error: Error, info: { componentStack: string }) => {
      console.error("Caught an error:", error, info);
    };

    return (
        <Suspense>
        <>
            <div className="sb-nav-fixed">
                
                
                <ProtectedSiteNavbar />
                <div id="layoutSidenav">
                   
                    <div id="layoutSidenav_nav">
                        <ProtectedSiteSidebar />                    
                    </div>
                    <div id="layoutSidenav_content">                 
                      <main>
                        <TopNav />
                        {/* <ErrorBoundary
                            FallbackComponent={ErrorFallback}
                            onReset={() => setErrorBoundaryKey(null)} // reset the state of your app here
                            resetKeys={[errorBoundaryKey]} // reset the error boundary when `someKey` changes
                            onError={logError}
                            > */}
                        <ErrorBoundary
                            FallbackComponent={ErrorFallback}
                            onError={logError}
                            >
                            { children }
                        </ErrorBoundary>
                      </main>
                      <ProtectedSiteFooter />
                    </div>
                </div>
            </div>
        </>
        </Suspense>
    );
}

export default ProtectedSiteLayout;