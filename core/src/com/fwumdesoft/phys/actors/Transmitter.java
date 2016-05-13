package com.fwumdesoft.phys.actors;

import com.fwumdesoft.phys.TransformType;

/**
 * This Actor can fire Waves.
 */
public class Transmitter extends HitboxActor implements TransformType {
	private byte fixed;
	
	@Override
	public byte getFixed() {
		return fixed;
	}

	@Override
	public void setFixed(byte fixed) {
		this.fixed = fixed;
	}
}
