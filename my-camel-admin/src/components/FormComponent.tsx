import Form from "@rjsf/core";
import { RJSFSchema } from '@rjsf/utils';
import React from "react";
import validator from '@rjsf/validator-ajv8';
function FormComponent() {
    const schema: RJSFSchema = require("../jsonFiles/schema.json")
    const formData = require("../jsonFiles/formData.json")
    const uiSchema = require("../jsonFiles/uiSchema.json")

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