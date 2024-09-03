import { Suspense } from "react";
import PublicSiteNavbar from "../Navbars/PublicSiteNavbar";
import PublicSiteFooter from "../Footers/PublicSiteFooter";

// @ts-ignore
const PublicSiteLayout = ({ children }) => {
    return (
        <Suspense>
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
        </Suspense>
    );
}


export default PublicSiteLayout;