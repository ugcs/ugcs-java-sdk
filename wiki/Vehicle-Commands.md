A command is sent to a vehicle through the UgCS server. What the commands do in UgCS is described
in [Commands](https://manuals-ugcs.sphengineering.com/docs/commands) in the UgCS manual.

## A command

A [Command](Objects-Commands#command) has a `code`, the subsystem of the vehicle it is for, and
its arguments:

```java
Command command = Command.newBuilder()
        .setCode("arm")
        .setSubsystem(Subsystem.S_FLIGHT_CONTROLLER)
        .setSubsystemId(0)
        .build();
```

An argument is a code and a [Value](Objects-Commands#value):

```java
Command command = Command.newBuilder()
        .setCode("waypoint")
        .setSubsystem(Subsystem.S_FLIGHT_CONTROLLER)
        .setSubsystemId(0)
        .addArguments(argument("latitude", Math.toRadians(56.9761591)))
        .addArguments(argument("longitude", Math.toRadians(24.0730345)))
        .addArguments(argument("altitude_agl", 100.0))
        .addArguments(argument("ground_speed", 5.0))
        .addArguments(argument("vertical_speed", 1.0))
        .build();

static CommandArgument argument(String code, double value) {
    return CommandArgument.newBuilder()
            .setCode(code)
            .setValue(Value.newBuilder().setDoubleValue(value))
            .build();
}
```

## Sending it

Lock the vehicle, send, and release the lock, as the `SendCommand` sample does:

```java
Vehicle vehicle = session.lookupVehicle("EMU-101");
session.gainVehicleControl(vehicle);
try {
    session.sendCommand(vehicle, command);
} finally {
    session.releaseVehicleControl(vehicle);
}
```

`sendCommand` sends a [SendCommandRequest](API-Commands#sendcommandrequest) and throws
`IllegalStateException("Command error: ...")` with the [CommandStatus](Enums#commandstatus) for any
status but `CS_SUCCEEDED`:

| Status | |
|---|---|
| `CS_SUCCEEDED` | The command succeeded. |
| `CS_SENT` | The command was sent. |
| `CS_REJECTED` | The command was rejected. |
| `CS_FAILED` | The command failed. |

To get the result instead, send the request yourself. It takes several vehicles, which all get the
command, or a different command for each vehicle in `commands`; the response has a
[VehicleCommandResultDto](API-Commands#vehiclecommandresultdto) per vehicle, with the status and an
`errorMessage`.

## The commands

The commands of the `SendCommand` sample, for the flight controller (`S_FLIGHT_CONTROLLER`):

| Code | Arguments |
|---|---|
| `arm`, `disarm` | |
| `auto` | |
| `manual` | |
| `guided` | |
| `joystick` | |
| `takeoff_command` | |
| `land_command` | |
| `emergency_land` | |
| `return_to_home` | |
| `mission_pause`, `mission_resume` | |
| `waypoint` | `latitude`, `longitude` (radians), `altitude_amsl` or `altitude_agl`, `altitude_origin`, `ground_speed`, `vertical_speed`, `acceptance_radius`, `heading` |
| `direct_vehicle_control` | the control values of pitch, roll, yaw and throttle |

What a command does depends on the autopilot: see the "Command execution specifics" of each
autopilot in the UgCS manual, e.g. for
[ArduPilot](https://manuals-ugcs.sphengineering.com/docs/command-execution-specifics-1).

> **Emergency land** disarms the motors in mid-air, says the UgCS manual: use `emergency_land` only
> as a last resort.

## The commands a vehicle supports

Vehicles have more commands than those of the sample, e.g. for the camera and ADS-B, and each
vehicle has its own set. Subscribe to them with a [CommandSubscription](Objects-Events#commandsubscription):
each [CommandEvent](Objects-Events#commandevent) has the vehicle's
[CommandDefinition](Objects-Commands#commanddefinition)s, with the code, subsystem, whether the
command is `enabled` and `available`, and its arguments with their
[Semantic](Enums#semantic), default, minimum and maximum, and whether they are `mandatory`.

```java
client.addNotificationListener(event -> {
    EventWrapper wrapper = event.getEvent();
    if (!wrapper.hasCommandEvent())
        return;
    for (CommandDefinition command : wrapper.getCommandEvent().getCommandsList()) {
        StringBuilder sb = new StringBuilder(command.getCode())
                .append(command.getAvailable() ? "" : " (not available)");
        for (CommandArgumentDefinition argument : command.getArgumentsList())
            sb.append(" ").append(argument.getCode()).append(":").append(argument.getSemantic());
        System.out.println(sb);
    }
});
SubscribeEventResponse response = client.execute(SubscribeEventRequest.newBuilder()
        .setClientId(session.getClientId())
        .setSubscription(EventSubscriptionWrapper.newBuilder()
                .setCommandSubscription(CommandSubscription.newBuilder()
                        .setVehicle(vehicle)))
        .build());
```

See [[Events and Subscriptions]] for how subscriptions work. The commands a license does not allow
are in its `forbiddenCommandCodes` (see [[Errors and Licensing]]).
