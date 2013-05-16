package chalmers.manel.jps.agents;

import chalmers.manel.jps.render.ManagerEnviroment;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class particle extends Agent {
	protected void setup(){
		ManagerEnviroment.xPosAgent[0] = 15.0f;
		ManagerEnviroment.yPosAgent[0] = 15.0f;
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
		        update();
		      } 
		    });
	}
	
	public void update(){
		ManagerEnviroment.xPosAgent[0] += 1.0f;
		ManagerEnviroment.yPosAgent[0] += 1.0f;
	}
}
