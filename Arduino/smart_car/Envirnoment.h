#ifdef __ENVIRONMENT__
#define __ENVIRONMENT__

class Envirnoment {
	State s; 

	float distance;
	bool touch;
	
public:
	void setDistance(float);
	float getDistance();

	void setTouched(bool);
	bool getTouched();

	State getState();
	void setState(State);
}