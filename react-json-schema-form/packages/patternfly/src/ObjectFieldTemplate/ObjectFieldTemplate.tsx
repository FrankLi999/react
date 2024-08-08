import { Grid, GridItem } from '@patternfly/react-core';
import { Card, Panel } from '@patternfly/react-core';

import {
  canExpand,
  descriptionId,
  FormContextType,
  getTemplate,
  getUiOptions,
  ObjectFieldTemplateProps,
  RJSFSchema,
  StrictRJSFSchema,
  titleId,
} from '@rjsf/utils';

export default function ObjectFieldTemplate<
  T = any,
  S extends StrictRJSFSchema = RJSFSchema,
  F extends FormContextType = any
>({
  description,
  title,
  properties,
  required,
  uiSchema,
  idSchema,
  schema,
  formData,
  onAddClick,
  disabled,
  readonly,
  registry,
}: ObjectFieldTemplateProps<T, S, F>) {
  const uiOptions = getUiOptions<T, S, F>(uiSchema);
  const TitleFieldTemplate = getTemplate<'TitleFieldTemplate', T, S, F>('TitleFieldTemplate', registry, uiOptions);
  const DescriptionFieldTemplate = getTemplate<'DescriptionFieldTemplate', T, S, F>(
    'DescriptionFieldTemplate',
    registry,
    uiOptions
  );
  // Button templates are not overridden in the uiSchema
  const {
    ButtonTemplates: { AddButton },
  } = registry.templates;
  return (
    <>
      {title && (
        <TitleFieldTemplate
          id={titleId<T>(idSchema)}
          title={title}
          required={required}
          schema={schema}
          uiSchema={uiSchema}
          registry={registry}
        />
      )}
      {description && (
        <DescriptionFieldTemplate
          id={descriptionId<T>(idSchema)}
          description={description}
          schema={schema}
          uiSchema={uiSchema}
          registry={registry}
        />
      )}
      <Panel className='pf-v5-u-p-0'>
        {properties.map((element: any, index: number) => (
          <Grid key={index} style={{ marginBottom: '10px' }} className={element.hidden ? 'pf-v5-u-display-none' : undefined}>
            <GridItem sm={12}> {element.content}</GridItem>
          </Grid>
        ))}
        {canExpand(schema, uiSchema, formData) ? (
          <Grid>
            <GridItem sm={3} smOffset={9} className='pf-v5-u-py-sm'>
              <AddButton
                onClick={onAddClick(schema)}
                disabled={disabled || readonly}
                className='object-property-expand'
                uiSchema={uiSchema}
                registry={registry}
              />
            </GridItem>
          </Grid>
        ) : null}
      </Panel>
    </>
  );
}
