import { Grid, GridItem } from '@patternfly/react-core';
import { Panel, PanelMain, PanelMainBody } from '@patternfly/react-core';
import { Card, CardTitle, CardBody, CardFooter } from '@patternfly/react-core';
import {
  ArrayFieldTemplateItemType,
  ArrayFieldTemplateProps,
  FormContextType,
  getTemplate,
  getUiOptions,
  RJSFSchema,
  StrictRJSFSchema,
} from '@rjsf/utils';

export default function ArrayFieldTemplate<
  T = any,
  S extends StrictRJSFSchema = RJSFSchema,
  F extends FormContextType = any
>(props: ArrayFieldTemplateProps<T, S, F>) {
  const { canAdd, disabled, idSchema, uiSchema, items, onAddClick, readonly, registry, required, schema, title } =
    props;
  const uiOptions = getUiOptions<T, S, F>(uiSchema);
  const ArrayFieldDescriptionTemplate = getTemplate<'ArrayFieldDescriptionTemplate', T, S, F>(
    'ArrayFieldDescriptionTemplate',
    registry,
    uiOptions
  );
  const ArrayFieldItemTemplate = getTemplate<'ArrayFieldItemTemplate', T, S, F>(
    'ArrayFieldItemTemplate',
    registry,
    uiOptions
  );
  const ArrayFieldTitleTemplate = getTemplate<'ArrayFieldTitleTemplate', T, S, F>(
    'ArrayFieldTitleTemplate',
    registry,
    uiOptions
  );
  // Button templates are not overridden in the uiSchema
  const {
    ButtonTemplates: { AddButton },
  } = registry.templates;
  return (
    <div>
      <Grid className='pf-v5-u-p-0, pf-v5-u-m-0'>
        <GridItem className='pf-v5-u-p-0, pf-v5-u-m-0'>
          <ArrayFieldTitleTemplate
            idSchema={idSchema}
            title={uiOptions.title || title}
            schema={schema}
            uiSchema={uiSchema}
            required={required}
            registry={registry}
          />
          <ArrayFieldDescriptionTemplate
            idSchema={idSchema}
            description={uiOptions.description || schema.description}
            schema={schema}
            uiSchema={uiSchema}
            registry={registry}
          />
          
          <Panel>
            <PanelMain>  
            <PanelMainBody key={`array-item-list-${idSchema.$id}`} className='pf-v5-u-p-0, pf-v5-u-m-0'>
            {items &&
              items.map(({ key, ...itemProps }: ArrayFieldTemplateItemType<T, S, F>) => (
                <ArrayFieldItemTemplate key={key} {...itemProps} />
              ))}
            {canAdd && (
              <Card ouiaId="BasicCard">
                <Grid className='pf-v5-u-mt-xs'>
                  <GridItem span={9}></GridItem>
                  <GridItem span={3} className='pf-v5-u-py-sm'>
                    <AddButton
                      className='array-item-add' 
                      onClick={onAddClick}
                      disabled={disabled || readonly}
                      uiSchema={uiSchema}
                      registry={registry}
                    />
                  </GridItem>
                </Grid>
              </Card>
            )}
            </PanelMainBody>
            </PanelMain>  
          </Panel>  
        </GridItem>
      </Grid>
    </div>
  );
}
