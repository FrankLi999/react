// import Form from "@rjsf/core";
import { RJSFSchema } from '@rjsf/utils';
import React from "react";
import validator from '@rjsf/validator-ajv8';
import Form from "@rjsf/bootstrap-4";
// import schemaForm from '../jsonFiles/schema.json';
// import formData from '../jsonFiles/formData.json';
// import uiSchema from '../jsonFiles/uiSchema.json';



function FormComponent() {
   const schema: RJSFSchema = require("../jsonFiles/schema.json")
   const formData = require("../jsonFiles/formData.json")
   const uiSchema = require("../jsonFiles/uiSchema.json")
    // const schema: RJSFSchema = schemaForm;
    return (
        <div className="form">
            <Form
                schema={schema}
                uiSchema={uiSchema}
                formData={formData}
                validator={validator}
            >
            </Form>
        </div>
    )
}

export default FormComponent