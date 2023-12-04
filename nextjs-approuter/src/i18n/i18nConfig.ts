import { Config } from 'next-i18n-router';
export const i18nConfig: Config = {
    locales: ['en', 'fr'],
    defaultLocale: 'en'
    // localeCookie?: string;
    // localeDetector?: ((request: NextRequest, config: Config) => string) | false;
    // prefixDefault?: boolean;
    // basePath?: string;
};
