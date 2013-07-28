package com.carrotgarden.build;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProgressChangeListener implements PropertyChangeListener {

	long current = System.currentTimeMillis();

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		if (System.currentTimeMillis() - current > 1000) {
			current = System.currentTimeMillis();
			System.out.println("progress: " + event.getNewValue());
		}
	}

}
