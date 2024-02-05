## Introduction

This guide explains on how to setup the infrastructure to use and test the KUKSA Companion App.

## Requirements

- Android Device with API Level >= 27 (Android 8.1)
- a running [KUKSA DataBroker](https://github.com/eclipse/kuksa.val/tree/master/kuksa_databroker)
- a running [Vehicle Mock Service](https://github.com/eclipse/kuksa.val.services/tree/main/mock_service) (Optional)

## Setup KUKSA Databroker

To use the Companion App a connection to a KUKSA Databroker is required. The 
KUKSA Databroker might run inside a car or on your local system for testing purposes. The steps 
to set up a local KUKSA Databroker can be found in
the [KUKSA Quickstart Guide](https://github.com/eclipse/kuksa.val/blob/master/doc/quickstart.md)

## Setup Vehicle Mock Service (Optional)

This step is only required when connecting to a local running KUKSA Databroker, which is not 
integrated into an actual vehicle.

To use the Companion App the KUKSA Databroker needs to be connected to the Mock Service.
The Mock Service simulates the interaction with the car e.g. sensors and actuators of a car. 
The steps to set up a local instance of the Vehicle Mock Service can be found here:
[Running mockservice](https://github.com/eclipse/kuksa.val.services/tree/main/mock_service#running-mockservice)

The Mock Service needs to be run with the
following [mock.py](https://github.com/eclipse-kuksa/kuksa-android-companion/blob/main/mock/mock.py).
Simply replace the mock.py inside the Vehicle Mock Service repository with the one provided by this
project to use it. 

## Configure Companion App

When you have access to a KUKSA Databroker, the next step is to setup the Companion App so it 
can connect to the it.
For this enter the "Settings" screen. The Settings screen can be found when clicking on the gear 
icon in the Navigation Bar. Depending on your orientation the Navigation Bar can be found on top
(portrait) or to the left (landscape) of the screen.

Here you have to enter the following connection information:

- Host: The host on which the KUKSA Databroker is running.
- Port: The port on which the KUKSA Databroker is running.
  Make sure you used the same port, when starting the KUKSA Databroker. The default value 
  expected by the App is 55556 while the KUKSA Databroker by default starts at port 55555.
- Enable TLS: This switch needs to be enabled, when you started your KUKSA Databroker in 
  secure mode. Make sure you used the same mode, when starting the KUKSA Databroker.
- Certificate: If TLS is enabled, it is mandatory to set a Certificate, which is used to establish
  the secure connection.

You can press the "refresh"-button, to start a new connection cycle. The "refresh"-button is the 
self-referencing arrow in the Connection Status Bar. Depending on your connection status the
Connection Status Bar is colored red if no connection could be established or green if you are 
successfully connected to the KUKSA Databroker.

## FAQ

### Can't connect to KUKSA Databroker from Android Emulator

When the App is run from an Android Emulator and the host device is running the 
KUKSA Databroker. The host to be used is `10.0.2.2`. This is the internal loopback address of 
the host device. See the [android documentation](https://developer.android.com/studio/run/emulator-networking) 
for further details

### I can't connect to the KUKSA Databroker from my mobile device. I am using a Proxy.

The Proxy might prevent communication between the mobile device and the host device. Therefore even
though the host device is running the KUKSA Databroker just fine, it is not possible for the 
mobile device to connect to it.

This can be solved by
a) using an Android Emulator
b) setting up a reverse port forwarding using adb
`adb reverse tcp:55556 tcp:55556`
This forwards all data from the host device port 55556 to the mobile device port 55556.
The host to be used is `localhost:55556` in this case.

Reverse port forwarding requires host device and mobile device to be connected using usb,
usb debugging to be enabled on the mobile device and adb to be accessible on the host device.

## Which Signals are used inside the Companion App?

The KUKSA Companion App is based on version 4.0 of the Vehicle Signal Specification.

It uses only a small portion of the Signals specified by the Vehicle Signal Specification for
it's show case.

Here is a list of signals which are used inside the app:

Door Control:
- Vehicle.Cabin.Door.Row1.DriverSide.IsLocked
- Vehicle.Cabin.Door.Row1.PassengerSide.IsLocked
- Vehicle.Cabin.Door.Row2.DriverSide.IsLocked
- Vehicle.Cabin.Door.Row2.PassengerSide.IsLocked
- Vehicle.Body.Trunk.Rear.IsLocked
- Vehicle.Cabin.Door.Row1.DriverSide.IsOpen
- Vehicle.Cabin.Door.Row1.PassengerSide.IsOpen
- Vehicle.Cabin.Door.Row2.DriverSide.IsOpen
- Vehicle.Cabin.Door.Row2.PassengerSide.IsOpen
- Vehicle.Body.Trunk.Rear.IsOpen

Temperature Control:
- Vehicle.Cabin.HVAC.Station.Row1.Driver.Temperature
- Vehicle.Cabin.HVAC.Station.Row1.Passenger.Temperature
- Vehicle.Cabin.HVAC.Station.Row2.Driver.Temperature
- Vehicle.Cabin.HVAC.Station.Row2.Passenger.Temperature

Light Control:
- Vehicle.Body.Lights.Beam.High.IsOn
- Vehicle.Body.Lights.Beam.Low.IsOn
- Vehicle.Body.Lights.DirectionIndicator.Left.IsSignaling
- Vehicle.Body.Lights.DirectionIndicator.Right.IsSignaling
- Vehicle.Body.Lights.Fog.Front.IsOn
- Vehicle.Body.Lights.Fog.Rear.IsOn
- Vehicle.Body.Lights.Hazard.IsSignaling
- Vehicle.Body.Lights.Parking.IsOn
- Vehicle.Body.Lights.Running.IsOn

Tire Pressure:
- Vehicle.Chassis.Axle.Row1.Wheel.Left.Tire.Pressure
- Vehicle.Chassis.Axle.Row1.Wheel.Right.Tire.Pressure
- Vehicle.Chassis.Axle.Row2.Wheel.Left.Tire.Pressure
- Vehicle.Chassis.Axle.Row2.Wheel.Right.Tire.Pressure

You can find more information about the signals used inside the app or other existing signals
[here](https://covesa.github.io/vehicle_signal_specification/).

## Are there alternate ways to change the data of a VSS path?

You can change the data of an underlying VSS path manually by using the
- [KUKSA CLI](https://github.com/eclipse/kuksa.val/blob/master/doc/quickstart.md#reading-and-writing-vss-data-via-cli)
- [KUKSA Python Library](https://github.com/eclipse/kuksa.val/blob/master/doc/quickstart.md#reading-and-writing-vss-data-with-code)
