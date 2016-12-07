#include "Arduino.h"
#include "config.h"
#include "ControllerTask.h"

ControllerTask::ControllerTask(Environment* env){
	this->env = env;
}

void ControllerTask::init(int period){
	Task::init(period);  
}

void ControllerTask::tick(){
	
	if (this->env->getChannel()->isMsgAvailable()){

		env->updateLastMsg(env->getChannel()->receiveMsg()->getContent());
		
  		env->setMsgAvalible(true);
  		 
  	
		if (env->getLastMsg() == "Spenta in parcheggio"){ 
			env->setState(PARK);  		
		} 
		if (env->getLastMsg() == "Accesa in movimento"){ 
			env->setState(MOVEMENT);      			
		} 
		if (env->getLastMsg() == "Spenta non in parcheggio"){ 
			env->setState(OFF);      			
		} 
		
	} else {
		env->setMsgAvalible(false);
	}
}
