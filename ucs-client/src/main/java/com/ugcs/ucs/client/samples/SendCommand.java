package com.ugcs.ucs.client.samples;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import com.ugcs.ucs.client.Client;
import com.ugcs.ucs.proto.DomainProto;
import com.ugcs.ucs.proto.DomainProto.Command;
import com.ugcs.ucs.proto.DomainProto.CommandArgument;
import com.ugcs.ucs.proto.DomainProto.Vehicle;
import com.ugcs.ucs.proto.MessagesProto;

public class SendCommand {
	public static void main(String[] args) {
		String vehicleName = null;
		String commandCode = null;
		Map<String, Double> commandArguments = new HashMap<>();

		boolean usage = false;
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals("-c")) {
				if (i + 1 == args.length) {
					usage = true;
					break;
				}
				commandCode = args[++i];
				continue;
			}
			if (args[i].equals("-a")) {
				if (i + 1 == args.length) {
					usage = true;
					break;
				}
				String[] tokens = args[++i].split("=");
				if (tokens.length < 2) {
					usage = true;
					break;
				}
				commandArguments.put(tokens[0], Double.parseDouble(tokens[1]));
				continue;
			}
			vehicleName = args[i];
			break;
		}
		if (vehicleName == null)
			usage = true;

		if (usage) {
			System.err.println("SendCommand -c commandCode [-a commandArgument=value]* vehicleName");
			System.err.println("");
			System.err.println("\tList of supported command codes:");
			System.err.println("");
			System.err.println("\t  * Arm");
			System.err.println("\t  * AutoMode");
			System.err.println("\t  * CameraTrigger");
			System.err.println("\t  * Continue");
			System.err.println("\t  * Disarm");
			System.err.println("\t  * EmergencyLand");
			System.err.println("\t  * Hold");
			System.err.println("\t  * Land");
			System.err.println("\t  * ManualMode");
			System.err.println("\t  * ReturnHome");
			System.err.println("\t  * Takeoff");
			System.err.println("\t  * Waypoint (args: latitude, longitude, altitude, speed, heading)");
			System.err.println("");
			System.err.println("\tFor more details on the supported commands and its arguments and expected");
			System.err.println("\tvehicle behavior see UgCS User Manual (\"Direct Vehicle Control\" section).");
			System.err.println("");
			System.err.println("Example:");
			System.err.println("");
			System.err.println("\tSendCommand -c Takeoff \"EmuCopter-101\"");
			System.err.println("\tSendCommand -c AutoMode \"EmuCopter-101\"");
			System.err.println("\tSendCommand -c Waypoint -a latitude=0.99442 -a longitude=0.42015 -a altitude=100.0 -a speed=5.0 \"EmuCopter-101\"");
			System.exit(1);
		} else {
			try {
				sendCommand(vehicleName, commandCode, commandArguments);
			} catch (Exception e) {
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}

	public static void sendCommand(String vehicleName, String commandCode, Map<String, Double> commandArguments)
			throws Exception {
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

			// Authorize client & login user.
			session.authorizeHci();
			session.login(
					properties.getProperty("user.login"),
					properties.getProperty("user.password"));

			// Find a vehicle with the specified name.
			Vehicle vehicle = session.lookupVehicle(vehicleName);
			if (vehicle == null)
				throw new IllegalStateException("Vehicle not found: " + vehicleName);

			// Construct command object.
			Command command = session.buildCommand(commandCode, commandArguments);
			session.sendCommand(vehicle, command);
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
			MessagesProto.AuthorizeHciRequest request = MessagesProto.AuthorizeHciRequest.newBuilder()
					.setClientId(clientId)
					.build();
			MessagesProto.AuthorizeHciResponse response = client.execute(request);
			clientId = response.getClientId();
		}

		public void login(String login, String password) throws Exception {
			if (login == null || login.isEmpty())
				throw new IllegalArgumentException("login");
			if (password == null || password.isEmpty())
				throw new IllegalArgumentException("password");

			MessagesProto.LoginRequest request = MessagesProto.LoginRequest.newBuilder()
					.setClientId(clientId)
					.setUserLogin(login)
					.setUserPassword(password)
					.build();
			client.execute(request);
		}

		public Vehicle lookupVehicle(String tailNumber) throws Exception {
			if (tailNumber == null || tailNumber.isEmpty())
				throw new IllegalArgumentException("tailNumber cannot be empty");

			MessagesProto.GetObjectListRequest request = MessagesProto.GetObjectListRequest.newBuilder()
					.setClientId(clientId)
					.setObjectType("Vehicle")
					.build();
			MessagesProto.GetObjectListResponse response = client.execute(request);
			for (DomainProto.DomainObjectWrapper item : response.getObjectsList()) {
				if (item == null || item.getVehicle() == null)
					continue;
				if (tailNumber.equals(item.getVehicle().getTailNumber()))
					return item.getVehicle();
			}
			return null;
		}

		public Command buildCommand(String code, Map<String, Double> arguments) {
			Objects.requireNonNull(code);
			Objects.requireNonNull(arguments);

			Command.Builder builder = Command.newBuilder()
					.setCode(code);
			for (Map.Entry<String, Double> entry : arguments.entrySet()) {
				builder.addArguments(CommandArgument.newBuilder()
						.setCode(entry.getKey())
						.setValue(DomainProto.Value.newBuilder().setDoubleValue(entry.getValue())));
			}
			return builder.build();
		}

		public void sendCommand(Vehicle vehicle, Command command) throws Exception {
			Objects.requireNonNull(vehicle);

			MessagesProto.SendCommandRequest request = MessagesProto.SendCommandRequest.newBuilder()
					.setClientId(clientId)
					.addVehicles(vehicle)
					.setCommand(command)
					.build();
			client.execute(request);
		}
	}
}
