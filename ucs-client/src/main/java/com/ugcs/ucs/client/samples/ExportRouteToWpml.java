package com.ugcs.ucs.client.samples;

import com.ugcs.ucs.client.Client;
import com.ugcs.ucs.client.ClientSession;
import com.ugcs.ucs.proto.DomainProto;
import com.ugcs.ucs.proto.DomainProto.DomainObjectWrapper;
import com.ugcs.ucs.proto.DomainProto.ProcessedRoute;
import com.ugcs.ucs.proto.DomainProto.Route;
import com.ugcs.ucs.proto.DomainProto.VehicleProfile;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class ExportRouteToWpml {
    private ExportRouteToWpml() {
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Missed required argument: <destination_file_path>.");
            System.err.println("Usage:");
            System.err.println("   ExportRouteToWpml <destination_file_path>");
            System.err.println();
            System.err.println("For example:");
            System.err.println("   ExportRouteToWpml \"c:\\temp\\exported-route.kmz\"");

            System.exit(1);
            return;
        }

        String destinationPathFromArgs = String.join("", args);
        Path destinationFile;
        try {
            destinationFile = Paths.get(destinationPathFromArgs);
        } catch (InvalidPathException e) {
            System.err.println("Invalid path: '" + destinationPathFromArgs + "'.");
            System.exit(1);
            return;
        }

        exportRoute(destinationFile);

        System.out.println("The route exported.");
    }

    private static void exportRoute(Path destinationFilePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream in = classLoader.getResourceAsStream("client.properties")) {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }

        InetSocketAddress serverAddress = new InetSocketAddress(
                properties.getProperty("server.host", "localhost"),
                Integer.parseInt(properties.getProperty("server.port", "3334")));

        try (Client client = new Client(serverAddress)) {

            client.connect();

            ClientSession session = new ClientSession(client);

            // To create a new client session application should send
            // an AuthorizeHciRequest message and set the clientId field
            // value to -1. In this case server will create a new session
            // object and return its identity in the clientId field.
            session.authorizeHci();

            // Any session should be associated with an authenticated user.
            // To authenticate user provide its credentials via LoginRequest
            // message.
            session.login(
                    properties.getProperty("user.login"),
                    properties.getProperty("user.password"));

            // Create a route for DJI M350
            var m350VehicleProfile = getVehicleProfileByName(session, "DJI Matrice 350 RTK");
            Route route = buildSimpleRoute(m350VehicleProfile);

            // Constructed route is just a definition (a plan) of the target
            // mission. Direct path between a vehicle and a target point
            // can collide terrain, No-Flight Zones or buildings.
            // We call processRoute routine to make a low-level plan
            // for the route.
            ProcessedRoute processedRoute = session.processRoute(route);

            session.exportRouteToWpml(
                    processedRoute,
                    DomainProto.WpmlExportAltitudeMode.W_RELATIVE_TO_FIRST_WP,
                    () -> {
                        try {
                            Files.createDirectories(destinationFilePath.getParent());
                            return new FileOutputStream(destinationFilePath.toFile());
                        } catch (Throwable e) {
                            throw new RuntimeException("Failed to create file.", e);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }


    private static Route buildSimpleRoute(VehicleProfile vehicleProfile) throws Exception {
        DomainProto.Figure.Builder figure = DomainProto.Figure.newBuilder()
                .setType(DomainProto.FigureType.FT_POINT)
                .addPoints(DomainProto.FigurePoint.newBuilder()
                        .setLatitude(Math.toRadians(46.7740639))
                        .setLongitude(Math.toRadians(8.3350036))
                        .setAglAltitude(30)
                        .setAltitudeType(DomainProto.AltitudeType.AT_AGL));

        DomainProto.SegmentDefinition.Builder routeSegment = DomainProto.SegmentDefinition
                .newBuilder()
                .setAlgorithmClassName("com.ugcs.ucs.service.routing.impl.WaypointAlgorithm")
                .setFigure(figure)
                .addParameterValues(DomainProto.ParameterValue.newBuilder()
                        .setName("speed")
                        .setValue(Double.toString(5.0)))
                .addParameterValues(DomainProto.ParameterValue.newBuilder()
                        .setName("wpTurnType")
                        .setValue("STOP_AND_TURN"))
                .addParameterValues(DomainProto.ParameterValue.newBuilder()
                        .setName("avoidObstacles")
                        .setValue("true"))
                .addParameterValues(DomainProto.ParameterValue.newBuilder()
                        .setName("avoidTerrain")
                        .setValue("true"))
                .addParameterValues(DomainProto.ParameterValue.newBuilder()
                        .setName("cornerRadius")
                        .setValue(""))
                .addParameterValues(DomainProto.ParameterValue.newBuilder()
                        .setName("altitudeType")
                        .setValue("AGL"));

        Route.Builder route = Route.newBuilder()
                .setName("WP-Direct sample route")
                .setCheckAerodromeNfz(true)
                .setCheckCustomNfz(false)
                .setInitialSpeed(5.0)
                .setMaxSpeed(10.0)
                .setMaxAltitude(10000.0)
                .setSafeAltitude(50.0)
                .setCornerRadius(15.0)
                .addFailsafes(DomainProto.Failsafe.newBuilder()
                        .setReason(DomainProto.FailsafeReason.FR_GPS_LOST)
                        .setAction(DomainProto.FailsafeAction.FA_WAIT))
                .addSegments(routeSegment);
        route.setVehicleProfile(vehicleProfile);

        return route.build();
    }

    private static VehicleProfile getVehicleProfileByName(ClientSession ucs, String name) throws Exception {
        return ucs.getObjectList(VehicleProfile.class)
                .stream()
                .map(DomainObjectWrapper::getVehicleProfile)
                .filter(vehicleProfile -> vehicleProfile.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found."));
    }
}
