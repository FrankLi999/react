import ProtectedSiteLayout from "../../components/layouts/ProtectedSiteLayout";
import { Outlet, Navigate } from "react-router-dom";
import { isLoggedIn } from '../../service/auth.service';
const AuthGuard = () => {
    const authenticated = true; //isLoggedIn();

    return authenticated ? (
        <ProtectedSiteLayout>
            <Outlet />
        </ProtectedSiteLayout>
    ) : (
        <Navigate to="/public/login" />
    );
}
export default AuthGuard;