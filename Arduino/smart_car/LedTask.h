#ifndef __LEDTASK__
#define __LEDTASK__

#include "Task.h"
#include "Led.h"
#include "Environment.h"

class LedTask: public Task {
	int pinL1, pinL2;
	Led* l1;
	Led* l2;
	long currentTime, initialTime;
	Environment* env;

public:
	LedTask(int pinL1, int pinL2, Environment* env);  
	void init(int period);  
	void tick();
};

#endif
