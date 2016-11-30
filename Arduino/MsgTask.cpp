#include "MsgTask.h"
#include "Arduino.h"
#include "config.h"

MsgTask::MsgTask(/*float differenceThreshold,*/ GlobalState* Global){
this->Global = Global;
}

void MsgTask::init(int period){
	Task::init(period);
	emptyString = "\0";	
	MsgService.init();
}

void MsgTask::tick(){
	//Se c'è un messaggio ed è un comando gli risponde
	if (MsgService.isMsgAvailable()) {
		Msg* msg = MsgService.receiveMsg();  
		if (msg->getContent() == COMMAND_USERS){
			MsgService.sendMsg(getUsers());
		}
		if (msg->getContent() == COMMAND_STATE){
			MsgService.sendMsg(getState());
		}
		delete msg;
	}
	//Ogni clock stampa ciò che c'è nel buffer se è pieno
	flushBuffer();
}

char* MsgTask::getUsers(){
	char response[10];
	sprintf(response,"%d",Global->getUsers());
	return response;
}

char* MsgTask::getState(){
	char state[10];
	if (Global->getPresence() || Global->isCleaning()){
		strcpy(state,"Occupato");	
	} else if (Global->getAlarm()){
		strcpy(state,"Allarme");  
	} else {
		strcpy(state,"Libero"); 
	}
	return state;


}	

void MsgTask::flushBuffer(){

	char* output = Global->getWritingBuffer();
	if (strlen(output)>0){
		MsgService.sendMsg(output);		
		Global->setWritingBuffer("\0");
	}
}

