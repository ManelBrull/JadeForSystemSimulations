package chalmers.manel.jps.agents;

import chalmers.manel.jps.render.ManagerEnviroment;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;

public class particle extends Agent {
	protected void setup(){
		addBehaviour(new WakerBehaviour(this, 3000) {
			protected void handleElapsedTimeout() {
				init();
			}
		});
	}
	
	public void init(){
		ManagerEnviroment.xPosAgent[0] = 15.0f;
		ManagerEnviroment.yPosAgent[0] = 15.0f;
		addBehaviour(new TickerBehaviour(this, 10) {
			@Override
			protected void onTick() {
				// TODO Auto-generated method stub
				update();
			} 
		});
		
	}
	
	public void update(){
		ManagerEnviroment.xPosAgent[0] += 1.0f;
		ManagerEnviroment.yPosAgent[0] += 1.0f;
	}
}
