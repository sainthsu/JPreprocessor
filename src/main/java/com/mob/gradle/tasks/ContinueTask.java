package com.mob.gradle.tasks;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class ContinueTask extends Task {
	
	public void execute() throws BuildException {
		LoopTask.pushContinue();
	}
	
}
