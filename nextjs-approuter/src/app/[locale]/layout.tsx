import './globals.css';
import { dir } from 'i18next';
import type { Metadata } from 'next';
import { languages } from '@/i18n/settings';
import StyledComponentsRegistry from '@/lib/registry';
import { Providers } from '@/components/Providers';

export async function generateStaticParams() {
  return languages.map((lng) => ({ lng }))
}

export const metadata: Metadata = {
	title: 'NextJS App Router, Styled Components, i18next, federation',
	description:
		'A simple project starter to work with TypeScrip, React, NextJS and Styled Components, i18next, Federation',
	icons: '/img/icon-512.png',
	manifest: 'manifest.json'
};


export default function RootLayout({
  children,
  params: {
    lng
  }
}) {
  return (
    <html lang={lng} dir={dir(lng)}>
      <head />
      <body>
        <StyledComponentsRegistry>
          <Providers>{children}</Providers>
        </StyledComponentsRegistry>
      </body>
    </html>
  )
}