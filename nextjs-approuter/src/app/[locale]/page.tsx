'use client'
import { useState, lazy} from 'react';
import initTranslations from '@/i18n/i18n';
import styles from './page.module.css';
import ExampleClientComponent from '@/components/ExampleClientComponent';
import TranslationsProvider from '@/components/TranslationsProvider';
import LanguageChanger from '@/components/LanguageChanger';
import MainStyleedComponent from '@/components/MainStyleedComponent';
import ThemeToggle from '@/components/ThemeToggle';
import SkeletonCard from '@/components/SkeletonCard';
// import dynamic from 'next/dynamic';

// const NextjsRemoteComponent = dynamic(() => import('remote/nextjs-remote-component'), {
//   ssr: false,
// });
// const NextjsRemoteComponent = process.browser ? lazy(
//  const NextjsRemoteComponent = `typeof window`  ? lazy(() => import('remote/nextjs-remote-component').catch(console.error)) : ()=>null
const i18nNamespaces = ['home'];

//  async/await is not yet supported in Client Components, only Server Components.
// export default function Home({ params: { locale } }: {
export default async function Home({ params: { locale } }: {
    params: {
        locale: string;
    };}) {
  const [user, setUser] = useState(null);    
  const { t, resources } = await initTranslations(locale, i18nNamespaces);
  return (
    <TranslationsProvider
      namespaces={i18nNamespaces}
      locale={locale}
      resources={resources}>
      
      <main className={styles.main}>
        <h1>{t('header')}</h1>
        <ExampleClientComponent />
        <br/><br/>
        <LanguageChanger/>
        
      </main>
      
      <MainStyleedComponent />
        <br/><br/>
        <ThemeToggle/>
        <br/><br/>
        <SkeletonCard/>
        <br/><br/>
        { /* <NextjsRemoteComponent /> */}
    </TranslationsProvider>
  );
}