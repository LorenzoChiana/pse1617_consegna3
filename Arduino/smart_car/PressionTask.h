#ifndef __PRESSIONTASK__
#define __PRESSIONTASK__

#include "Task.h"
#include "Button.h"
#include "Environment.h"
#include "ServoTimer2.h"

class PressionTask: public Task {
	int pin, servoPin;	
	Button* button;
	bool firstPress, waitingMsg;
	ServoTimer2* servo;
	long currentTime, initialTime;
	Environment* env;

	void setAngle(int angle);
	bool is_int(char const* p);
	
public:
	PressionTask(int pin, int servoPin, Environment* env);  
	void init(int period);  
	void tick();

};

#endif
