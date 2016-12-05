#include "Arduino.h"
#include "config.h"
#include "ButtonImpl.h"
#include "PressionTask.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

PressionTask::PressionTask(int pin, int servoPin, Environment* env){
	this->pin = pin;
	this->servoPin = servoPin;
	this->env = env;
	this->currentTime = this->initialTime = 0;
	this->firstPress = true;
	this->waitingMsg = false;
}

void PressionTask::init(int period){
	Task::init(period);	
	//servo.attach(this->servoPin);  
	button = new ButtonImpl(this->pin); 
}

void PressionTask::tick(){
	int angle;
	State currentState = env->getState();
	bool buttonState = button->isPressed();
	switch(currentState){
		case OFF:
			//Non rilevare
		break;
		case MOVEMENT:
			//Messaggio “contatto”
			//Se lo premo una volta
			if (buttonState && firstPress && !waitingMsg){
        		Msg* temp = new Msg("contatto");
				this->env->getChannel()->sendMsg(*temp);
        		delete temp;

				firstPress = false;
				waitingMsg = true;
			}
			//Quando rilascio il bottone posso reinviare il messaggio
			if (!buttonState){
				firstPress = true;
			}
			//Accetto i messaggi in arrivo fino a quando viene mandato fine
			if (this->env->isMsgAvalible() && waitingMsg){
				//Asepttare risposta di contatto con indicazioni sul motorino 
				  
    			if (env->getLastMsg() == "fine"){ 
    				waitingMsg = false;      			
       			} 
       			char* contenuto ;
       			env->getLastMsg().toCharArray(contenuto, sizeof(env->getLastMsg()));
       			if (is_int(contenuto)) {
       				setAngle(env->getLastMsg().toInt());
       			}
       			waitingMsg = false;
    		}	
		break;
		case PARK:
			//Accendi L2 per due secondi
			if (buttonState){
				initialTime = currentTime = millis();
				//Manda comando per la posizione
        		Msg* temp = new Msg("contatto");
				this->env->getChannel()->sendMsg(*temp);
				delete temp;
			} 
			else {
				currentTime = millis();
			}

			if (currentTime - initialTime > 2000){
				env->setTouched(false);
			}
			else {
				env->setTouched(true);
			}
		break;
	}
}

void PressionTask::setAngle(int angle){
	angle = angle>180?180:angle;
	angle = angle<0?0:angle;
	//servo.write(angle);
}	
bool PressionTask::is_int(char const* p)
{
    int length = strlen(p);
    for (int i=0;i<length; i++)
        if (!isdigit(p[i]))
        {
            return false;
        }
    return true;
}
