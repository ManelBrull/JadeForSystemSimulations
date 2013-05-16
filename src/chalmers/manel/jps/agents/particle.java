package chalmers.manel.jps.agents;

import chalmers.manel.jps.render.ManagerEnviroment;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;

public class particle extends basicParticle {
	private float side = 32;
	protected void setup(){
		super.setup();
	}
	
	@Override
	protected void init(){
		float posi[] = new float[2];
		posi = validInitialPosition();
		this.xPos = posi[0];
		this.yPos = posi[1];
		//This shall be deleted in the future
		ManagerEnviroment.xPosAgent[0] = posi[0];
		ManagerEnviroment.yPosAgent[0] = posi[1];
		
	}
	
	@Override
	protected void update(){
//		rotate(1);
		this.xPos += 1.0f;
		this.yPos += 1.0f;
		if(wallReached()){
			float posi[] = new float[2];
			posi = validInitialPosition();
			this.xPos = posi[0];
			this.yPos = posi[1];
		}
		//That should be implemented in another method of basic Particle
		ManagerEnviroment.xPosAgent[0] = xPos;
		ManagerEnviroment.yPosAgent[0] = yPos;
	}
	
	public void rotate(float angle){
		angle = (float) ((Math.PI*angle)/180.0);
		float x = (float) (xPos*Math.cos(angle) - yPos*Math.sin(angle));
		float y = (float) (xPos*Math.sin(angle) + yPos*Math.cos(angle));
		xPos = x;
		yPos = y;
	}
}
