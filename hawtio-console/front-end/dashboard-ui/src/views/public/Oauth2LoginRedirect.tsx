import { useNavigate } from "react-router";
import { useSearchParams, useLocation } from 'react-router-dom';
const Oauth2LoginRedirect = () => {
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useSearchParams();
    const token = searchParams.get('token');
    const error = searchParams.get("error");

    // or 
    // const location = useLocation();
    // const token = new URLSearchParams(location.search).get('token');

    // const headers = new HttpHeaders().set("Authorization", `Bearer ${token}`);
    // /user/api/me
    // saveUserProfile(userProfile);
    // (userProfile as UserProfile).accessToken = token;
    // this._authStateService.loginSuccessful(userProfile as UserProfile);
    navigate(error || !token ? "/public/login" : "/integrator/configuration-data");
    return (
        <p>
             redirect-handler works!
        </p>
    )
}
export default Oauth2LoginRedirect;