import { useCookies } from 'react-cookie';
import {
  PageSection, 
  PageSectionVariants, 
  Text, 
  TextContent,
  Form,
  FormGroup,
  TextInput,
  ActionGroup,
  Button,
  HelperText,
  HelperTextItem,
  FormHelperText
} from '@patternfly/react-core';
import React from 'react'
import { RefreshConfigModel } from './model';

export const RefreshConfig: React.FunctionComponent = () => {
  const [mpgPods, setMpgPods] = React.useState('');
  const [ccpayPods, setCcpayPods] = React.useState('');
  const [servicePods, setServicePods] = React.useState('');
  const [isRefreshing, setIsRefreshing] = React.useState<boolean>(false);
  const [refreshResult, setRefreshResult] = React.useState('');
  interface RefreshingPropsType {
    spinnerAriaValueText: string;
    spinnerAriaLabelledBy?: string;
    spinnerAriaLabel?: string;
    isLoading: boolean;
  }

  const primaryRefreshingProps = {} as RefreshingPropsType;
  primaryRefreshingProps.spinnerAriaValueText = 'Loading';
  primaryRefreshingProps.spinnerAriaLabelledBy = 'primary-loading-button';
  primaryRefreshingProps.isLoading = isRefreshing;

  const refreshConfig = async () => {
    // const [cookies] = useCookies(['XSRF-TOKEN']);
    
    let refreshConfigUrls: RefreshConfigModel[] = [];
    mpgPods.length > 0 && mpgPods.split(",").forEach((pod) =>  refreshConfigUrls.push({
        pod: pod,
        refreshUrl: pod.startsWith('http://') || pod.startsWith('https://') ? pod : `http://${pod}:8080/actuator/refresh`
        }));
    ccpayPods.length > 0 && ccpayPods.split(",").forEach((pod) =>  refreshConfigUrls.push({
        pod: pod,
        refreshUrl: pod.startsWith('http://') || pod.startsWith('https://') ? pod : `http://${pod}:8080/actuator/refresh`
        }));
    servicePods.length > 0 && servicePods.split(",").forEach((pod) =>  refreshConfigUrls.push({
        pod: pod,
        refreshUrl: pod.startsWith('http://') || pod.startsWith('https://') ? pod : `http://${pod}:8080/actuator/refresh`
        }));
    console.log('Refresh config for ', refreshConfigUrls)
    try {
        const requestOptions = {
            method: 'POST',
            // headers: { 'Content-Type': 'application/json', 'accept': '*/*', 'X-XSRF-TOKEN': cookies['XSRF-TOKEN'] },
            headers: { 'Content-Type': 'application/json', 'accept': '*/*' },
            body: JSON.stringify({
                requestHeader: {
                    traceId: '123'
                },
                refreshConfigurations: refreshConfigUrls
            }),
        };
        setIsRefreshing(() => true);
        await fetch("/my-camel/admin/api/refreshConfig", requestOptions)
        // await fetch("/api/refreshConfig", requestOptions)
                    .then(response => response.json())
                    .then((data: any) => {
                      setRefreshResult(() => JSON.stringify(data));
                      setIsRefreshing(() => false);
                    });
        console.log("done refreshing");
    } catch (err) {
        console.log(err);
        setIsRefreshing(() => false);
        setRefreshResult(() => JSON.stringify(err));
    }    
    
  }
  return <>
    <PageSection variant={PageSectionVariants.light}>
      <TextContent>
        <Text component='h1'>Refresh S2I integrator Configurations</Text>
        <Text component='p'>Refresh Configurations of S2I MPG, CCPay and Services integrators</Text>
        (refreshR)
      </TextContent>
      </PageSection>
      <Form isHorizontal>
      <FormGroup label="s2i-mpg-integrator Pods" isRequired fieldId="horizontal-form-mpg-pods">
        <TextInput
          value={mpgPods}
          isRequired
          type="text"
          id="horizontal-form-mpg-pods"
          aria-describedby="horizontal-form-mpg-pods-helper"
          name="horizontal-form-mpg-pods"
          onChange={(_event, value) => setMpgPods(value.trim())}
        />
        <FormHelperText>
          <HelperText>
            <HelperTextItem>Input s2i-mpg-integrator pod URLs</HelperTextItem>
          </HelperText>
        </FormHelperText>
      </FormGroup>
      <FormGroup label="s2i-ccpay-integrator Pods" isRequired fieldId="horizontal-form-ccpay-pods">
        <TextInput
          value={ccpayPods}
          isRequired
          type="text"
          id="horizontal-form-ccpay-pods"
          aria-describedby="horizontal-form-ccpay-pods-helper"
          name="horizontal-form-ccpay-pods"
          onChange={(_event, value) => setCcpayPods(value.trim())}
        />
        <FormHelperText>
          <HelperText>
            <HelperTextItem>Input s2i-ccpay-integrator pod URLs.</HelperTextItem>
          </HelperText>
        </FormHelperText>
      </FormGroup>
      <FormGroup label="s2i-service-integrator Pods" isRequired fieldId="horizontal-form-service-pods">
        <TextInput
          value={servicePods}
          isRequired
          type="text"
          id="horizontal-form-service-pods"
          aria-describedby="horizontal-form-service-pods-helper"
          name="horizontal-form-service-pods"
          onChange={(_event, value) => setServicePods(value.trim())}
        />
        <FormHelperText>
          <HelperText>
            <HelperTextItem>Input s2i-services-integrator pod URLs.</HelperTextItem>
          </HelperText>
        </FormHelperText>
      </FormGroup>
      <ActionGroup>
        <Button 
          variant="primary"
          onClick={() => refreshConfig()}
          isDisabled={isRefreshing}
          {...primaryRefreshingProps}
          >
           {isRefreshing ? 'Refreshing' : 'Refresh Config'}
        </Button>
      </ActionGroup>
    </Form>
   
  </>
}
