Gradle Kubectl Plugin
======================


Gradle plugin that uses Kubectl to manage pods used during Gradle build phase

## Using the plugin

```groovy
plugins {
  id "uk.co.boothen.gradle.kubectl" version "0.1" apply false
}
```

## Example

```groovy
apply plugin: 'uk.co.boothen.gradle.kubectl'

kubectl {
    requiredBy test // task
    podName 'reference-dynamodb' // name of the pod in the configuration file
    file 'kube.yaml' // configuration file to apply
    portForward 8000 // port to forward between local and pod
}
```