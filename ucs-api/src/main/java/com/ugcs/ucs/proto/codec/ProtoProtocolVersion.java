package com.ugcs.ucs.proto.codec;

import com.ugcs.ucs.proto.MessagesProto;
import com.ugcs.ucs.proto.OptionsProto;

public class ProtoProtocolVersion {
	public static int getMajor() {
		return MessagesProto.getDescriptor().getOptions().getExtension(OptionsProto.ugcsProtocolMajorVersion);
	}
	public static int getMinor() {
		return MessagesProto.getDescriptor().getOptions().getExtension(OptionsProto.ugcsProtocolMinorVersion);
	}
}
