#include "Arduino.h"
#include "config.h"
#include "ButtonImpl.h"
#include "PressionTask.h"
#include "ServoTimer2.h"
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
	this->servo = new ServoTimer2();
}

void PressionTask::init(int period){
	Task::init(period);	
	servo->attach(this->servoPin);  
	button = new ButtonImpl(this->pin); 
}

void PressionTask::tick(){
	int angle;
	State currentState = env->getState();
	bool buttonState = button->isPressed();
	switch(currentState){
		case OFF:{
			//Non rilevare
		}
		break;
		case MOVEMENT:{
			//Messaggio “contatto”
			//Se lo premo una volta
			#ifdef debug
				Serial.print("Bottone: "); Serial.print(buttonState); 
				Serial.print("  firstPress: "); Serial.print(firstPress);
				Serial.print("  waitingMsg: "); Serial.println(waitingMsg);
			#endif
			if (buttonState && firstPress && !waitingMsg){        	
				env->getChannel()->sendMsg(Msg("contatto"));
				firstPress = false;
				waitingMsg = true;
			}
			//Quando rilascio il bottone posso reinviare il messaggio
			if (!buttonState){
				firstPress = true;
			}
			#ifdef debug
				Serial.print("isMsgAvalible: ");
				Serial.println(env->isMsgAvalible());
				Serial.print("waitingMsg: ");
				Serial.println(waitingMsg);
			#endif
			//Accetto i messaggi in arrivo fino a quando viene mandato fine
			if (env->isMsgAvalible() && waitingMsg){
				//Asepttare risposta di contatto con indicazioni sul motorino 
    			if (env->getLastMsg() == "fine"){ 
    				#ifdef debug
    					Serial.print("Ricevuto fine");
    				#endif
    				waitingMsg = false;      			
       			} 
       			char contenuto[4];

       			#ifdef debug
       				Serial.print("Numero ricevuto: "); Serial.println(env->getLastMsg());
       			#endif

       			env->getLastMsg().toCharArray(contenuto, /*sizeof(env->getLastMsg())*/4);
       			if (is_int(contenuto)) {
       				setAngle(env->getLastMsg().toInt());
       				#ifdef debug
       					Serial.print("Angolo settato a "); Serial.println(env->getLastMsg().toInt());
       				#endif
       			} 
       			//altrimenti non è un numero quindi una richiesta diversa dal setServo
       			else {
       				waitingMsg = false;
       			}
       			
    		}	
			break;
		}
		case PARK:{
			//Accendi L2 per due secondi
			if (buttonState && firstPress){
				firstPress = false;
				initialTime = currentTime = millis();
				//Manda comando per la posizione
				env->getChannel()->sendMsg(Msg("contatto"));
			} 
			else {
				currentTime = millis();
			}

			//Quando rilascio il bottone posso reinviare il messaggio
			if (!buttonState){
				firstPress = true;
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
}
//Funzione che regola l'angolo del servo
void PressionTask::setAngle(int thisAngle){
	thisAngle = thisAngle>180?180:thisAngle;
	thisAngle = thisAngle<0?0:thisAngle;
	
	int myValue = map(thisAngle, 0, 180, 500, 2200);
	this->servo->write(myValue);
}	
//funziona che controlla che una stringa sia un numero 
bool PressionTask::is_int(char const* p){
    int length = strlen(p);
    for (int i=0;i<length; i++) {
        if (!isdigit(p[i])){
            return false;
        }
    }
    return true;
}
