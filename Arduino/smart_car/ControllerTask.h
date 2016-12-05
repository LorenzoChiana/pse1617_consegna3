#ifndef __CONTROLLERTASK__
#define __CONTROLLERTASK__

#include "Task.h"
#include "Environment.h"

class ControllerTask: public Task {

	Environment* env;

public:
	ControllerTask(Environment* env);  
	void init(int period);  
	void tick();

};

#endif
