// import Form from "@rjsf/core";
import { RJSFSchema } from '@rjsf/utils';
import React from "react";
import validator from '@rjsf/validator-ajv8';
import Form from "@rjsf/bootstrap-4";
// import schemaForm from '../jsonFiles/schema.json';
// import formData from '../jsonFiles/formData.json';
// import uiSchema from '../jsonFiles/uiSchema.json';



function FormComponent() {
   const schema: RJSFSchema = require("../jsonFiles/config-data-schema.json")
   const formData = require("../jsonFiles/config-data-form-data.json")
   const uiSchema = require("../jsonFiles/config-data-form-uischema.json")
   const log = (type: any) => console.log.bind(console, type);
    // const schema: RJSFSchema = schemaForm;
    return (
        <div className="form">
            <Form
                schema={schema}
                uiSchema={uiSchema}
                formData={formData}
                validator={validator}
                onSubmit={data => submitData(data)}
            >
            </Form>
        </div>
    )
}

async function submitData(data: any) {
    console.log('submitted data', data)
    console.log('submitted for data', data.formData)
    try {
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'accept': 'text/html' },
            body: JSON.stringify(data.formData),
        };
        await fetch("/api/config-data", requestOptions);
      } catch (err) {
        console.log(err);
      }
}
export default FormComponent