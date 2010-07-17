package com.sgnn7.cpassistant.copyimpl;

import java.io.File;
import java.io.IOException;

public interface ICopyImplementation {
	public void copyFile(File in, File out) throws IOException;
}
