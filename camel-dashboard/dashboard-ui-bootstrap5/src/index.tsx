import React, { Children } from 'react';
import { CookiesProvider } from 'react-cookie';
import ReactDOM from 'react-dom/client';
import { BrowserRouter,  createBrowserRouter,  redirect, Route, RouterProvider, Routes  } from 'react-router-dom';
import "@fortawesome/fontawesome-free/css/all.min.css";
import "./assets/scss/my-camel-dashboard.scss";
import routes from "./routes";
import reportWebVitals from './reportWebVitals';
import Loader from './components/loader/Loader';
import './i18n';
const getReactRoutes = (routes) => {
  let reactRoutes: any = [];
  routes.map((prop, index) => {
    const route = {
      path: prop.path ? prop.path : undefined,
      key: index,
      loader: prop.loader ? prop.loader : undefined,
      element: prop.element ? prop.element: null,
      children: prop.children ? getReactRoutes(prop.children) : null,
    };
    reactRoutes.push(route);
  });

  return reactRoutes;
};

const getRoutes = (routes) => {
  return routes.map((prop, index) => {
    console.log(`create route for, `, index, prop);
    if (prop.children) {
      console.log(`create child route for, `, prop.path);
      return (
        <Route
          path={prop.path ? prop.path : undefined}
          key={index}
          loader = {prop.loader ? prop.loader: undefined }
          element={prop.element ? prop.element : null}
        >
          {getRoutes(prop.children)}
        </Route>)
    } else {
      return (
        <Route
          path={prop.path ? prop.path : undefined}
          key={index}
          loader = {prop.loader ? prop.loader : () => null }
          element={prop.element ? prop.element : null}
        />
      )
    }
  });
};


const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
const basename = ''; // '/my/camel/spring/admin/dashboard'
root.render(
  <React.StrictMode>
    <CookiesProvider>    
      <RouterProvider router={createBrowserRouter(getReactRoutes(routes))} />
      {/* <BrowserRouter basename={basename}>
        <React.Suspense fallback={<Loader/>}>
          <Routes>
            {getRoutes(routes)} 
          </Routes>
        </React.Suspense>
      </BrowserRouter> */}
    </CookiesProvider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
