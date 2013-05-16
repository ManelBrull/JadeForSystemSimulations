package chalmers.manel.jps.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import chalmers.manel.jps.render.ManagerEnviroment;

public class particle2 extends Agent {
	protected void setup(){
		addBehaviour(new WakerBehaviour(this, 3000) {
			protected void handleElapsedTimeout() {
				init();
			}
		});
	}

	public void init(){
		ManagerEnviroment.xPosAgent[1] = 400.0f;
		ManagerEnviroment.yPosAgent[1] = 15.0f;
		addBehaviour(new TickerBehaviour(this, 10){
			@Override
			protected void onTick() {
				// TODO Auto-generated method stub
				update();
			} 
		});

	}

	public void update(){
		ManagerEnviroment.xPosAgent[1] -= 1.0f;
		ManagerEnviroment.yPosAgent[1] += 1.0f;
	}
}
