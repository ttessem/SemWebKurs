#!/bin/bash

mvn exec:java -Dexec.mainClass="com.computas.sem.uib.GrizzlyContainer" -Dexec.args="$1"
