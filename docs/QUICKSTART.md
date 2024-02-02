## Introduction

This guide explains on how to setup the infrastructure to use and test the KUKSA Companion App.

## Requirements

- Android Device with API Level >= 27 (Android 8.1)
- a running DataBroker
- a running MockService (Optional)

## Setup Databroker

To use the Companion App a connection to a Databroker is required. This Databroker might run inside
a car or on your local system for testing purposes. The steps to set up a local Databroker can be
found in
the [KUKSA.val Quickstart Guide](https://github.com/eclipse/kuksa.val/blob/master/doc/quickstart.md)

## Setup Mock Service (Optional)

This step is only required when connecting to a local running Databroker, which is not integrated
into an actual vehicle.

To use the Companion App the Databroker needs to be connected to the Mock Service.
The Mock Service simulates the interaction with the car e.g. interactions with the different
interactors of a car. The steps to set up a local Databroker can be found in the
[KUKSA.val Quickstart Guide](https://github.com/eclipse/kuksa.val/blob/master/doc/quickstart.md)

The Mock Service needs to be run with the
following [mock.py](https://github.com/eclipse-kuksa/kuksa-android-companion/blob/main/mock/mock.py)

## Configure Companion App

When you have access to a Databroker, the next step is to setup the Companion App so it can connect
to the Databroker.
For this enter the "Settings" screen from inside the NavigationBar of the app.

Here you have to enter the following connection information:

- Host: The host on which the Databroker is running.
- Port: The port on which the Databroker is running.
  Make sure you used the same port, when starting the Databroker. The Apps default value is 55556.
- Enable TLS: This switch needs to be enabled, when you started your Databroker in secure mode.
  Make sure you used the same mode, when starting the Databroker.
- Certificate: If TLS is enabled, it is mandatory to set a Certificate, which is used to establish
  the secure connection.

You can press the small "refresh" button in the Connection Status Bar, to start a new connection
cycle.

## Questions

### Can't connect to Databroker from Android Emulator

When the App is run from an Android Emulator and the host device is running the Databroker. The host
to be used is `10.0.2.2`. This is the internal loopback address of the host device. See for further
details: https://developer.android.com/studio/run/emulator-networking

### I can't connect to the Databroker from my mobile device. I am using a Proxy.

The Proxy might prevent communication between the mobile device and the host device. Therefore even
though the host device is running the Databroker just fine, it is not possible for the mobile device
to connect to it.

This can be solved by
a) using an Android Emulator
b) setting up a reverse port forwarding using adb
`adb reverse tcp:55556 tcp:55556`
This forwards all data from the host device port 55556 to the mobile device port 55556.
The host to be used is `localhost:55556` in this case.

Reverse port forwarding requires host device and mobile device to be connected using usb,
usb debugging to be enabled on the mobile device and adb to be accessible on the host device.
