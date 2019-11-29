Gradle Kubectl Plugin
======================

Gradle plugin that uses Kubectl to start/stop pods and services around a Gradle task. For instance where the test task requires a datastore to be running, the plugin could start and stop the container containing that datastore. The intention of the plugin is to use Kubernetes instead of Docker to manage the containers used during the Gradle task. This could be a requirement where Jenkins slaves are managed by Kubernetes. 

Requires Kubectl version 1.12 or higher to be installed.

## Using the plugin

```groovy
plugins {
  id "uk.co.boothen.gradle.kubectl" version "0.7"
}

kubectl {
    // See configuration below
}
```

### Kubetctl extension configuration
Name | Type | Description | Default
--- | --- | --- | ---
requiredBy | String | the task to start and stop the services before and after | -
file | String | the kubernetes yaml file containing the pods and services configuration | - 
forceStopBeforeStart | boolean | will try to stop previous services and pods from a previous execution | false
waitForTimeout | Integer | timeout (in seconds) to wait for the pods to start up | 60 
pod | List | list of pods contained in the configuration file | -
portForward | List | lists the port forwards between the localhost and the services | -

### pod configuration
Name | Type | Description | Default
--- | --- | --- | ---
podName | String | name of a pod in the configuration file | -

### portForward configuration
Name | Type | Description | Default
--- | --- | --- | ---
service | String | name of a service in the configuration file | -
port | Integer | port on the localhost to listen on | -
targetPort | Integer | port on the service to forward to | -
 
### Single Pod and Service example

```groovy
plugins {
    id "uk.co.boothen.gradle.kubectl"
}

kubectl {
    requiredBy test 
    file 'kube.yaml' 
    forceStopBeforeStart true 
    waitForTimeout 60
    pod {
        podName 'reference-dynamodb'
    }
   
    portForward {
        service 'reference-dynamodb'
        port 8000 
        targetPort 8000
    }
}
```

### Multiple Pod and Service example
```groovy
plugins {
    id "uk.co.boothen.gradle.kubectl"
}

kubectl {
    requiredBy test 
    file 'kube.yaml' 
    forceStopBeforeStart true 
    waitForTimeout 60
    pod {
        podName 'pod-postgres'
    }
    
    pod {
        podName 'pod-localstack'
    }

    pod {
        podName 'init-localstack'
    }
   
    portForward {
        service 'service-postgres' 
        port 5432 
        targetPort 5432
    }

    portForward {
        service 'service-localstack'
        port 4575
        targetPort 4575
    }

    portForward {
        service 'service-localstack'
        port 4576
        targetPort 4576
    }
}
```