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
import { RJSFSchema } from '@react-jsf/utils';
import validator from '@react-jsf/ajv';
import Form from "@react-jsf/patternfly";
import ConfigDataSchema from "./form/config-data-schema";
import ConfigDataFormUISchemaCreate from './form/config-data-form-uischema-create';
import ConfigDataFormData from './form/config-data-form-data';

function ConfigurationDataCreateForm() {
    const [cookies] = useCookies(['XSRF-TOKEN']);
    const log = (type: any) => console.log.bind(console, type);
    async function submitData(data: any) {
        console.log('submitted data', data)
        console.log('submitted for data', data.formData)
        try {
            const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'accept': 'application/json', 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] },
                body: JSON.stringify([data.formData]),
            };
            await fetch("/my-camel/admin/api/spring-config/configurations", requestOptions);
        } catch (err) {
            console.log(err);
        }
    }
    return (
        <>
          <PageSection variant={PageSectionVariants.light}>
            <TextContent>
              <Text component="h4">Camel Integrator Configurations</Text>
              <Text component="p">Create configurations for an application profile</Text>
            </TextContent>
              <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                <Link to="/spring-config">
                  Go to configurations page
                </Link>
              </div>
          </PageSection>
          <PageSection>
            <Card>
              <CardBody>
                <Form schema={ConfigDataSchema} uiSchema={ConfigDataFormUISchemaCreate} formData={ConfigDataFormData} validator={validator} onSubmit={data => submitData(data)} />
              </CardBody>
              <CardFooter>
                <div className="small form-group d-flex align-items-center justify-content-between mt-4 mb-0">
                  <Link to="/spring-config">
                    Go to configurations page
                  </Link>
                </div>
              </CardFooter>
            </Card>
          </PageSection>
          </>
    )
}
export default ConfigurationDataCreateForm;
