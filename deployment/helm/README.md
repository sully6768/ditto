# Eclipse Ditto :: Helm

The Ditto Helm chart is managed at the [Eclipse IoT Packages](https://github.com/eclipse/packages/tree/master/charts/ditto) project.

## Install Ditto via Helm

To install the chart with the release name eclipse-ditto, run the following commands:

```shell script
helm repo add eclipse-iot https://www.eclipse.org/packages/charts/
helm install eclipse-iot/ditto --name eclipse-ditto
```

# Uninstall the Chart

To uninstall/delete the eclipse-ditto deployment:

```shell script
helm delete eclipse-ditto
```
