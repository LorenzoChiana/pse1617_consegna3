#ifndef __PRESSIONTASK__
#define __PRESSIONTASK__

#include "Task.h"
#include "Button.h"
#include "Environment.h"
#include "ServoEx.h"

class PressionTask: public Task {
	int pin, servoPin;	
	Button* button;
	ServoEx servo;

	long currentTime, initialTime;

	Environment* env;

	void setAngle(int angle);
public:
	PressionTask(int pin, int servoPin, Environment* env);  
	void init(int period);  
	void tick();
};

#endif
