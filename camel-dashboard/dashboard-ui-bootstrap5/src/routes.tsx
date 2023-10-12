
import { createHashRouter, redirect } from "react-router-dom";
import PublicSite from "./views/public/PublicSite";
import AuthGuard from "./views/protected/AuthGuard";
import Error404 from "./views/error/Error404";
import { lazy } from "react";
import { ConfigurationModel } from "./views/protected/configuration-data/ConfigurationModel";
const ConfigurationDataForm = lazy(() => import("./views/protected/configuration-data/ConfigurationDataForm"));
const Configurations = lazy(() => import("./views/protected/configuration-data/Configurations"));
const ConfigurationAppDetails = lazy(() => import("./views/protected/configuration-data/ConfigurationAppDetails"));
const RefreshConfiguration = lazy(() => import("./views/protected/configuration-data/RefreshConfiguration"));
const Login = lazy(() => import("./views/public/Login"));
const LockScreen = lazy(() => import("./views/public/LockScreen"));
// var routes = createBrowserRouter([
var routes = createHashRouter([    
    
    {
        path: "/integrator",
        element: <AuthGuard />,
        children: [{
            path: "/integrator/configuration-data",
            element: <Configurations/>
        },
        {
            path: "/integrator/configuration-app-details",
            element:<ConfigurationAppDetails/>
        },
        {
            path: "/integrator/configuration-form",
            element: <ConfigurationDataForm/>
        },
        {
            path: "/integrator/refresh-configuration",
            element: <RefreshConfiguration/>
        }]
    },
    {
        path: "/public",
        element: <PublicSite />,
        children: [{
            path: "/public/login",
            element: <Login/>
        },
        {
            path: "/public/lock-screen",
            element: <LockScreen/>
        }]
    },
    {
        path: "/",
        loader: () => redirect("/integrator/configuration-data"),
    },
    {
        path: "*",
        element: <Error404 />,
    }
]);

export default routes;