# camel-consul:
    
    https://www.hashicorp.com/blog/herding-apache-camels-with-hashicorp-consul
    https://github.com/sigreen/camel-spring-boot-consul
    ```
    docker pull consul
    docker run -d -p 8500:8500 -p 8600:8600/udp --name=badger consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0

    docker run --name=fox -d -e CONSUL_BIND_INTERFACE=eth0 consul agent -node=client-1 -dev -join=172.17.0.2 -ui
    docker run --name=weasel -d -e CONSUL_BIND_INTERFACE=eth0 consul agent -node=client-2 -dev -join=172.17.0.2 -ui
    git clone https://github.com/sigreen/camel-spring-boot-consul 
    cd camel-spring-boot-consul/services/src/main/resources/consul
    docker cp services.json weasel:/consul/config/services.json
    docker exec weasel consul reload
    docker cp services.json fox:/consul/config/services.json
    docker exec fox consul reload
    ```
    consule and envoy docker:
        https://xsreality.medium.com/consul-connect-with-envoy-and-docker-dc0cf53b8c1a

            Dummy Interface solution: https://medium.com/zendesk-engineering/making-docker-and-consul-get-along-5fceda1d52b9
            it introduces a new interface 169.254.1.1

                --net=host runs the docker container on the host machine. It is the recommended way by HashiCorp to run Consul container.

                -server runs Consul in server mode.

                -grpc-port tells Consul to start the gRPC server. This is needed because we want to use Envoy as the data plane.

                -client makes Consul bind the HTTP, DNS and gRPC server on this IP. If not provided, defaults to 127.0.0.1 which will not allow other containers to reach the Consul endpoints. The REST endpoint will be available on http://169.254.1.1:8500 and gRPC endpoint on 169.254.1.1:8502.

                -bind makes Consul use this IP for all internal cluster communication.

                -bootstrap-expect tells Consul the number of nodes participating in the cluster, in this case 1.

                -ui because we want to see the shiny UI.

        # docker run -d --name=consul-server --net=host  -v  ./envoy_demo.hcl:/etc/consul/envoy_demo.hcl consul:latest agent -server -config-file /etc/consul/envoy_demo.hcl -grpc-port 8502 -client 192.168.86.1 -bind 192.168.86.182 -bootstrap-expect 1 -ui
        docker run -d -p 8500:8500 -p 8600:8600/udp --name=consul-server -v  ./envoy_demo.hcl:/etc/consul/envoy_demo.hcl consul:latest agent -server -ui -config-file=/etc/consul/envoy_demo.hcl -grpc-port=8502 -node=server-1 -bootstrap-expect=1 -client=0.0.0.0
        

        run echo service:
        # docker run --rm -d --dns 169.254.1.1 --name echo-service --net=bridge -p 169.254.1.1:9090:9090 abrarov/tcp-echo --port 9090
        docker run --rm -d --name echo-service --net=bridge -p 9090:9090 abrarov/tcp-echo --port 9090

    https://hub.docker.com/r/timarenz/envoy-consul
    docker pull timarenz/envoy-consul:v1.14.4_1.8.4
    
    ```
    docker run -d -p 8500:8500 -p 8600:8600/udp --name=server-1 timarenz/envoy-consul:v1.14.4_1.8.4 consul agent -server -ui -node=server-1  -bootstrap-expect=1 -client=0.0.0.0 -sidecar-for
    docker run --name=client-1 -d -e CONSUL_BIND_INTERFACE=eth0 timarenz/envoy-consul:v1.14.4_1.8.4 agent -node=client-1 -dev -join=172.17.0.2 -ui
    docker run --name=client-2 -d -e CONSUL_BIND_INTERFACE=eth0 timarenz/envoy-consul:v1.14.4_1.8.4 agent -node=client-2 -dev -join=172.17.0.2 -ui

    # docker pull consul
    # docker run -d -p 8500:8500 -p 8600:8600/udp --name=badger  consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0 -grpc-port=8502
    docker cp envoy_demo.hcl badger:/consul/config/envoy_demo.hcl
    docker cp services.json badger:/consul/config/services.json
    docker exec badger consul reload

    docker run -d --name http-echo -p 5678:5678 hashicorp/http-echo -text="hello world"
    docker run --rm -d --name echo-service -p 9090:9090 abrarov/tcp-echo --port 9090
    or docker run -p 8080:8080 --name http-echo hashicorp/http-echo -listen=:8080 -text="hello world"
    

    curl http://host.docker.internal:5678
    docker run -d --name tcpecho-proxy timarenz/envoy-consul:v1.14.4_1.8.4 -sidecar-for tcpecho -http-addr http://host.docker.internal:8500 -admin-bind 127.0.0.1:0 -- -l trace
    docker run -d --name httpecho-proxy timarenz/envoy-consul:v1.14.4_1.8.4 -sidecar-for httpecho -http-addr http://host.docker.internal:8500 -admin-bind 127.0.0.1:9999 -- -l trace


    docker run --rm -d --name client-proxy timarenz/envoy-consul:v1.14.4_1.8.4 -sidecar-for client -http-addr http://host.docker.internal:8500 -grpc-addr http://host.docker.internal:8502  -admin-bind 127.0.0.1:0  -- -l trace
        
        
    docker run -it --rm  -p 9191:9191 gophernet/netcat -l -p 9191
    docker run -ti --rm  gophernet/netcat host.docker.internal 9191
    -----------------------
    docker build -t consul-envoy .
    docker run --rm -d --name echo-proxy consul-envoy -sidecar-for echo -http-addr http://host.docker.internal:8500 -admin-bind 127.0.0.1:0 -- -l trace
    docker run --rm -d --dns 169.254.1.1 --name echo-proxy \
         --network host \
         consul-envoy \
         -sidecar-for echo \
         -http-addr http://169.254.1.1:8500 \
         -grpc-addr 169.254.1.1:8502 \
         -admin-bind 127.0.0.1:0 \
         -- -l trace

    docker run --rm --dns 169.254.1.1 --name client-proxy \
         --network host \
         consul-envoy \
         -sidecar-for client \
         -http-addr http://169.254.1.1:8500 \
         -grpc-addr 169.254.1.1:8502 \
         -admin-bind 127.0.0.1:0 \
         -- -l trace

    # docker run --name=fox -d -e CONSUL_BIND_INTERFACE=eth0 consul agent -node=client-1 -dev -join=172.17.0.2 -ui
    # docker run --name=weasel -d -e CONSUL_BIND_INTERFACE=eth0 consul agent -node=client-2 -dev -join=172.17.0.2 -ui



    # docker run --name=fox -d -e CONSUL_BIND_INTERFACE=eth0 consul agent -node=client-1 -dev -join=172.17.0.2 -ui
    # docker run --name=weasel -d -e CONSUL_BIND_INTERFACE=eth0 consul agent -node=client-2 -dev -join=172.17.0.2 -ui

# camel - envoy
    https://developer.hashicorp.com/consul/tutorials/developer-mesh/service-mesh-with-envoy-proxy
    https://github.com/hashicorp/demo-consul-101

    envoy: https://www.envoyproxy.io/

    docker run --rm -it -p 9901:9901 -p 10000:10000 envoyproxy/envoy:v1.24.0
    docker run --rm -it -v $(pwd)/envoy-custom.yaml:/envoy-custom.yaml -p 9901:9901 -p 10000:10000 envoyproxy/envoy:v1.24.0 -c /envoy-custom.yaml

docker pull consul

The above steps run a completely in-memory Consul server agent with default bridge networking and no services exposed on the host. This is useful for development but should not be used in production. Since the server is running at internal address 172.17.0.2, you can run a three-node cluster for development by starting up two more instances and telling them to join the first node.

docker run -d -p 8500:8500 -p 8600:8600/udp --name=badger consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0
docker run --rm -d --name echo-service -p 9090:9090 abrarov/tcp-echo --port 9090
docker run --name=fox -d -e CONSUL_BIND_INTERFACE=eth0 consul agent -node=client-1 -dev -join=172.17.0.2 -ui
docker run --name=weasel -d -e CONSUL_BIND_INTERFACE=eth0 consul agent -node=client-2 -dev -join=172.17.0.2 -ui

http://localhost:8085