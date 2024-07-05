import React, { Suspense } from "react";
import ProtectedSiteNavbar from "../components/Navbars/ProtectedSiteNavbar";
import ProtectedSiteFooter from "../components/Footers/ProtectedSiteFooter";
import ProtectedSiteSidebar from "../components/Sidebar/ProtectedSiteSidebar";
import TopNav from "../components/TopNav/TopNav";
import { logger } from "../logging/Logger";
// @ts-ignore
const ProtectedSiteLayout = ({ children }) => {
    logger.log("generated ProtectedSiteLayout.", "log level");
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
                        { children }
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