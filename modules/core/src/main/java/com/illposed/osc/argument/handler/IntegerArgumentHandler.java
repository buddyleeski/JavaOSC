/*
 * Copyright (C) 2015, C. Ramakrishnan / Illposed Software.
 * All rights reserved.
 *
 * This code is licensed under the BSD 3-Clause license.
 * See file LICENSE (or LICENSE.html) for more information.
 */

package com.illposed.osc.argument.handler;

import com.illposed.osc.OSCParseException;
import com.illposed.osc.OSCSerializeException;
import com.illposed.osc.argument.ArgumentHandler;
import com.illposed.osc.SizeTrackingOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Parses and serializes an OSC integer type (32bit signed integer).
 */
public class IntegerArgumentHandler implements ArgumentHandler<Integer>, Cloneable {

	public static final ArgumentHandler<Integer> INSTANCE = new IntegerArgumentHandler();

	/** Allow overriding, but somewhat enforce the ugly singleton. */
	protected IntegerArgumentHandler() {
		// ctor declared only for setting the access level
	}

	@Override
	public char getDefaultIdentifier() {
		return 'i';
	}

	@Override
	public Class<Integer> getJavaClass() {
		return Integer.class;
	}

	@Override
	public void setProperties(final Map<String, Object> properties) {
		// we make no use of any properties
	}

	@Override
	public boolean isMarkerOnly() {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public IntegerArgumentHandler clone() throws CloneNotSupportedException {
		return (IntegerArgumentHandler) super.clone();
	}

	@Override
	public Integer parse(final ByteBuffer input) throws OSCParseException {
		final BigInteger intBits = BlobArgumentHandler.readBigInteger(input, 4);
		return intBits.intValue();
	}

	@Override
	public void serialize(final SizeTrackingOutputStream stream, final Integer value)
			throws IOException, OSCSerializeException
	{
		int curValue = value;
		final byte[] intBytes = new byte[4];
		intBytes[3] = (byte)curValue; curValue >>>= 8;
		intBytes[2] = (byte)curValue; curValue >>>= 8;
		intBytes[1] = (byte)curValue; curValue >>>= 8;
		intBytes[0] = (byte)curValue;

		stream.write(intBytes);
	}
}
