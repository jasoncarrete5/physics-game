package com.fwumdesoft.phys;

/**
 * Encapsulates bit masks for determining if a reflector/refractor is fixed.
 */
public class TransformType {
	public static final byte positionFixed = 1 << 0;
	public static final byte rotationFixed = 1 << 1;
	
	public static final byte fixed = positionFixed | rotationFixed;
}
