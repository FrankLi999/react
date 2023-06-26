connect {
  enabled = true
}
services {
  name = "client"
  port = 8080
  connect {
    sidecar_service {
      proxy {
        local_service_address = "host.docker.internal"
        upstreams {
          destination_name = "tcpecho"
          local_bind_address = "host.docker.internal"
          local_bind_port = 9191
        }
      }
    }
  }
}
services {
  name = "tcpecho"
  port = 9090
  connect {
    sidecar_service {
      proxy {
        local_service_address = "host.docker.internal"
      }
    }
  }
}

services {
  name = "httpecho"
  port = 5678
  connect {
    sidecar_service {
      proxy {
        local_service_address = "host.docker.internal"
      }
    }
  }
}