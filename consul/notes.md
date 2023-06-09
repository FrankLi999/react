# open telemetry
    https://help.sumologic.com/docs/apm/traces/get-started-transaction-tracing/opentelemetry-instrumentation/java/traceid-spanid-injection-into-logs-configuration/
	https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/docs/logger-mdc-instrumentation.md
	https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/main/instrumentation/logback/logback-mdc-1.0/library
# api governance
    service discovery: needed?
	https://www.digitalml.com/api-governance-best-practices/
	https://www.postman.com/api-platform/api-governance/
	
	spectral guideline: define governance rule
		https://learning.postman.com/docs/api-governance/configurable-rules/spectral/
		lint tools: https://redocly.com/
	Discoverable: APIs are easy to find and use by the entire organization

	Complete, Consistent, and Compliant: APIs are high quality and deliver reliable consumer experiences

	Reusable: APIs can be easily reused/updated/extended, and composed together

	Secure: so that data is safe, risk is minimized, and you’re complying with regulation

	Collaborative: well thought-out and well-documented so that everyone in your ecosystem can understand, use, and collaborate on them.


	API governance is the practice of defining and applying standards, policies, and processes that ensure your APIs are standardized, reliable, and secure. 
	Fundamentally, API governance provides the guardrails that result in great APIs, and is a key component of a mature API management program.
	
		> Policies and standards are applied through checks and validations, for e.g. checking API basepath uniqueness. This can either be 
		done manually or automatically.
	
		> common model:	https://www.digitalml.com/7-reasons-to-use-a-resource-model-while-designing-restful-apis/
		
		>>> 1. Have a centralized set of enterprise-wide API governance rules…
			Ensuring API uniqueness
			Checking important metadata fields which ensure discoverability and reusability e.g. owner, lifecycle state, the capability the API supports, classification
			Access control rules for who can do what with your APIs and when

		>>> 2. But build in flexibility and responsiveness
		
			Needing a certain API security policy based on LoB or taxonomy classification
			When an API design meets a certain condition, a particular error response (which has been defined in the canonical model) is needed
			Based on HTTP Method Type, certain headers and/or response codes are required

		>>> 3. Automate governance checks and validation for speed and enablement
			https://www.digitalml.com/ignite-platform/
			
		>>> 4. Manage your APIs as abstracted Designs in a Holistic Catalog
		
		>>>> 5. Build in data consistency through reusable domain models and components
		>>>> 6. Apply governance at all stages of the API lifecycle
		>>>> 7. Implement robust API versioning
		>>>> 8. Ensure your API governance rules are met before an API can be deployed
		>>>> 9. Education and training to reframe the API governance mindset
		
# gravitee
  
  https://medium.com/graviteeio/setting-up-gravitee-api-management-behind-consul-service-mesh-b2e9085ebc27
  https://shenyu.apache.org/
  https://github.com/apache/shenyu
		API governance: Request, response, parameter mapping, Hystrix, RateLimiter plugin
# Logging

## Docker plugin:

  loki: docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions
  fluentd: 
     https://adamtheautomator.com/efk-stack/
## Openshift
   elastic-search:  deploying the OpenShift Elasticsearch and Red Hat OpenShift Logging Operators. The OpenShift Elasticsearch Operator creates and manages the Elasticsearch cluster used by OpenShift Logging. The logging subsystem Operator creates and manages the components of the logging stack.

  loki: https://docs.openshift.com/container-platform/4.13/logging/cluster-logging-loki.html


consul service mesh:
  secure communication
  observability, 
  reliability,
  traffic control.

https://learning.oreilly.com/library/view/consul-up-and/9781098106133/
https://learning.oreilly.com/videos/hashicorp-certified/9781805128861/
https://developer.hashicorp.com/consul/tutorials/kubernetes/kubernetes-openshift-red-hat

# observerbility
    https://developer.hashicorp.com/consul/tutorials/docker/docker-compose-observability

    docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions
#     

