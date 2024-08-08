import { FormContextType, IconButtonProps, RJSFSchema, StrictRJSFSchema, TranslatableString } from '@rjsf/utils';
import { Button } from '@patternfly/react-core';
import { BsPlus } from 'react-icons/bs';

export default function AddButton<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any>({
  uiSchema,
  registry,
  ...props
}: IconButtonProps<T, S, F>) {
  const { translateString } = registry;
  return (
    <Button
      {...props}
      style={{ width: '100%' }}
      className={`ml-1 ${props.className}`}
      title={translateString(TranslatableString.AddItemButton)}
    >
      <BsPlus />
    </Button>
  );
}
