import { Card, CardHeader, CardBody } from '@patternfly/react-core';
import {  List, ListItem, FormHelperText, HelperText, HelperTextItem } from '@patternfly/react-core';

import { ErrorListProps, FormContextType, RJSFSchema, StrictRJSFSchema, TranslatableString } from '@react-jsf/utils';

export default function ErrorList<T = any, S extends StrictRJSFSchema = RJSFSchema, F extends FormContextType = any>({
  errors,
  registry,
}: ErrorListProps<T, S, F>) {
  const { translateString } = registry;
  return (
    <Card className='pf-v5-u-mb-sm, pf-m-error'>
      <CardHeader className='alert-danger'>{translateString(TranslatableString.ErrorsLabel)}</CardHeader>
      <CardBody className='pf-v5-u-p-0'>
        <FormHelperText>
          <List isPlain>
            {errors.map((error, i: number) => {
              return (
                <ListItem key={i}>
                  <HelperText><HelperTextItem variant="error">{error.stack}</HelperTextItem></HelperText>
                </ListItem>
              );
            })}
          </List>
        </FormHelperText>
      </CardBody>
    </Card>
  );
}
