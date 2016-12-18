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

	#ifdef debug
		Serial.print("Messaggio disponibile: ");
		Serial.println(this->env->getChannel()->isMsgAvailable());
	#endif

	if (this->env->getChannel()->isMsgAvailable()){

		env->updateLastMsg(env->getChannel()->receiveMsg()->getContent());		
  		env->setMsgAvalible(true);
      
  		#ifdef debug
  			Serial.print("Ricevuto : ");
  			Serial.println(env->getLastMsg());
  		#endif
		
		if (env->getLastMsg() == STR_PARK){ 
			env->setState(PARK);  		
		} 
		if (env->getLastMsg() == STR_MOVEMENT){ 
			env->setState(MOVEMENT);      			
		} 
		if (env->getLastMsg() == STR_OFF){ 
			env->setState(OFF);      			
		}		
	} else {
		env->setMsgAvalible(false);
	}
}
