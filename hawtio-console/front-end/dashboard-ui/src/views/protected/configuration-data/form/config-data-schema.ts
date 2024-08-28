import { RJSFSchema } from '@react-jsf/utils';

const ConfigDataSchema: RJSFSchema = {
  title: 'Config Server Data form',
  description: 'Config Server Data form.',
  type: 'object',
  required: ['application', 'profile', 'label'],
  properties: {
    application: {
      type: 'string',
      title: 'Spring application name',
    },
    profile: {
      type: 'string',
      title: 'Spring Profile',
      default: 'default',
    },
    label: {
      type: 'string',
      title: 'Spring config label',
      default: 'master',
    },
    props: {
      type: 'array',
      items: {
        type: 'object',
        uniqueItems: true,
        minItems: 1,
        properties: {
          propKey: {
            type: 'string',
          },
          propValue: {
            type: 'string',
          },
        },
      },
    },
  },
};
export default ConfigDataSchema;
