import { hawtio, HawtioPlugin } from '@hawtio/react';
import { RefreshConfig } from './refresh-config';

export const registerRefreshConfig: HawtioPlugin = () => {
  hawtio.addPlugin({
    id: 'refresh-configurations',
    title: 'Refresh Configurations',
    path: '/refresh-config',
    component: RefreshConfig,
    isActive: async () => true,
  });
};
