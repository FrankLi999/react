import { configManager, hawtio, Hawtio, Logger, registerPlugins } from '@hawtio/react';
import React from 'react';
import ReactDOM from 'react-dom/client';
import { registerMyPlugins } from './plugins';
const hawtioVersion = '4.1.0';

const log = Logger.get('hawtio-console');
log.info('Hawtio console:', hawtioVersion);

// const setBaseHref = () => {
//   console.log(import.meta.env.VITE_APP_BASE_URL);
//   const base = document.createElement('base');
//   base.setAttribute('href', import.meta.env.VITE_APP_BASE_URL);
//   document.head.insertBefore(base, document.head.firstChild);
// }

// setBaseHref();
// Register builtin plugins
registerPlugins();
registerMyPlugins();

hawtio
  // Set up plugin location
  .addUrl('plugin')
  // Bootstrap Hawtio
  .bootstrap();

// Configure the console version
configManager.addProductInfo('Hawtio', hawtioVersion);

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(
  <React.StrictMode>
    <Hawtio />
  </React.StrictMode>
);
