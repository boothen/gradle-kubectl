Gradle Kubectl Plugin
======================


Gradle plugin that uses Kubectl to manage pods used during Gradle build phase

## Using the plugin

```groovy
plugins {
  id "uk.co.boothen.gradle.kubectl" version "0.5" apply false
}
```

## Example

```groovy
apply plugin: 'uk.co.boothen.gradle.kubectl'

kubectl {
    requiredBy test // task
    file 'kube.yaml' // configuration file to apply
    forceStopBeforeStart true // stop any pods and services lingering from a previous build before starting them.
    pod {
        podName 'reference-dynamodb' // name of the pod in the configuration file
    }
   
    portForward {
        service 'reference-dynamodb'
        port 8000 // localhost port
        targetPort 8000 // service port
    }
}
```