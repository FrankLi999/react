## Form
jsonpointer
json-schema

## validators

    import validator from '@react-jsf/ajv';

    <Form schema={ConfigDataSchema} uiSchema={ConfigDataFormUISchemaEdit} formData={formData} validator={validator} onSubmit={data => submitData(data)} />
      props.validator is required
      createSchemaUtils (props.validator)



    AJV8PrecompiledValidator
    AJV8Validator
    compileSchemaValidators -> compileSchemaValidatorsCode -> schemaParser ->parseSchema ->ParserValidator
	
	
	SchemaField 
	   FieldTemplate
	   FiedlErrorTemplate
	   FieldHelpTemplate
	   
	   
	   
Error:
		ErrorListProps
		    ErrorSchema: {
		      key: ErrorSchema,
			  nest:
				_errors: string[]			  
		    }
		    FormValidationError[] = { name, property, message/params, stack, schemaPath}
		   
		FieldErrorProps

           ErrorSchema
           errors: Array<string|ReactElement>		   
		   
Registry:

	fields: RegistryFieldsType		   
		 [name: string]: Field<T, S, F>;
	templates: TemplatesType
	widgets: RegistryWidgetsType	 
		 [name: string]: Widget<T, S, F>;


WidgetProps
	rawError: string[]	