# kuksa-android-companion

The KUKSA Companion App demonstrates the capabilities of the Android SDK to connect to a vehicle with an integrated KUKSA Databroker, read data and interact with the actuators of the vehicle. The KUKSA framework is based on the COVESA Vehicle Signal Specification (VSS).

The VSS defines the names and semantics of a large set of data entries that represent the current and/or intended state of a vehicle's sensors and actuators organized in a tree-like structure. For example, the vehicle's current speed is represented by the Vehicle.Speed entry.

However, VSS does not define how these signals are to be collected and managed within a vehicle, nor does it prescribe how other components in the vehicle can read or write signal values from and to the tree.

The KUKSA Databroker is a resource efficient implementation of the VSS signal tree and is intended to be run within a vehicle on a microprocessor based platform. It allows applications in the vehicle to interact with the vehicle's sensors and actuators using a uniform, high level gRPC API for querying signals, updating current and target values of sensors and actuators and getting notified about changes to signals of interest.

Showcases covered by the Companion App are:

- Door Control
- Temperature Control
- Light Control
- Tire Pressure Check

Find out more about KUKSA:

- Vehicle Signal Specification: https://covesa.github.io/vehicle_signal_specification/
- KUKSA Databroker: https://github.com/eclipse/kuksa.val/
- KUKSA Android SDK: https://github.com/eclipse-kuksa/kuksa-android-sdk/
- KUKSA Companion App: https://github.com/eclipse-kuksa/kuksa-android-companion/

Read our [quickstart guide](docs/QUICKSTART.md) to find out how to use the KUKSA Companion App and setup it's infrastructure.

Includes digital car model by BMW AG licensed under CC-BY-4.0
(https://github.com/bmwcarit/digital-car-3d).
