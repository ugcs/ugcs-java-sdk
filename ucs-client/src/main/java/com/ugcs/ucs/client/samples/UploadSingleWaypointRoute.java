package com.ugcs.ucs.client.samples;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Properties;

import com.ugcs.ucs.client.Client;
import com.ugcs.ucs.client.ClientSession;
import com.ugcs.ucs.proto.DomainProto;
import com.ugcs.ucs.proto.DomainProto.AltitudeType;
import com.ugcs.ucs.proto.DomainProto.DomainObjectWrapper;
import com.ugcs.ucs.proto.DomainProto.Failsafe;
import com.ugcs.ucs.proto.DomainProto.FailsafeAction;
import com.ugcs.ucs.proto.DomainProto.FailsafeReason;
import com.ugcs.ucs.proto.DomainProto.Figure;
import com.ugcs.ucs.proto.DomainProto.FigurePoint;
import com.ugcs.ucs.proto.DomainProto.FigureType;
import com.ugcs.ucs.proto.DomainProto.ParameterValue;
import com.ugcs.ucs.proto.DomainProto.ProcessedRoute;
import com.ugcs.ucs.proto.DomainProto.Route;
import com.ugcs.ucs.proto.DomainProto.SegmentDefinition;
import com.ugcs.ucs.proto.DomainProto.TrajectoryType;
import com.ugcs.ucs.proto.DomainProto.Vehicle;

public final class UploadSingleWaypointRoute {

	private UploadSingleWaypointRoute() {
	}

