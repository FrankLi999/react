import { FocusEvent } from 'react';
import {
  ADDITIONAL_PROPERTY_FLAG,
  FormContextType,
  RJSFSchema,
  StrictRJSFSchema,
  TranslatableString,
  WrapIfAdditionalTemplateProps,
} from '@rjsf/utils';
import { Grid, GridItem } from '@patternfly/react-core';
import { FormGroup, TextInput } from '@patternfly/react-core';

export default function WrapIfAdditionalTemplate<
  T = any,
  S extends StrictRJSFSchema = RJSFSchema,
  F extends FormContextType = any
>({
  classNames,
  style,
  children,
  disabled,
  id,
  label,
  onDropPropertyClick,
  onKeyChange,
  readonly,
  required,
  schema,
  uiSchema,
  registry,
}: WrapIfAdditionalTemplateProps<T, S, F>) {
  const { templates, translateString } = registry;
  // Button templates are not overridden in the uiSchema
  const { RemoveButton } = templates.ButtonTemplates;
  const keyLabel = translateString(TranslatableString.KeyLabel, [label]);
  const additional = ADDITIONAL_PROPERTY_FLAG in schema;

  if (!additional) {
    return (
      <div className={classNames} style={style}>
        {children}
      </div>
    );
  }

  const handleBlur = ({ target }: FocusEvent<HTMLInputElement>) => onKeyChange(target.value);
  const keyId = `${id}-key`;

  return (
    <Grid className={classNames} style={style} key={keyId}>
      <GridItem sm={5}>
        <FormGroup label={keyLabel} fieldId={keyId}>
          <TextInput
            isRequired={required}
            defaultValue={label}
            isDisabled={disabled || readonly}
            id={keyId}
            name={keyId}
            onBlur={!readonly ? handleBlur : undefined}
          />
        </FormGroup>
      </GridItem>
      <GridItem sm={5}>{children}</GridItem>
      <GridItem sm={2} className='py-4'>
        <RemoveButton
          iconType='block'
          className='w-100'
          disabled={disabled || readonly}
          onClick={onDropPropertyClick(label)}
          uiSchema={uiSchema}
          registry={registry}
        />
      </GridItem>
    </Grid>
  );
}
