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
	/*Serial.print("Messaggio disponibile: ");
	Serial.println(this->env->getChannel()->isMsgAvailable());*/
	if (this->env->getChannel()->isMsgAvailable()){

		Msg* tempMsg = env->getChannel()->receiveMsg();
		String tempVal = tempMsg->getContent();
		env->updateLastMsg(tempVal);		
  		env->setMsgAvalible(true);
  		 
  		Serial.print("Ricevuto : ");
  		Serial.println(env->getLastMsg());
		if (env->getLastMsg() == "Spenta in parcheggio"){ 
			env->setState(PARK);  		
		} 
		if (env->getLastMsg() == "Accesa in movimento"){ 
			env->setState(MOVEMENT);      			
		} 
		if (env->getLastMsg() == "Spenta non in parcheggio"){ 
			env->setState(OFF);      			
		} 
		
		delete tempMsg;
		
	} else {
		env->setMsgAvalible(false);
	}
}
