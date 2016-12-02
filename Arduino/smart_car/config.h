/*
 *  This header file stores symbols that concerns 
 *  the configuration of the system.
 *
 */
#ifndef __CONFIG__
#define __CONFIG__

#define T1_PIN 10
#define T2_PIN 11
#define L1_PIN 4
#define L2_PIN 5
#define L3_PIN 6
#define ECHO_PIN 7
#define TRIG_PIN 8
#define PIR_PIN 9

#define DMIN_WC 0.2
#define DMAX_WC 0.5

#define INTENSITY 5

#define CORRECTION_BOUNCE 5

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

#include "AlarmTask.h"
#include "GlobalState.h"
#include "CleaningTask.h"
#include "DetectMotionTask.h"
#include "DetectPresenceTask.h"
#include "FadeTask.h"
#include "IlluminateTask.h"
#include "LedCleaningTask.h"
#include "MsgTask.h"
#include "Scheduler.h"
#include "InputAlarmTask.h"
#include "StopAlarmTask.h"
#include "AlarmLedTask.h"

#endif

