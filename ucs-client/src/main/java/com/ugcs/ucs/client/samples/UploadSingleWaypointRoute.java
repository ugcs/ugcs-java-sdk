package com.ugcs.ucs.client.samples;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import com.ugcs.ucs.client.Client;
import com.ugcs.ucs.client.ClientSession;
import com.ugcs.ucs.proto.DomainProto.AltitudeType;
import com.ugcs.ucs.proto.DomainProto.DomainObjectWrapper;
import com.ugcs.ucs.proto.DomainProto.EmergencyAction;
import com.ugcs.ucs.proto.DomainProto.Figure;
import com.ugcs.ucs.proto.DomainProto.FigurePoint;
import com.ugcs.ucs.proto.DomainProto.FigureType;
import com.ugcs.ucs.proto.DomainProto.HomeLocationSource;
import com.ugcs.ucs.proto.DomainProto.ParameterValue;
import com.ugcs.ucs.proto.DomainProto.Route;
import com.ugcs.ucs.proto.DomainProto.RouteDefinition;
import com.ugcs.ucs.proto.DomainProto.RouteProcessingStatus;
import com.ugcs.ucs.proto.DomainProto.RouteSegment;
import com.ugcs.ucs.proto.DomainProto.TrajectoryType;
import com.ugcs.ucs.proto.DomainProto.TraverseAlgorithm;
import com.ugcs.ucs.proto.DomainProto.Vehicle;

public class UploadSingleWaypointRoute {
	public static void main(String[] args) {
		String vehicleName = null;
		double[] waypoint = null;
		double speed = 5.0;
		
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
				waypoint = new double[] {
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
			System.err.println("UploadSingleWaypointRoute -w waypoint [-s speed] vehicleName");
			System.err.println("");
			System.err.println("\tWaypoint is specified as \"lat,lon,alt\" string, with respective values");
			System.err.println("\tin degrees (latitude and longitude) and AGL meters (altitude). Positive");
			System.err.println("\tdirections for latitude and longitude are North and East.");
			System.err.println("");
			System.err.println("Example:");
			System.err.println("");
			System.err.println("\tUploadSingleWaypointRoute -w \"56.9761591,24.0730345,100.0\" -s 5.0 \"EmuCopter-101\"");
			System.exit(1);
		} else {
			try {
				uploadSingleWaypointRoute(vehicleName, waypoint, speed);
			} catch (Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}
	
	public static void uploadSingleWaypointRoute(String vehicleName, double[] waypoint, double speed) throws Exception {
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

			SampleSession session = new SampleSession(client);
			
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

			// Find a vehicle with the specified name.
			Vehicle vehicle = session.lookupVehicle(vehicleName);
			if (vehicle == null)
				throw new IllegalStateException("Vehicle not found: " + vehicleName);
			
			// Build a route, containing a single waypoint, that should be
			// approached with the specified speed.
			Route route = session.buildRoute(vehicle, waypoint, speed);
			
			// Constructed route is just a definition (a plan) of the target
			// mission. Direct path between a vehicle and a target point
			// can collide terrain, No-Flight Zones or buildings.
			// We call processRoute routine to make a low-level plan
			// for the route.
			Route processedRoute = session.processRoute(route);
			
			try {
				// Before the actual upload we lock the vehicle.
				session.gainVehicleControl(vehicle);
				session.uploadRoute(processedRoute, vehicle);
			} finally {
				session.releaseVehicleControl(vehicle);
			}
		}
	}

	private static class SampleSession extends ClientSession {
		public SampleSession(Client client) {
			super(client);
		}

		private TraverseAlgorithm lookupTraverseAlgorithm(String implementationClass) throws Exception {
			Objects.requireNonNull(implementationClass);

			List<DomainObjectWrapper> algorithms = getObjectList(TraverseAlgorithm.class);
			for (DomainObjectWrapper w : algorithms) {
				if (w != null
						&& w.getTraverseAlgorithm() != null
						&& implementationClass.equals(w.getTraverseAlgorithm().getImplementationClass()))
					return w.getTraverseAlgorithm();
			}
			return null;
		}

		public Route buildRoute(Vehicle vehicle, double[] waypoint, double speed) throws Exception {
			if (waypoint == null || waypoint.length < 3)
				throw new IllegalArgumentException("Waypoint array cannot be null and should contain 3 components");

			TraverseAlgorithm waypointAlgorithm = lookupTraverseAlgorithm(
					"com.ugcs.ucs.service.routing.impl.WaypointAlgorithm");
			if (waypointAlgorithm == null)
				throw new IllegalStateException("Waypoint algorithm not supported");

			Figure.Builder figure = Figure.newBuilder()
					.setType(FigureType.FT_POINT)
					.addPoints(FigurePoint.newBuilder()
							.setLatitude(waypoint[0])
							.setLongitude(waypoint[1])
							.setAglAltitude(waypoint[2])
							.setAltitudeType(AltitudeType.AT_AGL));

			RouteDefinition.Builder routeDefinition = RouteDefinition.newBuilder()
					.setAlgorithm(waypointAlgorithm)
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
							.setValue("true"));

			RouteSegment.Builder routeSegment = RouteSegment.newBuilder()
					.setName("WP-Direct")
					.setRouteDefinition(routeDefinition)
					.setOrder(0)
					.setRouteStatus(RouteProcessingStatus.RPS_MODIFIED);

			Route.Builder route = Route.newBuilder()
					.setName("WP-Direct " + System.currentTimeMillis())
					.setHomeLocationSource(HomeLocationSource.HLS_FIRST_WAYPOINT)
					.setCheckAerodromeNfz(true)
					.setCheckCustomNfz(false)
					.setInitialSpeed(speed)
					.setMaxSpeed(10.0)
					.setMaxAltitude(10000.0)
					.setSafeAltitude(50.0)
					.setAltitudeType(AltitudeType.AT_AGL)
					.setTrajectoryType(TrajectoryType.TT_STRAIGHT)
					.setGpsLostAction(EmergencyAction.EA_WAIT)
					.setRcLostAction(EmergencyAction.EA_CONTINUE)
					.setLowBatteryAction(EmergencyAction.EA_LAND)
					.addSegments(routeSegment);
			if (vehicle != null)
				route.setVehicleProfile(vehicle.getProfile());

			return route.build();
		}
	}
}
