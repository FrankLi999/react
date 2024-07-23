import React from "react";
import { useCookies } from 'react-cookie';
import { CookiesProvider } from 'react-cookie';
import { Link } from "react-router-dom";
import {
    Card,
    CardBody,
    CardFooter,
    PageSection,
    PageSectionVariants,
    Text,
    TextContent,
  } from '@patternfly/react-core';
import { RJSFSchema } from '@rjsf/utils';
import validator from '@rjsf/validator-ajv8';
import Form from "../../components/form/Form";
import ErrorBoundaryContextProvider from "../../utils/error-boundary/ErrorBoundaryContextProvider";
import ErrorBoundary from "../../utils/error-boundary/ErrorBoundary";

function ConfigurationDataCreateForm() {
    const [cookies] = useCookies(['XSRF-TOKEN']);
    const schema: RJSFSchema = require("./json-files/config-data-schema.json");
    const uiSchema = require("./json-files/config-data-form-uischema-create.json");
    const log = (type: any) => console.log.bind(console, type);
    // const schema: RJSFSchema = schemaForm;
    const formData = require("./json-files/config-data-form-data.json")
    async function submitData(data: any) {
        console.log('submitted data', data)
        console.log('submitted for data', data.formData)
        try {
            const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'accept': 'application/json', 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] },
                body: JSON.stringify([data.formData]),
            };
            await fetch("/api/configurations", requestOptions);
        } catch (err) {
            console.log(err);
        }
    }
    return (
        <ErrorBoundaryContextProvider>
        <ErrorBoundary>
          <PageSection variant={PageSectionVariants.light}>
            <TextContent>
              <Text component="h4">Camel Integrator Configurations</Text>
              <Text component="p">Create configurations for an application profile</Text>
            </TextContent>
              <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                <Link to="/integrator/configuration-data">
                  Go to configurations page
                </Link>
              </div>
          </PageSection>
          <PageSection>
            <Card>
              <CardBody>
                <Form schema={schema} uiSchema={uiSchema} formData={formData} validator={validator} onSubmit={data => submitData(data)} />
              </CardBody>
              <CardFooter>
                <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                  <Link to="/integrator/configuration-data">
                    Go to configurations page
                  </Link>
                </div>
              </CardFooter>
            </Card>
          </PageSection>
        </ErrorBoundary>
        </ErrorBoundaryContextProvider>
    )
}
export default ConfigurationDataCreateForm;
