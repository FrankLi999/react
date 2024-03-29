import { GlobalStateProvider } from '@/context/global/GolobalStateProvider';
import { Inter, Outfit } from "next/font/google";
// import 'bootstrap/dist/css/bootstrap.min.css';
// import 'react-perfect-scrollbar/dist/css/styles.css';
import '@/styles/scss/nextjs-admin.scss';
import NextAuthProvider from '@/context/session/NextAuthProvider';
import * as oracleDBUtil from '@/utils/oracleDBUtil';
import log4jsInit from '@/Log4js';
const inter = Inter({ subsets: ["latin"] });
// metadata
export const metadata = {
  title: 'Camel | Admin dashboard',
  description: 'NextJs camel admin dashboard',
  keywords: ['NextJs', 'React NextJs', 'Next.js', 'React template', 'react admin', 'react node', 'react bootstrap', 'responsive web application', 'react webapp', 'Camel dashboard'],
}


// const inter = Inter({
//   weight: ['300', '400', '500', '600', '700'],
//   subsets: ['latin'],
//   variable: "--tg-body-font-family",
//   display: 'swap',
// })
// const outfit = Outfit({
//   weight: [ '400', '500', '600', '700','800','900'],
//   subsets: ['latin'],
//   variable: "--tg-heading-font-family",
//   display: 'swap',
// })

log4jsInit();
export default async function RootLayout({ children }: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      { /*  <body className={`${inter.variable} ${outfit.variable}`}>*/ }
      <body className={`${inter.className}`}> 
        <NextAuthProvider>
          <GlobalStateProvider>
            {children}
          </GlobalStateProvider>
        </NextAuthProvider>  
      </body>
    </html>
  )
}

/*

import "../styles/styles.css";
import React, { PropsWithChildren } from "react";
import { PageLayout } from "@/components/page-layout";
import { siteMetadata } from "@/components/page-head";
import { PreloadResources } from "@/app/preload-resources";
import { UserProvider } from "@auth0/nextjs-auth0/client";

export const metadata = siteMetadata;

const RootLayout: React.FC<PropsWithChildren> = ({ children }) => {
  return (
    <html lang="en">
      <PreloadResources />
      <body>
        <UserProvider>
          <PageLayout>{children}</PageLayout>
        </UserProvider>
      </body>
    </html>
  );
};

export default RootLayout;
*/