import React, { ReactNode } from 'react';
import PublicSiteNavbar from "../components/Navbars/PublicSiteNavbar";
import PublicSiteFooter from "../components/Footers/PublicSiteFooter";

// @ts-ignore
const PublicSiteLayout = ({ children }) => {
    return (
        <>
            <div id="layoutAuthentication" className="bg-primary">
                <PublicSiteNavbar />
                <div id="layoutAuthentication_content" >
                    <main>                    
                        { children }
                    </main>
                    
                </div>
                <PublicSiteFooter />
            </div>
        </>
    );
}


export default PublicSiteLayout;