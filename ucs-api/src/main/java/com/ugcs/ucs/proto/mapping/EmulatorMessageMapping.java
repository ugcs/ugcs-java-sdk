package com.ugcs.ucs.proto.mapping;

import com.ugcs.ucs.proto.EmulatorProto;
import com.ugcs.ucs.proto.codec.ProtoMessageMapping;

public class EmulatorMessageMapping extends ProtoMessageMapping {

	public EmulatorMessageMapping() {
		putMapping(1, EmulatorProto.GetVehicleParametersRequest.class);
		putMapping(2, EmulatorProto.GetVehicleParametersResponse.class);
		putMapping(3, EmulatorProto.GetElevationRequest.class);
		putMapping(4, EmulatorProto.GetElevationResponse.class);
	}
}
