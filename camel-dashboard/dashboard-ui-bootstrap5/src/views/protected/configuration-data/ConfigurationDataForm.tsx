import React from "react";
import { RJSFSchema } from '@rjsf/utils';
import validator from '@rjsf/validator-ajv8';
// import Form from "@rjsf/bootstrap-4";
import Form from "../../../components/bootstrap5/Form";
// import schemaForm from './jsonFiles/schema.json';
// import formData from './jsonFiles/formData.json';
// import uiSchema from './jsonFiles/uiSchema.json';

import {
    Card,
    Container,
    Row,
    Col
} from "react-bootstrap";

function ConfigurationDataForm() {
    const schema: RJSFSchema = require("./json-files/config-data-schema.json")
    const formData = require("./json-files/config-data-form-data.json")
    const uiSchema = require("./json-files/config-data-form-uischema.json")
    const log = (type: any) => console.log.bind(console, type);
    // const schema: RJSFSchema = schemaForm;
    return (
        <>
        <Container fluid>
            <Row>
                <Col md="12">
                    <Card>
                        <Card.Header>
                            <Card.Title as="h4">Camel Integrator Configurations</Card.Title>
                            <p className="card-category">All service configurations</p>
                        </Card.Header>
                        <Card.Body>
                            <Row>
                                <Col md="12">
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
                                </Col>
                            </Row>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
        </>
    )
}

async function submitData(data: any) {
    console.log('submitted data', data)
    console.log('submitted for data', data.formData)
    try {
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'accept': 'text/html' },
            body: JSON.stringify([data.formData]),
        };
        await fetch("/api/configurations", requestOptions);
    } catch (err) {
        console.log(err);
    }
}
export default ConfigurationDataForm;