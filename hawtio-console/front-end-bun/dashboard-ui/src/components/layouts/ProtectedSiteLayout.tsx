import { Suspense } from "react";
import ProtectedSiteNavbar from "../Navbars/ProtectedSiteNavbar";
import ProtectedSiteFooter from "../Footers/ProtectedSiteFooter";
import ProtectedSiteSidebar from "../Sidebar/ProtectedSiteSidebar";
import TopNav from "../TopNav/TopNav";
import { logger } from "../../utils/logging/Logger";
import ErrorBoundary from "../../utils/error-boundary/ErrorBoundary";
import ErrorBoundaryContextProvider from "../../utils/error-boundary/ErrorBoundaryContextProvider";
import { useMyConfigContext } from "../../utils/config/context";

// @ts-ignore
const ProtectedSiteLayout = ({ children }) => {
    logger.log("generated ProtectedSiteLayout.", "log level");
    const { config } = useMyConfigContext();
    console.log(">>>>>resolved my config", config);
     //  const [errorBoundaryKey, setErrorBoundaryKey] = React.useState(null)
    // const logError = (error: Error, info: { componentStack: string }) => {
    //   console.error("Caught an error:", error, info);
    // };

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
                        <ErrorBoundaryContextProvider>
                          <ErrorBoundary>
                            { children }
                          </ErrorBoundary>
                        </ErrorBoundaryContextProvider>
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