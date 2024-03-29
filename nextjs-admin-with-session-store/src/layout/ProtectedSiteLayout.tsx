import React, { Suspense } from "react";
import ProtectedSiteNavbar from "../components/Navbars/ProtectedSiteNavbar";
import ProtectedSiteFooter from "../components/Footers/ProtectedSiteFooter";
import Sidebar from "../components/Sidebars/Sidebar";
// @ts-ignore
const ProtectedSiteLayout = ({ children }) => {
    return (
        <Suspense>
        <>
            <div className="sb-nav-fixed">
                
                <ProtectedSiteNavbar />
                <div id="layoutSidenav">
                    <div id="layoutSidenav_nav">
                        <Sidebar />                    
                    </div>
                    <div id="layoutSidenav_content">
                        <main>
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