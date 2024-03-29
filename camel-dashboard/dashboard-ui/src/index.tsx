import React from 'react';
import ReactDOM from 'react-dom/client';
import { CookiesProvider } from 'react-cookie';
import { RouterProvider } from "react-router-dom";
import "@fortawesome/fontawesome-free/css/all.min.css";
import "./assets/scss/my-camel-dashboard.scss";
import routes from "./routes";
import Auth from "./auth/Auth";
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    <CookiesProvider>
    <Auth>
      <RouterProvider router={routes} />
    </Auth>
    </CookiesProvider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
