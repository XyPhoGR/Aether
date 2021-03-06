package com.ralitski.aether.force;

import com.ralitski.aether.Body;
import com.ralitski.aether.Force;

public class ForceRedirect implements Force {
	
	private ForceSimple force;
	
	public ForceRedirect(ForceSimple force) {
		this.force = force;
	}

	@Override
	public void act(Body source, Body toForce, double timeStep) {
		toForce.accelerate(force.act(source, toForce, timeStep));
	}

	@Override
	public Force getOpposite() {
		return new ForceRedirectOpposite();
	}
	
	private class ForceRedirectOpposite implements Force {

		@Override
		public void act(Body source, Body toForce, double timeStep) {
			toForce.accelerate(force.act(source, toForce, timeStep).negateCopy());
		}

		@Override
		public Force getOpposite() {
			//return original
			return ForceRedirect.this;
		}
		
	}

}
