import PublicSiteLayout from "../../components/layouts/PublicSiteLayout";
import { Outlet, redirect } from "react-router-dom";
const PublicSite = () => {
    return (
        <>
            <PublicSiteLayout>
                <Outlet />
            </PublicSiteLayout>
        </>
    );
};
export default PublicSite;