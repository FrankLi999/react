import { FieldErrorProps, FormContextType, RJSFSchema, StrictRJSFSchema, errorId } from '@rjsf/utils';
import { FormHelperText, HelperText, HelperTextItem } from '@patternfly/react-core';

/** The `FieldErrorTemplate` component renders the errors local to the particular field
 *
 * @param props - The `FieldErrorProps` for the errors being rendered
 */
export default function FieldErrorTemplate<
  T = any,
  S extends StrictRJSFSchema = RJSFSchema,
  F extends FormContextType = any
>(props: FieldErrorProps<T, S, F>) {
  const { errors = [], idSchema } = props;
  if (errors.length === 0) {
    return null;
  }
  const id = errorId<T>(idSchema);

  return (
    <HelperText id={id}>
      {errors.map((error, i) => {
        return (
          <FormHelperText>
            <HelperText>
              <HelperTextItem variant="error" key={i} className='pf-v5-u-p-0, pf-v5-u-m-0'>
                <small className='m-0 text-danger'>{error}</small>
              </HelperTextItem>
            </HelperText> 
          </FormHelperText>
        );
      })}
    </HelperText>
  );
}
