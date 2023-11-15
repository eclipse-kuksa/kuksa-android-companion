# /********************************************************************************
# * Copyright (c) 2023 Contributors to the Eclipse Foundation
# *
# * See the NOTICE file(s) distributed with this work for additional
# * information regarding copyright ownership.
# *
# * This program and the accompanying materials are made available under the
# * terms of the Apache License 2.0 which is available at
# * http://www.apache.org/licenses/LICENSE-2.0
# *
# * SPDX-License-Identifier: Apache-2.0
# ********************************************************************************/

from lib.animator import RepeatMode
from lib.dsl import (
    create_animation_action,
    create_behavior,
    create_event_trigger,
    create_set_action,
    get_datapoint_value,
    mock_datapoint,
)
from lib.trigger import ClockTrigger, EventType


# Door IsLocked
doors = [
    "Vehicle.Cabin.Door.Row1.DriverSide.IsLocked",
    "Vehicle.Cabin.Door.Row1.PassengerSide.IsLocked",
    "Vehicle.Cabin.Door.Row2.DriverSide.IsLocked",
    "Vehicle.Cabin.Door.Row2.PassengerSide.IsLocked",
    "Vehicle.Body.Trunk.Rear.IsLocked",
    "Vehicle.Cabin.Door.Row1.DriverSide.IsOpen",
    "Vehicle.Cabin.Door.Row1.PassengerSide.IsOpen",
    "Vehicle.Cabin.Door.Row2.DriverSide.IsOpen",
    "Vehicle.Cabin.Door.Row2.PassengerSide.IsOpen",
    "Vehicle.Body.Trunk.Rear.IsOpen",
]
for vssPath in doors:
    mock_datapoint(
        path=vssPath,
        initial_value="false",
        behaviors=[
            create_behavior(
                trigger=create_event_trigger(EventType.ACTUATOR_TARGET),
                action=create_set_action("$event.value")
            )
        ]
    )

# Temperature (trigger animation with e.g. set Vehicle.Cabin.HVAC.Station.Row1.Driver.Temperature 25)
# if the animation is triggered directly after starting the mock client, try restarting the databroker.
# this can occur when the mock client is killed / and restarted

mock_datapoint(
    path="Vehicle.Cabin.HVAC.Station.Row1.Driver.Temperature",
    initial_value=14,
    behaviors=[
        create_behavior(
            trigger=create_event_trigger(EventType.ACTUATOR_TARGET),
            action=create_animation_action(
                duration=30.0,
                repeat_mode=RepeatMode.ONCE,
                values=["$self", "$event.value"]
            ),
        ),
    ],
)

mock_datapoint(
    path="Vehicle.Cabin.HVAC.Station.Row1.Passenger.Temperature",
    initial_value=14,
    behaviors=[
        create_behavior(
            trigger=create_event_trigger(EventType.ACTUATOR_TARGET),
            action=create_animation_action(
                duration=35.0,
                repeat_mode=RepeatMode.ONCE,
                values=["$self", "$event.value"]
            ),
        ),
    ],
)

mock_datapoint(
    path="Vehicle.Cabin.HVAC.Station.Row2.Driver.Temperature",
    initial_value=12,
    behaviors=[
        create_behavior(
            trigger=create_event_trigger(EventType.ACTUATOR_TARGET),
            action=create_animation_action(
                duration=45.0,
                repeat_mode=RepeatMode.ONCE,
                values=["$self", "$event.value"]
            ),
        ),
    ],
)

mock_datapoint(
    path="Vehicle.Cabin.HVAC.Station.Row2.Passenger.Temperature",
    initial_value=12,
    behaviors=[
        create_behavior(
            trigger=create_event_trigger(EventType.ACTUATOR_TARGET),
            action=create_animation_action(
                duration=45.0,
                repeat_mode=RepeatMode.ONCE,
                values=["$self", "$event.value"]
            ),
        ),
    ],
)

# Tire Pressure

mock_datapoint(
    path="Vehicle.Chassis.Axle.Row1.Wheel.Left.Tire.Pressure",
    initial_value=207,
)

mock_datapoint(
    path="Vehicle.Chassis.Axle.Row1.Wheel.Right.Tire.Pressure",
    initial_value=207,
)

mock_datapoint(
    path="Vehicle.Chassis.Axle.Row2.Wheel.Left.Tire.Pressure",
    initial_value=199,
)

mock_datapoint(
    path="Vehicle.Chassis.Axle.Row2.Wheel.Right.Tire.Pressure",
    initial_value=200,
)

# Lights

lights = [
    "Vehicle.Body.Lights.Beam.High.IsOn",
    "Vehicle.Body.Lights.Beam.Low.IsOn",
    "Vehicle.Body.Lights.DirectionIndicator.Left.IsSignaling",
    "Vehicle.Body.Lights.DirectionIndicator.Right.IsSignaling",
    "Vehicle.Body.Lights.Fog.Front.IsOn",
    "Vehicle.Body.Lights.Fog.Rear.IsOn",
    "Vehicle.Body.Lights.Hazard.IsSignaling",
    "Vehicle.Body.Lights.Parking.IsOn",
    "Vehicle.Body.Lights.Running.IsOn"
]
for vssPath in lights:
    mock_datapoint(
        path=vssPath,
        initial_value="false",
        behaviors=[
            create_behavior(
                trigger=create_event_trigger(EventType.ACTUATOR_TARGET),
                action=create_set_action("$event.value")
            )
        ]
    )
