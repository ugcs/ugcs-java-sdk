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

public final class GetRouteList {

	private GetRouteList() {
	}

	public static void main(String[] args) throws Exception {
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

			List<DomainObjectWrapper> routes = session.getObjectList(Route.class);
			for (DomainObjectWrapper wrapper : routes) {
				System.out.println(wrapper.getRoute().getName());
			}
		}
	}
}
