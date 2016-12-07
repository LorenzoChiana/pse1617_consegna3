#ifndef __LEDTASK__
#define __LEDTASK__

#include "Task.h"
#include "Led.h"
#include "LedExt.h"
#include "Environment.h"

class LedTask: public Task {
	int pinL1, pinL2;
	LedExt* l1;
	Led* l2;
	int intensity;
	long currentTime, initialTime;
	Environment* env;

	enum {FIRST, PULSE, PULSE_UP, PULSE_DOWN} PulseState;

public:
	LedTask(int pinL1, int pinL2, Environment* env);  
	void init(int period);  
	void tick();
};

#endif
