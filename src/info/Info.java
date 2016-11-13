package info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import root.Main;
import processing.core.*;

public class Info {
	private static Info info;
	
	private Info() {}
	public static Info getInstance() {
		if (info == null) {
			info = new Info();
		}
		return info;
	}
}
