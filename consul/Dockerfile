FROM consul:latest
FROM envoyproxy/envoy:v1.25.0 
COPY --from=0 /bin/consul /bin/consul 
ENTRYPOINT ["consul", "connect", "envoy"]