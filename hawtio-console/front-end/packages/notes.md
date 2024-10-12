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
	
Render form:

    Framework specific Form component:
	  core Form compoment

	    ErrorListTemplate: ErrorList,
		SchemaField 
		   <FieldTemplate>
		       - select field -> Object, String, Null, AnyOf, Array, OneOf, schema, boolean, number
			   
			    ArrayField:
					ArrayFieldTemplate
					  ArrayFieldItemTemplate
					    ArrayFieldTitleTemplate
						  TitleFieldTemplate: TitleField,
					    ArrayFieldDescriptionTemplate 
						
					Multiple choice - array of string (enum) - renderMultiSelect
					    SelectWidget
                    List with fixed items + additional items -  renderFixedArray()
					custom widget: renderCustomWidget	
						<Widget>
					files array: renderFiles
					    FilesWidget
				    List with Minumum ahd rest: renderNormalArray
					  <ArrayFieldTemplate {...arrayProps} />
  
				ObjectField: fieldset
				  ObjectFieldTemplate
				   TitleFieldTemplate: TitleField,
				   DescriptionFieldTemplate
				   {properties.map((prop: ObjectFieldTemplatePropertyType) => prop.content)}
				       each property -> SchemaField
				   AddButton
				MultiSchemaField-> 
				  AnyOfField
                    Number Widget
                    SchemaField					
				  OneOfField
				    Number Widget
                    SchemaField
					Add Button
		   <FieldTemplate>
		   - select fields -> Object, String, Null, AnyOf, Array, OneOf, schema, boolean, number
		   - select template
		   - select widgets
		
Template:		
		  FieldTemplate
		    <WrapIfAdditionalTemplate {...props}>
			  {displayLabel && <Label label={label} required={required} id={id} />}
			  {displayLabel && description ? description : null}  // DescriptionFieldTemplate: DescriptionField,
			  {children}
			  {errors} - // FieldErrorTemplate
			  {help}     // FieldHelpTemplate
			</WrapIfAdditionalTemplate>
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


COMPONENT_TYPES:
	const COMPONENT_TYPES: { [key: string]: string } = {
	  array: 'ArrayField',
	  boolean: 'BooleanField',
	  integer: 'NumberField',
	  number: 'NumberField',
	  object: 'ObjectField',
	  string: 'StringField',
	  null: 'NullField',
	};
		   
Registry:

	fields: RegistryFieldsType		   
		 [name: string]: Field<T, S, F>;
	templates: TemplatesType
	widgets: RegistryWidgetsType	 
		 [name: string]: Widget<T, S, F>;


WidgetProps
	rawError: string[]	