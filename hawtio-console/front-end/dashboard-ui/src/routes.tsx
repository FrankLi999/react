
import { createHashRouter, createBrowserRouter, redirect } from "react-router-dom";
import PublicSite from "./pages/public/PublicSite";
import AuthGuard from "./pages/protected/AuthGuard";
import Error404 from "./pages/error/Error404";
import { lazy } from "react";
import { ConfigurationModel } from "./pages/protected/configuration-data/ConfigurationModel";
import Oauth2LoginRedirect from "./pages/public/Oauth2LoginRedirect";
const ConfigurationDataEditForm = lazy(() => import("./pages/protected/configuration-data/ConfigurationDataEditForm"));
const ConfigurationDataCreateForm = lazy(() => import("./pages/protected/configuration-data/ConfigurationDataCreateForm"));
const Configurations = lazy(() => import("./pages/protected/configuration-data/Configurations"));
const ConfigurationAppDetails = lazy(() => import("./pages/protected/configuration-data/ConfigurationAppDetails"));
const RefreshConfiguration = lazy(() => import("./pages/protected/configuration-data/RefreshConfiguration"));
const Login = lazy(() => import("./pages/public/Login"));
const LockScreen = lazy(() => import("./pages/public/LockScreen"));
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
        protected: true,
        icon: 'fas fa-book-open',
        state: "openIntegrators",
        collapse: true,
        name: "nav.springConfig",
        children: [{
            path: "/integrator/configuration-data",
            element: <Configurations/>,
            name: "nav.configData"
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
            element: <RefreshConfiguration/>,
            name: "nav.reloadConfig"
        }]
    },
    {
        path: "/public",
        element: <PublicSite />,
        collapse: true,
        protected: false,
        name: "nav.public",
        icon: 'fas fa-book-open',
        state: "openPublic",
        children: [{
            path: "/public/login",
            name: "nav.login",
            element: <Login/>
        },
        {
            path: "/public/redirect",
            element: <Oauth2LoginRedirect/>
        },
        {
            path: "/public/lock-screen",
            element: <LockScreen/>,
            name: "nav.lockScreen",
        }]
    },
    {
        path: "/",
        redirect: "/integrator/configuration-data",
        loader: async () => redirect("/integrator/configuration-data"),
    },
    {
        path: "*",
        element: <Error404 />,
    }
];

export default routes;