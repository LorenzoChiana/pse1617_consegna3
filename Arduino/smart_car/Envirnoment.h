#ifdef __ENVIRONMENT__
#define __ENVIRONMENT__


class Envirnoment {

	enum {MOVEMENT, PARK, OFF} state;
	float distance;
	bool touch;

	void setDistance(float);
	float getDistance();

	void setTouched(bool);
	bool getTouched();

}