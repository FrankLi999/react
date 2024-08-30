import { HawtioPlugin } from '@hawtio/react';
import { registerSpringConfig } from './spring-config';
import { registerRefreshConfig } from './refresh-config';
export const registerMyPlugins: HawtioPlugin = () => {
  registerSpringConfig();
  registerRefreshConfig();
};
