import { GlobalStateProvider } from '@/context/global/GolobalStateProvider';
import { Inter, Outfit } from "next/font/google";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-perfect-scrollbar/dist/css/styles.css';
import '@/styles/scss/nextjs-admin.scss';
// const inter = Inter({ subsets: ["latin"] });
// metadata
export const metadata = {
  title: 'Camel | Admin dashboard',
  description: 'NextJs camel admin dashboard',
  keywords: ['NextJs', 'React NextJs', 'Next.js', 'React template', 'react admin', 'react node', 'react bootstrap', 'responsive web application', 'react webapp', 'Camel dashboard'],
}


const inter = Inter({
  weight: ['300', '400', '500', '600', '700'],
  subsets: ['latin'],
  variable: "--tg-body-font-family",
  display: 'swap',
})
const outfit = Outfit({
  weight: [ '400', '500', '600', '700','800','900'],
  subsets: ['latin'],
  variable: "--tg-heading-font-family",
  display: 'swap',
})


export default function RootLayout({ children }: {
  children: React.ReactNode;
}) {

  return (
    <html lang="en">
      { /* <body className={`${inter.className}> */}
      <body className={`${inter.variable} ${outfit.variable}`}>
        <GlobalStateProvider>
          {children}
        </GlobalStateProvider>
      </body>
    </html>
  )
}
