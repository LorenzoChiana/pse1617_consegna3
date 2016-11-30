#ifndef __MSGTASK__
#define __MSGTASK__

#include "Task.h"
#include "MsgService.h"
#include "GlobalState.h"

class MsgTask: public Task {	
	char* getUsers();
	char* getState();
	void flushBuffer();
	char* emptyString;
	GlobalState *Global;
public:
	MsgTask(GlobalState *Global);  
	void init(int period);  
	void tick();
};

#endif
