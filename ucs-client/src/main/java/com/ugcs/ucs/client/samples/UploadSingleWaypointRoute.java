package com.ugcs.ucs.client.samples;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

import com.ugcs.ucs.client.Client;
import com.ugcs.ucs.proto.DomainProto.AltitudeType;
import com.ugcs.ucs.proto.DomainProto.DomainObjectWrapper;
import com.ugcs.ucs.proto.DomainProto.EmergencyAction;
import com.ugcs.ucs.proto.DomainProto.Figure;
import com.ugcs.ucs.proto.DomainProto.FigurePoint;
import com.ugcs.ucs.proto.DomainProto.FigureType;
import com.ugcs.ucs.proto.DomainProto.HomeLocationSource;
import com.ugcs.ucs.proto.DomainProto.LocalisedMessage;
import com.ugcs.ucs.proto.DomainProto.ParameterValue;
import com.ugcs.ucs.proto.DomainProto.Route;
import com.ugcs.ucs.proto.DomainProto.RouteDefinition;
import com.ugcs.ucs.proto.DomainProto.RouteProcessingStatus;
import com.ugcs.ucs.proto.DomainProto.RouteSegment;
import com.ugcs.ucs.proto.DomainProto.TrajectoryType;
import com.ugcs.ucs.proto.DomainProto.TraverseAlgorithm;
import com.ugcs.ucs.proto.DomainProto.Vehicle;
import com.ugcs.ucs.proto.MessagesProto.AcquireLockRequest;
import com.ugcs.ucs.proto.MessagesProto.AuthorizeHciRequest;
import com.ugcs.ucs.proto.MessagesProto.AuthorizeHciResponse;
import com.ugcs.ucs.proto.MessagesProto.GetObjectListRequest;
import com.ugcs.ucs.proto.MessagesProto.GetObjectListResponse;
import com.ugcs.ucs.proto.MessagesProto.LoginRequest;
import com.ugcs.ucs.proto.MessagesProto.ProcessRouteRequest;
import com.ugcs.ucs.proto.MessagesProto.ProcessRouteResponse;
import com.ugcs.ucs.proto.MessagesProto.ReleaseLockRequest;
import com.ugcs.ucs.proto.MessagesProto.UploadRouteRequest;

public class UploadSingleWaypointRoute {
	public static void main(String[] args) {
		String tailNumber = null;
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
			tailNumber = args[i];
			break;
		}
		if (tailNumber == null)
			usage = true;
		
		if (usage) {
			System.err.println("UploadSingleWaypointRoute -w waypoint [-s speed] tailNumber");
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
				uploadSingleWaypointRoute(tailNumber, waypoint, speed);
			} catch (Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}
	
	public static void uploadSingleWaypointRoute(String tailNumber, double[] waypoint, double speed) throws Exception {
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
			
			Session session = new Session(client);
			
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
			Vehicle vehicle = session.lookupVehicle(tailNumber);
			if (vehicle == null)
				throw new IllegalStateException("Vehicle not found: " + tailNumber);
			
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

	static class Session {
		private final Client client;
		private int clientId = -1;
		
		public Session(Client client) {
			if (client == null)
				throw new IllegalArgumentException("client");
			
			this.client = client;
		}
		
		public void authorizeHci() throws Exception {
			clientId = -1;
			AuthorizeHciRequest request = AuthorizeHciRequest.newBuilder()
				.setClientId(clientId)
				.build();
			AuthorizeHciResponse response = client.execute(request);
			clientId = response.getClientId();
		} 
	
		public void login(String login, String password) throws Exception {
			if (login == null || login.isEmpty())
				throw new IllegalArgumentException("login");
			if (password == null || password.isEmpty())
				throw new IllegalArgumentException("password");

			LoginRequest request = LoginRequest.newBuilder()
					.setClientId(clientId)
					.setUserLogin(login)
					.setUserPassword(password)
					.build();
			client.execute(request);
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
		
		private TraverseAlgorithm lookupTraverseAlgorithm(String implementationClass) throws Exception {
			if (implementationClass == null)
				throw new IllegalArgumentException("implementationClass cannot be null");
			
			GetObjectListRequest request = GetObjectListRequest.newBuilder()
					.setClientId(clientId)
					.setObjectType("TraverseAlgorithm")
					.build();
			GetObjectListResponse response = client.execute(request);
			for (DomainObjectWrapper item : response.getObjectsList()) {
				if (item == null || item.getTraverseAlgorithm() == null)
					continue;
				if (implementationClass.equals(
						item.getTraverseAlgorithm().getImplementationClass()))
					return item.getTraverseAlgorithm();
			}
			return null;
		}	
		
		public Vehicle lookupVehicle(String tailNumber) throws Exception {
			if (tailNumber == null || tailNumber.isEmpty())
				throw new IllegalArgumentException("tailNumber cannot be empty");
			
			GetObjectListRequest request = GetObjectListRequest.newBuilder()
					.setClientId(clientId)
					.setObjectType("Vehicle")
					.build();
			GetObjectListResponse response = client.execute(request);
			for (DomainObjectWrapper item : response.getObjectsList()) {
				if (item == null || item.getVehicle() == null)
					continue;
				if (tailNumber.equals(item.getVehicle().getTailNumber()))
					return item.getVehicle();
			}
			return null;
		}
		
		public void gainVehicleControl(Vehicle vehicle) throws Exception {
			if (vehicle == null)
				throw new IllegalArgumentException("vehicle cannot be null");
			
			AcquireLockRequest request = AcquireLockRequest.newBuilder()
					.setClientId(clientId)
					.setObjectType("Vehicle")
					.setObjectId(vehicle.getId())
					.build();
			client.execute(request);
		}
		
		public void releaseVehicleControl(Vehicle vehicle) throws Exception {
			if (vehicle == null)
				throw new IllegalArgumentException("vehicle cannot be null");
			
			ReleaseLockRequest request = ReleaseLockRequest.newBuilder()
					.setClientId(clientId)
					.setObjectType("Vehicle")
					.setObjectId(vehicle.getId())
					.setIfExclusive(true)
					.build();
			client.execute(request);
		}
		
		private Route processRoute(Route route) throws Exception {
			if (route == null)
				throw new IllegalArgumentException("route cannot be null");
			
			ProcessRouteRequest request = ProcessRouteRequest.newBuilder()
					.setClientId(clientId)
					.setRoute(route)
					.build();
			ProcessRouteResponse response = client.execute(request);
			Route processedRoute = response.getRoute();
			boolean processed = true;
			for (RouteSegment segment : processedRoute.getSegmentsList()) {
				if (segment.getRouteStatus() != RouteProcessingStatus.RPS_PROCESSED) {
					processed = false;
					for (LocalisedMessage message : segment.getMessageSet().getMessagesList()) {
						System.err.println(message.getSeverity() + ": " + message.getDefaultText());
					}
				}
			}
			if (!processed)
				throw new IllegalStateException("Route processing error");
			return processedRoute;
		}
		
		private void uploadRoute(Route route, Vehicle vehicle) throws Exception {
			if (route == null)
				throw new IllegalArgumentException("route cannot be null");
			if (vehicle == null)
				throw new IllegalArgumentException("vehicle cannot be null");
			
			UploadRouteRequest request = UploadRouteRequest.newBuilder()
					.setClientId(clientId)
					.setRoute(route)
					.setVehicle(vehicle)
					.build();
			client.execute(request);
		}
	}
}
