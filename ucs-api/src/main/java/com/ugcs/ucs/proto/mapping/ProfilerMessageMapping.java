package com.ugcs.ucs.proto.mapping;

import com.ugcs.ucs.proto.ProfilerProto;
import com.ugcs.ucs.proto.codec.ProtoMessageMapping;

public class ProfilerMessageMapping extends ProtoMessageMapping {

	public ProfilerMessageMapping() {
		putMapping(1, ProfilerProto.ActionSummaryRequest.class);
		putMapping(2, ProfilerProto.ActionSummaryResponse.class);
	}
}
