import React from "react";
import ProtectedSiteNavbar from "../components/Navbars/ProtectedSiteNavbar";
import ProtectedSiteFooter from "../components/Footers/ProtectedSiteFooter";
import Sidebar from "../components/Sidebar/Sidebar";
// @ts-ignore
const ProtectedSiteLayout = ({ children }) => {
    return (
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
    );
}

export default ProtectedSiteLayout;