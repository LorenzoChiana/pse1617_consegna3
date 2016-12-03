#include "Arduino.h"
#include "config.h"
#include "ButtonImpl.h"
#include "PressionTask.h"

PressionTask::PressionTask(int pin, int servoPin, Environment* env){
	this->pin = pin;
	this->servoPin = servoPin;
	this->env = env;
	this->currentTime = this->initialTime = 0;
}

void PressionTask::init(int period){
	Task::init(period);
	button = new ButtonImpl(this->pin);
	servo.attach(this->servoPin);   
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
			//Asepttare risposta di contatto con indicazioni sul motorino 
			//setAngle(angle);
		break;
		case PARK:
			//Accendi L2 per due secondi
			if (buttonState){
				initialTime = currentTime = millis();
				//Manda comando per la posizione
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

	servo.write(angle);
}	
