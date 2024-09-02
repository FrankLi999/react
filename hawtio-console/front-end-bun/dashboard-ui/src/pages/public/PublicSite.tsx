import PublicSiteLayout from "../../components/layouts/PublicSiteLayout";
import { Outlet } from "react-router-dom";
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