import { hawtio, HawtioPlugin } from '@hawtio/react';
import { SpringConfig } from './spring-config';

export const registerSpringConfig: HawtioPlugin = () => {
  hawtio.addPlugin({
    id: 'spring-configurations',
    title: 'Spring Configurations',
    path: '/spring-config',
    component: SpringConfig,
    isActive: async () => true,
  });
};
