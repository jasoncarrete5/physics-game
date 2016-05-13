package com.fwumdesoft.phys.actors;

import com.fwumdesoft.phys.TransformType;

/**
 * This Actor receives Waves.
 */
public class Receiver extends HitboxActor implements TransformType {
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
