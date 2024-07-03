
import { createHashRouter, createBrowserRouter, redirect } from "react-router-dom";
import PublicSite from "./views/public/PublicSite";
import AuthGuard from "./views/protected/AuthGuard";
import Error404 from "./views/error/Error404";
import { lazy } from "react";
import { ConfigurationModel } from "./views/protected/configuration-data/ConfigurationModel";
import Oauth2LoginRedirect from "./views/public/Oauth2LoginRedirect";
const ConfigurationDataEditForm = lazy(() => import("./views/protected/configuration-data/ConfigurationDataEditForm"));
const ConfigurationDataCreateForm = lazy(() => import("./views/protected/configuration-data/ConfigurationDataCreateForm"));
const Configurations = lazy(() => import("./views/protected/configuration-data/Configurations"));
const ConfigurationAppDetails = lazy(() => import("./views/protected/configuration-data/ConfigurationAppDetails"));
const RefreshConfiguration = lazy(() => import("./views/protected/configuration-data/RefreshConfiguration"));
const Login = lazy(() => import("./views/public/Login"));
const LockScreen = lazy(() => import("./views/public/LockScreen"));
// var routes = createBrowserRouter([
// // var routes = createHashRouter([    
//     {
//         path: "/integrator",
//         element: <AuthGuard />,
//         children: [{
//             path: "/integrator/configuration-data",
//             element: <Configurations/>
//         },
//         {
//             path: "/integrator/configuration-app-details",
//             element:<ConfigurationAppDetails/>
//         },
//         {
//             path: "/integrator/configuration-form-edit",
//             element: <ConfigurationDataEditForm/>
//         },
//         {
//             path: "/integrator/configuration-form-create",
//             element: <ConfigurationDataCreateForm/>
//         },
//         {
//             path: "/integrator/refresh-configuration",
//             element: <RefreshConfiguration/>
//         }]
//     },
//     {
//         path: "/public",
//         element: <PublicSite />,
//         children: [{
//             path: "/public/login",
//             element: <Login/>
//         },
//         {
//             path: "/public/redirect",
//             element: <Oauth2LoginRedirect/>
//         },
//         {
//             path: "/public/lock-screen",
//             element: <LockScreen/>
//         }]
//     },
//     {
//         path: "/",
//         loader: () => redirect("/integrator/configuration-data"),
//     },
//     {
//         path: "*",
//         element: <Error404 />,
//     }
// ]);

var routes = [
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
            path: "/integrator/configuration-form-edit",
            element: <ConfigurationDataEditForm/>
        },
        {
            path: "/integrator/configuration-form-create",
            element: <ConfigurationDataCreateForm/>
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
            path: "/public/redirect",
            element: <Oauth2LoginRedirect/>
        },
        {
            path: "/public/lock-screen",
            element: <LockScreen/>
        }]
    },
    {
        path: "/",
        loader: async () => redirect("/integrator/configuration-data"),
    },
    {
        path: "*",
        element: <Error404 />,
    }
];

export default routes;