# set up
```
crc setup
crc start
#linux
eval $(crc oc-env)
#windows
@FOR /f "tokens=*" %i IN ('crc oc-env') DO @call %i
oc login -u kubeadmin https://api.crc.testing:6443

  Username: kubeadmin
  Password: mAXsB-GCyIU-URVku-4IVPG

 kubectl cluster-info
 oc new-project consul
 kubectl create -f openshift-secret.yml --namespace=consul
```
# deploy
```
helm repo add hashicorp https://helm.releases.hashicorp.com
helm search repo hashicorp/consul
helm repo update

oc import-image hashicorp/consul:1.14.3-ubi --from=registry.connect.redhat.com/hashicorp/consul:1.14.3-ubi --confirm
oc import-image hashicorp/consul-k8s-control-plane:1.0.2-ubi --from=registry.connect.redhat.com/hashicorp/consul-k8s-control-plane:1.0.2-ubi --confirm
oc import-image hashicorp/consul-dataplane:1.0.0-ubi --from=registry.connect.redhat.com/hashicorp/consul-dataplane:1.0.0-ubi --confirm
helm install consul hashicorp/consul --values values.yaml --create-namespace --namespace consul --version "1.0.2" --wait

watch kubectl get pods --namespace consul
kubectl port-forward consul-server-0 --namespace consul 8500:8500
Open http://localhost:8500 in a new browser tab

export CONSUL_HTTP_ADDR=http://127.0.0.1:8500

oc new-project demo
export TARGET_NAMESPACE=demo
oc adm policy add-scc-to-group anyuid system:serviceaccounts:$TARGET_NAMESPACE
oc adm policy add-scc-to-group anyuid system:serviceaccounts:demo

oc adm policy remove-scc-from-group anyuid system:serviceaccounts:$TARGET_NAMESPACE
oc create -f networkattachementdefinition.yaml -n demo
oc delete network-attachment-definition consul-cni -n demo

oc apply -f server.yaml -n demo
oc apply -f client.yaml -n demo
```
# Envoy as a sidecar proxy

https://developer.hashicorp.com/consul/tutorials/developer-mesh/service-mesh-with-envoy-proxy

run local consul
```
consul agent -dev -node machine
```
   http://localhost:8500


install envoy
https://www.envoyproxy.io/docs/envoy/latest/start/install

ubuntu:
```
sudo apt update
sudo apt install apt-transport-https gnupg2 curl lsb-release
curl -sL 'https://deb.dl.getenvoy.io/public/gpg.8115BA8E629CC074.key' | sudo gpg --dearmor -o /usr/share/keyrings/getenvoy-keyring.gpg
Verify the keyring - this should yield "OK"
echo a077cb587a1b622e03aa4bf2f3689de14658a9497a9af2c427bba5f4cc3c4723 /usr/share/keyrings/getenvoy-keyring.gpg | sha256sum --check
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/getenvoy-keyring.gpg] https://deb.dl.getenvoy.io/public/deb/ubuntu $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/getenvoy.list
sudo apt update
sudo apt install -y getenvoy-envo
```

windows:
```
docker pull envoyproxy/envoy-windows-dev:b64e1f6d1e2f1d868d77e0d0f6f517ea0101c312
docker run --rm envoyproxy/envoy-windows-dev:b64e1f6d1e2f1d868d77e0d0f6f517ea0101c312 --version
```


# try the service mesh
```
# -s: silent mode -S: show error
kubectl exec consul-server-0 -n consul -- curl -sS http://frontend.default:6060
```
you can bypass frontend’s proxy by using kubectl exec to run a command from inside the 
frontend container and call backend directly:
```
#  -i, --include       Include protocol response headers in the output
kubectl exec deploy/frontend -c frontend -- curl -si http://backend/bird
```
Consul’s iptables rules instruct Linux to route all packets entering and leaving the pod to the sidecar proxy.


# service communication topolofy
```
http://localhost:8500/ui/dc1/services/backend/topology
```


