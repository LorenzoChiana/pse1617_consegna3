#include "Arduino.h"
#include "config.h"
#include "LedTask.h"



LedTask::LedTask(int pinL1, int pinL2, Environment* env){
	this->pinL1 = pinL1;
	this->pinL2 = pinL2;
	this->env = env;
}

void LedTask::init(int period){
	Task::init(period);
	l1 = new LedExt(this->pinL1,255); 
	l1->switchOn();
	l2 = new Led(this->pinL2); 
	currentTime = initialTime = 0;
	ledState = false; 
	this->PulseState = FIRST;  
}

void LedTask::tick(){
	float currentDistance;
	State currentState = env->getState();
	switch(currentState){
		case OFF:{
			//Mettere tutto off			
			l1->switchOff();
			l2->switchOff();

			PulseState = FIRST;
		}
		break;
		case MOVEMENT:{
			l1->switchOn();
			l1->setIntensity(255);
			if (env->getDistance() < DMIN){
				l2->switchOn();
			} else {
				l2->switchOff();
			}

			PulseState = FIRST;
			break;
		}
		case PARK:{
			//L1 deve pulsare	
			l1->switchOn();
			
			switch(PulseState){
				case FIRST:{
					Serial.println("First");			
					intensity = 0;
					PulseState = PULSE_UP;	        							
					break;
				}
				case PULSE_UP: {								
					for (int i = 0; i < 50; i++){
						intensity++;
						l1->setIntensity(255);
                //Serial.print("Intensita: "); 
                //Serial.println(intensity);   
					}  
					if (intensity >= 250){
						PulseState = PULSE_DOWN;
					}
					break;	
				}				
				case PULSE_DOWN:{					
					for (int i = 0; i < 50; i++){
						intensity--;
						l1->setIntensity(0);

                //Serial.print("Intensita: ");
                //Serial.println(intensity);       
					}  
					if (intensity <= 0){
						PulseState = PULSE_UP;
					}
					break;	
				}
			}			
			
			//Temp blink
			/*
			if (currentTime - initialTime > 200) {
				initialTime = currentTime = millis();
				ledState = !ledState;
				if (ledState){
					l1->setIntensity(255);
				} else {
					l1->setIntensity(0);
				}
			} else {
				currentTime = millis();
			}*/
			//Se è stato premuto attiva due secondi L2 e invia messaggio di posizione geografica + mail se c'è notifica
			if (env->getTouched()){
				l2->switchOn();
			} else {
				l2->switchOff();
			}
			
			break;
		}
	}
}
