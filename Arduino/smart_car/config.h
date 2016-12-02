/*
 *  This header file stores symbols that concerns 
 *  the configuration of the system.
 *
 */
#ifndef __CONFIG__
#define __CONFIG__

#define T1_PIN 10
#define L1_PIN 4
#define L2_PIN 5
#define ECHO_PIN 7
#define TRIG_PIN 8

#define DMIN 0.2
#define DMAX 0.5

#define INTENSITY 5

#define TMAX 20000

#define N_PULSE 125

#define MAX_BRINGHTNESS 255

#define TMAX_FADE 5000
#define TMAX_ILLUMINATE 10000
#define TSTART_ALARM 2000
#define TMAX_CLEANING 10000

#define DELAY_LED 250

#define COMMAND_USERS "user"
#define COMMAND_STATE "state"

#define STILL_DISTANCE_THRESHOLD 0.05

#define CLOCK 30

#include "DistanceTask.h"
#include "LedTask.h"
#include "Environment.h"

typedef enum {MOVEMENT, PARK, OFF} State;

#endif

