import { Suspense, Fragment } from "react";
import ProtectedSiteLayout from "../../layouts/ProtectedSiteLayout";
import { Outlet, Navigate } from "react-router-dom";
function AuthGuard() {
    let authenticated = true;
    return authenticated ? (
        <Fragment>
            <ProtectedSiteLayout>
                <Outlet />
            </ProtectedSiteLayout>
        </Fragment>

    ) : (
        <Navigate to="/public/login" />
    );

}
export default AuthGuard;