	public static void main(String[] args) {
		String vehicleName = null;
		double[] waypoint = null;
		double speed = 5.0; // init with a default value

		boolean usage = false;
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals("-w")) {
				if (i + 1 == args.length) {
					usage = true;
					break;
				}
				String[] tokens = args[++i].split(",");
				if (tokens.length < 3) {
					usage = true;
					break;
				}
				waypoint = new double[]{
						Math.toRadians(Double.parseDouble(tokens[0].trim())),
						Math.toRadians(Double.parseDouble(tokens[1].trim())),
						Double.parseDouble(tokens[2].trim())};
				continue;
			}
			if (args[i].equals("-s")) {
				if (i + 1 == args.length) {
					usage = true;
					break;
				}
				speed = Double.parseDouble(args[++i]);
				continue;
			}
			vehicleName = args[i];
			break;
		}
		if (vehicleName == null)
			usage = true;

		if (usage) {
			System.err.println("CreateSingleWaypointRoute -w waypoint [-s speed] vehicleName");
			System.err.println();
			System.err.println("\tWaypoint is specified as \"lat,lon,alt\" string, with respective values");
			System.err.println("\tin degrees (latitude and longitude) and AGL meters (altitude). Positive");
			System.err.println("\tdirections for latitude and longitude are North and East.");
			System.err.println();
			System.err.println("Example:");
			System.err.println();
			System.err.println("\tUploadSingleWaypointRoute -w \"56.9761591,24.0730345,100.0\" "
					+ "-s 5.0 \"EMU-101\"");
			System.exit(1);
		} else {
			try {
				createSingleWaypointRoute(vehicleName, waypoint, speed);
			} catch (Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}

	public static void createSingleWaypointRoute(String vehicleName, double[] waypoint, double speed) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try (InputStream in = classLoader.getResourceAsStream("client.properties")) {
			properties.load(in);
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

			// Find a vehicle with the specified tail number.
			Vehicle vehicle = session.lookupVehicle(vehicleName);
			if (vehicle == null)
				throw new IllegalStateException("Vehicle not found: " + vehicleName);

			DomainProto.Mission mission = createMissionIfNotExists(session);
			// Build a route, containing a single waypoint, that should be
			// approached with the specified speed.
			Route route = buildRoute(mission, vehicle, waypoint, speed);

			//Safe route
			route = saveRoute(session, route);
			System.out.println(route.getName() + " saved");

			// Constructed route is just a definition (a plan) of the target
			// mission. Direct path between a vehicle and a target point
			// can collide terrain, No-Flight Zones or buildings.
			// We call processRoute routine to make a low-level plan
			// for the route.
			ProcessedRoute processedRoute = session.processRoute(route);

			// Before the actual upload we lock the vehicle.
			session.gainVehicleControl(vehicle);
			System.out.println(vehicleName + " control gained");

			try {
				session.uploadRoute(vehicle, processedRoute);
				System.out.println("route uploaded");
			} finally {
				session.releaseVehicleControl(vehicle);
				System.out.println(vehicleName + " control released");
			}
		}
	}

	private static DomainProto.Mission createMissionIfNotExists(ClientSession session) throws Exception {
		List<DomainObjectWrapper> mission = session.getObjectList(DomainProto.Mission.class);
		String sampleMissionName = "Sample mission";
		for (DomainObjectWrapper objectWrapper : mission) {
			if (objectWrapper.getMission().getName().equals(sampleMissionName)) {
				System.out.println(sampleMissionName + " found");
				return objectWrapper.getMission();
			}
		}
		DomainProto.User user = session.getObjectList(DomainProto.User.class).get(0).getUser();
		DomainProto.Mission sampleMission = DomainProto.Mission.newBuilder()
				.setName(sampleMissionName)
				.setOwner(user)
				.build();
		DomainObjectWrapper missionWrapper = DomainObjectWrapper.newBuilder()
				.setMission(sampleMission)
				.build();
		sampleMission = session.createOrUpdateObject(missionWrapper, DomainProto.Mission.class).getMission();
		System.out.println(sampleMissionName + " created");
		return sampleMission;
	}

	private static Route saveRoute(ClientSession session, Route route) throws Exception {
		DomainObjectWrapper routeWrapper = DomainObjectWrapper.newBuilder()
				.setRoute(route)
				.build();
		return session.createOrUpdateObject(routeWrapper, route.getClass()).getRoute();
	}

	private static Route buildRoute(DomainProto.Mission mission, Vehicle vehicle, double[] waypoint, double speed) {
		if (waypoint == null || waypoint.length < 3)
			throw new IllegalArgumentException("Waypoint array cannot be null and should contain 3 components");

		Figure.Builder figure = Figure.newBuilder()
				.setType(FigureType.FT_POINT)
				.addPoints(FigurePoint.newBuilder()
						.setLatitude(waypoint[0])
						.setLongitude(waypoint[1])
						.setAglAltitude(waypoint[2])
						.setAltitudeType(AltitudeType.AT_AGL));

		SegmentDefinition.Builder routeSegment = SegmentDefinition
				.newBuilder()
				.setAlgorithmClassName("com.ugcs.ucs.service.routing.impl.WaypointAlgorithm")
				.setFigure(figure)
				.addParameterValues(ParameterValue.newBuilder()
						.setName("speed")
						.setValue(Double.toString(speed)))
				.addParameterValues(ParameterValue.newBuilder()
						.setName("wpTurnType")
						.setValue("STOP_AND_TURN"))
				.addParameterValues(ParameterValue.newBuilder()
						.setName("avoidObstacles")
						.setValue("true"))
				.addParameterValues(ParameterValue.newBuilder()
						.setName("avoidTerrain")
						.setValue("true"))
				.addParameterValues(ParameterValue.newBuilder()
						.setName("altitudeType")
						.setValue("AGL"));

		Route.Builder route = Route.newBuilder()
				.setMission(mission)
				.setName("WP-Direct sample route")
				.setCheckAerodromeNfz(true)
				.setCheckCustomNfz(false)
				.setInitialSpeed(speed)
				.setMaxSpeed(10.0)
				.setMaxAltitude(10000.0)
				.setSafeAltitude(50.0)
				.setTrajectoryType(TrajectoryType.TT_STRAIGHT)
				.addFailsafes(Failsafe.newBuilder()
						.setReason(FailsafeReason.FR_GPS_LOST)
						.setAction(FailsafeAction.FA_WAIT))
				.addSegments(routeSegment);
		if (vehicle != null)
			route.setVehicleProfile(vehicle.getProfile());

		return route.build();
	}
}
