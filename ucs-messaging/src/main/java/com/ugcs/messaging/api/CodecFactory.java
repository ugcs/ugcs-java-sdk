package com.ugcs.messaging.api;

/**
 * Codec factory pairs complementary encoders and decoders
 * and serves as a producer of its instances. It assumes that
 * encoders and decoders can store some coding state and allows
 * to produce separate instances for separate serialization
 * contexts.
 */
public interface CodecFactory {

	/**
	 * Get encoder instance.
	 * For most implementations a new instance should be produced
	 * on each call. Shared instance can be returned, if its implementation
	 * is state-less.
	 *
	 * @return encoder instance
	 */
	MessageEncoder getEncoder();

	/**
	 * Get decoder instance.
	 * For most implementations a new instance should be produced
	 * on each call. Shared instance can be returned, if its implementation
	 * is state-less.
	 *
	 * @return decoder instance
	 */
	MessageDecoder getDecoder();
}