# Consul ingress gateway.
https://developer.hashicorp.com/consul/docs/k8s/connect/ingress-controllers

Under the hood, ingress gateways are Envoy proxies, just like sidecar proxies, 
but configured differently.

```
consul-k8s upgrade -config-file values.yaml
```

# Config Entries

Config entries are sets of Consul configuration that allow you to configure the service mesh dynamically. 

kind: Ingress gateway, Proxy defaults, Service intentions, Service router, service resolver, service splitter

## configure your ingress gateway

    port:
    protocol: TCP, HTTP, HTTP2, or gRPC.
    tls: TLS can be disabled so you don’t need to deal with certificates, since this is just for testing.
    Routing configuration: How the listener decides which service to route to. 
        For example, one possible configuration is for the listener to watch for requests with the host header frontend.example.com and then route those requests to the frontend service.

        Host: www.google.com

        for example, say you have two services you want to expose, website and billing, and your users access them at http://website.example.com and http://billing.exam⁠ple​.com respectively. You can configure DNS to route both these domains to your ingress gateway. Then the gateway can inspect the host header and route to either the website or billing service.

    kubectl apply -f proxy-defaults.yaml
    kubectl apply -f ingress-gateway.yaml
    
    kubectl get proxydefaults global -n consul
    kubectl get ingressgateway ingress-gateway -n consul

    kubectl describe ingressgateway ingress-gateway -n consul

    kubectl port-forward service/consul-ingress-gateway -n consul 8080.
    http://localhost:8500/ui/dc1/services/ingress-gateway/upstreams


# security - authen, author, intentions

On Kubernetes, traffic between the service and its proxy happens over localhost on the pod’s internal network since all containers in a pod share the same networking stack. This means the traffic isn’t exposed outside of the pod.

Consul implements authorization via its intentions system. Intentions are rules governing which services are allowed to communicate.


intentions:
    frontend => backend (allow)
    * => * (deny)

    You can configure intentions via config entries, or Consul’s UI, CLI, or HTTP API.

    ui:  click create button on http://localhost:8500/ui/dc1/intentions 


    ```
    kubectl apply -f frontend-service-intentions.
    kubectl apply -f backend-service-intentions.yaml
    kubectl apply -f deny-all-service-intentions.yaml

    kubectl get serviceintentions frontend
    kubectl get serviceintentions backend
    kubectl get serviceintentions deny-all -n consul
    ```

Application Aware Intentions
    By default, all paths and methods are allowed if the intention is an allow intention. 

# Observability
    metrics
    logs
    distributed traces

    Proxies can automatically capture network metrics and emit them to a metrics database.

    There are many metrics storage solutions. Some are free and open source, such as Prometheus, Graphite, Elasticsearch, and InfluxDB, and some are paid solutions, such as Datadog and New Relic.

    you can hook Consul up with any metrics storage that supports Prometheus metrics (which most do).

    in values.yaml, add,
        prometheus:
          enabled: true

   Viewing Consul UI Metrics: on service topology
        RPS: reqs per seconds
        ER: error rate
        P50: Latency at the 50th percentile. In other words, 50% of requests were completed in this time or less.
      etc/prometheus/prometheus.yml

        global:
          scrape_interval: 10s

        scrape_configs:
        - job_name: consul
          metrics_path: /metrics
          consul_sd_configs:
          - server: 'localhost:8500'
          relabel_configs:
          - source_labels:
            - __meta_consul_tagged_address_lan
            - __meta_consul_service_metadata_prometheus_port
            regex: '(.*);(.*)'
            replacement: '${1}:${2}'
            target_label: '__address__'
            action: 'replace'
grafana:

  helm install grafana grafana \
      --version 6.17.1 \
      --repo https://grafana.github.io/helm-charts \
      --set service.type=LoadBalancer \
      --set service.port=3000 \
      --set persistence.enabled=true \
      --set rbac.pspEnabled=false \
      --set admin.existingSecret=grafana-admin \
      --wait