package chalmers.manel.jps.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import chalmers.manel.jps.render.ManagerEnviroment;

public class particle2 extends Agent {
	protected void setup(){
		ManagerEnviroment.xPosAgent[1] = 400.0f;
		ManagerEnviroment.yPosAgent[1] = 15.0f;
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
		        update();
		      } 
		    });
	}
	
	public void update(){
		ManagerEnviroment.xPosAgent[1] -= 1.0f;
		ManagerEnviroment.yPosAgent[1] += 1.0f;
	}
}
