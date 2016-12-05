#include "Arduino.h"
#include "config.h"
#include "ControllerTask.h"
#include <stdio.h>
#include <math.h>

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
  		
		if (env->getLastMsg() == "park"){ 
			env->setState(PARK);      			
		} 
		if (env->getLastMsg() == "movement"){ 
			env->setState(MOVEMENT);      			
		} 
		if (env->getLastMsg() == "off"){ 
			env->setState(OFF);      			
		} 
	} else {
		env->setMsgAvalible(false);
	}
}
