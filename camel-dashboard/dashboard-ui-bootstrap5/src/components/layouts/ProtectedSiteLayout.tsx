import React, { Suspense } from "react";
import ProtectedSiteNavbar from "../Navbars/ProtectedSiteNavbar";
import ProtectedSiteFooter from "../Footers/ProtectedSiteFooter";
import ProtectedSiteSidebar from "../Sidebar/ProtectedSiteSidebar";
import TopNav from "../TopNav/TopNav";
import { logger } from "../../utils/logging/Logger";
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